package backend;

import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Path("/edit")
public class EditTicketService {
    private static final String DEPARTUREDATE_PATTERN = "[0-9]{4}-[0-9]*-[0-9]*";
    private static final String ARRIVINGDATE_PATTERN = "[0-9]{4}-[0-9]*-[0-9]*";
    private static final String PRICE_PATTERN = "[0-9]+\\.?[0-9]*";
    private static final String FNAME_PATTERN = "[A-Z][a-z]*";
    private static final String LNAME_PATTERN = "[A-Z][a-z]*";
    private static final String DOCUMENTID_PATTERN = "[0-9]*";
    private static final String PHONENUMBER_PATTERN = "[0-9]{10}";
    private static final String DOB_PATTERN = "[0-9]{4}+-[0-9]+-[0-9]+";

    @POST
    public Response register(@FormParam("ticket[]") List<String> ticket, @FormParam("passenger[]") List<String> passenger) throws SQLException, ClassNotFoundException {
        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        if (ticket.get(0) == null || ticket.get(1) == null || ticket.get(2) == null || ticket.get(3) == null || ticket.get(4) == null || ticket.get(5) == null || ticket.get(6) == null|| ticket.get(7) == null || ticket.get(8) == null || ticket.get(9) == null || ticket.get(10) == null  ) {

            return Response.serverError().entity("Error! One of the fields is empty! book").build();

        }

        if (passenger.get(0) == null || passenger.get(1)== null || passenger.get(2) == null ||  passenger.get(3) == null || passenger.get(4) == null || passenger.get(5) == null || passenger.get(6) == null || passenger.get(7) == null || passenger.get(8) == null) {

            return Response.serverError().entity("Error! One of the fields is empty!").build();

        }
        System.out.println(passenger.toString());
        if (!ticket.get(3).matches(PRICE_PATTERN)) {

            return Response.serverError().entity("Error! Price provided is invalid!").build();

        }

        if (!ticket.get(1).matches(DEPARTUREDATE_PATTERN)) {

            return Response.serverError().entity("Error! Departure date provided is invalid!").build();

        }

        if (!ticket.get(2).matches(ARRIVINGDATE_PATTERN)) {

            return Response.serverError().entity("Error! Arriving date provided is invalid!").build();

        }

        if (!passenger.get(3).matches(FNAME_PATTERN)) {

            return Response.serverError().entity("Error! First Name provided is invalid!").build();

        }

        if (!passenger.get(4).matches(LNAME_PATTERN)) {

            return Response.serverError().entity("Error! Last Name provided is invalid!").build();

        }

        if (!passenger.get(2).matches(DOCUMENTID_PATTERN)) {

            return Response.serverError().entity("Error! Document ID provided is invalid! ").build();

        }

        if (!passenger.get(5).matches(PHONENUMBER_PATTERN)) {

            return Response.serverError().entity("Error! Number provided is invalid!").build();

        }

        if (!passenger.get(8).matches(DOB_PATTERN)) {

            return Response.serverError().entity("Error! Date of birth provided is invalid!" + passenger.get(8)).build();

        }

        ResultSet rs = db.getData("select SD.ScheduleID, SA.ScheduleID\r\n" +
                "from Schedule SD, Schedule SA, Route R\r\n" +
                "where SA.RouteID = R.RouteID and SD.RouteID = R.RouteID and SA.StationAbbr = R.StationTo and SD.StationAbbr = R.StationFrom and R.RouteID = " + ticket.get(5) + ";");

        rs.next();
        int ScheduleFromID = rs.getInt(1);
        int ScheduleToID = rs.getInt(2);

        String gender1;

        if(passenger.get(7).compareTo("Male") == 0)
            gender1 = "M";
        else
            gender1 = "F";

        db.insertData("INSERT INTO `EditTicket` ("
                + "TicketID,"
                + "DepartureDate, "
                + "ArrivingDate, "
                + "Price, "
                + "OrderID, "
                + "RouteID, "
                + "TrainID, "
                + "SeatNumber, "
                + "CarriageNumber, "
                + "ScheduleFromID, "
                + "ScheduleToID, "
                + "DocumentType,"
                + "Tariff,"
                + "DocumentID,"
                + "FName,"
                + "LName,"
                + "PhoneNumber,"
                + "Citizenship,"
                + "Gender,"
                + "DateOfBirth) VALUES ('"
                + ticket.get(0) + "', '"
                + ticket.get(1) + "', '"
                + ticket.get(2) + "', '"
                + ticket.get(3) + "', "
                + ticket.get(4) + ", "
                + ticket.get(5) + ", "
                + ticket.get(6) + ", "
                + ticket.get(7) + ", "
                + ticket.get(8) + ", "
                + ScheduleFromID + ", "
                + ScheduleToID + ", '"
                + passenger.get(0) + "', '"
                + passenger.get(1) + "', "
                + passenger.get(2) + ", '"
                + passenger.get(3) + "', '"
                + passenger.get(4) + "', "
                + passenger.get(5) + ", '"
                + passenger.get(6) + "', '"
                + gender1 + "', '"
                + passenger.get(8) + "');");


        db.closeConnection();
        return Response.ok().build();
    }

