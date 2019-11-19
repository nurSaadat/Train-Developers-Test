package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/addSchedule")
public class AddScheduleService {

	@POST
	public Response addNews(@FormParam("schedule[]") List<String> schedule) throws ClassNotFoundException, SQLException {

		if (schedule.get(0) == null || schedule.get(1) == null || schedule.get(2) == null || schedule.get(3) == null
				|| schedule.get(0).compareTo("") == 0 || schedule.get(1).compareTo("") == 0 || schedule.get(2).compareTo("") == 0 || schedule.get(3).compareTo("") == 0) {
			
			return Response.serverError().entity("Error! One of the fields is empty!").build();
		}
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
		
		ResultSet rs = db.getData("SELECT * " + 
    			"FROM Station " + 
    			"WHERE Abbreviation = '" + schedule.get(3) + "';");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! The given station is not present in the database!").build();
		
    	rs = db.getData("SELECT * FROM Route WHERE RouteID = '" + schedule.get(0) + "';");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! Given route is not present in the database!").build();
    	
    	db.insertData("INSERT INTO `RailwayStation`.`Schedule` " + 
    			"(`ArrivalTime`, " + 
    			"`DepartureTime`, " + 
    			"`RouteID`, " + 
    			"`StationAbbr`) " + 
    			"VALUES " + 
    			"('" + schedule.get(1) + "', " + 
    			"'" + schedule.get(2) + "', " + 
    			schedule.get(0) + ", " + 
    			"'" + schedule.get(3) + "');");
    	
		db.closeConnection();
		
		return Response.ok().build();
	}
}
