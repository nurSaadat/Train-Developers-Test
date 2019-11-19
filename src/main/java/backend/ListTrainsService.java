package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/trains")
public class ListTrainsService {

	@GET
    public Response getList() throws ClassNotFoundException, SQLException {
    	
    	List<List<String>> trains = new CopyOnWriteArrayList<>();
    	
    	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
    	
    	ResultSet rs = db.getData("SELECT * FROM Train;");
    	
        trains.clear();
        
        while (rs.next()) {
        	
            trains.add(new CopyOnWriteArrayList<String>());
            trains.get(trains.size() - 1).add(rs.getString("TrainID"));
            trains.get(trains.size() - 1).add(rs.getString("Capacity"));
            trains.get(trains.size() - 1).add(rs.getString("NumberOfCarriages"));
            trains.get(trains.size() - 1).add(rs.getString("Type"));
        }

        Collections.reverse(trains);
        db.closeConnection();

        Gson gson = new Gson();
        return Response.ok(gson.toJson(trains)).build();

    }
}
