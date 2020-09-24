package backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/logs")
public class ListLogsService {

	private static boolean log = false;
	
	@GET
    public Response getList(@QueryParam("page") int page, @QueryParam("type") String type) throws ClassNotFoundException, SQLException, IOException {
    	
		if(page < 0)
			return Response.serverError().entity("Error! Page number is negative!").build();
		
		if(page == 0)
			return Response.ok(log).build(); 
		
		if(type == null || type.compareTo("") == 0)
			return Response.serverError().entity("Error! Type field is empty!").build();
		
    	List<String> logs = new CopyOnWriteArrayList<>();
    	File logsFile;
    	
    	if(!(type.compareTo("auth") == 0))
    		logsFile = new File("Project_log/server.log");
    	else
    		logsFile = new File("Project_log/serverAuth.log");
    	
    	BufferedReader bf = new BufferedReader(new FileReader(logsFile));
    	
    	int lineNum = 0;
    	
    	while(bf.readLine() != null)
    		lineNum++;
    	
    	bf.close();
    	bf = new BufferedReader(new FileReader(logsFile));
    	
    	for(int i = 0; i < lineNum - (page  * 50); i++) {
    		
    		if(bf.readLine() == null)
    			break;
    	}
    		
    	
    	for(int i = 0; i < 50; i++) {
    		
    		String line = bf.readLine();
    		
    		if(line != null) {
    			
    			logs.add(line);	
    		} else
    			break;
    	}
    	
    	bf.close();
    	
        Collections.reverse(logs);
	    
        Gson gson = new Gson();
        return Response.ok(gson.toJson(logs)).build(); 
    }
	
	@POST
	public Response addNews() {

		log = !log;
		
		return Response.ok(log).build();
	}
	
	public static boolean isLog() {
		
		return log;
	}
}
