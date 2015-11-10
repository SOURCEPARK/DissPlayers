package de.sourcepark.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import de.sourcepark.dissplayer.controller.ErrorCode;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import spark.Request;
import spark.Response;

/**
 * The ExampleCandyService class is exactly that: an example candy service
 * mapping the routes /example and /test to an instance of
 * {@code ExampleGetRoute}.
 *
 * @author smatyba
 */
public class AuthService extends CandyService {

    //REST URL
    private static final String REST_URL = "http://localhost:9999/control/authorize/";

    /**
     * Initializes a new instance of the ExampleCandyService class.
     */
    public AuthService() {
        this.setName("Dissplayer Auth Service");
        this.enable();
    }

    /**
     * The ExampleGetRoute class is an example implementation of a Spark Route.
     * It will create a new {@code Example} instance and convert it to a JSON
     * String before returning it (which is handling the request made by the
     * client, btw).
     */
    public class ExampleGetRoute extends CandyRoute {

        /**
         * Handles the request (a GET request in this case).
         *
         * @param request The actual request object
         * @param response The actual response object
         * @return An instance of {@code Example} converted to JSON
         * @throws CandyRouteDisabledException If Route is disabled.
         * @throws com.fasterxml.jackson.core.JsonProcessingException in case of
         * conversion problems
         */
        @Override
        public Object handle(Request request, Response response) throws CandyRouteDisabledException, JsonProcessingException, IOException {
            if (!isEnabled()) {
                throw new CandyRouteDisabledException();
            }
            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);

            User user = new User();
            WebResource webResource = client.resource(UriBuilder.fromUri(REST_URL + request.params("no")).build());
            String responseString = "";

            ObjectMapper mapper = new ObjectMapper();
            try {
                ClientResponse responseAuth = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class);

                responseString = responseAuth.getEntity(String.class);
                user = mapper.readValue(responseString, User.class);
                if (!user.getCardId().equals("")) {
                    System.out.println("Response: " + user.getCardId());
                } else {
                    System.out.println("ErrorMessage: ");
                    user.setCardId("unknown");
                }
            } catch (ClientHandlerException | UniformInterfaceException ex) {                
                System.out.println(Arrays.toString(ex.getStackTrace()));
            } catch (IOException ex) {
                ErrorCode errorCode = mapper.readValue(responseString, ErrorCode.class);
                System.out.println(errorCode.getErrorMessage());
                return errorCode.getErrorMessage();
            }

            return user.getCardId();
        }
    }

    /**
     * Generates and returns a {@code RouteMap} representing all the routes and
     * handlers this service provides.
     *
     * @return A {@code RouteMap} containing all routes/handlers this service
     * provides.
     */
    @Override
    public RouteMap initializeRoutes() {
        final RouteMap map = new RouteMap();
        final Map<String, CandyRoute> postMap = new HashMap<>();
        postMap.put("/auth/:no", new ExampleGetRoute());
        map.put(HTTPMethod.POST, postMap);
        return map;
    }
}
