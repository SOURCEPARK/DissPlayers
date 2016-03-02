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
import de.sourcepark.dissplayer.Context;
import de.sourcepark.dissplayer.DissPlayer;
import de.sourcepark.dissplayer.exceptions.SlotNotFoundException;
import de.sourcepark.dissplayer.pojo.OrderClient;
import de.sourcepark.dissplayer.pojo.Slot;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author jnaperkowski
 */
public class BitcoinViewController implements Initializable {

    private static final transient Logger LOG = LoggerFactory.getLogger(BitcoinViewController.class);

    @FXML
    public ImageView qrCode;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Label msg;

    @FXML
    private Label euro;

    @FXML
    private Label mBtc;

    private static final String FXML_RESOURCE = "/fxml/StartPage.fxml";

    private static final String MSG_WELCOME = "Scanne den QR-Code mit deiner mobilen Bitcoin Wallet und sende den angezeigten Betrag.";
    private static final String MSG_BITCOIN_SERVER_OFFLINE = "Bitcoin-Payment-Server ist nicht erreichbar";
    private static final String MSG_UNKNOWN_ERROR = "unbekannter Fehler: %s";
    private static final String MSG_BITCOIN_NOT_ALLOWED = "Bitcoin-Zahlung ist für diesen Slot nicht erlaubt!";

