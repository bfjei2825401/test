package com.bjtu.user;

import java.util.Date;

/**
 * Created by hasee on 2016/6/8.
 */
public class Setting {
    private String name;
    private int frequency;
    private int article_num;
    private Date last_time;
    public String getName(){
        return this.name;
    }
    public int getFrequency(){
        return frequency;
    }
    public int getArticle_num(){
        return this.article_num;
    }
    public Date getLast_time(){
        return this.last_time;
    }
    public void setFrequency(int freq){
        this.frequency = freq;
    }
    public void setArticle_num(int articleNum){
        this.article_num = articleNum;
    }
    public void setLast_time(Date lastTime){
        this.last_time = lastTime;
    }
    @Override
    public String toString(){
        return "Setting [name="+this.name+", frequency="+this.frequency+", article_num="+this.frequency+", last_time="+this.last_time+"]";
    }
}
