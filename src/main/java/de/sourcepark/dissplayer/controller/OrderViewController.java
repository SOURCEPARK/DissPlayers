/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

/**
 * FXML Controller class
 *
 * @author cjelinski
 */
public class OrderViewController implements Initializable {

    //REST URL
    private static final String REST_URL = "http://adelphi:9999/control/order/";
    @FXML
    private Text orderNumber;
    @FXML
    private Button closeButton;
    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void addNumber(ActionEvent event) {
        Button buttonx = (Button) event.getSource();
        String numberToAdd = buttonx.getText();
        System.out.println("add a number " + numberToAdd);
        if (orderNumber.getText().length() < 4) {
            orderNumber.setText(orderNumber.getText().concat(numberToAdd));
        }
    }

    @FXML
    public void clearLastNumber() {
        if (!orderNumber.getText().isEmpty()) {
            orderNumber.setText(removeLastChar(orderNumber.getText()));
        }
    }

    @FXML
    public void clearAll() {
        orderNumber.setText("");
    }

    @FXML
    public void orderCandy() {
        if (orderNumber.getText().length() == 4) {
            callOrderService(orderNumber.getText());
        }
    }

    @FXML
    private void cancel() {
        Stage stage;
        Parent root = null;
        System.out.println("Auth selected");
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

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
