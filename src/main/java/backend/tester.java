package backend;

import java.sql.ResultSet;
import java.sql.SQLException;

public class tester {

	public static void main(String[] args) throws SQLException {
		
		MySQLConnector db = new MySQLConnector(3306, "EmployeeDB", "user", "8264");
		
		ResultSet rs = db.getData("select * from EMPLOYEE");
		
		rs.next();
	    System.out.println(rs.getString(1));
	    
	    db.updateData("update EMPLOYEE set FName = 'Anne2' where FName = 'Anne'");
	    db.updateData("EMPLOYEE", "FName = 'Anne'", "FName = 'Anne2'");
	    
	    rs = db.getData("*", "EMPLOYEE");
		
		rs.next();
	    System.out.println(rs.getString(1));
	    
	    db.closeConnection();
		
	}

}
