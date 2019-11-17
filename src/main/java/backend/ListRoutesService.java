package backend;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

@Path("/routes")
public class ListRoutesService {
    
    @GET
    public Response getList() throws ClassNotFoundException, SQLException {
    	
    	List<List<String>> routes = new CopyOnWriteArrayList<>();
    	
    	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

    	ResultSet rs = db.getData("SELECT * FROM Route;");

    	while (rs.next()) {

    		routes.add(new CopyOnWriteArrayList<String>());
    		routes.get(routes.size() - 1).add(rs.getString("RouteID"));
    		routes.get(routes.size() - 1).add(rs.getString("DateFrom"));
    		routes.get(routes.size() - 1).add(rs.getString("DateTo"));
    		routes.get(routes.size() - 1).add(rs.getString("TimeFrom"));
    		routes.get(routes.size() - 1).add(rs.getString("TimeTo"));
    		routes.get(routes.size() - 1).add(rs.getString("Distance"));
    		routes.get(routes.size() - 1).add(rs.getString("Status"));
    		routes.get(routes.size() - 1).add(rs.getString("StationFrom"));
    		routes.get(routes.size() - 1).add(rs.getString("StationTo"));

    	}
            
        Collections.reverse(routes);
        db.closeConnection();
	    
        Gson gson = new Gson();
        return Response.ok(gson.toJson(routes)).build(); 
    }
    
    @POST
    public Response editRoute(@FormParam("route[]") List<String> route) throws ClassNotFoundException, SQLException {
    	
    	if (route.get(0) == null || route.get(1) == null || route.get(2) == null || route.get(3) == null || route.get(4) == null || route.get(5) == null || route.get(6) == null || route.get(7) == null || route.get(8) == null || route.get(9) == null  
				|| route.get(0).compareTo("") == 0 || route.get(1).compareTo("") == 0 || route.get(2).compareTo("") == 0 || route.get(3).compareTo("") == 0 || route.get(4).compareTo("") == 0 || route.get(5).compareTo("") == 0 || route.get(6).compareTo("") == 0 || route.get(7).compareTo("") == 0 || route.get(8).compareTo("") == 0 || route.get(9).compareTo("") == 0) {
			
			return Response.serverError().entity("Error! One of the fields is empty!").build();
		}
    	
    	if(route.get(9).length() > 449)
    		return Response.serverError().entity("Error! News text is too large!").build();
    	
    	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
    	
    	ResultSet rs = db.getData("SELECT * " + 
    			"FROM Station A, Station B " + 
    			"WHERE A.Abbreviation = '" + route.get(7) + "' and B.Abbreviation = '" + route.get(8) + "';");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! One of the given stations is not present in the database!").build();
		
    	rs = db.getData("SELECT StationFrom, StationTo FROM Route WHERE RouteID = '" + route.get(0) + "';");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! Given route is not present in the database!").build();
		
    	String oldFrom = rs.getString("StationFrom");
    	String oldTo = rs.getString("StationTo");
    	
    	db.updateData("UPDATE `RailwayStation`.`Route` " + 
    			"SET " + 
    			"`DateFrom` = '" + route.get(1) + "', " + 
    			"`DateTo` = '" + route.get(2) + "', " + 
    			"`TimeFrom` = '" + route.get(3) + "', " + 
    			"`TimeTo` = '" + route.get(4) + "', " + 
    			"`Distance` = " + route.get(5) + ", " + 
    			"`Status` = '" + route.get(6) + "', " + 
    			"`StationFrom` = '" + route.get(7) + "', " + 
    			"`StationTo` = '" + route.get(8) + "' " + 
    			"WHERE `RouteID` = " + route.get(0) + ";");
    	
    	db.updateData("UPDATE `RailwayStation`.`Schedule` " + 
    			"SET " + 
    			"`ArrivalTime` = '" + route.get(3) + "', " + 
    			"`DepartureTime` = '" + route.get(3) + "', " + 
    			"`StationAbbr` = '" + route.get(7) + "' " + 
    			"WHERE `ScheduleID` = 1 AND `RouteID` = " + route.get(0) + " AND `StationAbbr` = '" + oldFrom + "';");
    	
    	db.updateData("UPDATE `RailwayStation`.`Schedule` " + 
    			"SET " + 
    			"`ArrivalTime` = '" + route.get(4) + "', " + 
    			"`DepartureTime` = '" + route.get(4) + "', " + 
    			"`StationAbbr` = '" + route.get(8) + "' " + 
    			"WHERE `ScheduleID` = 2 AND `RouteID` = " + route.get(0) + " AND `StationAbbr` = '" + oldTo + "';");
    	
    	db.insertData("INSERT INTO `RailwayStation`.`News` " + 
    			"(`Text`) " + 
    			"VALUES " + 
    			"'" + route.get(9) + "');");
    	
    	db.closeConnection();
    	
        return Response.ok().build(); 
    }
    
