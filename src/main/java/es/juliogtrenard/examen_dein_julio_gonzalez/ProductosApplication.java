package es.juliogtrenard.examen_dein_julio_gonzalez;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ProductosApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductosApplication.class.getResource("/es/juliogtrenard/examen_dein_julio_gonzalez/fxml/productos.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setTitle("PRODUCTOS");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/es/juliogtrenard/examen_dein_julio_gonzalez/img/carrito.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}