package backend;

import java.sql.*;

public class MySQLConnector {
	
	protected Connection con;
	protected Statement stmt;
	
	/**
	 * @param port - port to your database over TCP?IP connection (address is set to localhost)
	 * @param schema - schema of the database
	 * @param username - used to log into the database
	 * @param password - used to log into the database
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public MySQLConnector(int port, String schema, String username, String password) throws SQLException, ClassNotFoundException {
		
		Class.forName("com.mysql.jdbc.Driver");  
		con = DriverManager.getConnection("jdbc:mysql://localhost:" + port +"/" + schema + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password); 
		stmt = con.createStatement();
		
	}

	/**
	 * @param selectStatement - write any valid SQL select statement here. The template is 'SELECT field1, field2,...fieldN FROM table_name1, table_name2... [WHERE Clause] [OFFSET M ] [LIMIT N]' (e.g. select * from EMPLOYEE)
	 * @return ResultSet - returns a table for a given query; Use ResultSet.next() and ResultSet.get{DATATYPE}(int Column), where the DATATYPE is the datatype of a variable with its column number Column  
	 * @throws SQLException
	 */
	public ResultSet getData(String selectStatement) throws SQLException {
		
		return stmt.executeQuery(selectStatement);
		
	}
	
	/**
	 * @param select - select portion of a query (e.g. FName, LName)
	 * @param from - from portion of a query (e.g. EMPLOYEE)
	 * @return ResultSet - returns a table for a given query; Use ResultSet.next() and ResultSet.get{DATATYPE}(int Column), where the DATATYPE is the datatype of a variable with its column number Column  
	 * @throws SQLException
	 */
	public ResultSet getData(String select, String from) throws SQLException {
		
		return stmt.executeQuery("SELECT " + select + " FROM " + from);
		
	}
	
	/**
	 * @param select - select portion of a query (e.g. FName, LName)
	 * @param from - from portion of a query (e.g. EMPLOYEE)
	 * @param where - where portion of a query (e.g. FName = 'Anne')
	 * @return ResultSet - returns a table for a given query; Use ResultSet.next() and ResultSet.get{DATATYPE}(int Column), where the DATATYPE is the datatype of a variable with its column number Column  
	 * @throws SQLException
	 */
	public ResultSet getData(String select, String from, String where) throws SQLException {
		
		return stmt.executeQuery("SELECT " + select + " FROM " + from + " WHERE " + where);
		
	}

	/**
	 * @param updateStatement - write any valid SQL update statement here. The template is 'UPDATE `table_name` SET `column_name` = `new_value' [WHERE condition]' (e.g. update EMPLOYEE set FName = 'Anne2' where FName = 'Anne')  
	 * @throws SQLException
	 */
	public void updateData(String updateStatement) throws SQLException {
		
		stmt.executeUpdate(updateStatement);
		
	}
	
	/**
	 * @param update - update portion of a query (e.g. EMPLOYEE)
	 * @param set - set portion of a query (e.g. FName = 'Anne2')
	 * @param where - where portion of a query (e.g. FName = 'Anne')
	 * @throws SQLException
	 */
	public void updateData(String update, String set, String where) throws SQLException {
		
		stmt.executeUpdate("UPDATE " + update + " SET " + set + " WHERE " + where);
		
	}
	
	/**
	 * @param insertStatement - write any valid SQL insert statement here. The template is 'INSERT INTO `table_name`(column_1,column_2,...) VALUES (value_1,value_2,...)' (e.g. insert into EMPLOYEE values ('name', 'surname', 1, '1999-05-13', 'address', 'M', 123, 1, NULL))  
	 * @throws SQLException
	 */
	public void insertData(String insertStatement) throws SQLException {
		
		stmt.executeUpdate(insertStatement);
	}
	
	/**
	 * @param deleteStatement - write any valid SQL delete statement here. the template is 'DELETE FROM `table_name` [WHERE condition]' (e.g. Delete from EMPLOYEE where FName = 'name')  
	 * @throws SQLException
	 */
	public void deleteData(String deleteStatement) throws SQLException {
		
		stmt.executeUpdate(deleteStatement);
	}
	
	/**
	 * Do not forget to close the connection with your database when you are done using it!
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException { 
	
		con.close(); 
		
	}

}
