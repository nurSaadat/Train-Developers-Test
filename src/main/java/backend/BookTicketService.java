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
    private static final String PRICE_PATTERN = "[0-9]+\\.?[0-9]*";
    private static final String FNAME_PATTERN = "[A-Z][a-z]*";
    private static final String LNAME_PATTERN = "[A-Z][a-z]*";
    private static final String DOCUMENTID_PATTERN = "[0-9]*";
    private static final String PHONENUMBER_PATTERN = "[0-9]{10}";
    private static final String DOB_PATTERN = "[0-9]{4}+-[0-9]+-[0-9]+";

    @POST
    public Response register(@FormParam("ticket[]") List<String> ticket, @FormParam("passenger[]") List<String> passenger) throws SQLException, ClassNotFoundException {
        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        if (ticket.get(0) == null || ticket.get(1) == null || ticket.get(2) == null || ticket.get(3) == null || ticket.get(4) == null || ticket.get(5) == null || ticket.get(6) == null|| ticket.get(7) == null ) {

            return Response.serverError().entity("Error! One of the fields is empty! book").build();

        }
        
        if (passenger.get(0) == null || passenger.get(1)== null || passenger.get(2) == null ||  passenger.get(3) == null || passenger.get(4) == null || passenger.get(5) == null || passenger.get(6) == null || passenger.get(7) == null || passenger.get(8) == null || passenger.get(9) == null) {

            return Response.serverError().entity("Error! One of the fields is empty!").build();

        }

        if (!ticket.get(2).matches(PRICE_PATTERN)) {

            return Response.serverError().entity("Error! Price provided is invalid!").build();

        }

        if (!ticket.get(0).matches(DEPARTUREDATE_PATTERN)) {

            return Response.serverError().entity("Error! Departure date provided is invalid!").build();

        }

        if (!ticket.get(1).matches(ARRIVINGDATE_PATTERN)) {

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
        		"where SA.RouteID = R.RouteID and SD.RouteID = R.RouteID and SA.StationAbbr = R.StationTo and SD.StationAbbr = R.StationFrom and R.RouteID = " + ticket.get(4) + ";");

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
        		+ ticket.get(0) + "', '" 
        		+ ticket.get(1) + "', " 
        		+ ticket.get(2) + ", " 
        		+ ticket.get(3) + ", " 
        		+ ticket.get(4) + ", " 
        		+ ticket.get(5) + ", " 
        		+ ticket.get(6) + ", " 
        		+ ticket.get(7) + ", " 
        		+ ScheduleFromID + ", " 
        		+ ScheduleToID + ");");
        
        rs = db.getData("select MAX(TicketID) from Ticket where OrderID = " + ticket.get(3) + ";");
        rs.next();
        
        int ticketID = rs.getInt(1);
        
        String gender1;
        
        if(passenger.get(7).compareTo("Male") == 0)
        	gender1 = "M";
        else
        	gender1 = "F";
        
        db.insertData("INSERT INTO Passenger ( DocumentType, Tariff, DocumentID, FName, LName, PhoneNumber, Citizenship, Gender, DateOfBirth, OrderID, TicketID) VALUES ( '" + passenger.get(0) + "', '" + passenger.get(1) + "', " + passenger.get(2) + ", '" + passenger.get(3) + "', '" + passenger.get(4) + "', " + passenger.get(5) + ", '" + passenger.get(6) + "', '" + gender1 + "', '" + passenger.get(8) + "', " + passenger.get(9) + ", " + ticketID +  ");");
        
        db.closeConnection();
        return Response.ok().build();
    }
}