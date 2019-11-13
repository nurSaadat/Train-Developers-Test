package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/tickets")
public class ListTicketsService {
	
	@GET
    public Response getList(@Context HttpServletRequest req) throws ClassNotFoundException, SQLException {
		
		List<List<String>> tickets = new CopyOnWriteArrayList<>();
		
		HttpSession session = req.getSession(false);
		
		String email = (String)session.getAttribute("email");
		
		MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

		ResultSet rs = db.getData("select T.TicketID, O.OrderID, T.DepartureDate, SD.DepartureTime, T.ArrivingDate as ArrivalDate, SA.ArrivalTime, T.Price, T.TrainID, T.CarriageNumber, T.SeatNumber, P.DocumentID, concat(P.FName, \" \", P.LName) as `Name`\n" +
				"from `User` U, `Order` O, `Ticket` T, `Passenger` P, `Schedule` SD, `Schedule` SA\n" + 
				"where U.Email = '" + email + "' and O.UserEmail = U.Email and T.OrderID = O.OrderID and P.TicketID = T.TicketID and P.OrderID = T.OrderID and T.ScheduleFromID = SD.ScheduleID and T.ScheduleToID = SA.ScheduleID;");

		while (rs.next()) {

			tickets.add(new CopyOnWriteArrayList<String>());
			tickets.get(tickets.size() - 1).add(rs.getString("TicketID"));
			tickets.get(tickets.size() - 1).add(rs.getString("OrderID"));
			tickets.get(tickets.size() - 1).add(rs.getString("DepartureDate"));
			tickets.get(tickets.size() - 1).add(rs.getString("DepartureTime"));
			tickets.get(tickets.size() - 1).add(rs.getString("ArrivalDate"));
			tickets.get(tickets.size() - 1).add(rs.getString("ArrivalTime"));
			tickets.get(tickets.size() - 1).add(rs.getString("Price"));
			tickets.get(tickets.size() - 1).add(rs.getString("TrainID"));
			tickets.get(tickets.size() - 1).add(rs.getString("CarriageNumber"));
			tickets.get(tickets.size() - 1).add(rs.getString("SeatNumber"));
			tickets.get(tickets.size() - 1).add(rs.getString("DocumentID"));
			tickets.get(tickets.size() - 1).add(rs.getString("Name"));


		}
	
		db.closeConnection();
	    
        Gson gson = new Gson();
        return Response.ok(gson.toJson(tickets)).build();
	    
    }
}
