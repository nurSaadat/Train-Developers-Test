package backend;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

@Path("/routes")
public class ListRoutesService {
    
    private List<List<String>> routes = new CopyOnWriteArrayList<>();
    
    public ListRoutesService() throws SQLException, ClassNotFoundException {

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
        
    }
    
    @GET
    public Response getList() {
	    
        Gson gson = new Gson();
        return Response.ok(gson.toJson(routes)).build();
	    
    }
    
    @GET
    @Path("{id: [0-9]+}")
    public Response getListItem(@PathParam("id") String id) {
	    
        int i = Integer.parseInt(id);
        return Response.ok(routes.get(i)).build();
	    
    }
    
    @POST
    public Response postListItem(@FormParam("newEntry") List<String> entry) {
    	
        routes.add(0, entry);
        return Response.ok().build();
        
    }
    
    @DELETE
    public Response clear() {
    	
    	Gson gson = new Gson();
    	routes.clear();
    	return Response.ok(gson.toJson(routes)).build();
    	
    }
    
    @DELETE
    @Path("{id: [0-9]+}")
    public Response clearItem(@PathParam("id") String id) {
	    
    	int i = Integer.parseInt(id);
    	routes.remove(i);
        return Response.ok(routes).build();
        
    }
    
}
