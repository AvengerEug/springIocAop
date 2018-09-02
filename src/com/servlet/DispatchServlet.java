package com.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.common.ActionConfig;
import com.common.BeanFactory;
import com.common.ModelAndView;

public class DispatchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Map<String, ActionConfig> configs = new HashMap<String, ActionConfig>();
    
    public DispatchServlet() {
        super();
    }
    
    public void init(ServletConfig config) {
        // 加载配置文件将配置信息放入configs中
        
        InputStream is = DispatchServlet.class.getClassLoader().getResourceAsStream("action.xml");
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            Element element = document.getDocumentElement();
            
            NodeList actionNodeList = element.getElementsByTagName("action");
            
            if (actionNodeList != null) {
                for (int i = 0; i < actionNodeList.getLength(); i++) {
                    Element actionElement = (Element)actionNodeList.item(i);
                    String action = actionElement.getAttribute("name");
                    String clsName = actionElement.getAttribute("class");
                    String method = actionElement.getAttribute("method");
                    
                    ActionConfig actionConfig = new ActionConfig();
                    actionConfig.setName(action);
                    actionConfig.setClz(clsName);
                    actionConfig.setMethodName(method);
                    
                    configs.put(action, actionConfig);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String resource = uri.substring(request.getContextPath().length() + 1);
        if (configs.containsKey(resource)) {
            HttpSession session = request.getSession();
            
            System.out.println(request.getParameter("userName"));
            
            // Get actionConfig object by show.action
            ActionConfig actionConfig = configs.get(resource);
            try {
                // Get all session value and put it into sessionMap
                Map<String, Object> sessionMap = new HashMap<String, Object>();
                Enumeration<String> sessionEnum = session.getAttributeNames();
                while (sessionEnum.hasMoreElements()) {
                    String key = sessionEnum.nextElement();
                    sessionMap.put(key, session.getAttribute(key));
                }
                
                Map<String, Object> requestMap = new HashMap<String, Object>();
                Enumeration<String> requestEnum = request.getParameterNames();
                while (requestEnum.hasMoreElements()) {
                    String key = requestEnum.nextElement();
                    requestMap.put(key, request.getParameter(key));
                }
                
                Enumeration<String> requestAttributeEnum = request.getAttributeNames();
                while (requestAttributeEnum.hasMoreElements()) {
                    String key = requestAttributeEnum.nextElement();
                    requestMap.put(key, request.getAttribute(key));
                }
                
                // Get classLoader and new instance
                /*Class<?> clz = Class.forName(actionConfig.getClz());
                Object obj = clz.newInstance();*/

                Object obj = BeanFactory.getInstance().getBean(actionConfig.getClz());

                Class<?>[] parametersClass = new Class[2];
                parametersClass[0] = Map.class;
                parametersClass[1] = Map.class;

                Method method = obj.getClass().getMethod(actionConfig.getMethodName(), parametersClass);

                // invoke method
                ModelAndView modelAndView = (ModelAndView)method.invoke(obj, new Object[]{requestMap, sessionMap});

                String view = modelAndView.getView();
                boolean isRedirect = modelAndView.isRedirect();

                // 将controller中set到sessionMap和requestMap的值拿出来, 再重新放到容器的request和session中去
                Map<String, Object> fromControllerRequestMap = modelAndView.getRequest();
                Set<String> fromControllerRequestMapKey = fromControllerRequestMap.keySet();
                for (String strKey : fromControllerRequestMapKey) {
                    request.setAttribute(strKey, fromControllerRequestMap.get(strKey));
                }

                Map<String, Object> fromControllerSessionMap = modelAndView.getSession();
                Set<String> fromControllerSessionKey = fromControllerSessionMap.keySet();
                for (String strKey : fromControllerSessionKey) {
                    Object value = fromControllerSessionMap.get(strKey);
                    if ("remove".equals(value)) {
                        session.removeAttribute(strKey);
                    } else {
                        session.setAttribute(strKey, value);
                    }
                }

                System.out.println(request.getAttribute("requestKey"));
                System.out.println(session.getAttribute("sessionKey"));
                
                if (isRedirect) {
                    response.sendRedirect(view);
                } else {
                    request.getRequestDispatcher(view).forward(request, response);
                }
                
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            response.sendError(400);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

}
