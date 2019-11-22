package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/bookMult")
public class BookMultTicketService {
	private static final String DEPARTUREDATE_PATTERN2 = "[0-9]{4}-[0-9]*-[0-9]*";
    private static final String ARRIVINGDATE_PATTERN2 = "[0-9]{4}-[0-9]*-[0-9]*";
    private static final String PRICE_PATTERN2 = "[0-9]+\\.?[0-9]*";
    private static final String FNAME_PATTERN2 = "[A-Z][a-z]*";
    private static final String LNAME_PATTERN2 = "[A-Z][a-z]*";
    private static final String DOCUMENTID_PATTERN2 = "[0-9]*";
    private static final String PHONENUMBER_PATTERN2 = "[0-9]{10}";
    private static final String DOB_PATTERN2 = "[0-9]{4}+-[0-9]+-[0-9]+";
    
    @POST
    public Response registerMult(@FormParam("ticket1[]") List<String> ticket1, @FormParam("ticket2[]") List<String> ticket2, @FormParam("ticket3[]") List<String> ticket3, @FormParam("passenger1[]") List<String> passenger1, @FormParam("passenger2[]") List<String> passenger2, @FormParam("passenger3[]") List<String> passenger3, @FormParam("OrderID") int OrderID, @FormParam("NumberOfPassengers") int NumberOfPassengers) throws SQLException, ClassNotFoundException {
    	
    	List<List<String>> ticket = new CopyOnWriteArrayList<>();
    	List<List<String>> passenger = new CopyOnWriteArrayList<>();
    	
    	ticket.add(ticket1);
    	ticket.add(ticket2);
    	ticket.add(ticket3);
    	
    	passenger.add(passenger1);
    	passenger.add(passenger2);
    	passenger.add(passenger3);
    	
    	if(OrderID < 0 || NumberOfPassengers < 1) {
    		
    		return Response.serverError().entity("Error! One of the fields is empty! book").build();
    	}
    	
    	MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");
    	
    	for(int i = 0; i < NumberOfPassengers; i++) {
    		if (ticket.get(i).get(0) == null || ticket.get(i).get(1) == null || ticket.get(i).get(2) == null || ticket.get(i).get(3) == null || ticket.get(i).get(4) == null || ticket.get(i).get(5) == null || ticket.get(i).get(6) == null|| ticket.get(i).get(7) == null || ticket.get(i).get(8) == null) {

                return Response.serverError().entity("Error! One of the fields is empty! book").build();

            }
            
            if (passenger.get(i).get(0) == null || passenger.get(i).get(1)== null || passenger.get(i).get(2) == null ||  passenger.get(i).get(3) == null || passenger.get(i).get(4) == null || passenger.get(i).get(5) == null || passenger.get(i).get(6) == null || passenger.get(i).get(7) == null || passenger.get(i).get(8) == null) {

                return Response.serverError().entity("Error! One of the fields is empty!").build();

            }
            
            if (!ticket.get(i).get(5).matches(PRICE_PATTERN2)) {

                return Response.serverError().entity("Error! Price provided is invalid!").build();

            }

            if (!ticket.get(i).get(0).matches(DEPARTUREDATE_PATTERN2)) {

                return Response.serverError().entity("Error! Departure date provided is invalid!").build();

            }

            if (!ticket.get(i).get(1).matches(ARRIVINGDATE_PATTERN2)) {

                return Response.serverError().entity("Error! Arriving date provided is invalid!").build();

            }
            
            if (!passenger.get(i).get(3).matches(FNAME_PATTERN2)) {

                return Response.serverError().entity("Error! First Name provided is invalid!").build();

            }

            if (!passenger.get(i).get(4).matches(LNAME_PATTERN2)) {

                return Response.serverError().entity("Error! Last Name provided is invalid!").build();

            }

            if (!passenger.get(i).get(2).matches(DOCUMENTID_PATTERN2)) {

                return Response.serverError().entity("Error! Document ID provided is invalid! ").build();

            }

            if (!passenger.get(i).get(5).matches(PHONENUMBER_PATTERN2)) {

                return Response.serverError().entity("Error! Number provided is invalid!").build();

            }
            
            if (!passenger.get(i).get(8).matches(DOB_PATTERN2)) {

                return Response.serverError().entity("Error! Date of birth provided is invalid!" + passenger.get(i).get(8)).build();

            }
    	}
    	        
        ResultSet rs = db.getData("select SD.ScheduleID, SA.ScheduleID\r\n" + 
        		"from Schedule SD, Schedule SA, Route R\r\n" + 
        		"where SA.RouteID = R.RouteID and SD.RouteID = R.RouteID and SA.StationAbbr = R.StationTo and SD.StationAbbr = R.StationFrom and R.RouteID = " + ticket.get(0).get(2) + ";");

        rs.next();
        int ScheduleFromID = rs.getInt(1);
        int ScheduleToID = rs.getInt(2);
        
        for(int i = 0; i < NumberOfPassengers; i++) {
        	
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
            		+ ticket.get(i).get(0) + "', '" 
            		+ ticket.get(i).get(1) + "', " 
            		+ ticket.get(i).get(5) + ", " 
            		+ OrderID + ", " 
            		+ ticket.get(i).get(2) + ", " 
            		+ ticket.get(i).get(6) + ", " 
            		+ ticket.get(i).get(7) + ", " 
            		+ ticket.get(i).get(8) + ", " 
            		+ ScheduleFromID + ", " 
            		+ ScheduleToID + ");");
            
            rs = db.getData("select MAX(TicketID) from Ticket where OrderID = " + OrderID + ";");
            rs.next();
            
            int ticketID = rs.getInt(1);
            
            String gender1;
            
            if(passenger.get(i).get(7).compareTo("Male") == 0)
            	gender1 = "M";
            else
            	gender1 = "F";
            
            db.insertData("INSERT INTO Passenger ( DocumentType, Tariff, DocumentID, FName, LName, PhoneNumber, Citizenship, Gender, DateOfBirth, OrderID, TicketID) VALUES ( '" 
            									+ passenger.get(i).get(0) + "', '" + passenger.get(i).get(1) + "', " + passenger.get(i).get(2) + ", '" + passenger.get(i).get(3) + "', '" + passenger.get(i).get(4) + "', " + passenger.get(i).get(5) + ", '" + passenger.get(i).get(6) + "', '" + gender1 + "', '" + passenger.get(i).get(8) + "', " + OrderID + ", " + ticketID +  ");");
            
        }        
        
        db.closeConnection();
        return Response.ok().build();
    }
}
