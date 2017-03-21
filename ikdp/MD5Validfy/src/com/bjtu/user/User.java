package com.bjtu.user;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * Created by hasee on 2016/6/9.
 */
public class User {
    private String source;
    public User(){
        this.source = "conf.xml";
        InputStream inputStream = User.class.getResourceAsStream(this.source);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

    }
}
