package backend;

import java.sql.SQLException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/addNews")
public class AddNewsService {

	@POST
	public Response addNews(@FormParam("text") String text, @FormParam("title") String title) throws ClassNotFoundException, SQLException {

		if(text.length() > 449)
    		return Response.serverError().entity("Error! News text is too large!").build();
    	
    	if(title.length() > 99)
    		return Response.serverError().entity("Error! News title is too large!").build();
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
		
		db.insertData("INSERT INTO `RailwayStation`.`News` " + 
    			"(`Text`, " + 
    			"`Title`) " + 
    			"VALUES " + 
    			"('" + text + "', '" + title + "');");
		
		db.closeConnection();
		
		return Response.ok().build();
	}
}
