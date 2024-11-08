package es.juliogtrenard.examen_dein_julio_gonzalez.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProductosController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}