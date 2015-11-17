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
import java.util.Arrays;
import java.util.ResourceBundle;

import de.sourcepark.dissplayer.Context;
import de.sourcepark.dissplayer.pojo.OrderClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void addNumber(ActionEvent event) {
        Button buttonx = (Button) event.getSource();
        String numberToAdd = buttonx.getText();
        System.out.println("add a number " + numberToAdd);
        if (orderNumber.getText().length() < 2) {
            orderNumber.setText(orderNumber.getText().concat(numberToAdd));
        }
        if (orderNumber.getText().length() == 2) {
            orderButton.setDisable(false);
        }
    }

    @FXML
    public void clearLastNumber() {
        if (!orderNumber.getText().isEmpty()) {
            orderNumber.setText(removeLastChar(orderNumber.getText()));
            orderButton.setDisable(true);
        }
    }

    @FXML
    public void clearAll() {
        orderNumber.setText("");
    }

    @FXML
    public void orderCandy() {
        mainPane.setDisable(true);
        if (orderNumber.getText().length() == 2) {
            Context.getInstance().setActiveOrderNumber(orderNumber.getText());

            if (Context.getInstance().getPaymentType() == Context.PaymentType.Bitcoin)
                showBitcoinView();
            else if (Context.getInstance().getPaymentType() == Context.PaymentType.Card) {
                String errMsg = OrderClient.callOrderService(orderNumber.getText());
                if (errMsg != null)
                    errorMessage.setText(errMsg);
            }
        }
        mainPane.setDisable(false);
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

    @FXML
    private void maintenance() {
        Stage stage;
        Parent root = null;
        stage = (Stage) cancelButton.getScene().getWindow();
        //load up OTHER FXML document
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/MaintenanceView.fxml"));
        } catch (IOException io) {
        }
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
