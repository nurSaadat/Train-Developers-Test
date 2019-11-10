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

@Path("/accInfo")
public class ProfileInfoService {
	
	@GET
    public Response checkin(@Context HttpServletRequest request) throws ClassNotFoundException, SQLException {
		
		HttpSession session = request.getSession(false);
		
        if (session != null) {
        	
        	List<String> info = new CopyOnWriteArrayList<>();
        	String email = (String)session.getAttribute("email");
        	
        	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

    		ResultSet rs = db.getData("SELECT * "
    				+ "FROM `User`  "
    				+ "WHERE Email = '" + email + "' ;");
    		
    		if(rs.next()) {
    			
    			info.add(rs.getString("Fname"));
    			info.add(rs.getString("Lname"));
    			info.add(rs.getString("Email"));
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
