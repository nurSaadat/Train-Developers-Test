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

    @POST
    public Response book(@FormParam("data") List<String> data) throws SQLException, ClassNotFoundException {
        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).length() == 0 && i != 0) {

                return Response.serverError().entity("Error! One of the fields is empty!").build();

            }
            if (i == 0) {

                ResultSet rs = db.getData("SELECT MAX(TicketID) FROM Ticket;");
                rs.next();
                if (rs.getInt( 1) == 0) {
                    data.set(0, "1");
                } else{
                    int newId = rs.getInt( 1) + 1;
                    data.set(0, Integer.toString(newId) );
                }
            }
        }
        db.insertData("INSERT INTO Ticket (TicketID, DepartureDate, ArrivingDate, Price, OrderID, RouteID, TrainID, SeatNumber, CarriageNumber, ScheduleFromID, ScheduleToID) VALUES (" + data.get(0) + ", " + data.get(1) + ", " + data.get(2) + ", " + data.get(3) + data.get(4) + data.get(5) + data.get(6) + data.get(7) + data.get(8) + data.get(9) + data.get(10) + ");");
        return Response.ok().build();
    }
}
