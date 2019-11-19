package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/show-recent-news")
public class ShowRecentNewsService {
	
	@GET
    public Response checkin(@Context HttpServletRequest request) throws ClassNotFoundException, SQLException {
		
		List<List<String>> news = new CopyOnWriteArrayList<>();
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
		
		ResultSet rs = db.getData("SELECT * "
				+ "FROM `News` "
				+ "ORDER BY NewsID DESC LIMIT 3;");
		
		while (rs.next()) {
			List<String> instance = new ArrayList<>();
			instance.add(rs.getString("Text"));
			instance.add(rs.getString("Title"));
			instance.add(rs.getString("Date"));
			news.add(instance);
		}
		
		db.closeConnection();
	    
        Gson gson = new Gson();
        return Response.ok(gson.toJson(news)).build();
	}
	
}

