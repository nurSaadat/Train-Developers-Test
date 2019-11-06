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
    private static final String LNAME_PATTERN = "[A-Z][a-z]*";
    private static final String DOCUMENTID_PATTERN = "[0-9]*";
    private static final String PHONENUMBER_PATTERN = "[0-9]*";
    private static final String DOB_PATTERN = ".";

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

        if (DateOfBirth.matches(DOB_PATTERN)) {

            return Response.serverError().entity("Error! Date of birth provided is invalid!").build();

        }
        
        String gender1 = "";
        
        if(Gender.compareTo("Male") == 0)
        	gender1 = "M";
        if(Gender.compareTo("Female") == 0)
        	gender1 = "F";


        db.insertData("INSERT INTO Passenger ( DocumentType, Tariff, DocumentID, FName, LName, PhoneNumber, Citizenship, Gender, DateOfBirth, OrderID, TicketID) VALUES ( '" + DocumentType + "', '" + Tariff + "', " + DocumentID + ", '" + FName + "', '" + LName + "', " + PhoneNumber + ", '" + Citizenship + "', '" + gender1 + "', '" + DateOfBirth + "', " + OrderID+ ", " + TicketID + ");");
        return Response.ok().build();
    }
}

