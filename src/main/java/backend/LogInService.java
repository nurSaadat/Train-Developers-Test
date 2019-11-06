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

@Path("/login")
public class LogInService {
	
	private static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,40})";
	private static final String FNAME_PATTERN = "[A-Z][a-z]*";
	private static final String LNAME_PATTERN = "[A-Z]+([ '-][a-zA-Z]+)*";
	
	@Context private HttpServletRequest request;
	@POST
    public Response login(@FormParam("username") String email, @FormParam("password") String pass) throws SQLException, ClassNotFoundException {
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
			
		if (email == null || pass == null || email.compareTo("") == 0 || pass.compareTo("") == 0) {
			
			return Response.serverError().entity("Error! Some field is empty!").build();
			
		}
			
		ResultSet rs = db.getData("SELECT Email, Password FROM User WHERE Email = '" + email + "';");
		
		while (rs.next()) {
			
			if (rs.getString("Email").toLowerCase().compareTo(email.toLowerCase()) == 0 || rs.getString("Password").compareTo(pass) == 0) {
				
				HttpSession oldSession = request.getSession(false);
				
		        if (oldSession != null) 
		        	oldSession.invalidate();
				
				HttpSession session = request.getSession(true);
				
				// checking user type
				if (email.matches(".@traindev.kz")) {
					session.setAttribute("type", "user");
				}
				else {
					session.setAttribute("type", "user");
				}
				
				// set email as an attribute						
				session.setAttribute("email", email);
				
				//setting session to expiry in 30 mins
				session.setMaxInactiveInterval(30*60);
				
		        return Response.ok().build();
				
			}
		}
		
		db.closeConnection();
		
		return Response.serverError().entity("Error! Email address is not present in the database!").build();
        
    }

}
