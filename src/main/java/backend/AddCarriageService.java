package backend;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/addCarriage")
public class AddCarriageService {

	@POST
	public Response addCarriage(@FormParam("numberOfSeats") String numberOfSeats, @FormParam("classType") String classType, @FormParam("trainID") String trainID) throws ClassNotFoundException, SQLException {

		if(numberOfSeats == null || classType == null || trainID == null
				|| numberOfSeats.compareTo("") == 0 || classType.compareTo("") == 0 || trainID.compareTo("") == 0) {
			
			return Response.serverError().entity("Error! One of the fields is empty!").build();	
		}
		
		if(Integer.parseInt(numberOfSeats) < 1)
			return Response.serverError().entity("Error! Number of seats is less than one!").build();	
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
		
		ResultSet rs = db.getData("SELECT * FROM Train WHERE TrainID = " + trainID + ";");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! The given train is not present in the database!").build();
    	
    	rs = db.getData("SELECT * FROM `Class` WHERE ClassName = '" + classType + "';");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! The given train is not present in the database!").build();
    	
		
		db.insertData("INSERT INTO `RailwayStation`.`Carriage` " + 
				"(`NumberOfSeats`, " + 
				"`TrainID`, " + 
				"`Class`) " + 
				"VALUES " + 
				"(" + numberOfSeats + ", " + 
				trainID + ", " + 
				"'" + classType + "');" + 
				"");
		
		rs = db.getData("select MAX(CarriageNumber) from `RailwayStation`.`Carriage`;");
        rs.next(); 
        
        int carriageNum = rs.getInt(1);
        
        for(int i = 0; i < Integer.parseInt(numberOfSeats); i++) {
        	
        	db.insertData("INSERT INTO `RailwayStation`.`Seat` " + 
        			"(`Status`, " + 
        			"`CarriageID`) " + 
        			"VALUES " + 
        			"('Free', " + 
        			carriageNum + ");");
        }
		
		db.closeConnection();
		
		return Response.ok().build();
	}
}
