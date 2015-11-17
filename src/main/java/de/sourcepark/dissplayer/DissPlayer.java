package de.sourcepark.dissplayer;

import de.sourcepark.dissplayer.controller.StartPageController;
import de.sourcepark.services.DissplayerServer;
import de.sourcepark.services.AuthService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author cjelinski
 */
public class DissPlayer extends Application {

    public static final transient int PORT = 8888;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/StartPage.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        Scene scene = new Scene(root);
        
        final DissplayerServer server = new DissplayerServer();
        AuthService authService = new AuthService();
        authService.addObserver((StartPageController)fxmlLoader.getController());
        
        server.startServer(PORT, authService);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
