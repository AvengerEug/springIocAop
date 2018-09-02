package com.model;

public class User {
    
    private int id;
    private String name;
    private boolean sex;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isSex() {
        return sex;
    }
    public void setSex(boolean sex) {
        this.sex = sex;
    }
    public User(int id, String name, boolean sex) {
        super();
        this.id = id;
        this.name = name;
        this.sex = sex;
    }
    public User() {
        super();
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", sex=" + sex + "]";
    }
}
