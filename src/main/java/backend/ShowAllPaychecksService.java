package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/show-all-paychecks")
public class ShowAllPaychecksService {

	@GET
    public Response listPaychecks(@QueryParam("page") int page, @Context HttpServletRequest request) throws ClassNotFoundException, SQLException {			
		
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			
			String email = (String)session.getAttribute("email");
			
			List<List<String>> paychecks = getPaychecks(email);    
	        PagedHelper p = new PagedHelper();
	        int max_page;
	        Gson gson = new Gson();
	        
	        if (paychecks.size() % 10 != 0) {
	        	max_page = (paychecks.size() / 10) + 1 ;
	        }
	        else {
	        	max_page = paychecks.size() / 10;
	        }
	        
	        if (page * 10 + 10 > paychecks.size()){
	            p.setList(paychecks.subList(page * 10, paychecks.size()));
	        }
	        else {
	        	p.setList(paychecks.subList(page * 10, page * 10 + 10));
	        }
	        
	        if (page == max_page - 1) {
	        	p.setNext(String.valueOf(-1));
	        }
	        else {
	        	 p.setNext(String.valueOf(page + 1));
	        }       
	        
	        p.setPrev(String.valueOf(page - 1));
	        
	        return Response.ok(gson.toJson(p, PagedHelper.class)).build();	
		} else {
			
			return Response.serverError().entity("Error! No session does not exists!").build();
		}
	}
	
	private List<List<String>> getPaychecks(String email) throws ClassNotFoundException, SQLException {
		List<List<String>> paychecks = new CopyOnWriteArrayList<>();		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");	
		
		ResultSet rs = db.getData("SELECT P.Date, P.Sum FROM Paycheck P, Employee E WHERE P.ContractID = E.ContractID AND E.Email = '" + email + "' ORDER BY P.Date DESC;");
		
		while (rs.next()) {
			List<String> instance = new ArrayList<>();
			instance.add(rs.getString("Date"));
			instance.add(rs.getString("Sum"));
			paychecks.add(instance);
		}
		
		db.closeConnection();
		
		return paychecks;		
	}
}
