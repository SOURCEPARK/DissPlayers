/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sourcepark.dissplayer.controller;

import de.sourcepark.services.User;

import de.sourcepark.dissplayer.Context;
import static de.sourcepark.dissplayer.DissPlayer.PORT;
import de.sourcepark.services.AuthService;
import de.sourcepark.services.DissplayerServer;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author cjelinski
 */
public class StartPageController implements Initializable, Observer {

    //REST URL
    private static final String REST_URL = "http://localhost:9999/control/order/";

    @FXML
    private Button closeButton;

    @FXML
    private ImageView rfid;

    @FXML
    private ImageView bitcoinImg;

    @FXML
    private Text errorMessage;

    @FXML
    private String userID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize StartPageController");
    }

    @FXML
    private void exit() {
        System.out.println("Exit gewählt");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void authentificate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Parent root = null;

                System.out.println("Auth selected");
                Context.getInstance().setPaymentType(Context.PaymentType.Card);
                //load up OTHER FXML document
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/OrderView.fxml"));
                    Stage stage = (Stage) closeButton.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);

                    stage.show();
                } catch (IOException io) {
                }
                //create a new scene with root and set the stage

            }
        });

    }

    public void activateBitcoinMode() {
        Context.getInstance().setPaymentType(Context.PaymentType.Bitcoin);
        openOrderView();
    }

    @FXML
    public void openOrderView() {
        Parent root = null;
        Stage stage = (Stage) bitcoinImg.getScene().getWindow();

        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/OrderView.fxml"));
        } catch (IOException io) {
        }

        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    /*
     Authentification Method for RFID CARD
     */
    private boolean callAuthentificationService(String userID) {
        boolean retVal = true;
        //call rest service here
        return retVal;
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            User user = (User) arg;
            Context.getInstance().setActiveUser(user);
            System.out.println(user.getCardId());
            authentificate();
        } catch (Exception ex) {
            ErrorCode errorCode = (ErrorCode) arg;
            System.out.println(errorCode.getErrorMessage());
            errorMessage.setText(errorCode.getErrorMessage());
        }
    }
}
