package com.bjtu.md5validfy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.google.gson.*;
public class GSONTest {
	
	private String url; // 数据库连接规则(ip地址,数据库类型,端口号,数据库名称)
	private String username; // 数据库用户名
	private String passwd; // 数据库密码
	

	
	private List<String> table_names; // 提取的表名
	private Map<String, List<String>> table_column; // 表对应的列名
	private Map<String, String> table_IDTable; // 自有id表名对应id映射表表名(源表名->id映射表表名)
	private Map<String, Map<String, String>> IDTable_Columns; // id映射表映射关系(id映射表表名->(关键字列->id列))
	private List<String> mainTables; // 主表名(无需二次查找id的表)
	private Map<String,String> table_IDColName;
	private static final String MySQLDriver = "com.mysql.jdbc.Driver";
	public void printAttributes(){
		System.out.println(url);
		System.out.println(username);
		System.out.println(passwd);
		
		System.out.println(table_names);
		
		System.out.println(table_column);
		
		System.out.println(table_IDTable);
		
		System.out.println(IDTable_Columns);
		
		System.out.println(mainTables);
		
		System.out.println(table_IDColName);
		
	}

	private static final String KeySrcColName = "SrcColName";// 用于id映射表,源列名关键字
	private static final String KeyDstColName = "DstColName";// 用于id映射表,目的列名关键字
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		//Type type = new TypeToken<List<String>>(){}.getType();
		Reader reader = new FileReader(new File("conf\\conf.json"));
		GSONTest gsontest = gson.fromJson(reader, GSONTest.class);
		gsontest.printAttributes();
	}

}
