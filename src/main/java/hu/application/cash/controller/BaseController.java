package hu.application.cash.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;

import javax.inject.Inject;

import org.jboss.weld.environment.se.WeldContainer;
import org.slf4j.Logger;

import hu.application.cash.cdi.CDIControllerFactory;
import hu.application.cash.cdi.StageProducer;
import hu.application.cash.cdi.WeldContainerProducer;

/**
 * Base Controller osztály, a többi Controller osztály támogatásához.
 *
 * @author Pregh András
 */
public class BaseController extends AlertController {

    @Inject
    private Logger logger;

    /**
     * A képernyő megváltoztatása a paraméterül adott fxml-re.
     *
     * @param url az fxml elérési útja, amelyre le szeretnénk cserélni a képernyőt.
     */
    protected void changeScreen(String url) {
        try {
            WeldContainer weldContainer = WeldContainerProducer.getStaticWeldContainer();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            loader.setControllerFactory(weldContainer.select(CDIControllerFactory.class).get());
            Parent root = loader.load();
            Stage stage = StageProducer.getStaticStage();
            stage.setScene(new Scene(root, 1024, 800));
            stage.show();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    /**
     * Új képernyő megnyitása a paraméterül adott fxml-ből.
     *
     * @param url az fxml elérési útja, amelyet meg szeretnénk nyitni.
     */
    protected void openScreen(String url) {
        try {
            WeldContainer weldContainer = WeldContainerProducer.getStaticWeldContainer();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            loader.setControllerFactory(weldContainer.select(CDIControllerFactory.class).get());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 300));
            stage.toFront();
            stage.showAndWait();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    /**
     * Stage lezárása és az alkalmazás bezárása.
     */
    protected void closeStage() {
        ButtonType yesButton = new ButtonType("Igen");
        ButtonType cancelButton = new ButtonType("Mégsem");
        Optional<ButtonType> result =
                confirmationAlert(yesButton, cancelButton, "Kilépés", "Biztosan ki szeretne lépni az alkalmazásból?").showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            StageProducer.getStaticStage().close();
            System.exit(0);
        }
    }

}
