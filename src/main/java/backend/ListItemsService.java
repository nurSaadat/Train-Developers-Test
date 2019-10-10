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

@Path("/items")
public class ListItemsService {
    
    private List<String> list = new CopyOnWriteArrayList<String>();
    
    public ListItemsService() {
    	
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
    	
        for (int i = 0; i < 20; i++) {
            list.add("Entry " + String.valueOf(i) + "@" + dtf.format(now).toString());

        }
        
        Collections.reverse(list);
    }
    
    @GET
    public Response getList() {
        Gson gson = new Gson();
        
        return Response.ok(gson.toJson(list)).build();
    }
    
    @GET
    @Path("{id: [0-9]+}")
    public Response getListItem(@PathParam("id") String id) {
        int i = Integer.parseInt(id);
        
        return Response.ok(list.get(i)).build();
    }
    
    @POST
    
    public Response postListItem(@FormParam("newEntry") String entry) {
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
        list.add(0, entry +  "@" + dtf.format(now).toString());
        return Response.ok().build();
        
    }
    
    @DELETE
    public Response clear() {
    	
    	Gson gson = new Gson();
    	list.clear();
    	return Response.ok(gson.toJson(list)).build();
    	
    }
    
    @DELETE
    @Path("{id: [0-9]+}")
    public Response clearItem(@PathParam("id") String id) {
    	int i = Integer.parseInt(id);
    	list.remove(i);
        return Response.ok(list).build();
        
    }
    
}
