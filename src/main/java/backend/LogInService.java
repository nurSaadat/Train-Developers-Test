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
	
	@Context private HttpServletRequest request;
	@POST
    public Response login(@FormParam("username") String email, @FormParam("password") String pass) throws SQLException, ClassNotFoundException {
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
			
		if (email == null || pass == null || email.compareTo("") == 0 || pass.compareTo("") == 0) {
			
			return Response.serverError().entity("Error! Some field is empty!").build();
			
		}
			
		ResultSet rs = db.getData("SELECT Email, Password FROM User WHERE Email = '" + email + "';");
		
		while (rs.next()) {
			
			if (rs.getString("Email").compareTo(email) == 0 && rs.getString("Password").compareTo(pass) == 0) {
				
				HttpSession oldSession = request.getSession(false);
				
		        if (oldSession != null) 
		        	oldSession.invalidate();
				
				HttpSession session = request.getSession(true);
				
				// checking user type
				if (email.matches(".@td.kz")) {
						
					ResultSet td = db.getData("SELECT Position FROM Employee WHERE Email = '" + email + "';");

					while (td.next()) {

						if (td.getString("Position").toLowerCase().compareTo("agent") == 0) {

							session.setAttribute("type", "agent");

						} else if (td.getString("Position").toLowerCase().compareTo("manager") == 0) {

							session.setAttribute("type", "manager");

						} else {

							session.setAttribute("type", "user");

						}

					}

				} else {

					session.setAttribute("type", "user");

				}
				
				// set email as an attribute						
				session.setAttribute("email", email);
				
				//setting session to expiry in 30 mins
				session.setMaxInactiveInterval(30*60);
				
		        return Response.ok(session.getAttribute("type")).build();
				
			}
		}
		
		db.closeConnection();
		
		return Response.serverError().entity("Error! Email address is not present in the database or wrong password was provided!").build();
        
    }

}
