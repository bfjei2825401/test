package com.bjtu.user;
import java.util.List;
/**
 * Created by hasee on 2016/6/7.
 */
public class Journal {
    private String name;
    private List<Integer> journal_id;
    public String getName(){
        return this.name;
    }
    public List<Integer> getJournal_id(){
        return this.journal_id;
    }
    public void setJournal_id(List<Integer> journal_idList){
        this.journal_id = journal_idList;
    }
    @Override
    public String toString(){
        return "Journal [name=" + this.name + ", journal_id=" + this.journal_id + "]";
    }

}
