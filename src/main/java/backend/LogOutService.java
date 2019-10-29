package backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/logout")
public class LogOutService {
	
	
	@POST
    public Response logout(@Context HttpServletRequest request) {
		
		HttpSession Session = request.getSession(false);
		
		String token = (String)Session.getAttribute("type");
		
        if (Session != null) 
        	Session.invalidate();
        
        return Response.ok(token).build();
	}

}
