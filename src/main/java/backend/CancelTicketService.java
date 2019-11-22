package backend;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Path("/cancel")
public class CancelTicketService {

    @POST
    public Response register(@FormParam("OrderID") String OrderID, @FormParam("TicketID") String TicketID) throws SQLException, ClassNotFoundException {
        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        if (OrderID == null || TicketID== null) {

            return Response.serverError().entity("Error! One of the fields is empty!").build();

        }


        db.insertData("INSERT INTO `CancelledTicket` (OrderID, TicketID) VALUES (" + OrderID +", " + TicketID + ");");


        db.closeConnection();

        return Response.ok().build();
    }

    @GET
    public Response getList(@Context HttpServletRequest req) throws ClassNotFoundException, SQLException {

        List<List<String>> tickets = new CopyOnWriteArrayList<>();
        
        HttpSession session = req.getSession(false);

		String email = (String)session.getAttribute("email");

        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        ResultSet rs = db.getData("select distinct O.UserEmail, O.OrderID, T.TicketID,  SD.StationAbbr, T.DepartureDate, SD.DepartureTime, SA.StationAbbr as StationTo, T.ArrivingDate as ArrivalDate, SA.ArrivalTime, T.Price, T.TrainID, T.CarriageNumber, T.SeatNumber, P.DocumentID, P.FName as `Name`\n" +
                "                from `Order` O, `Ticket` T, `Passenger` P, `Schedule` SD, `Schedule` SA, `Route` R, `CancelledTicket` CT\n" +
                "                where T.OrderID = O.OrderID and P.TicketID = T.TicketID and P.OrderID = T.OrderID and T.ScheduleFromID = SD.ScheduleID and T.ScheduleToID = SA.ScheduleID and CT.OrderID = O.OrderID and CT.TicketID = T.TicketID and O.UserEmail = '" + email + "' and R.RouteID = T.RouteID and R.RouteID = SA.RouteID and R.RouteID = SD.RouteID;");

        while (rs.next()) {

            tickets.add(new CopyOnWriteArrayList<String>());
            tickets.get(tickets.size() - 1).add(rs.getString("UserEmail"));
            tickets.get(tickets.size() - 1).add(rs.getString("OrderID"));
            tickets.get(tickets.size() - 1).add(rs.getString("TicketID"));
            tickets.get(tickets.size() - 1).add(rs.getString("StationAbbr"));
            tickets.get(tickets.size() - 1).add(rs.getString("DepartureDate"));
            tickets.get(tickets.size() - 1).add(rs.getString("DepartureTime"));
            tickets.get(tickets.size() - 1).add(rs.getString("StationTo"));
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

    @DELETE
    public Response deleteTicket(@FormParam("orderID") int orderID, @FormParam("ticketID") int ticketID) throws ClassNotFoundException, SQLException {

        if (orderID < 0 || ticketID < 0) {

            return Response.serverError().entity("Error! Negative OrderID or Ticket is given!").build();
        }

        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");


        ResultSet rs = db.getData("SELECT * FROM CancelledTicket WHERE OrderID = '" + orderID + "' and TicketID = '" + ticketID + "';");

        if(!rs.next())
            return Response.serverError().entity("Error! Given ticket is not present in the database!").build();

        db.deleteData("DELETE FROM `RailwayStation`.`CancelledTicket` " +
                " WHERE OrderID = '" + orderID + "' and TicketID = '" + ticketID + "';");

        db.deleteData("DELETE FROM `RailwayStation`.`Passenger` " +
                " WHERE OrderID = '" + orderID + "' and TicketID = '" + ticketID + "';");

        db.deleteData("DELETE FROM `RailwayStation`.`Ticket` " +
                " WHERE OrderID = '" + orderID + "' and TicketID = '" + ticketID + "';");

        db.deleteData("DELETE FROM `RailwayStation`.`Order` " +
                "WHERE OrderID = '" + orderID + "';");

        db.closeConnection();

        return Response.ok().build();
    }
    
    @Path("/confirm")
    @GET
    public Response getListAgent(@Context HttpServletRequest req) throws ClassNotFoundException, SQLException {

        List<List<String>> tickets = new CopyOnWriteArrayList<>();

        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        ResultSet rs = db.getData("select distinct O.UserEmail, O.OrderID, T.TicketID,  SD.StationAbbr, T.DepartureDate, SD.DepartureTime, SA.StationAbbr as StationTo, T.ArrivingDate as ArrivalDate, SA.ArrivalTime, T.Price, T.TrainID, T.CarriageNumber, T.SeatNumber, P.DocumentID, P.FName as `Name`\n" +
                "                from `Order` O, `Ticket` T, `Passenger` P, `Schedule` SD, `Schedule` SA, `CancelledTicket` CT, `Route` R\n" +
                "                where T.OrderID = O.OrderID and P.TicketID = T.TicketID and P.OrderID = T.OrderID and T.ScheduleFromID = SD.ScheduleID and T.ScheduleToID = SA.ScheduleID and CT.OrderID = O.OrderID and CT.TicketID = T.TicketID and R.RouteID = SA.RouteID and R.RouteID = SD.RouteID and T.RouteID = R.RouteID;");

        while (rs.next()) {

            tickets.add(new CopyOnWriteArrayList<String>());
            tickets.get(tickets.size() - 1).add(rs.getString("UserEmail"));
            tickets.get(tickets.size() - 1).add(rs.getString("OrderID"));
            tickets.get(tickets.size() - 1).add(rs.getString("TicketID"));
            tickets.get(tickets.size() - 1).add(rs.getString("StationAbbr"));
            tickets.get(tickets.size() - 1).add(rs.getString("DepartureDate"));
            tickets.get(tickets.size() - 1).add(rs.getString("DepartureTime"));
            tickets.get(tickets.size() - 1).add(rs.getString("StationTo"));
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
