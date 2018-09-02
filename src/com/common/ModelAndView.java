package com.common;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private String view;
    private boolean isRedirect;
    
    private Map<String, Object> session = new HashMap<String, Object>();
    private Map<String, Object> request = new HashMap<String, Object>();
    
    public void removeSessionAttribute(String key, Object value) {
        session.put(key, "remove");
    }
    
    public void addSessionAttribute(String key, Object value) {
        session.put(key, value);
    }
    
    public void addRequestAttribute(String key, Object value) {
        request.put(key, value);
    }
    
    public String getView() {
        return view;
    }
    public void setView(String view) {
        this.view = view;
    }
    public boolean isRedirect() {
        return isRedirect;
    }
    public void setRedirect(boolean isRedirect) {
        this.isRedirect = isRedirect;
    }
    public Map<String, Object> getSession() {
        return session;
    }
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    public Map<String, Object> getRequest() {
        return request;
    }
    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }
}
