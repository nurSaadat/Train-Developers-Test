package backend;
import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/seats")
public class ListSeatsService {

    private List<List<String>> seats;

    @POST
    public Response ListSeats(@FormParam("RouteID") String RouteID) throws SQLException, ClassNotFoundException {
        seats = new CopyOnWriteArrayList<>();
        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        ResultSet rs = db.getData("select S.Number, S.Status, C.CarriageNumber, Cl.Name, Cl.Price, T.TrainID\n" +
                "from Seat S, Carriage C, Train T, ScheduleHasTrain SHT, Class Cl\n" +
                "where S.CarriageID = C.CarriageNumber and C.TrainID = T.TrainID and T.TrainID = SHT.TrainID and SHT.RouteID = '1' and C.Class_Name = Cl.Name; ");

        while (rs.next()) {
            seats.add(new CopyOnWriteArrayList<String>());
            seats.get(seats.size() - 1).add(rs.getString("Number"));
            seats.get(seats.size() - 1).add(rs.getString("Status"));
            seats.get(seats.size() - 1).add(rs.getString("CarriageNumber"));
            seats.get(seats.size() - 1).add(rs.getString("Name"));
            seats.get(seats.size() - 1).add(rs.getString("Price"));
            seats.get(seats.size() - 1).add(rs.getString("TrainID"));

        }

        Collections.reverse(seats);
        db.closeConnection();

        Gson gson = new Gson();
        return Response.ok(gson.toJson(seats)).build();
    }

    @GET
    public Response getList() {

        Gson gson = new Gson();
        return Response.ok(gson.toJson(seats)).build();

    }

    @GET
    @Path("{id: [0-9]+}")
    public Response getListItem(@PathParam("id") String id) {

        int i = Integer.parseInt(id);
        return Response.ok(seats.get(i)).build();

    }


    @DELETE
    public Response clear() {

        Gson gson = new Gson();
        seats.clear();
        return Response.ok(gson.toJson(seats)).build();

    }

    @DELETE
    @Path("{id: [0-9]+}")
    public Response clearItem(@PathParam("id") String id) {

        int i = Integer.parseInt(id);
        seats.remove(i);
        return Response.ok(seats).build();

    }
}
