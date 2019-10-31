package backend;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/change")
public class ChangePasswordService {
	
	private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,40})";
	
	@Context private HttpServletRequest request;
	
	@POST
    public Response login(@FormParam("currPass") String oldPass, @FormParam("newPass") String newPass) throws SQLException, ClassNotFoundException {
		HttpSession currentSession = request.getSession(false);
		
		if (currentSession != null) {
			MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
			String email = (String)currentSession.getAttribute("email");
			String passwordFromDB;
			
			if (oldPass == null || oldPass.compareTo("") == 0 || newPass == null || newPass.compareTo("") == 0) {				
				return Response.serverError().entity("Error! Some field is empty").build();				
			}
			
			ResultSet rs = db.getData("SELECT Password FROM User WHERE Email = '" + email + "';");
			rs.next();
			passwordFromDB = rs.getString("Password");    			
			
			if (passwordFromDB.compareTo(oldPass) != 0) {				
				return Response.serverError().entity("Error! Old password don't match").build();				
			}
			
			if (!newPass.matches(PASSWORD_PATTERN)) {				
				return Response.serverError().entity("Error! Password should have between 6 and 40 characters. It should contain at least 1 digit, 1 lowercase and 1 uppercase character! Allowed symbols, from which at least 1 should be present: @#$%!").build();
			}
			
			db.updateData("UPDATE User SET Password='" + newPass + "' WHERE Email='" + email + "';");
					
			return Response.ok("Password is successfully updated").build();
			
		}		
		
		return Response.ok("No user session opened").build();
	}
}
