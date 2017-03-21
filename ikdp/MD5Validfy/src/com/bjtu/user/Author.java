package com.bjtu.user;
import java.util.List;
/**
 * Created by hasee on 2016/6/8.
 */
public class Author {
    private String name;
    private List<Integer> author_id;
    public String getName(){
        return this.name;
    }
    public List<Integer> getAuthor_id(){
        return this.author_id;
    }
    public void setAuthor_id(List<Integer> author_idList){
        this.author_id = author_idList;
    }
    @Override
    public String toString(){
        return "Author [name=" + this.name + ", author_id="+this.author_id+"]";
    }
}
