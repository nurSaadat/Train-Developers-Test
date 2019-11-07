package backend;

import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/seats")
public class ListSeatsService {

    @GET
    public Response getList(@QueryParam("RouteID") int RouteID) throws ClassNotFoundException, SQLException {
    	
    	List<List<String>> seats = new CopyOnWriteArrayList<>();
    	
    	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        ResultSet rs = db.getData("select distinct S.Number, S.Status, C.CarriageNumber, Cl.ClassName, Cl.Price, T.TrainID\r\n" + 
        		"from Seat S, Carriage C, Train T, ScheduleHasTrain SHT, Class Cl\r\n" + 
        		"where S.CarriageID = C.CarriageNumber and C.TrainID = T.TrainID and T.TrainID = SHT.TrainID and SHT.RouteID = '" + RouteID + "' and C.Class = Cl.ClassName;");
        
        seats.clear();
        
        while (rs.next()) {
            seats.add(new CopyOnWriteArrayList<String>());
            seats.get(seats.size() - 1).add(rs.getString("Number"));
            seats.get(seats.size() - 1).add(rs.getString("Status"));
            seats.get(seats.size() - 1).add(rs.getString("CarriageNumber"));
            seats.get(seats.size() - 1).add(rs.getString("ClassName"));
            seats.get(seats.size() - 1).add(rs.getString("Price"));
            seats.get(seats.size() - 1).add(rs.getString("TrainID"));

        }

        Collections.reverse(seats);
        db.closeConnection();

        Gson gson = new Gson();
        return Response.ok(gson.toJson(seats)).build();

    }

    @POST
    public Response updateSeat(@FormParam("seatNum") String seatNum, @FormParam("carriageID") String carriageID) throws SQLException, ClassNotFoundException {

        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
        if (seatNum== null || carriageID== null) {

            return Response.serverError().entity("Error! One of the fields is empty!").build();

        }

        db.updateData("update Seat\n" +
                "set Status = 'Reserved'\n" +
                "where Number = " + seatNum + " and CarriageID = " + carriageID + ";");

        return Response.ok().build();
    }
}