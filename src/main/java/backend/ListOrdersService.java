package backend;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Path("/orders")
public class ListOrdersService {
    private static final String EMAIL_PATTERN = "[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]";
    private static final String NUMBEROFPASS_PATTERN = "[1|2|3|4|5]";
    private static final String TOTALPRICE_PATTERN = "[0-9]+";
    private static final String BOOKINGDATE_PATTERN = "([0-9]*)-([0-9]*)-([0-9]*)}";
    private static final String BOOKINGTIME_PATTERN = ".";

    @POST

    public Response register(@FormParam("NumberOfPassengers") String NumberOfPassengers, @FormParam("BookingDate") String BookingDate, @FormParam("BookingTime") String BookingTime, @FormParam("PaymentType") String PaymentType,@FormParam("TotalPrice") String TotalPrice, @FormParam("UserEmail") String Email) throws SQLException, ClassNotFoundException {
        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        if (NumberOfPassengers == null || BookingDate== null || BookingTime == null || PaymentType == null || TotalPrice == null || Email == null) {

            return Response.serverError().entity("Error! One of the fields is empty!").build();

        }
        int OrderID;
        ResultSet rs = db.getData("select MAX(OrderID) from `RailwayStation`.`Order`;");
        rs.next();
        if (rs.getInt( 1) == 0) {
            OrderID = 1;
        }
        else{
                OrderID = rs.getInt(1) + 1;
            }

        if (!Email.matches(EMAIL_PATTERN)) {

            return Response.serverError().entity("Error! Email provided is invalid!").build();

        }

        if (!NumberOfPassengers.matches(NUMBEROFPASS_PATTERN)) {

            return Response.serverError().entity("Error! Number of passengers should be from 1 to 5").build();

        }

        if (!TotalPrice.matches(TOTALPRICE_PATTERN)) {

            return Response.serverError().entity("Error! Total price provided is invalid!").build();

        }

//        if (!BookingDate.matches(BOOKINGDATE_PATTERN)) {
//
//            return Response.serverError().entity("Error! Booking date provided is invalid!").build();
//
//        }
        
//        if (!BookingTime.matches(BOOKINGTIME_PATTERN)) {
//
//            return Response.serverError().entity("Error! Booking time provided is invalid!").build();
//
//        }

        db.insertData("INSERT INTO `Order` (OrderID, NumberOfPassengers, BookingDate, BookingTime, PaymentType, TotalPrice, UserEmail) VALUES (" +  OrderID +", " + NumberOfPassengers +", '" + BookingDate +"', '" + BookingTime +"', '" + PaymentType +"', " + TotalPrice +", '" + Email + "');");
        return Response.ok().build();
    }
}
