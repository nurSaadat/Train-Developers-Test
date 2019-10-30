package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/signup")
public class SignUpService {
	
	private static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,40})";
	private static final String FNAME_PATTERN = "[A-Z][a-z]*";
	private static final String LNAME_PATTERN = "[A-Z]+[a-z]*";
	
	@POST
    public Response register(@FormParam("email") String email, @FormParam("fname") String fname, @FormParam("lname") String lname, @FormParam("password") String password) throws SQLException, ClassNotFoundException {
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
		
		if (email == null || fname == null || lname == null || password == null || email.compareTo("") == 0 || fname.compareTo("") == 0 || lname.compareTo("") == 0 || password.compareTo("") == 0) {
			
			return Response.serverError().entity("Error! One of the fields is empty!").build();
			
		}
				
		ResultSet rs = db.getData("SELECT Email FROM User WHERE Email = '" + email + "';");
		
		while (rs.next()) {
			
			if (rs.getString("Email").toLowerCase().compareTo(email.toLowerCase()) == 0) {
				
				return Response.serverError().entity("Error! That email address is already present in the database!").build();
				
			}
			
		}
		
		if (!email.matches(EMAIL_PATTERN)) {
			
			return Response.serverError().entity("Error! Email provided is invalid!").build();
			
		}
				
		if (!fname.matches(FNAME_PATTERN)) {
			
			return Response.serverError().entity("Error! First name provided is invalid!").build();
			
		}
				
		if (!lname.matches(LNAME_PATTERN)) {
			
			return Response.serverError().entity("Error! Last name provided is invalid!").build();
			
		}
				
		if (!password.matches(PASSWORD_PATTERN)) {
			
			return Response.serverError().entity("Error! Password should have between 6 and 40 characters. It should contain at least 1 digit, 1 lowercase and 1 uppercase character! Allowed symbols, from which at least 1 should be present: @#$%!").build();
			
		}
		
		db.insertData("INSERT INTO User (Email, FName, LName, Password) VALUES (\"" + email + "\", \"" + fname + "\", \"" + lname + "\", \"" + password + "\");");
    	
        return Response.ok().build();
        
    }

}