    @GET
    public Response getList(@Context HttpServletRequest req) throws ClassNotFoundException, SQLException {

        List<List<String>> tickets = new CopyOnWriteArrayList<>();

        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        ResultSet rs = db.getData("                select * from EditTicket join (select SD.StationAbbr, SD.DepartureTime, SA.StationAbbr as toStation, SA.ArrivalTime, R.RouteID as route from `Schedule` SD, `Schedule` SA, Route R where SA.RouteID = R.RouteID and SD.RouteID = R.RouteID and SA.StationAbbr = R.StationTo and SD.StationAbbr = R.StationFrom group by R.RouteID) as abbrT on EditTicket.RouteID = abbrT.route join (select O.UserEmail, O.OrderID from `Order` O, `Ticket` T where T.OrderID = O.OrderID) as emailT on EditTicket.OrderID = emailT.OrderID  ");

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
            tickets.get(tickets.size() - 1).add(rs.getString("DocumentType"));
            tickets.get(tickets.size() - 1).add(rs.getString("Tariff"));
            tickets.get(tickets.size() - 1).add(rs.getString("DocumentID"));
            tickets.get(tickets.size() - 1).add(rs.getString("FName"));
            tickets.get(tickets.size() - 1).add(rs.getString("LName"));
            tickets.get(tickets.size() - 1).add(rs.getString("PhoneNumber"));
            tickets.get(tickets.size() - 1).add(rs.getString("Citizenship"));
            tickets.get(tickets.size() - 1).add(rs.getString("Gender"));
            tickets.get(tickets.size() - 1).add(rs.getString("DateOfBirth"));
            tickets.get(tickets.size() - 1).add(rs.getString("StationAbbr"));
            tickets.get(tickets.size() - 1).add(rs.getString("DepartureTime"));
            tickets.get(tickets.size() - 1).add(rs.getString("toStation"));
            tickets.get(tickets.size() - 1).add(rs.getString("ArrivalTime"));
            tickets.get(tickets.size() - 1).add(rs.getString("route"));
            tickets.get(tickets.size() - 1).add(rs.getString("UserEmail"));
        }

        db.closeConnection();

        Gson gson = new Gson();
        return Response.ok(gson.toJson(tickets)).build();

    }

    @Path("/confirm")
    @POST
    public Response login(@FormParam("orderID") String OrderID, @FormParam("ticketID") String TicketID) throws SQLException, ClassNotFoundException {
        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
        ResultSet rs = db.getData("select * from EditTicket where OrderID = " + OrderID + " and  TicketID = " + TicketID + ";");
        rs.next();

        db.updateData("UPDATE Ticket SET"
                + " DepartureDate = '" + rs.getString(2)
                + "', ArrivingDate = '" + rs.getString(3)
                + "', Price = '" + rs.getString(4)
                + "', RouteID = '" + rs.getString(6)
                + "', TrainID = '" + rs.getString(7)
                + "', SeatNumber = '" + rs.getString(8)
                + "', CarriageNumber = '" + rs.getString(9)
                + "', ScheduleFromID = '" + rs.getString(10)
                + "', ScheduleToID = '" + rs.getString(11)
                + "' WHERE TicketID = '" + TicketID
                + "' and OrderID = '" + OrderID + "';" );

        db.updateData(" UPDATE `Passenger` SET"
                + " DocumentType = '" + rs.getString(12)
                + "', Tariff = '" + rs.getString(13)
                + "', DocumentID = '" + rs.getString(14)
                + "', FName = '" + rs.getString(15)
                + "', LName = '" + rs.getString(16)
                + "', PhoneNumber = '" + rs.getString(17)
                + "', Citizenship = '" + rs.getString(18)
                + "', Gender = '" + rs.getString(19)
                + "', DateOfBirth = '" + rs.getString(20)
                + "' WHERE TicketID = '" + TicketID
                + "' and OrderID = '" + OrderID + "';" );

        db.deleteData(" DELETE FROM `RailwayStation`.`EditTicket` " +
                " WHERE OrderID = '" + OrderID + "' and TicketID = '" + TicketID + "';");

        db.closeConnection();
        return Response.ok().build();

    }
}