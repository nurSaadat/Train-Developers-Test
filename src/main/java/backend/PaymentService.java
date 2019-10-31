package backend;
import java.sql.SQLException;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Path("/payment")
public class PaymentService {

    private static final String NAME_PATTERN = "[A-Z]+ [A-Z]+";
    private static final String NUMBER_PATTERN = "([0-9]){16}";
    private static final String CVV_PATTERN = "([0-9]){3}";

    @POST
    public Response doPay(@FormParam("cardName") String cardName, @FormParam("cardNumber") String cardNumber, @FormParam("expMonth") String expMonth, @FormParam("expYear") String expYear, @FormParam("cvv") String cvv) throws SQLException, ClassNotFoundException {

        if (cardName == null || cardNumber == null || expMonth == null || expYear == null || cvv == null || cardName.compareTo("") == 0 || cardNumber.compareTo("") == 0 || expMonth.compareTo("") == 0 || expYear.compareTo("") == 0 || cvv.compareTo("") == 0) {

            return Response.serverError().entity("Error! One of the fields is empty!").build();

        }

        if (!cardName.matches(NAME_PATTERN)) {

            return Response.serverError().entity("Error! Cardholder's Name provided is invalid!").build();

        }

        if (!cardNumber.matches(NUMBER_PATTERN)) {

            return Response.serverError().entity("Error! Card Number provided is invalid!").build();

        }

        if (!cvv.matches(CVV_PATTERN)) {

            return Response.serverError().entity("Error! CVV provided is invalid!").build();

        }

        return Response.ok().build();

    }

}
