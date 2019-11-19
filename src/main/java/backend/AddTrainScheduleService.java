package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/addTrainSchedule")
public class AddTrainScheduleService {

	@POST
	public Response addNews(@FormParam("train[]") List<String> train) throws ClassNotFoundException, SQLException {

		if (train.get(0) == null || train.get(1) == null || train.get(2) == null
				|| train.get(0).compareTo("") == 0 || train.get(1).compareTo("") == 0 || train.get(2).compareTo("") == 0) {
			
			return Response.serverError().entity("Error! One of the fields is empty!").build();
		}
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
		
		ResultSet rs = db.getData("SELECT * FROM Train WHERE TrainID = " + train.get(0) + ";");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! The given train is not present in the database!").build();
    	
    	rs = db.getData("SELECT * FROM Route WHERE RouteID = '" + train.get(1) + "';");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! Given route is not present in the database!").build();
    	
    	rs = db.getData("SELECT * FROM Schedule WHERE RouteID = " + train.get(1) + " and ScheduleID = " + train.get(2) + ";");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! The given schedule is not present in the database!").build();
		
    	
    	db.insertData("INSERT INTO `RailwayStation`.`ScheduleHasTrain`\n" + 
    			"(`TrainID`, " + 
    			"`RouteID`, " + 
    			"`ScheduleID`) " + 
    			"VALUES " + 
    			"(" + train.get(0) + ", " + 
    			train.get(1) + ", " + 
    			train.get(2) + ");");
    	
		db.closeConnection();
		
		return Response.ok().build();
	}
}
