package com.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Storage every elements in bean.xml 
 * @author Eugene.Huang
 */
public class BeanConfig {

    private String id;
    private String clz;
    private boolean singleton;
    
    private List<BeanProperty> beanPropertys = new ArrayList<BeanProperty>();
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getClz() {
        return clz;
    }
    public void setClz(String clz) {
        this.clz = clz;
    }
    public boolean isSingleton() {
        return singleton;
    }
    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }
    public BeanConfig() {
        super();
    }
    public BeanConfig(String id, String clz, boolean singleton) {
        super();
        this.id = id;
        this.clz = clz;
        this.singleton = singleton;
    }
    public List<BeanProperty> getBeanPropertys() {
        return beanPropertys;
    }
    public void setBeanPropertys(List<BeanProperty> beanPropertys) {
        this.beanPropertys = beanPropertys;
    }
}
