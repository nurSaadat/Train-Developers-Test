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

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 

import com.google.gson.Gson;

@Path("/routes")
public class ListRoutesService {
    
    private List<List<String>> routes = new CopyOnWriteArrayList<>();
    
    public ListRoutesService() {
    	
        for (int i = 0; i < 5; i++) {
        	
        	routes.add(new CopyOnWriteArrayList<String>());
        	routes.get(i).add(Integer.toString(i));
        	routes.get(i).add("0" + Integer.toString(i) + "-10-2019");
        	routes.get(i).add("1" + Integer.toString(i) + "-10-2019");
        	routes.get(i).add("0" + Integer.toString(i) + ":00");
        	routes.get(i).add("1" + Integer.toString(i) + ":00");
        	routes.get(i).add("1250");
        	routes.get(i).add("In transit");
        	routes.get(i).add("AST");
        	routes.get(i).add("ALM"); 

        }
        
        Collections.reverse(routes);
        
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
