package hu.application.cash.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Controller osztály, amely a {@link Alert} felugró üzenetek vezérlését végzi el.
 *
 * @author Pregh András
 */
public class AlertController {

    /**
     * Sikeres üzenet megjelenítése.
     *
     * @param title a cím
     * @param text  a szöveg
     * @return {@link Alert}
     */
    public Alert successAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        fillAlert(alert, title, text);
        return alert;
    }

    /**
     * Megerősítő üzenet megjelenítése.
     *
     * @param yesButton    az igen gomb
     * @param cancelButton a mégse gomb
     * @param title        a cím
     * @param text         a szöveg
     * @return {@link Alert}
     */
    public Alert confirmationAlert(ButtonType yesButton, ButtonType cancelButton, String title, String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        fillAlert(alert, title, text);
        alert.getButtonTypes().setAll(yesButton, cancelButton);
        return alert;
    }

    private void fillAlert(Alert alert, String title, String text) {
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
    }

}
