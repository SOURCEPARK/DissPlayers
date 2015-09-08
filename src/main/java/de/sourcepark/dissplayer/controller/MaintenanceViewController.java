package de.sourcepark.dissplayer.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private TextArea logText;

    @FXML
    private void testAuswurf(ActionEvent event) {
        System.out.println("Test-Aufruf gestartet");
        logText.setWrapText(true);
        if (inputField.getText().length() < 2) {
            logText.appendText("Test-Aufruf gestartet für Produkt: ungültige Eingabe\n");
        } else {
            logText.appendText("Test-Aufruf gestartet für Produkt: " + inputField.getText() + "\n");
        }
    }

    @FXML
    private void calibrate(ActionEvent event) {
        System.out.println("Kalibration gestartet");
        logText.setWrapText(true);
        if (inputField.getText().length() < 2) {
            logText.appendText("Kalibration gestartet für Fach: ungültige Eingabe\n");
        } else {
            logText.appendText("Kalibration gestartet für Fach: " + inputField.getText() + "\n");
        }
    }

    @FXML
    private void motor(ActionEvent event) {
        System.out.println("Test-Aufruf gestartet");
        logText.setWrapText(true);
        if (inputField.getText().length() < 2) {
            logText.appendText("Motor-Aufruf gestartet für: ungültige Eingabe\n");
        } else {
            logText.appendText("Motor-Aufruf gestartet für: " + inputField.getText() + "\n");
        }
    }

    @FXML
    private void allOff(ActionEvent event) {
        System.out.println("Test-Aufruf gestartet");
        logText.setWrapText(true);
            logText.appendText("NOT STOP!!!\n");
    }

    @FXML
    private void step(ActionEvent event) {
        System.out.println("Test-Aufruf gestartet");
        logText.setWrapText(true);
        if (inputField.getText().length() < 2) {
            logText.appendText("Step-Aufruf gestartet für: ungültige Eingabe\n");
        } else {
            logText.appendText("Step-Aufruf gestartet für: " + inputField.getText() + "\n");
        }
    }

    @FXML
    private void col(ActionEvent event) {
        System.out.println("Test-Aufruf gestartet");
        logText.setWrapText(true);
        if (inputField.getText().length() < 2) {
            logText.appendText("Col-Aufruf gestartet für: ungültige Eingabe\n");
        } else {
            logText.appendText("Col-Aufruf gestartet für: " + inputField.getText() + "\n");
        }
    }

    @FXML
    private void row(ActionEvent event) {
        System.out.println("Test-Aufruf gestartet");
        logText.setWrapText(true);
        if (inputField.getText().length() < 2) {
            logText.appendText("Row-Aufruf gestartet für: ungültige Eingabe\n");
        } else {
            logText.appendText("Row-Aufruf gestartet für: " + inputField.getText() + "\n");
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
    private void clearLog() {
        logText.clear();
    }

    @FXML
    public void addNumber(ActionEvent event) {
        Button buttonx = (Button) event.getSource();
        String numberToAdd = buttonx.getText();
        System.out.println("add a number " + numberToAdd);
        if (inputField.getText().length() < 2) {
            inputField.setText(inputField.getText().concat(numberToAdd));
        }
    }

    @FXML
    public void clearLastNumber() {
        if (!inputField.getText().isEmpty()) {
            inputField.setText(removeLastChar(inputField.getText()));
        }
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
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

}
