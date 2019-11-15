package backend;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/book")
public class BookTicketService {
    private static final String DEPARTUREDATE_PATTERN = "[0-9]{4}-[0-9]*-[0-9]*";
    private static final String ARRIVINGDATE_PATTERN = "[0-9]{4}-[0-9]*-[0-9]*";
    private static final String PRICE_PATTERN = "[0-9]+\\.?[0-9]*";

    @POST
    public Response register(@FormParam("DepartureDate") String DepartureDate, @FormParam("ArrivingDate") String ArrivingDate, @FormParam("Price") String Price, @FormParam("OrderID") String OrderID, @FormParam("RouteID") String RouteID, @FormParam("TrainID") String TrainID, @FormParam("SeatNumber") String SeatNumber, @FormParam("CarriageNumber") String CarriageNumber ) throws SQLException, ClassNotFoundException {
        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        if (DepartureDate == null || ArrivingDate== null || Price== null || RouteID == null || TrainID == null || SeatNumber == null|| CarriageNumber == null ) {

            return Response.serverError().entity("Error! One of the fields is empty! book").build();

        }

        if (!Price.matches(PRICE_PATTERN)) {

            return Response.serverError().entity("Error! Price provided is invalid!").build();

        }

        if (!DepartureDate.matches(DEPARTUREDATE_PATTERN)) {

            return Response.serverError().entity("Error! Departure date provided is invalid!").build();

        }

        if (!ArrivingDate.matches(ARRIVINGDATE_PATTERN)) {

            return Response.serverError().entity("Error! Arriving date provided is invalid!").build();

        }
        
        ResultSet rs = db.getData("select SD.ScheduleID, SA.ScheduleID\r\n" + 
        		"from Schedule SD, Schedule SA, Route R\r\n" + 
        		"where SA.RouteID = R.RouteID and SD.RouteID = R.RouteID and SA.StationAbbr = R.StationTo and SD.StationAbbr = R.StationFrom and R.RouteID = " + RouteID + ";");

        rs.next();
        int ScheduleFromID = rs.getInt(1);
        int ScheduleToID = rs.getInt(2);
        
        db.insertData("INSERT INTO `Ticket` ("
        		+ "DepartureDate, "
        		+ "ArrivingDate, "
        		+ "Price, "
        		+ "OrderID, "
        		+ "RouteID, "
        		+ "TrainID, "
        		+ "SeatNumber, "
        		+ "CarriageNumber, "
        		+ "ScheduleFromID, "
        		+ "ScheduleToID) VALUES ('" 
        		+ DepartureDate + "', '" 
        		+ ArrivingDate + "', " 
        		+ Price + ", " 
        		+ OrderID + ", " 
        		+ RouteID + ", " 
        		+ TrainID + ", " 
        		+ SeatNumber + ", " 
        		+ CarriageNumber + ", " 
        		+ ScheduleFromID + ", " 
        		+ ScheduleToID + ");");
        
        rs = db.getData("select MAX(TicketID) from Ticket where OrderID = " + OrderID + ";");
        rs.next();
        
        int ticketID = rs.getInt(1);
        
        db.closeConnection();
        return Response.ok(ticketID).build();
    }
}