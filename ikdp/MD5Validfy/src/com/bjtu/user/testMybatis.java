package com.bjtu.user;
import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * Created by hasee on 2016/6/7.
 */
public class testMybatis {
    public static void main(String[] args){
        String resource = "conf.xml";
        //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        InputStream is = testMybatis.class.getClassLoader().getResourceAsStream(resource);
        //构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        //使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
        //Reader reader = Resources.getResourceAsReader(resource);
        //构建sqlSession的工厂
        //SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        //创建能执行映射文件中sql的sqlSession
        SqlSession session = sessionFactory.openSession();
        /**
         * 映射sql的标识字符串，
         * me.gacl.mapping.userMapper是userMapper.xml文件中mapper标签的namespace属性的值，
         * getUser是select标签的id属性值，通过select标签的id属性值就可以找到要执行的SQL
         */
        String statement = "com.bjtu.user.mapping.UserBasicMapper.getUserBasic";//映射sql的标识字符串
        String statement2 = "com.bjtu.user.mapping.UserJournalMapper.getUserJournal";
        //执行查询返回一个唯一user对象的sql
        Basic ub = session.selectOne(statement, "leelddd");
        Journal uj = session.selectOne(statement2, "leelddd");
        System.out.println(ub);
        System.out.println(uj);
    }
}
