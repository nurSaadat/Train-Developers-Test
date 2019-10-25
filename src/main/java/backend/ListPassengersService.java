package backend;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Path("/passengers")
public class ListPassengersService {
    private static final String FNAME_PATTERN = "[A-Z][a-z]*";
    private static final String LNAME_PATTERN = "[A-Z]+([ '-][a-zA-Z]+)*";
    private static final String DOCUMENTID_PATTERN = "^[0-9]{9}";
    private static final String PHONENUMBER_PATTERN = "^[8]{1}[0-9]{10}";

    @POST
    public Response book(@FormParam("data") List<String> data) throws SQLException, ClassNotFoundException {
        MySQLConnector db = new MySQLConnector(3306, "RailwayStation", "user", "Password123!");

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).length() == 0 && i != 0) {

                return Response.serverError().entity("Error! One of the fields is empty!").build();

            }
            if (i == 0) {

                ResultSet rs = db.getData("SELECT MAX(PassengerID) FROM Passenger;");
                rs.next();
                if (rs.getInt( 1) == 0) {
                    data.set(0, "1");
                } else{
                    int newId = rs.getInt( 1) + 1;
                    data.set(0, Integer.toString(newId) );
                }
            }
            if (i == 3) {

                if (!data.get(3).matches(DOCUMENTID_PATTERN)) {
                    return Response.serverError().entity("Error! Document number provided is invalid!").build();
                }

            }

            if (i == 4) {

                if (!data.get(i).matches(FNAME_PATTERN)) {
                    return Response.serverError().entity("Error! First name provided is invalid!").build();
                }

            }

            if (i == 5) {

                if (!data.get(i).matches(LNAME_PATTERN)) {
                    return Response.serverError().entity("Error! Last name provided is invalid!").build();
                }

            }

            if (i == 6) {

                if (!data.get(i).matches(PHONENUMBER_PATTERN)) {
                    return Response.serverError().entity("Error! Phone number provided is invalid!").build();
                }

            }

        }
        db.insertData("INSERT INTO Passenger (PassengerID, DocumentType, Tariff, DocumentID, FName, LName, PhoneNumber, Citizenship, Gender, DateOfBirth, OrderID, TicketID) VALUES (" + data.get(0) + ", " + data.get(1) + ", " + data.get(2) + ", " + data.get(3) + data.get(4) + data.get(5) + data.get(6) + data.get(7) + data.get(8) + data.get(9) + data.get(10) + data.get(11) + ");");
        return Response.ok().build();
    }
}

