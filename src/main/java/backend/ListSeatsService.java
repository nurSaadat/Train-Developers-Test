package backend;

import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/seats")
public class ListSeatsService {

    @GET
    public Response getList(@QueryParam("RouteID") int RouteID) throws ClassNotFoundException, SQLException {
    	
    	List<List<String>> seats = new CopyOnWriteArrayList<>();
    	
    	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        ResultSet rs = db.getData("select distinct S.Number, S.Status, C.CarriageNumber, Cl.ClassName, Cl.Price, T.TrainID\r\n" + 
        		"from (Seat S, Carriage C, Train T, ScheduleHasTrain SHT, Class Cl) left outer join Ticket TI on TI.CarriageNumber = C.CarriageNumber and TI.RouteID = SHT.RouteID and TI.SeatNumber = S.Number\r\n" + 
        		"where S.CarriageID = C.CarriageNumber and C.TrainID = T.TrainID and T.TrainID = SHT.TrainID and SHT.RouteID = " + RouteID + " and C.Class = Cl.ClassName and (TI.TicketID is NULL or (TI.TicketID is not NULL and (TI.TicketID, TI.OrderID) in (select CT.TicketID, CT.OrderID\r\n" + 
        				"                                                                                                                                                                            from CancelledTicket CT\r\n" + 
        				"                                                                                                                                                                            )));");
        
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
}