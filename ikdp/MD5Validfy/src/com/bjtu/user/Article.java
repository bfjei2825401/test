package com.bjtu.user;

import java.util.List;

/**
 * Created by hasee on 2016/6/8.
 */
public class Article {
    private String name;
    private List<Integer> article_id;
    public String getName(){
        return this.name;
    }
    public List<Integer> getArticle_id(){
        return this.article_id;
    }
    public void setArticle_id(List<Integer> article_idList){
        this.article_id = article_idList;
    }
    @Override
    public String toString(){
        return "Article [name="+this.name+", article_id="+this.article_id+"]";

    }

}
