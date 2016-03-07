/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sourcepark.dissplayer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import de.sourcepark.dissplayer.Context;
import de.sourcepark.dissplayer.pojo.OrderClient;
import de.sourcepark.services.AuthService;
import de.sourcepark.services.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 * FXML Controller class
 *
 * @author cjelinski
 */
public class OrderViewController implements Initializable {

    private static final transient Logger LOG = LoggerFactory.getLogger(OrderViewController.class);

    private final String MAINTENANCESERVICE = "http://localhost:9999/control/";

    @FXML
    private TextField orderNumber;
    @FXML
    private Button closeButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button orderButton;
    @FXML
    private Text errorMessage;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private ImageView maintenance;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        if (Context.getInstance().getPaymentType() == Context.PaymentType.Card) {
            String errorText = "";
            if (Context.getInstance().getActiveUser().getNickname() != null) {
                errorText = "Hello: "
                        + Context.getInstance().getActiveUser().getNickname();
            }
            errorMessage.setText(errorText);
            System.out.println("login time: " + Context.getInstance().getActiveUser().getTtl());
            maintenance.setVisible(Context.getInstance().getActiveUser().isMaintenanceStaff());

        }else{
            maintenance.setVisible(false);
        }
        
    }

    @FXML
    public void addNumber(ActionEvent event) {
        try {
            Button buttonx = (Button) event.getSource();
            String numberToAdd = buttonx.getText();
            System.out.println("add a number " + numberToAdd);
            if (orderNumber.getText().length() < 2) {
                orderNumber.setText(orderNumber.getText().concat(numberToAdd));
            }
            if (orderNumber.getText().length() == 2) {
                orderButton.setDisable(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    public void clearLastNumber(ActionEvent event) {
        if (!orderNumber.getText().isEmpty()) {
            orderNumber.setText(removeLastChar(orderNumber.getText()));
            orderButton.setDisable(true);
        }
    }

    @FXML
    public void clearAll(ActionEvent event) {
        orderNumber.setText("");
    }

    @FXML
    public void orderCandy(ActionEvent event) throws Exception {
        mainPane.setDisable(true);
        if (orderNumber.getText().length() == 2) {
            Context.getInstance().setActiveOrderNumber(orderNumber.getText());

            if (Context.getInstance().getPaymentType() == Context.PaymentType.Bitcoin) {
                createBitcoinSession();
                showBitcoinView();
            } else if (Context.getInstance().getPaymentType() == Context.PaymentType.Card
                    && isNotOutOfTime()) {
                String errMsg = OrderClient.callOrderService(orderNumber.getText());
                if (errMsg != null) {
                    errorMessage.setText(errMsg);
                }
            }
        }

        mainPane.setDisable(false);
        //navigate back to main scene
        cancel();
    }

    private void createBitcoinSession() throws Exception {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        User user = new User();
        WebResource webResource = client.resource(UriBuilder.fromUri(AuthService.REST_URL + "bitcoin").build());
        String responseString = "";

        ObjectMapper mapper = new ObjectMapper();
        try {
            ClientResponse responseAuth = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class);

            responseString = responseAuth.getEntity(String.class);
            user = mapper.readValue(responseString, User.class);
            if (user.getCardId().isEmpty()) {
                throw new Exception("Bitcoin session could not be created due to Hubba-Bubba Error");
            }

            LOG.info("Bitcoin session created successfully");
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            LOG.error(Arrays.toString(ex.getStackTrace()));
        }
    }

    @FXML
    private void showBitcoinView() {
        Parent root = null;
        Stage stage = (Stage) cancelButton.getScene().getWindow();

        //load up OTHER FXML document
        try {
            URL bitcoinViewFxml = getClass().getResource("/fxml/BitcoinView.fxml");
            root = FXMLLoader.load(bitcoinViewFxml);
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        } catch (IOException io) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel() throws Exception {
        Stage stage;
        Parent root = null;

        System.out.println("Auth selected");
        stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
//        //load up OTHER FXML document
//        try {
//            root = FXMLLoader.load(getClass().getResource("/fxml/StartPage.fxml"));
//        } catch (IOException io) {
//        }

        //reset current user
        Context.getInstance().setActiveUser(null);
//        //create a new scene with root and set the stage
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//
//        stage.show();
    }

    @FXML
    private void maintenance() throws Exception {
        if (isNotOutOfTime()) {
            Stage stage;
            Parent root = null;
            stage = (Stage) cancelButton.getScene().getWindow();
            //load up OTHER FXML document
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml/MaintenanceView.fxml"));
            } catch (IOException io) {
            }

            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);
            WebResource webResource = client.resource(UriBuilder.fromUri(MAINTENANCESERVICE + "maintenanceMode").build());
            ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class);
            String responseText = response.getEntity(String.class);

            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        }
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private boolean isNotOutOfTime() throws Exception {
        if (Context.getInstance().getActiveUser() != null) {
            if (System.currentTimeMillis() - Context.getInstance().getActiveUser().getTtl() > 30000) {
                System.out.println("OUT OF TIMEEEE");
                cancel();
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
