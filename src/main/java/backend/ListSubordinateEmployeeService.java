package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/myworkers")
public class ListSubordinateEmployeeService {

	@Context private HttpServletRequest request;
	@GET
    public Response getList() throws ClassNotFoundException, SQLException {
    	
    	List<List<String>> workers = new CopyOnWriteArrayList<>();
    	HttpSession session = request.getSession(true);
    	
    	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
    	
    	ResultSet rs = db.getData("SELECT U.Email, U.FName, U.LName, E.Position, E.WorkingHours, E.StartDate, E.EndDate, E.HoursPerDay, E.DaysPerWeek, E.Wage, E.Payday\n" + 
    			"FROM Employee AS E, Employee AS M, User AS U\n" + 
    			"WHERE E.ManagerID = M.ContractID AND M.Email = '" + session.getAttribute("email") + "' AND U.Email = E.Email;");
        
        while (rs.next()) {
        	
        	workers.add(new CopyOnWriteArrayList<String>());
        	workers.get(workers.size() - 1).add(rs.getString("Email"));
        	workers.get(workers.size() - 1).add(rs.getString("FName"));
        	workers.get(workers.size() - 1).add(rs.getString("LName"));
        	workers.get(workers.size() - 1).add(rs.getString("Position"));
        	workers.get(workers.size() - 1).add(rs.getString("WorkingHours"));
        	workers.get(workers.size() - 1).add(rs.getString("StartDate"));
        	workers.get(workers.size() - 1).add(rs.getString("EndDate"));
        	workers.get(workers.size() - 1).add(rs.getString("HoursPerDay"));
        	workers.get(workers.size() - 1).add(rs.getString("DaysPerWeek"));
        	workers.get(workers.size() - 1).add(rs.getString("Wage"));
        	workers.get(workers.size() - 1).add(rs.getString("Payday"));
        	
        }

        Collections.reverse(workers);
        db.closeConnection();

        Gson gson = new Gson();
        return Response.ok(gson.toJson(workers)).build();

    }
	
	@POST
	public Response setAgent(@FormParam("agent[]") List<String> agent) throws ClassNotFoundException, SQLException {

		if (agent.get(0) == null || agent.get(1) == null || agent.get(2) == null || agent.get(3) == null || agent.get(4) == null || agent.get(5) == null || agent.get(6) == null 
				|| agent.get(0).compareTo("") == 0 || agent.get(1).compareTo("") == 0 || agent.get(2).compareTo("") == 0 || agent.get(3).compareTo("") == 0 || agent.get(4).compareTo("") == 0 || agent.get(5).compareTo("") == 0 || agent.get(6).compareTo("") == 0) {
			
			return Response.serverError().entity("Error! One of the fields is empty!").build();
			
		}
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
		
		ResultSet rs = db.getData("SELECT Email FROM User WHERE Email = '" + agent.get(7) + "';");
		
		db.updateData("UPDATE Employee\n" + 
				"SET WorkingHours = '" + agent.get(0) + "', Wage = '" + agent.get(1) + "', StartDate = '" + agent.get(2) + "', EndDate = '" + agent.get(3) + "', HoursPerDay = '" + agent.get(4) + "', DaysPerWeek = '" + agent.get(5) + "', Payday = '" + agent.get(6) + "'\n" + 
				"WHERE Email = '" + agent.get(7) + "';");
		
		db.closeConnection();
		
		return Response.ok().build();
		
	}
	
}
