package com.common;

public class ActionConfig {
    private String name;
    private String clz;
    private String methodName;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getClz() {
        return clz;
    }
    public void setClz(String clz) {
        this.clz = clz;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public ActionConfig(String clz, String methodName) {
        super();
        this.clz = clz;
        this.methodName = methodName;
    }
    public ActionConfig() {
        super();
    }
}
