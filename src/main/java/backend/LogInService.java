package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/login")
public class LogInService {
	
	private static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,40})";
	private static final String FNAME_PATTERN = "[A-Z][a-z]*";
	private static final String LNAME_PATTERN = "[A-Z]+([ '-][a-zA-Z]+)*";
	
	@POST
    public Response login(@FormParam("data") List<String> data) throws SQLException, ClassNotFoundException {
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
			
		if (data.get(0).length() == 0 || data.get(1).length() == 0) {
			
			return Response.serverError().entity("Error! One of the fields is empty!").build();
			
		}
			
		ResultSet rs = db.getData("SELECT Email FROM User;");
		
		while (rs.next()) {
			
			if (!(rs.getString("Email").toLowerCase().compareTo(data.get(0).toLowerCase()) == 0)) {
				
				return Response.serverError().entity("Error! Email address is not present in the database!").build();
				
			}
			
		}
			
		rs = db.getData("SELECT Password FROM User WHERE Email = " + data.get(0) + ";");
			
		if (!(rs.getString("Password").compareTo(data.get(1)) == 0)) {
			
			return Response.serverError().entity("Error! Wrong password!").build();
			
		}
		
        return Response.ok().build();
        
    }

}
