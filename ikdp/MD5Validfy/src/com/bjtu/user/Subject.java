package com.bjtu.user;

import java.util.List;

/**
 * Created by hasee on 2016/6/8.
 */
public class Subject {
    private String name;
    private List<Integer> subject_id;
    public String getName(){
        return this.name;
    }
    public List<Integer> getSubject_id(){
        return this.subject_id;
    }
    public void setSubject_id(List<Integer> subject_idList){
        this.subject_id = subject_idList;
    }

    @Override
    public String toString(){
        return "Subject [name="+this.name+", subject_id="+this.subject_id+"]";
    }
}
