package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/show-all-news")
public class ShowAllNewsService {
	
	@GET
    public Response getNews(@QueryParam("page") int page) throws ClassNotFoundException, SQLException {			
		List<List<String>> news = GetNews();    
        PagedHelper p = new PagedHelper();
        int max_page;
        Gson gson = new Gson();
        
        if (news.size() % 10 != 0) {
        	max_page = (news.size() / 10) + 1 ;
        }
        else {
        	max_page = news.size() / 10;
        }
        
        if (page * 10 + 10 > news.size()){
            p.setList(news.subList(page * 10, news.size()));
        }
        else {
        	p.setList(news.subList(page * 10, page * 10 + 10));
        }
        
        if (page == max_page - 1) {
        	p.setNext(String.valueOf(-1));
        }
        else {
        	 p.setNext(String.valueOf(page + 1));
        }       
        
        p.setPrev(String.valueOf(page - 1));
        
        return Response.ok(gson.toJson(p, PagedHelper.class)).build();
	}
	
	private List<List<String>> GetNews() throws ClassNotFoundException, SQLException {
		List<List<String>> news = new CopyOnWriteArrayList<>();		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");	
		
		ResultSet rs = db.getData("SELECT * "
				+ "FROM `News` "
				+ "ORDER BY NewsID DESC;");
		
		while (rs.next()) {
			List<String> instance = new ArrayList<>();
			instance.add(rs.getString("Text"));
			instance.add(rs.getString("Title"));
			instance.add(rs.getString("Date"));
			news.add(instance);
		}
		
		db.closeConnection();
		
		return news;		
	}
	
}
