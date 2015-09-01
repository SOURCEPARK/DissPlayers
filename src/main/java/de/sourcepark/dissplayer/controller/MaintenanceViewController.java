package de.sourcepark.dissplayer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilderException;

/**
 *
 * @author cjelinski
 */
public class MaintenanceViewController {

    //REST URL
    private static final String REST_URL = "http://localhost:9999/control/order/";

    @FXML
    private Label label;
    @FXML
    private Button closeButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField inputField;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private void testAuswurf(ActionEvent event) {
        System.out.println("Test-Aufruf gestartet");
        label.setText(inputField.getText());
        if (inputField != null && !inputField.getText().isEmpty()) {
            callOrderService(inputField.getText());
        }
    }

    @FXML
    private void exit() {
        System.out.println("Exit gewählt");
        label.setText("Exit");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel() {
        Stage stage;
        Parent root = null;        
        stage = (Stage) cancelButton.getScene().getWindow();
        //load up OTHER FXML document
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/StartPage.fxml"));
        } catch (IOException io) {
        }
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    /*
     Call order REST Service
     */
    private void callOrderService(String value) throws ClientHandlerException, IllegalArgumentException, UniformInterfaceException, UriBuilderException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource webResource = client.resource(UriBuilder.fromUri(REST_URL + value).build());

//        MultivaluedMap formData = new MultivaluedMapImpl();
//        formData.add("name1", value);
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class);
        ObjectMapper mapper = new ObjectMapper();

        //System.out.println("Response " + response.getEntity(String.class));
        try {
            ErrorCode errorCode = mapper.readValue(response.getEntity(String.class), ErrorCode.class);
            System.out.println(errorCode.getErrorMessage());
        } catch (IOException ex) {
            System.out.println("mapper exception");
        }

    }

}
