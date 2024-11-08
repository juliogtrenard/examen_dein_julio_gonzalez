package es.juliogtrenard.examen_dein_julio_gonzalez.controladores;

import es.juliogtrenard.examen_dein_julio_gonzalez.dao.DaoProducto;
import es.juliogtrenard.examen_dein_julio_gonzalez.modelos.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
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
     * Tabla
     */
    @FXML
    private TableView<Producto> tabla;

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
    }

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
}