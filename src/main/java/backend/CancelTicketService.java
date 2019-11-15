package backend;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

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
}
