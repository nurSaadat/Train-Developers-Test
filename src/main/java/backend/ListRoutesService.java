package backend;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.ws.rs.GET;
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
}