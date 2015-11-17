package de.sourcepark.dissplayer.pojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import de.sourcepark.dissplayer.controller.ErrorCode;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by jnaperkowski on 17.11.15.
 */
public class OrderClient {

    //REST URL
    private static final String REST_URL = "http://localhost:9999/control/order/";

    /**
     *  Call order REST Service
     *  @param orderNumber order number
     *  @return error message or null if successful
     */
    public static String callOrderService(String orderNumber) throws ClientHandlerException, IllegalArgumentException, UniformInterfaceException, UriBuilderException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource webResource = client.resource(UriBuilder.fromUri(REST_URL + orderNumber).build());

        try {
            ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class);
            ObjectMapper mapper = new ObjectMapper();
            String responseText = response.getEntity(String.class);
            if (responseText.equals("OK")) {
                System.out.println("Response: " + responseText);
                //TODO: response seite für alles hat funktioniert
            } else {
                try {
                    ErrorCode errorCode = mapper.readValue(responseText, ErrorCode.class);
                    System.out.println("ErrorMessage: " + errorCode.getErrorMessage());
                    return String.format("Order-Service %s", errorCode.getErrorMessage());
                } catch (IOException ex) {
                    System.out.println("Error on errorcode handling");
                    ex.printStackTrace();
                    return "Unbekannter Fehler beim Aufruf von Order-Service (siehe stdout)";
                }
            }
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return "Order-Service nicht erreichbar";
        }

        return null;
    }
}
