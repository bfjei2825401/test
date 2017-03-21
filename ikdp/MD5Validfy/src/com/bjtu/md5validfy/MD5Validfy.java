package com.bjtu.md5validfy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.Gson;

public class MD5Validfy {
	private String username; // 数据库用户名
	private String passwd; // 数据库密码
	private String url; // 数据库连接规则(ip地址,数据库类型,端口号,数据库名称)

	private List<String> ids; // 需要验证的id
	private List<String> table_names; // 提取的表名
	private Map<String, List<String>> table_column; // 表对应的列名
	private Map<String, String> table_IDTable; // 自有id表名对应id映射表表名(源表名->id映射表表名)
	private Map<String, Map<String, String>> IDTable_Columns; // id映射表映射关系(id映射表表名->(关键字列->id列))
	private List<String> mainTables; // 主表名(无需二次查找id的表)
	private Map<String, String> table_IDColName;

	private Connection connection; // 数据库连接

	private static final String MySQLDriver = "com.mysql.jdbc.Driver";

	private static final String KeySrcColName = "SrcColName";// 用于id映射表,源列名关键字
	private static final String KeyDstColName = "DstColName";// 用于id映射表,目的列名关键字

	public void connectDB() throws SQLException, ClassNotFoundException {

		Class.forName(MySQLDriver);
		this.connection = DriverManager.getConnection(this.url, this.username,
				this.passwd);

	}

	/**
	 * 获取一张表中某行中选定列的拼接字符串
	 * 
	 * @param table_name
	 *            给定表名
	 * @param columns
	 *            给定列名(String数组)
	 * @param id
	 *            给定id
	 * @return 拼接好的字符串(每个分量用引号包围,再用逗号隔开)
	 */
	public String GetTableColMergeString(String table_name,
			List<String> columns, String IDColName, String id) {
		String SQLStr = "";
		String columnMerge = "";
		java.sql.PreparedStatement preStatement = null;
		ResultSet resultset = null;
		List<String> list = new ArrayList<String>();

		columnMerge = String.join(",", columns);
		SQLStr = "SELECT " + columnMerge + " FROM " + table_name + " WHERE "
				+ IDColName + "=?";

		//System.out.println(SQLStr);
		try {

			preStatement = this.connection.prepareStatement(SQLStr);
			preStatement.setString(1, id);
			resultset = preStatement.executeQuery();

			while (resultset.next()) {
				for (int i = 1; i <= columns.size(); i++) {
					String str = resultset.getString(i);
					if(str.isEmpty()){
						list.add("");
						
					}else{
						list.add(this.addQuote(str));
					}
				}
			}

			if (resultset != null) {
				resultset.close();
			}
			if (preStatement != null) {
				preStatement.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return String.join(",", list);
	}

	public void testGetTableColMergeString() {

		List<String> list = new LinkedList<String>();
		list.add("article_id");
		list.add("doi");
		list.add("title");
		list.add("pub_date_epub");
		list.add("pub_date_ppub");
		list.add("journal_id");

		System.out.println(this.GetTableColMergeString("article_basic", list,
				"article_id", "123456789"));
	}

	/**
	 * 在映射表中获取自有id
	 * 
	 * @param tableName
	 *            需要获取id的表名
	 * @param srcColName
	 *            源列名(源id列所在列的名字)
	 * @param dstColName
	 *            目的列名(目的id所在列的名字)
	 * @param srcID
	 *            源id(查找表自有id的凭据)
	 * @return 返回自有id(可能不止一个)
	 * @throws SQLException
	 */
	public List<String> GetID(String tableName, String srcColName,
			String dstColName, String srcID) throws SQLException {

		String SQLStr = "";
		List<String> resultIDsList = new ArrayList<String>();
		java.sql.PreparedStatement preStatement = null;
		ResultSet resultset = null;

		SQLStr = "SELECT " + dstColName + " FROM " + tableName + " WHERE "
				+ srcColName + "=?" + " ORDER BY " + dstColName;
		preStatement = this.connection.prepareStatement(SQLStr);
		preStatement.setString(1, srcID);
		resultset = preStatement.executeQuery();
		while (resultset.next()) {
			String str = resultset.getString(1);
			if(!str.isEmpty()){
				resultIDsList.add(str);
			}
			
		}
		if (resultset != null) {
			resultset.close();
		}
		if (preStatement != null) {
			preStatement.close();
		}

		return resultIDsList;
	}

	public void testGetID() throws SQLException {
		List<String> list = this.GetID("article_author", "article_id",
				"author_id", "123456789");
		for (String s : list) {
			System.out.println(s);
		}
	}

	/**
	 * 获取一个id对应的MD5记录
	 * 
	 * @param srcID
	 *            给定的id
	 * @return MD5字串
	 * @throws SQLException
	 */
	public String GetMD5(String srcID) throws SQLException {
		List<String> resultList = new LinkedList<String>();
		boolean isMainTable = false;

		for (String s : this.table_names) {
			List<String> colNames = this.table_column.get(s);

			for (String str : this.mainTables) {
				if (s.equals(str)) {
					isMainTable = true;
				}
			}

			if (isMainTable) {
				resultList.add(this.GetTableColMergeString(s, colNames,
						this.table_IDColName.get(s), srcID));
				isMainTable = false;
			} else {

				String IDTableName = this.table_IDTable.get(s);
				Map<String, String> IDTableCols = this.IDTable_Columns
						.get(IDTableName);
				List<String> tableID = this.GetID(IDTableName,
						IDTableCols.get(KeySrcColName),
						IDTableCols.get(KeyDstColName), srcID);

				for (String strlist : tableID) {
					resultList.add(this.GetTableColMergeString(s,
							this.table_column.get(s),
							this.table_IDColName.get(s), strlist));
				}

			}

		}

		return String.join(",", resultList);
	}

	public void testGetMD5() throws SQLException {
		this.table_names = new ArrayList<String>();

		this.table_names.add("article_basic");
		// this.table_names.add("journal");
		this.table_names.add("author");

		this.mainTables = new ArrayList<String>();

		this.mainTables.add("article_basic");
		String article_basic = "article_basic";

		List<String> article_basic_cols = new ArrayList<String>();

		article_basic_cols.add("article_id");
		article_basic_cols.add("doi");
		article_basic_cols.add("title");
		article_basic_cols.add("pub_date_epub");
		article_basic_cols.add("pub_date_ppub");
		article_basic_cols.add("journal_id");

		String author = "author";

		List<String> author_cols = new ArrayList<String>();
		author_cols.add("id");
		author_cols.add("contact");
		author_cols.add("name");

		this.table_IDTable = new HashMap<String, String>();

		this.table_IDTable.put(author, "article_author");

		Map<String, String> map = new HashMap<String, String>();

		map.put(KeySrcColName, "article_id");
		map.put(KeyDstColName, "author_id");

		this.IDTable_Columns = new HashMap<String, Map<String, String>>();

		this.IDTable_Columns.put("article_author", map);

		this.table_column = new HashMap<String, List<String>>();

		this.table_column.put(article_basic, article_basic_cols);
		this.table_column.put(author, author_cols);

		this.table_IDColName = new HashMap<String, String>();
		this.table_IDColName.put(author, "id");
		this.table_IDColName.put(article_basic, "article_id");

		System.out.println(this.GetMD5("123456789"));
	}

	private String CalcMD5(String str) {
		return DigestUtils.md5Hex(str);
	}

	private String addQuote(String str) {
		return "\"" + str + "\"";
	}

	@SuppressWarnings("unused")
	private void ConfigInit() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream("conf" + File.separator
				+ "DBProperties.properties"));

		this.url = prop.getProperty("url");
		this.username = prop.getProperty("username");
		this.passwd = prop.getProperty("passwd");
		String tablesTmp = prop.getProperty("tables");
		String tableColTmp = prop.getProperty("table_columns");
		String dstmaptmp = prop.getProperty("DstTable2IDMapTable");
		String idtablecolstmp = prop.getProperty("IDTableCols");
		String maintabletmp = prop.getProperty("mainTable");

		// String strid = prop.getProperty("ids");
		// this.ids = strid.split(",");

		// this.mainTables = maintabletmp.split(",");

		IDTable_Columns = new HashMap<String, Map<String, String>>();

		String[] idtablecols = idtablecolstmp.split(";");

		for (String str : idtablecols) {
			String[] tablecol = str.split(":");
			String tablename = tablecol[0];
			String[] cols = tablecol[1].split(",");
			Map<String, String> tmpmap = new HashMap<String, String>();
			tmpmap.put(KeySrcColName, cols[0]);
			tmpmap.put(KeyDstColName, cols[1]);
			IDTable_Columns.put(tablename, tmpmap);
		}

		table_IDTable = new HashMap<String, String>();

		String[] dstmaptabletmp = dstmaptmp.split(";");

		for (String str : dstmaptabletmp) {
			String[] tabletmp = str.split(":");
			table_IDTable.put(tabletmp[0], tabletmp[1]);
		}

		// table_column = new HashMap<String, String[]>();

		String[] tableCol = tableColTmp.split(";");
		for (String str : tableCol) {
			String[] tabletmp = str.split(":");
			String tablename = tabletmp[0];
			String[] cols = tabletmp[1].split(",");
			// table_column.put(tablename, cols);
		}

		table_names = new LinkedList<String>();
		String[] tables = tablesTmp.split(";");

		for (String str : tables) {
			table_names.add(str);
		}

	}

