package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/signup")
public class SignUpService {
	
	private static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,40})";
	private static final String FNAME_PATTERN = "[A-Z][a-z]*";
	private static final String LNAME_PATTERN = "[A-Z]+([ '-][a-zA-Z]+)*";
	
	@POST
    public Response register(@FormParam("data") List<String> data) throws SQLException, ClassNotFoundException {
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
		
		for (int i = 0; i < data.size(); i++) {
			
			if (data.get(i).length() == 0) {
				
				return Response.serverError().entity("Error! One of the fields is empty!").build();
				
			}
			
			if (i == 0) {
				
				ResultSet rs = db.getData("SELECT Email FROM User;");
				
				while (rs.next()) {
					
					if (rs.getString("Email").toLowerCase().compareTo(data.get(0).toLowerCase()) == 0) {
						
						return Response.serverError().entity("Error! That email address is already present in the database!").build();
						
					}
					
				}
				
				if (!data.get(0).matches(EMAIL_PATTERN)) {
					
					return Response.serverError().entity("Error! Email provided is invalid!").build();
					
				}
				
			}
			
			if (i == 1) {
				
				if (!data.get(i).matches(FNAME_PATTERN)) {
					
					return Response.serverError().entity("Error! First name provided is invalid!").build();
					
				}
				
			}
			
			if (i == 2) {
				
				if (!data.get(i).matches(LNAME_PATTERN)) {
					
					return Response.serverError().entity("Error! Last name provided is invalid!").build();
					
				}
				
			}
			
			if (i == 3) {
				
				if (data.get(3).length() < 6 || !data.get(3).matches(PASSWORD_PATTERN)) {
					
					return Response.serverError().entity("Error! Password should have between 6 and 40 characters. It should contain at least 1 digit, 1 lowercase and 1 uppercase character! Allowed symbols, from which at least 1 should be present: @#$%!").build();
					
				}
				
			}
			
		}
		
		db.insertData("INSERT INTO User (Email, FName, LName, Password) VALUES (" + data.get(0) + ", " + data.get(1) + ", " + data.get(2) + ", " + data.get(3) + ");");
    	
        return Response.ok().build();
        
    }

}
