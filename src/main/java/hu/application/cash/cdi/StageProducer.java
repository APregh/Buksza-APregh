package hu.application.cash.cdi;

import javafx.stage.Stage;
import java.io.Serializable;

/**
 * Producer osztály, amely az aktuális {@link Stage}-et tárolja
 *
 * @author Pregh András
 */
public class StageProducer implements Serializable {

    private static final Long serialVersionUID = 1L;

    private static Stage stage;

    /**
     * Ha az aktuális {@link Stage} null, akkor létrehoz egyet és beállítja, amúgy pedig visszaadja az aktiálus {@link Stage}-et
     *
     * @return
     */
    public static Stage getStaticStage() {
        if (stage == null) {
            stage = new Stage();
        }
        return stage;
    }

}
