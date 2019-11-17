package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/managers")
public class AddManagerService {
	
	@POST
	public Response setManager(@FormParam("manager[]") List<String> manager) throws ClassNotFoundException, SQLException {

		if (manager.get(0) == null || manager.get(2) == null || manager.get(3) == null || manager.get(4) == null || manager.get(5) == null || manager.get(6) == null || manager.get(7) == null || manager.get(8) == null || manager.get(9) == null || manager.get(10) == null || manager.get(11) == null || manager.get(12) == null || manager.get(13) == null
				|| manager.get(0).compareTo("") == 0 || manager.get(2).compareTo("") == 0 || manager.get(3).compareTo("") == 0 || manager.get(4).compareTo("") == 0 || manager.get(5).compareTo("") == 0 || manager.get(6).compareTo("") == 0 || manager.get(7).compareTo("") == 0 || manager.get(8).compareTo("") == 0 || manager.get(9).compareTo("") == 0 || manager.get(10).compareTo("") == 0 || manager.get(11).compareTo("") == 0 || manager.get(12).compareTo("") == 0 || manager.get(13).compareTo("") == 0) {
			
			return Response.serverError().entity("Error! One of the fields is empty!").build();
			
		}
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
		
		ResultSet rs = db.getData("SELECT Email FROM User WHERE Email = '" + manager.get(13) + "';");
		
		if(!rs.next())
			return Response.serverError().entity("Error! Given user email is not present in the database!").build();
		
		rs = db.getData("SELECT U.Email FROM User U, Employee E WHERE U.Email = '" + manager.get(13) + "' and E.Email = U.Email;");
		
		if(rs.next())
			return Response.serverError().entity("Error! Employee already exist in the database!").build();
		
		db.insertData("INSERT INTO `RailwayStation`.`Employee`\n" + "(" +
				"`WorkingHours`, " + 
				"`Position`, " + 
				"`Wage`, " + 
				"`BankAccountNumber`, " + 
				"`DateOfBirth`, " + 
				"`HomeAddress`, " + 
				"`PhoneNumber`, " + 
				"`Gender`, " + 
				"`StartDate`, " + 
				"`EndDate`, " + 
				"`HoursPerDay`, " + 
				"`DaysPerWeek`, " + 
				"`Payday`, " + 
				"`Email`, " + 
				"`ManagerID`)" +
				"VALUES (" + manager.get(0) + ", 'Manager', " + manager.get(2) + ", " + manager.get(3) + ", '" + manager.get(4) + "', '" + manager.get(5) + "', " + manager.get(6) + ", '" + manager.get(7) + "', '" + manager.get(8) + "', '" + manager.get(9) + "', " + manager.get(10) + ", " + manager.get(11) + ", '" + manager.get(12) + "', '" + manager.get(13) + "', NULL);");
		
		db.closeConnection();
		
		return Response.ok().build();
	}
}