	public void CloseConnection() throws SQLException {
		if (this.connection != null && !this.connection.isClosed()) {
			this.connection.close();
		}
	}

	public void InspectMD5(String filename) throws SQLException {

		File infile = new File(filename);
		String result = infile.getParent();
		result += "InspectResult.txt";

		File outFile = new File(result);

		BufferedReader reader = null;
		BufferedWriter writer = null;
		String readLine = "";
		String[] idmd5;

		try {
			reader = new BufferedReader(new FileReader(infile));
			writer = new BufferedWriter(new FileWriter(outFile));

			while ((readLine = reader.readLine()) != null) {
				idmd5 = readLine.split(":");

				System.out.println(this.GetMD5(idmd5[0]));
				if (!idmd5[1].equals(this.GetMD5(idmd5[0]))) {

					writer.write(idmd5[0]);
					writer.newLine();

				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void testMD5() throws SQLException {
		for (String s : ids) {
			System.out.println(GetMD5(s));
		}
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		//Type type = new TypeToken<List<String>>(){}.getType();
		Reader reader = new FileReader(new File("conf\\conf.json"));
		MD5Validfy md5 = gson.fromJson(reader, MD5Validfy.class);
		
		md5.connectDB();
		md5.InspectMD5("conf" + File.separator + "IDs");
		// md5.testGetID();
		// md5.testGetTableColMergeString();
		md5.CloseConnection();
		// md5.InspectMD5("");
		// md5.getString();

	}

}
