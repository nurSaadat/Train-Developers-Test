package backend;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Path("/book")
public class BookTicketService {
    private static final String DEPARTUREDATE_PATTERN = "[0-9]{4}-[0-9]*-[0-9]*";
    private static final String ARRIVINGDATE_PATTERN = "[0-9]{4}-[0-9]*-[0-9]*";
    private static final String PRICE_PATTERN = "[0-9]+";

    @POST
    public Response register(@FormParam("DepartureDate") String DepartureDate, @FormParam("ArrivingDate") String ArrivingDate, @FormParam("Price") String Price, @FormParam("OrderID") String OrderID, @FormParam("RouteID") String RouteID, @FormParam("TrainID") String TrainID, @FormParam("SeatNumber") String SeatNumber, @FormParam("CarriageNumber") String CarriageNumber,  @FormParam("ScheduleFromID") String ScheduleFromID,  @FormParam("ScheduleToID") String ScheduleToID ) throws SQLException, ClassNotFoundException {
        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        if (DepartureDate == null || ArrivingDate== null || Price== null || RouteID == null || TrainID == null || SeatNumber == null|| CarriageNumber == null || ScheduleFromID == null || ScheduleToID == null ) {

            return Response.serverError().entity("Error! One of the fields is empty! book").build();

        }

        int TicketID;
        ResultSet rs = db.getData("select MAX(TicketID) from Ticket;");
        rs.next();
        if (rs.getInt( 1) == 0) {
            TicketID = 1;
        }
        else{
            TicketID = rs.getInt(1) + 1;
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

        db.insertData("INSERT INTO `Ticket` ("
        		+ "TicketID, "
        		+ "DepartureDate, "
        		+ "ArrivingDate, "
        		+ "Price, "
        		+ "OrderID, "
        		+ "RouteID, "
        		+ "TrainID, "
        		+ "SeatNumber, "
        		+ "CarriageNumber, "
        		+ "ScheduleFromID, "
        		+ "ScheduleToID) VALUES (" 
        		+ TicketID + ", '" 
        		+ DepartureDate + "', '" 
        		+ ArrivingDate + "', " 
        		+ Price + ", " 
        		+ OrderID + ", " 
        		+ RouteID + ", " 
        		+ TrainID + ", " 
        		+ SeatNumber + ", " 
        		+ CarriageNumber + ", " 
        		+ 1 + ", " 
        		+ 2+ ");");
        db.closeConnection();
        return Response.ok(TicketID).build();
    }
}
