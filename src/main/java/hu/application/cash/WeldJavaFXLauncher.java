package hu.application.cash;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

import org.jboss.weld.environment.se.WeldContainer;

import hu.application.cash.cdi.CDIControllerFactory;
import hu.application.cash.cdi.StageProducer;
import hu.application.cash.cdi.WeldContainerProducer;

/**
 * Osztály, amely a JavaFX alkalmazásunkat elindítja, CDI támogatással.
 *
 * @author Pregh András
 */
public class WeldJavaFXLauncher extends Application {

    public static void main(String[] args) {
        launch(args);//ebben hívódik meg a lenti start
    }

    /**
     * Elindítja a JavaFX alkalmazásunkat CDI támogatással.
     *
     * @param primaryStage
     * @throws IOException
     */
    @Override //felül kell írni, mivel az Application-nek az absztrakt metódusát használjuk
    public void start(Stage primaryStage) throws IOException {
        WeldContainer weldContainer = WeldContainerProducer.getStaticWeldContainer();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/home.fxml"));//Loader létrehozás
        loader.setControllerFactory(weldContainer.select(CDIControllerFactory.class).get());
        Parent root = loader.load();//Loeader átadása a rootnak
        Stage stage = StageProducer.getStaticStage();
        stage.setTitle("Buksza");
        stage.getIcons().add(new Image("file:buksza_icon3.png"));
        stage.setScene(new Scene(root, 1024, 800));//root meghívása a stagen keresztül
        stage.setResizable(false);
        stage.show();//megmutatja ami be lett töltve a stage-ba
    }
}
