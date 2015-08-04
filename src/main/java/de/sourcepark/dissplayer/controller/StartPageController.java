/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sourcepark.dissplayer.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
public class StartPageController implements Initializable {

    @FXML
    private Button closeButton;

    @FXML
    private ImageView rfid;
    
    @FXML
    private Text errorMessage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void exit() {
        System.out.println("Exit gewählt");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void authentificate() {
        Stage stage;
        Parent root = null;
        int userID = 000;
        System.out.println("Auth selected");
        stage = (Stage) rfid.getScene().getWindow();
        //load up OTHER FXML document
        if (callAuthentificationService(userID)) {
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml/OrderView.fxml"));
            } catch (IOException io) {
            }
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        } else {
            errorMessage.setText("Karte nicht erkannt");
        }
    }

    /*
     Authentification Method for RFID CARD
     */
    private boolean callAuthentificationService(int userID) {
        boolean retVal = true;
        //call rest service here
        return retVal;
    }

}
