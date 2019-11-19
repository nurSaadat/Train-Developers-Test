package backend;

import java.sql.SQLException;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/addTrain")
public class AddTrainService {

	@POST
	public Response addTrain(@FormParam("capacity") String capacity, @FormParam("type") String type) throws ClassNotFoundException, SQLException {

		if (capacity == null || type == null
				|| capacity.compareTo("") == 0 || type.compareTo("") == 0) {
			
			return Response.serverError().entity("Error! One of the fields is empty!").build();
			
		}
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
		
		db.insertData("INSERT INTO `RailwayStation`.`Train` " + 
				"(`Capacity`, " + 
				"`NumberOfCarriages`, " + 
				"`Type`) " + 
				"VALUES " + 
				"(" + capacity + ", " + 
				"0, " + 
				"'" + type + "');");
		
		db.closeConnection();
		
		return Response.ok().build();
	}
}
