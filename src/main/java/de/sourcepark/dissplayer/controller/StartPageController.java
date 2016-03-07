/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sourcepark.dissplayer.controller;

import de.sourcepark.services.User;

import de.sourcepark.dissplayer.Context;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    @FXML
    private AnchorPane content;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize StartPageController");
//        AuthService.getInstance().deleteObservers();
//        AuthService.getInstance().addObserver(this);
    }

    @FXML
    private void exit() {
        System.out.println("Exit gewählt");
        Stage stage = getCurrentStage();
        stage.close();
    }

    public Stage getCurrentStage() {
        return (Stage) rfid.getScene().getWindow();
    }

    @FXML
    public void authentificate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Context.getInstance().setPaymentType(Context.PaymentType.Card);
                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/OrderView.fxml"));
                    System.out.println("Auth selected");

                    //FIXME: Only for debugging!
                    if (Context.getInstance().getActiveUser() == null) {
                        Context.getInstance().setActiveUser(new User());
                    }

                    //Stage stage = (Stage) rfid.getScene().getWindow();
                    Stage stage = new Stage();

                    Scene scene = new Scene(root);
                    stage.setScene(scene);

                    //             waitingPanel.hide();
                    stage.show();
                } catch (IOException io) {
                    io.printStackTrace();
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
        Stage stage = new Stage();

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

    @FXML
    private void showNotAuthorizedSite() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Show not authorized Stage");
                Parent root = null;
                Stage stage = new Stage();

                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/NotAuthorizedDialog.fxml"));
                } catch (IOException io) {
                }

                //create a new scene with root and set the stage
                Scene scene = new Scene(root);
                stage.setScene(scene);

                stage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(5));
                delay.setOnFinished(event -> stage.close());
                delay.play();
            }
        });
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
            showNotAuthorizedSite();
        }
    }
}
