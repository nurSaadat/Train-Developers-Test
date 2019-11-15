package backend;

import com.google.gson.Gson;

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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Path("/passengers")
public class BookPassengersService {
    private static final String FNAME_PATTERN = "[A-Z][a-z]*";
    private static final String LNAME_PATTERN = "[A-Z][a-z]*";
    private static final String DOCUMENTID_PATTERN = "[0-9]*";
    private static final String PHONENUMBER_PATTERN = "[0-9]{10}";
    private static final String DOB_PATTERN = "[0-9]{4}+-[0-9]+-[0-9]+";

    @POST
    public Response register(@FormParam("DocumentType") String DocumentType, @FormParam("Tariff") String Tariff, @FormParam("DocumentID") String DocumentID, @FormParam("FName") String FName,@FormParam("LName") String LName, @FormParam("PhoneNumber") String PhoneNumber, @FormParam("Citizenship") String Citizenship, @FormParam("Gender") String Gender, @FormParam("DateOfBirth") String DateOfBirth, @FormParam("OrderID") String OrderID, @FormParam("TicketID") String TicketID ) throws SQLException, ClassNotFoundException {

        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        if (DocumentType== null || Tariff== null || DocumentID == null ||  FName == null || LName == null || PhoneNumber == null || Citizenship == null || Gender == null || DateOfBirth == null || OrderID == null || TicketID== null) {

            return Response.serverError().entity("Error! One of the fields is empty!").build();

        }

        if (!FName.matches(FNAME_PATTERN)) {

            return Response.serverError().entity("Error! First Name provided is invalid!").build();

        }

        if (!LName.matches(LNAME_PATTERN)) {

            return Response.serverError().entity("Error! Last Name provided is invalid!").build();

        }

        if (!DocumentID.matches(DOCUMENTID_PATTERN)) {

            return Response.serverError().entity("Error! Document ID provided is invalid! ").build();

        }

        if (!PhoneNumber.matches(PHONENUMBER_PATTERN)) {

            return Response.serverError().entity("Error! Number provided is invalid!").build();

        }
        
        if (!DateOfBirth.matches(DOB_PATTERN)) {

            return Response.serverError().entity("Error! Date of birth provided is invalid!" + DateOfBirth).build();

        }
        
        String gender1 = "";
        
        if(Gender.compareTo("Male") == 0)
        	gender1 = "M";
        if(Gender.compareTo("Female") == 0)
        	gender1 = "F";


        db.insertData("INSERT INTO Passenger ( DocumentType, Tariff, DocumentID, FName, LName, PhoneNumber, Citizenship, Gender, DateOfBirth, OrderID, TicketID) VALUES ( '" + DocumentType + "', '" + Tariff + "', " + DocumentID + ", '" + FName + "', '" + LName + "', " + PhoneNumber + ", '" + Citizenship + "', '" + gender1 + "', '" + DateOfBirth + "', " + OrderID+ ", " + TicketID + ");");
        db.closeConnection();
        return Response.ok().build();
    }

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
