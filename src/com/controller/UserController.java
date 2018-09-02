package com.controller;

import java.util.List;
import java.util.Map;

import com.common.ModelAndView;
import com.model.User;
import com.service.UserService;

public class UserController {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ModelAndView showUsers(Map<String, Object> requestMap, Map<String, Object> sessionMap) {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(requestMap.get("userName"));
        List<User> users = userService.showUsers();
        System.out.println(users);
        modelAndView.setView("11.jsp");
        modelAndView.setRedirect(true);
        modelAndView.addRequestAttribute("requestKey", "requestValue");
        modelAndView.addSessionAttribute("sessionKey", "sessionKey");
        System.out.println(userService + "=====================");
        return modelAndView;
    }
    
    public ModelAndView createUser(Map<String, Object> requestMap, Map<String, Object> sessionMap) {
        ModelAndView modelAndView = new ModelAndView();
        
        System.out.println("createUser method");
        modelAndView.setRedirect(true);
        return modelAndView;
    }
}