    @PUT
    public Response addRoute(@FormParam("route[]") List<String> route) throws ClassNotFoundException, SQLException {
    	
    	if (route.get(0) == null || route.get(1) == null || route.get(2) == null || route.get(3) == null || route.get(4) == null || route.get(5) == null || route.get(6) == null || route.get(7) == null || route.get(8) == null 
				|| route.get(0).compareTo("") == 0 || route.get(1).compareTo("") == 0 || route.get(2).compareTo("") == 0 || route.get(3).compareTo("") == 0 || route.get(4).compareTo("") == 0 || route.get(5).compareTo("") == 0 || route.get(6).compareTo("") == 0 || route.get(7).compareTo("") == 0 || route.get(8).compareTo("") == 0) {
			
			return Response.serverError().entity("Error! One of the fields is empty!").build();
		}
    	
    	if(route.get(8).length() > 449)
    		return Response.serverError().entity("Error! News text is too large!").build();
    	
    	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
    	
    	ResultSet rs = db.getData("SELECT * " + 
    			"FROM Station A, Station B " + 
    			"WHERE A.Abbreviation = '" + route.get(6) + "' and B.Abbreviation = '" + route.get(7) + "';");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! One of the given stations is not present in the database!").build();
		
    	rs = db.getData("select MAX(RouteID) from `RailwayStation`.`Route`;");
        rs.next();
        
        int routeID = rs.getInt(1) + 1;
    	
    	db.insertData("INSERT INTO `RailwayStation`.`Route` " + 
    			"(`RouteID`, " + 
    			"`DateFrom`, " + 
    			"`DateTo`, " + 
    			"`TimeFrom`, " + 
    			"`TimeTo`, " + 
    			"`Distance`, " + 
    			"`Status`, " + 
    			"`StationFrom`, " + 
    			"`StationTo`) " + 
    			"VALUES " + 
    			"(" + routeID + ", " +
    			"'" + route.get(0) + "', " + 
    			"'" + route.get(1) + "', " + 
    			"'" + route.get(2) + "', " + 
    			"'" + route.get(3) + "', " + 
    			"" + route.get(4) + ", " + 
    			"'" + route.get(5) + "', " + 
    			"'" + route.get(6) + "', " + 
    			"'" + route.get(7) + "'); " + 
    			"");
    	
    	db.insertData("INSERT INTO `RailwayStation`.`Schedule` " + 
    			"(`ScheduleID`, " + 
    			"`ArrivalTime`, " + 
    			"`DepartureTime`, " + 
    			"`RouteID`, " + 
    			"`StationAbbr`) " + 
    			"VALUES " + 
    			"(1, " + 
    			"'" + route.get(2) + "', " + 
    			"'" + route.get(2) + "', " + 
    			"" + routeID + ", " + 
    			"'" + route.get(6) + "');");
    	
    	db.insertData("INSERT INTO `RailwayStation`.`Schedule` " + 
    			"(`ScheduleID`, " + 
    			"`ArrivalTime`, " + 
    			"`DepartureTime`, " + 
    			"`RouteID`, " + 
    			"`StationAbbr`) " + 
    			"VALUES " + 
    			"(2, " + 
    			"'" + route.get(3) + "', " + 
    			"'" + route.get(3) + "', " + 
    			"" + routeID + ", " + 
    			"'" + route.get(7) + "');");
    	
    	db.insertData("INSERT INTO `RailwayStation`.`News` " + 
    			"(`Text`) " + 
    			"VALUES " + 
    			"('" + route.get(8) + "');");
    	
    	db.closeConnection();
    	
        return Response.ok().build(); 
    }
    
    @DELETE
    public Response deleteRoute(@FormParam("routeID") int routeID) throws ClassNotFoundException, SQLException {
    	
    	if (routeID < 0) {
			
			return Response.serverError().entity("Error! Negative RouteID is given!").build();
		}
    	
    	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
    	
    	
    	ResultSet rs = db.getData("SELECT * FROM Route WHERE RouteID = '" + routeID + "';");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! Given route is not present in the database!").build();
		
    	db.deleteData("DELETE FROM `RailwayStation`.`ScheduleHasTrain` " + 
    			"WHERE RouteID = " + routeID + ";");
    	
    	db.deleteData("DELETE FROM `RailwayStation`.`Ticket` " + 
    			"WHERE RouteID = " + routeID + ";");
    	
    	db.deleteData("DELETE FROM `RailwayStation`.`Schedule` " + 
    			"WHERE RouteID = " + routeID + ";");
    	
    	db.deleteData("DELETE FROM `RailwayStation`.`Route` " + 
    			"WHERE RouteID = " + routeID + ";");
    	
    	db.closeConnection();
    	
        return Response.ok().build(); 
    }
}