    private static String HOST;
    private static String PORT;
    static {
        try {
            Properties properties = DissPlayer.getProperties();
            HOST = properties.getProperty("bitcoin_host");
            PORT = properties.getProperty("bitcoin_port");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String HUBBA_BUBBA_PORT = "9999";

    private static final String URL_QR_CODE = "http://" + HOST + ":" + PORT + "/qr_code?btc_amount=%f&btc_receiver_address=%s";
    private static final String URL_DETECT_PAYMENT = "http://" + HOST + ":" + PORT + "/detect_payment?btc_amount=%f&btc_receiver_address=%s";
    private static final String URL_EXCHANGE_RATE = "http://" + HOST + ":" + PORT + "/exchange_rate?eur_amount=%f";
    private static final String URL_SLOT_INFO = "http://" + HOST + ":" + HUBBA_BUBBA_PORT + "/control/slot/%s";

    private static final int MSG_SHOW_TIME = 5000; // ms

    private enum PaymentResult {
        Ok,
        PAYMENT_INSUFFICIENT,
        TIMEOUT,
        BACKEND_ERROR,
        UNKNOWN_ERROR
    }

    private static final Map<PaymentResult, String> PAYMENT_RESULT_MESSAGES = new HashMap<PaymentResult, String>();
    static {
        PAYMENT_RESULT_MESSAGES.put(PaymentResult.Ok, "Zahlung erhalten");
        PAYMENT_RESULT_MESSAGES.put(PaymentResult.PAYMENT_INSUFFICIENT, "erhaltene Zahlung ungenügend");
        PAYMENT_RESULT_MESSAGES.put(PaymentResult.TIMEOUT, "keine Zahlung erhalten");
        PAYMENT_RESULT_MESSAGES.put(PaymentResult.BACKEND_ERROR, "unbekannter Fehler im Bitcoin-Backend");
        PAYMENT_RESULT_MESSAGES.put(PaymentResult.UNKNOWN_ERROR, "fehlerhafte Antwort vom Bitcoin-Backend");
    }

    private static final Map<String, PaymentResult> PAYMENT_SERVER_RESULT_MAP = new HashMap<String, PaymentResult>();
    static {
        PAYMENT_SERVER_RESULT_MAP.put("Ok", PaymentResult.Ok);
        PAYMENT_SERVER_RESULT_MAP.put("InsufficientAmount", PaymentResult.PAYMENT_INSUFFICIENT);
        PAYMENT_SERVER_RESULT_MAP.put("Timeout", PaymentResult.TIMEOUT);
        PAYMENT_SERVER_RESULT_MAP.put("BackendError", PaymentResult.BACKEND_ERROR);
    }

    private enum ExchangeResult {
        Ok,
        BACKEND_ERROR
    }

    private static final Map<String, ExchangeResult> EXCHANGE_SERVER_RESULT_MAP = new HashMap<String, ExchangeResult>();
    static {
        EXCHANGE_SERVER_RESULT_MAP.put("Ok", ExchangeResult.Ok);
        EXCHANGE_SERVER_RESULT_MAP.put("BackendError", ExchangeResult.BACKEND_ERROR);
    }

    public class PaymentDetectionTask extends Task<Void> {
        private final double btcAmount;
        private final String btcAddress;

        public PaymentDetectionTask(double btcAmount, String btcAddress) {
            this.btcAmount = btcAmount;
            this.btcAddress = btcAddress;
        }

        @Override protected Void call() throws Exception {
            try {
                updateMessage(MSG_WELCOME);

                PaymentResult paymentResult = awaitPayment(btcAddress, btcAmount);
                String resultMsg = PAYMENT_RESULT_MESSAGES.get(paymentResult);
                if (paymentResult == PaymentResult.Ok) {
                    String slotNumber = Context.getInstance().getActiveOrderNumber();
                    String orderServiceErrMsg = OrderClient.callOrderService(slotNumber);
                    String slotDecreaseSlotItemCountServiceErrMsg = OrderClient.callDecreaseSlotItemCountService(slotNumber);

                    if (orderServiceErrMsg != null)
                        resultMsg = orderServiceErrMsg;

                    if (slotDecreaseSlotItemCountServiceErrMsg != null)
                        resultMsg = slotDecreaseSlotItemCountServiceErrMsg;
                }

                updateMessage(resultMsg);

                Thread.sleep(MSG_SHOW_TIME);
                return null;
            } finally {
                Context.getInstance().setPaymentType(null);
                Context.getInstance().setActiveOrderNumber(null);
            }
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Slot slot = getSlotInfo(Context.getInstance().getActiveOrderNumber());
            String btcAddress = getBtcAddress(slot);
            Double euroAmount = getEuroAmount(slot);
            Double btcAmount = this.convertEuroToBtc(euroAmount);

            this.renderQrCode(btcAddress, btcAmount);

            euro.setText(String.format("%.2f", euroAmount));
            // show mBTC instead of BTC
            mBtc.setText(String.format("%.2f", btcAmount * 1000));

            PaymentDetectionTask paymentDetectionTask = new PaymentDetectionTask(btcAmount, btcAddress);
            paymentDetectionTask.setOnSucceeded(e -> showStartPage());
            paymentDetectionTask.setOnFailed(e -> showStartPage());

            msg.textProperty().bind(paymentDetectionTask.messageProperty());

            Thread awaitPaymentThread = new Thread(paymentDetectionTask);
            awaitPaymentThread.setDaemon(true);
            awaitPaymentThread.start();
        } catch (ConnectException e) {
            msg.setText(MSG_BITCOIN_SERVER_OFFLINE);
        } catch (SlotNotFoundException e) {
            msg.setText(e.getMessage());
        } catch (Exception e) {
            msg.setText(String.format(MSG_UNKNOWN_ERROR, e.getMessage()));
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

    private String getBtcAddress(Slot slot) {
        return slot.getBtcRcvAddress();
    }

    private Double getEuroAmount(Slot slot) {
        return Double.parseDouble(slot.getPrize());
    }

    private Slot getSlotInfo(String slotNo) throws Exception {
        Slot slot;

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        String slotInfoUrl = String.format(URL_SLOT_INFO, slotNo);
        WebResource webResource = client.resource(UriBuilder.fromUri(slotInfoUrl).build());
        String responseString = "";

        ObjectMapper mapper = new ObjectMapper();
        try {
            ClientResponse responseAuth = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).get(ClientResponse.class);

            responseString = responseAuth.getEntity(String.class);
            slot = mapper.readValue(responseString, Slot.class);
            if (slot.getErrorCode() != null)
                throw new SlotNotFoundException(slot.getErrorMessage());
            if (!slot.isBtcAllowed())
                throw new SlotNotFoundException(MSG_BITCOIN_NOT_ALLOWED);
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            throw e;
        }

        return slot;
    }

    protected PaymentResult awaitPayment(String address, Double amount) throws Exception {
        PaymentResult paymentResult = PaymentResult.UNKNOWN_ERROR;

        try {
            URL url = new URL(
                    String.format(Locale.US, URL_DETECT_PAYMENT, amount, address)
            );

            String resultCode = httpGet(url);

            paymentResult = PAYMENT_SERVER_RESULT_MAP.getOrDefault(resultCode, PaymentResult.UNKNOWN_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return paymentResult;
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

    protected Double convertEuroToBtc(Double euroAmount) throws Exception {
        URL url = new URL(
                String.format(Locale.US, URL_EXCHANGE_RATE, euroAmount)
        );

        String body = httpGet(url);

        return Double.parseDouble(body);
    }

    /**
     *  Returns the response body
      */
    private String httpGet(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        InputStream inputStream = conn.getInputStream();

        StringBuilder sb = new StringBuilder();

        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }
}
