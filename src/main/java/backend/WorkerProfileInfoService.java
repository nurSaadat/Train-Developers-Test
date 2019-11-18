package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/workerAccInfo")
public class WorkerProfileInfoService {
	
	@GET
    public Response checkin(@Context HttpServletRequest request) throws ClassNotFoundException, SQLException {
		
		HttpSession session = request.getSession(false);
		
        if (session != null) {
        	
        	List<String> info = new CopyOnWriteArrayList<>();
        	String email = (String)session.getAttribute("email");
        	String type = (String)session.getAttribute("type");
        	
        	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
        	
        	ResultSet rs = db.getData("SELECT * "
    				+ "FROM `User`  "
    				+ "WHERE Email = '" + email + "';");
    		
    		if (rs.next()) {
    			
    			info.add(rs.getString("Fname"));
    			info.add(rs.getString("Lname"));
    			info.add(rs.getString("Email"));
    			
    			ResultSet td = db.getData("SELECT * FROM Employee WHERE Email = '" + email + "';");
    			
    			if (td.next()) {
        			
        			info.add(td.getString("Position"));
        			info.add(td.getString("BankAccountNumber"));
        			info.add(td.getString("DateOfBirth"));
        			info.add(td.getString("HomeAddress"));
        			info.add(td.getString("PhoneNumber"));
        			info.add(td.getString("Gender"));
        			info.add(td.getString("Payday"));
        			info.add(td.getString("Wage"));
        			
        			if (type.compareTo("agent") == 0) {
                		
                		ResultSet mg = db.getData("SELECT U.Email AS Email, U.FName AS FName, U.LName AS LName FROM User AS U, "
                				+ "Employee AS E, Employee as M WHERE U.Email = M.Email AND M.ContractID = E.ManagerID "
                				+ "AND E.Email = '" + email + "';");
                		
                		if (mg.next()) {
                			
                			info.add(mg.getString("Email"));
                			info.add(mg.getString("FName"));
                			info.add(mg.getString("LName"));
                			
                		} else {
                			
                			return Response.serverError().entity("Error! Email does not exist in the database!").build();
                			
                		}
                		
                	} 
        			
        		} else {
        			
        			return Response.serverError().entity("Error! Email does not exist in the Employee database!").build();
        			
        		}
    			
    		} else {
    			
    			return Response.serverError().entity("Error! Email does not exist in the database!").build();
    			
    		}
        	
    		db.closeConnection();
    	    
            Gson gson = new Gson();
            return Response.ok(gson.toJson(info)).build();
            
        } else {
        	
        	return Response.ok().build();
        	
        }		
        
	}

}
