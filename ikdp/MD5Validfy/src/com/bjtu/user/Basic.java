package com.bjtu.user;

/**
 * Created by hasee on 2016/6/7.
 */
public class Basic {
    private String name;
    private String mail;
    private String password;
    public String getName(){
        return this.name;
    }
    public String getMail(){
        return this.mail;
    }
    public String getPassword(){
        return this.password;
    }
    public void setName(String nameStr){
        this.name = nameStr;
    }
    public void setMail(String mailStr){
        this.name = mailStr;
    }
    public void setPassword(String passwordStr){
        this.password = passwordStr;
    }
    @Override
    public String toString(){
        return "Basic [name=" + this.name +", mail=" + this.mail + ", password=" + this.password + "]";
    }

}
