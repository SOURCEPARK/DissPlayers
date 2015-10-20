package de.sourcepark.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sourcepark.dissplayer.controller.StartPageController;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
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
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p;
            p = fxmlLoader.load(getClass().getResource("StartPage.fxml").openStream());
            StartPageController controller = (StartPageController) fxmlLoader.getController();
            controller.authentificate();
            
            return "Hello, " + request.params("no");
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
