package com.demo_eureka.demo.entity;

public class User {
    private String name;
    private String sex;
    private String phone;
    public User(){}
    public User(String name, String sex, String phone) {
        this.name = name;
        this.sex = sex;
        this.phone = phone;
    }

    public String toString(){
        return "user:{"
                +"name: " + name + ", "
                +"sex: " + sex + ", "
                +"phone: " + phone
                +" }";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
