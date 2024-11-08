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

        tabla.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                txtCodigo.setText(newValue.getCodigo());
                txtCodigo.setDisable(true);
                btnActualizar.setDisable(false);
                btnCrear.setDisable(true);
                txtNombre.setText(newValue.getNombre());
                txtPrecio.setText(String.valueOf(newValue.getPrecio()));
                cbDisponible.setSelected(newValue.isDisponible());

                if (newValue.getImagen() != null) {
                    InputStream imagen = null;
                    try {
                        imagen = newValue.getImagen().getBinaryStream();
                    } catch (SQLException e) {
                        alerta("Error al obtener la imagen");
                    }
                    assert imagen != null;
                    foto.setImage(new Image(imagen));
                }
            }
        });
    }

    /**
     * Se ejecuta cuando se pulsa el botón "Actualizar".
     */
    @FXML
    void actualizar() {
        String error = validarCampos();
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Producto productoSeleccionado = tabla.getSelectionModel().getSelectedItem();
            Producto productoNuevo = new Producto(txtCodigo.getText(), txtNombre.getText(), Double.parseDouble(txtPrecio.getText()), cbDisponible.isSelected(), imagen);

            DaoProducto.modificar(productoSeleccionado, productoNuevo);
            cargarProductos();
            limpiarCampos();
            confirmacion("Producto actualizado correctamente!");
            btnActualizar.setDisable(true);
            btnCrear.setDisable(false);
            txtCodigo.setDisable(false);
        }
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
     * Se ejecuta cuando se pulsa el botón "Crear". Valida que los campos de texto tengan un valor no vacío.
     * Si los campos son válidos, llama a {@link #procesarProducto()}
     */
    @FXML
    void crear() {
        String error = validarCampos();
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            procesarProducto();
        }
    }

    /**
     * Valida que los campos de texto tengan un valor no vacío
     * @return Cadena con los errores de validación o cadena vacía si los campos son válidos
     */
    private String validarCampos() {
        StringBuilder error = new StringBuilder();

        if (txtCodigo.getText().isEmpty()) {
            error.append("El codigo no puede estar vacío\n");
        } else if(txtCodigo.getText().length() > 5 || txtCodigo.getText().length() < 5) {
            error.append("El codigo debe tener 5 caracteres\n");
        }
        if (txtNombre.getText().isEmpty()) {
            error.append("El nombre no puede estar vacío\n");
        }
        if (txtPrecio.getText().isEmpty()) {
            error.append("El precio no puede estar vacío\n");
        }
        try{
            Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            error.append("El precio debe ser un número decimal\n");
        }

        return error.toString();
    }

    /**
     * Procesa el producto.
     */
    private void procesarProducto() {
        Producto producto = crearProducto();
        if (esProductoDuplicado(producto)) {
            alerta("El producto ya existe");
        } else {
            guardarProducto(producto);
        }
    }

    /**
     * Crea un objeto producto
     *
     * @return Producto con sus datos
     */
    private Producto crearProducto() {
        Producto producto = new Producto();
        producto.setCodigo(txtCodigo.getText());
        producto.setNombre(txtNombre.getText());
        producto.setPrecio(Double.parseDouble(txtPrecio.getText()));
        producto.setDisponible(cbDisponible.isSelected());
        producto.setImagen(imagen);
        return producto;
    }

    /**
     * Verifica si el producto ya existe
     *
     * @param producto a comparar
     * @return true si existe, false si no
     */
    private boolean esProductoDuplicado(Producto producto) {
        ObservableList<Producto> productos = DaoProducto.cargarListado();
        return productos.contains(producto);
    }

    /**
     * Guarda el producto en la BBDD
     * o un mensaje de error si ha habido un problema
     *
     * @param producto a insertar
     */
    private void guardarProducto(Producto producto) {
        boolean resultado = DaoProducto.insertar(producto);
        if (!resultado) {
            alerta("Ha habido un error insertando el producto. Por favor inténtelo de nuevo");
        } else {
            confirmacion("Producto insertado correctamente!");
            limpiarCampos();
            cargarProductos();
        }
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

    /**
     * Limpia los campos de entrada de texto.
     */
    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
    }
}