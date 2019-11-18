package backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/checkin")
public class CheckLogInService {	
	@GET
    public Response checkin(@Context HttpServletRequest request) {
		
		HttpSession Session = request.getSession(false);
		
        if (Session != null) {
        	return Response.ok(Session.getAttribute("type")).build();
        } else {
        	return Response.ok("false").build();
        }		
        
	}

}
