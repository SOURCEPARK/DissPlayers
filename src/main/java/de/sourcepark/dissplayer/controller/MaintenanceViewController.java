package de.sourcepark.dissplayer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import de.sourcepark.dissplayer.Context;
import java.io.IOException;
import java.util.Arrays;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

/**
 *
 * @author cjelinski
 */
public class MaintenanceViewController {

    //REST URL
    private static final String BASIS_REST_URL = "http://localhost:9999/control/";

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

    /*
     Call REST Services for maintenance
     */
    private void callMaintenanceService(String uri) throws ClientHandlerException, IllegalArgumentException, UniformInterfaceException, UriBuilderException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource webResource = client.resource(UriBuilder.fromUri(uri).build());

        try {
            ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class);
            ObjectMapper mapper = new ObjectMapper();
            String responseText = response.getEntity(String.class);
            if (responseText.equals("OK")) {
                System.out.println("Response: " + response.getEntity(String.class));
            } else {
                try {
                    ErrorCode errorCode = mapper.readValue(responseText, ErrorCode.class);
                    System.out.println("ErrorMessage: " + errorCode.getErrorMessage());
                    logText.appendText(errorCode.getErrorMessage() + "\n");
                } catch (IOException ex) {
                    System.out.println("mapper exception");
                }
            }
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            logText.appendText("Service nicht erreichbar");
        }

    }

    @FXML
    private void testAuswurf(ActionEvent event) {
        System.out.println("Test-Auswurf gestartet");
        logText.setWrapText(true);
        if (inputField.getText().length() == 2) {
            logText.appendText("Test-Auswurf gestartet für Fach: " + inputField.getText() + "\n");
            callMaintenanceService(BASIS_REST_URL + "order/" + inputField.getText());
            logText.appendText("Test-Auswurf beendet" + "\n");
        } else {
            logText.appendText("Test-Auswurf gestartet für Fach: ungültige Eingabe\n");
        }

    }

    @FXML
    private void calibrate(ActionEvent event) {
        System.out.println("Kalibration gestartet");
        logText.setWrapText(true);
        if (inputField.getText().length() == 2) {
            logText.appendText("Kalibration gestartet für Fach: " + inputField.getText() + "\n");
            callMaintenanceService(BASIS_REST_URL + "calibrate/" + inputField.getText());
            logText.appendText("Kalibration beendet" + "\n");
        } else {
            logText.appendText("Kalibration gestartet für Fach: ungültige Eingabe\n");
        }
    }

    @FXML
    private void motor(ActionEvent event) {
        System.out.println("Motortest gestartet");
        logText.setWrapText(true);
        if (inputField.getText().length() == 2) {
            logText.appendText("Motortest gestartet für Fach: " + inputField.getText() + "\n");
            callMaintenanceService(BASIS_REST_URL + "motor/" + inputField.getText());
            logText.appendText("Motortest beendet" + "\n");
        } else {
            logText.appendText("Motortest gestartet für Fach: ungültige Eingabe\n");
        }
    }

    @FXML
    private void allOff(ActionEvent event) {
        System.out.println("Alle Motoren aus gestartet");
        logText.setWrapText(true);
        logText.appendText("Alle Motoren werden ausgeschaltet..." + "\n");
        callMaintenanceService(BASIS_REST_URL + "alloff");
        logText.appendText("Alle Motoren erfolgreich ausgeschaltet." + "\n");
    }

    @FXML
    private void step(ActionEvent event) {
        System.out.println("Step gestartet");
        logText.setWrapText(true);
        if (inputField.getText().length() == 2) {
            logText.appendText("Step gestartet für Fach: " + inputField.getText() + "\n");
            callMaintenanceService(BASIS_REST_URL + "step/" + inputField.getText());
            logText.appendText("Step beendet" + "\n");
        } else {
            logText.appendText("Step gestartet für Fach: ungültige Eingabe\n");
        }
    }

    @FXML
    private void col(ActionEvent event) {
        System.out.println("Col-Test gestartet (Stromversorgung für Spalte)");
        logText.setWrapText(true);
        if (inputField.getText().length() == 2) {
            logText.appendText("Col-Test gestartet (Stromversorgung für Spalte) für Spalte: " + inputField.getText() + "\n");
            callMaintenanceService(BASIS_REST_URL + "colon/" + inputField.getText());
            logText.appendText("Spalte " + inputField.getText() + " mit Strom versorgt. Beenden mit All Off.\n");
        } else {
            logText.appendText("Col-Test gestartet (Stromversorgung für Spalte) für Spalte: ungültige Eingabe\n");
        }
    }

    @FXML
    private void row(ActionEvent event) {
        System.out.println("Row-Test gestartet (Stromversorgung für Reihe)");
        logText.setWrapText(true);
        if (inputField.getText().length() == 2) {
            logText.appendText("Row-Test gestartet (Stromversorgung für Reihe) für Reihe: " + inputField.getText() + "\n");
            callMaintenanceService(BASIS_REST_URL + "rowon/" + inputField.getText());
            logText.appendText("Reihe " + inputField.getText() + " mit Strom versorgt. Beenden mit All Off.\n");
        } else {
            logText.appendText("Row-Test gestartet (Stromversorgung für Reihe) für Reihe: ungültige Eingabe\n");
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

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource webResource = client.resource(UriBuilder.fromUri(BASIS_REST_URL + "maintenanceMode").build());
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class);
        String responseText = response.getEntity(String.class);

        Context.getInstance().setActiveUser(null);
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

}
