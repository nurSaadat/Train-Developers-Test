package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/schedules")
public class ListSchedulesService {

	@GET
    public Response getList(@QueryParam("RouteID") int RouteID) throws ClassNotFoundException, SQLException {
    	
    	List<List<String>> schedules = new CopyOnWriteArrayList<>();
    	
    	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
    	
    	ResultSet rs = db.getData("SELECT * FROM Route WHERE RouteID = '" + RouteID + "';");
    	
    	if(!rs.next())
			return Response.serverError().entity("Error! Given route is not present in the database!").build();

        rs = db.getData("SELECT * FROM Schedule WHERE RouteID = " + RouteID + ";");
    	
        schedules.clear();
        
        while (rs.next()) {
        	
            schedules.add(new CopyOnWriteArrayList<String>());
            schedules.get(schedules.size() - 1).add(rs.getString("ScheduleID"));
            schedules.get(schedules.size() - 1).add(rs.getString("ArrivalTime"));
            schedules.get(schedules.size() - 1).add(rs.getString("DepartureTime"));
            schedules.get(schedules.size() - 1).add(rs.getString("StationAbbr"));
        }

        Collections.reverse(schedules);
        db.closeConnection();

        Gson gson = new Gson();
        return Response.ok(gson.toJson(schedules)).build();

    }
}
