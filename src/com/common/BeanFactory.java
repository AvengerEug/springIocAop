package com.common;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Big factory: using deal with creating object
 * The class is singleton  
 * @author Eugene.Huang
 */
public class BeanFactory {

    public static BeanFactory beanFactory = null;
    
    // Storage bean.xml file information
    public static Map<String, BeanConfig> beanConfigs = new HashMap<String, BeanConfig>();
    
    public static Map<String, Object> objects = new HashMap<String, Object>();
    
    private BeanFactory() {
    }
    
    public static BeanFactory getInstance() {
        if (beanFactory == null) {
            beanFactory = new BeanFactory();
            // Exec init method to load bean.xml file information
            beanFactory.init();
        }
        return beanFactory;
    }
    
    /**
     * Loading bean.xml file into memory (beanConfigs)
     */
    private void init () {
        InputStream is = null;
        
        try {
            is = BeanFactory.class.getClassLoader().getResourceAsStream("bean.xml");
            
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            Element element = document.getDocumentElement();
            
            NodeList nodeList = element.getElementsByTagName("bean");
            
            if (nodeList != null) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element beanElement = (Element)nodeList.item(i);
                    String id = beanElement.getAttribute("id");
                    String clz = beanElement.getAttribute("class");
                    
                    // 若获取的是不存在的节点名称, 则返回的是空字符串
                    String scope = beanElement.getAttribute("123");
                    // 若不填也默认采用单例模式
                    if (scope.equals("")) {
                        scope = "singleton";
                    }

                    boolean isSingleton = "singleton".equals(scope);
                    BeanConfig beanConfig = new BeanConfig();
                    beanConfig.setId(id);
                    beanConfig.setClz(clz);
                    beanConfig.setSingleton(isSingleton);
                    
                    beanConfigs.put(id, beanConfig);
                    
                    // 将当前bean的所有属性都存入list属性中
                    NodeList properties = beanElement.getElementsByTagName("property");
                    if (properties != null) {
                        for (int j = 0; j < properties.getLength(); j++) {
                            Element propertyElement = (Element)properties.item(j);
                            String name = propertyElement.getAttribute("name");
                            String ref = propertyElement.getAttribute("ref");
                            
                            BeanProperty beanProperty = new BeanProperty();
                            beanProperty.setName(name);
                            beanProperty.setRef(ref);
                            
                            beanConfig.getBeanPropertys().add(beanProperty);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getBean(String id) {
        // If id not exitst in beanConfigs, the bean factory can't provide object
        if (beanConfigs.containsKey(id)) {
            
            if (objects.containsKey(id)) {
                return objects.get(id);
            }
            BeanConfig beanConfig = beanConfigs.get(id);

            if (id.equals(beanConfig.getId())) {
                String className = beanConfig.getClz();
                boolean singleton = beanConfig.isSingleton();
                try {
                    // Get class loader and create instance
                    Class<?> clz = Class.forName(className);
                    // Create object using bean.xml file
                    Object object = clz.newInstance();
                    if (singleton) {
                        objects.put(id, object);
                    }

                    // 处理DI
                    List<BeanProperty> properties = beanConfig.getBeanPropertys();
                    for (int i = 0; i < properties.size(); i++) {
                        BeanProperty beanProperty = properties.get(i);
                        String name = beanProperty.getName();
                        String ref = beanProperty.getRef();
                        // 此时已经获取到要创建对象的class文件 、以及要依赖注入的属性名. 只需要通过反射调用set方法即可
                        String propertyMethodName = "set" + name.substring(0,1).toUpperCase() + name.substring(1);

                        // 递归调用获取ref引用的对象，目的: 为了get ref中的对象
                        Object propertyObject = this.getBean(ref);

                        // 不要用这种方式来获取参数的类加载器, 因为对象可能实现多个接口, 最后导致参数数量对不上导致抛出异常
                        // 正确的方式应该是获取对象中的所有方法, 然后获取到set某个属性的方法
                        //Method method = clz.getMethod(propertyMethodName, propertyObject.getClass().getInterfaces());
                        
                        Method[] methods = clz.getMethods();
                        Method propertyMethod = null;
                        for (Method method : methods) {
                            System.out.println(method.getName());
                            if (propertyMethodName.equals(method.getName())) {
                                propertyMethod = method;
                                break;
                            }
                        }

                        propertyMethod.invoke(object, propertyObject);
                    }

                    return object;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
            }
        }
        return null;
    }
    
}
