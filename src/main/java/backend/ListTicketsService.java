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

		ResultSet rs = db.getData("SELECT T.TicketID, T.DepartureDate, T.ArrivingDate, T.Price, T.OrderID, T.RouteID, T.TrainID, T.SeatNumber, T.CarriageNumber, T.ScheduleFromID, T.ScheduleToID "
				+ "FROM `User` U, `Order` O, `Ticket` T "
				+ "WHERE U.Email = '" + email + "' AND O.UserEmail = U.Email AND T.OrderID = O.OrderID;");

		while (rs.next()) {

			tickets.add(new CopyOnWriteArrayList<String>());
			tickets.get(tickets.size() - 1).add(rs.getString("TicketID"));
			tickets.get(tickets.size() - 1).add(rs.getString("DepartureDate"));
			tickets.get(tickets.size() - 1).add(rs.getString("ArrivingDate"));
			tickets.get(tickets.size() - 1).add(rs.getString("Price"));
			tickets.get(tickets.size() - 1).add(rs.getString("OrderID"));
			tickets.get(tickets.size() - 1).add(rs.getString("RouteID"));
			tickets.get(tickets.size() - 1).add(rs.getString("TrainID"));
			tickets.get(tickets.size() - 1).add(rs.getString("SeatNumber"));
			tickets.get(tickets.size() - 1).add(rs.getString("CarriageNumber"));
			tickets.get(tickets.size() - 1).add(rs.getString("ScheduleFromID"));
			tickets.get(tickets.size() - 1).add(rs.getString("ScheduleToID"));

		}
	
		db.closeConnection();
	    
        Gson gson = new Gson();
        return Response.ok(gson.toJson(tickets)).build();
	    
    }
}
