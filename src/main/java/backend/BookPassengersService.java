package backend;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Path("/passengers")
public class BookPassengersService {

    @GET
    public Response getList(@Context HttpServletRequest req) throws ClassNotFoundException, SQLException {

        List<List<String>> passenger = new CopyOnWriteArrayList<>();

        HttpSession session = req.getSession(false);

        String email = (String)session.getAttribute("email");

        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        ResultSet rs = db.getData("select  DocumentType, Tariff, DocumentID, P.FName, P.LName, PhoneNumber, Citizenship, Gender, DateOfBirth, P.OrderID, P.TicketID \n" +
                "from `User` U, `Order` O, `Ticket` T, `Passenger` P\n" +
                "where U.Email = '" + email + "' and O.UserEmail = U.Email and T.OrderID = O.OrderID and P.TicketID = T.TicketID and P.OrderID = T.OrderID;");

        while (rs.next()) {

            passenger.add(new CopyOnWriteArrayList<String>());
            passenger.get(passenger.size() - 1).add(rs.getString("DocumentType"));
            passenger.get(passenger.size() - 1).add(rs.getString("Tariff"));
            passenger.get(passenger.size() - 1).add(rs.getString("DocumentID"));
            passenger.get(passenger.size() - 1).add(rs.getString("FName"));
            passenger.get(passenger.size() - 1).add(rs.getString("LName"));
            passenger.get(passenger.size() - 1).add(rs.getString("PhoneNumber"));
            passenger.get(passenger.size() - 1).add(rs.getString("Citizenship"));
            passenger.get(passenger.size() - 1).add(rs.getString("Gender"));
            passenger.get(passenger.size() - 1).add(rs.getString("DateOfBirth"));
            passenger.get(passenger.size() - 1).add(rs.getString("OrderID"));
            passenger.get(passenger.size() - 1).add(rs.getString("TicketID"));


        }

        db.closeConnection();

        Gson gson = new Gson();
        return Response.ok(gson.toJson(passenger)).build();

    }
}
