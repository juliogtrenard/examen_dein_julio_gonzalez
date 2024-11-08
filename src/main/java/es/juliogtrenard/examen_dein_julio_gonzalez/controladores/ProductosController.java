package es.juliogtrenard.examen_dein_julio_gonzalez.controladores;

import es.juliogtrenard.examen_dein_julio_gonzalez.dao.DaoProducto;
import es.juliogtrenard.examen_dein_julio_gonzalez.modelos.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductosController implements Initializable {
    /**
     * Campo de texto del codigo
     */
    @FXML
    private TextField txtCodigo;

    /**
     * Campo de texto del nombre
     */
    @FXML
    private TextField txtNombre;

    /**
     * Campo de texto del precio
     */
    @FXML
    private TextField txtPrecio;

    /**
     * CheckBox de la imagen
     */
    @FXML
    private CheckBox cbDisponible;

    /**
     * Boton para subir imagen
     */
    @FXML
    private Button btnImagen;

    /**
     * Boton para subir crear
     */
    @FXML
    private Button btnCrear;

    /**
     * Boton para actualizar
     */
    @FXML
    private Button btnActualizar;

    /**
     * Boton para limpiar
     */
    @FXML
    private Button btnLimpiar;

    /**
     * Tabla
     */
    @FXML
    private TableView<Producto> tabla;

    /**
     * La imagen
     */
    private Blob imagen;

    /**
     * La propiedad de la imagen
     */
    @FXML
    private ImageView foto;

    private ObservableList masterData = FXCollections.observableArrayList();
    private ObservableList filteredData = FXCollections.observableArrayList();

    /**
     * Se ejecuta cuando se carga la ventana
     *
     * @param url la url
     * @param resourceBundle los recursos
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarProductos();
        btnActualizar.setDisable(true);
    }

    /**
     * Carga los productos de la base de datos
     */
    private void cargarProductos() {
        tabla.getSelectionModel().clearSelection();
        masterData.clear();
        filteredData.clear();
        tabla.getItems().clear();
        tabla.getColumns().clear();

        TableColumn<Producto, String> colCodigo = new TableColumn<>("CODIGO");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        TableColumn<Producto, String> colNombre = new TableColumn<>("NOMBRE");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Producto, Double> colPrecio = new TableColumn<>("PRECIO");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        TableColumn<Producto, Boolean> colDisponible = new TableColumn<>("DISPONIBLE");
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        colCodigo.prefWidthProperty().bind(tabla.widthProperty().divide(4));
        colNombre.prefWidthProperty().bind(tabla.widthProperty().divide(4));
        colPrecio.prefWidthProperty().bind(tabla.widthProperty().divide(4));
        colDisponible.prefWidthProperty().bind(tabla.widthProperty().divide(4));

        tabla.getColumns().addAll(colCodigo,colNombre,colPrecio, colDisponible);

        ObservableList<Producto> productos = DaoProducto.cargarListado();
        masterData.setAll(productos);
        tabla.setItems(productos);
    }

    /**
     * Abre un FileChooser para seleccionar una imagen
     *
     * @param event El evento
     */
    @FXML
    void seleccionImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg", "*.jpeg","*.png"));
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(null);
        try {
            double kbs = (double) file.length() / 1024;
            if (kbs > 64) {
                alerta("La imagen es muy grande");
            } else {
                InputStream imagen = new FileInputStream(file);
                Blob blob = DaoProducto.convertFileToBlob(file);
                this.imagen = blob;
                foto.setImage(new Image(imagen));
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            alerta("Error al subir la foto");
        }
    }

    /**
     * Muestra un mensaje de fallo
     *
     * @param texto contenido de la alerta
     */
    public void alerta(String texto) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("Error");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

    /**
     * Muestra un mensaje de confirmación
     *
     * @param texto contenido de la alerta
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}