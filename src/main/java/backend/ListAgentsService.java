package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/agents")
public class ListAgentsService {

	@GET
    public Response getList() throws ClassNotFoundException, SQLException {
    	
    	List<List<String>> agents = new CopyOnWriteArrayList<>();
    	
    	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
    	
    	ResultSet rs = db.getData("SELECT U.Email, U.FName, U.LName "
    			+ "FROM User AS U "
    			+ "WHERE U.Email LIKE \"%@td.kz\" AND U.Email NOT IN "
    			+ "(SELECT E.Email "
    			+ "FROM Employee AS E);");
        
        while (rs.next()) {
        	
        	agents.add(new CopyOnWriteArrayList<String>());
        	agents.get(agents.size() - 1).add(rs.getString("Email"));
        	agents.get(agents.size() - 1).add(rs.getString("FName"));
        	agents.get(agents.size() - 1).add(rs.getString("LName"));
        	
        }

        Collections.reverse(agents);
        db.closeConnection();

        Gson gson = new Gson();
        return Response.ok(gson.toJson(agents)).build();

    }
	
	@POST
	public Response setAgent(@FormParam("agent[]") List<String> agent) throws ClassNotFoundException, SQLException {

		if (agent.get(0) == null || agent.get(1) == null || agent.get(2) == null || agent.get(3) == null || agent.get(4) == null || agent.get(5) == null || agent.get(6) == null || agent.get(7) == null || agent.get(8) == null || agent.get(9) == null || agent.get(10) == null || agent.get(11) == null || agent.get(12) == null || agent.get(13) == null || agent.get(14) == null 
				|| agent.get(0).compareTo("") == 0 || agent.get(1).compareTo("") == 0 || agent.get(2).compareTo("") == 0 || agent.get(3).compareTo("") == 0 || agent.get(4).compareTo("") == 0 || agent.get(5).compareTo("") == 0 || agent.get(6).compareTo("") == 0 || agent.get(7).compareTo("") == 0 || agent.get(8).compareTo("") == 0 || agent.get(9).compareTo("") == 0 || agent.get(10).compareTo("") == 0 || agent.get(11).compareTo("") == 0 || agent.get(12).compareTo("") == 0 || agent.get(13).compareTo("") == 0 || agent.get(14).compareTo("") == 0 ) {
			
			return Response.serverError().entity("Error! One of the fields is empty!").build();
			
		}
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
		
		ResultSet rs = db.getData("SELECT Email FROM User WHERE Email = '" + agent.get(13) + "';");
		
		if(!rs.next())
			return Response.serverError().entity("Error! Given user email is not present in the database!").build();
		
			
		rs = db.getData("SELECT U.Email FROM User U, Employee E WHERE U.Email = '" + agent.get(13) + "' and E.Email = U.Email;");
		
		if(rs.next())
			return Response.serverError().entity("Error! Employee already exist in the database!").build();
		
		
		rs = db.getData("SELECT U.Email, E.ContractID FROM User U, Employee E WHERE U.Email = '" + agent.get(14) + "' and E.Email = U.Email;");
		
		if(!rs.next())
			return Response.serverError().entity("Error! Given manager email is not present in the database!").build();
		
		int managerID = rs.getInt("ContractID");
		
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
				"VALUES (" + agent.get(0) + ", '" + agent.get(1) + "', " + agent.get(2) + ", " + agent.get(3) + ", '" + agent.get(4) + "', '" + agent.get(5) + "', " + agent.get(6) + ", '" + agent.get(7) + "', '" + agent.get(8) + "', '" + agent.get(9) + "', " + agent.get(10) + ", " + agent.get(11) + ", '" + agent.get(12) + "', '" + agent.get(13) + "', '" + managerID + "');");
		
		db.closeConnection();
		
		return Response.ok().build();
	}
}
