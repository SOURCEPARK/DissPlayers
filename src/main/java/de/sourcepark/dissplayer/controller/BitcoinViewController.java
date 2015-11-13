/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sourcepark.dissplayer.controller;


import de.sourcepark.dissplayer.Context;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author jnaperkowski
 */
public class BitcoinViewController implements Initializable {

    @FXML
    public ImageView qrCode;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Label msg;

    private static final String FXML_RESOURCE = "/fxml/StartPage.fxml";

    private static final String MSG_WELCOME = "Bitte scanne den QR-Code mit deiner mobilen Bitcoin-Wallet-App und schließe den Bezahlvorgang dort ab.";
    private static final String MSG_SUCCESS = "Zahlung erhalten";
    private static final String MSG_FAILURE = "Es ist ein Fehler aufgetreten";

    // FIXME: temporary solution: fixed btc address/amount
    private static final String BTC_ADDRESS = "13cSu17oJ2dFX5mTGeMTh8N3UTPv2pN5CZ";
    private static final Double BTC_AMOUNT = 0.0001;

    private static final String URL_QR_CODE = "http://localhost:1337/qr_code?btc_amount=%f&btc_receiver_address=%s";
    private static final String URL_DETECT_PAYMENT = "http://localhost:1337/detect_payment?btc_amount=%f&btc_receiver_address=%s";

    private static final String SERVER_OK = "payment received";

    Task<Void> awaitPaymentTask = new Task<Void>() {
        @Override protected Void call() throws Exception {
            updateMessage(MSG_WELCOME);

            String btcAddress = getBtcAddress();
            Double btcAmount = getBtcAmount();

            if (awaitPayment(btcAddress, btcAmount))
                updateMessage(MSG_SUCCESS);

                // TODO: drop candy!!
            else
                updateMessage(MSG_FAILURE);

            return null;
        }
    };

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        try {
            String btcAddress = getBtcAddress();
            Double btcAmount = getBtcAmount();

            this.renderQrCode(btcAddress, btcAmount);

            awaitPaymentTask.setOnSucceeded(e -> showStartPage());
            awaitPaymentTask.setOnFailed(e -> showStartPage());

            msg.textProperty().bind(awaitPaymentTask.messageProperty());

            Thread awaitPaymentThread = new Thread(awaitPaymentTask);
            awaitPaymentThread.setDaemon(true);
            awaitPaymentThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showStartPage() {
        try {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(FXML_RESOURCE));

            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getBtcAddress() {
        // TODO: implement btc address managemen/mapping from active orderNumber
        return BTC_ADDRESS;
    }

    private Double getBtcAmount() {
        // TODO: implement btc amount management/mapping from active orderNumber
        return BTC_AMOUNT;
    }

    protected boolean awaitPayment(String address, Double amount) {
        try {
            URL url = new URL(
                    String.format(Locale.US, URL_DETECT_PAYMENT, amount, address)
            );

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            InputStream inputStream = conn.getInputStream();

            StringBuilder sb = new StringBuilder();

            String line;

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            if (sb.toString().equals(SERVER_OK))
                return true;

            Context.getInstance().setPaymentType(null);
            Context.getInstance().setActiveOrderNumber(null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }

    protected void renderQrCode(String address, Double amount) throws Exception {
        URL url = new URL(
                String.format(Locale.US, URL_QR_CODE, amount, address)
        );

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        Image qrCodeImage = new Image(conn.getInputStream());
        qrCode.setImage(qrCodeImage);
    }
}
