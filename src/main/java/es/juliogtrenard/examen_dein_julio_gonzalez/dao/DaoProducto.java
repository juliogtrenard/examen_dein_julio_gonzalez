package es.juliogtrenard.examen_dein_julio_gonzalez.dao;

import es.juliogtrenard.examen_dein_julio_gonzalez.db.DBConnect;
import es.juliogtrenard.examen_dein_julio_gonzalez.modelos.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

/**
 * Clase para manejar las operaciones de la base de datos
 */
public class DaoProducto {
    /**
     * Carga los datos de la tabla Productos y los devuelve para usarlos en un listado
     *
     * @return listado Lista de productos
     */
    public static ObservableList<Producto> cargarListado() {
        DBConnect connection;
        ObservableList<Producto> productos = FXCollections.observableArrayList();
        try{
            connection = new DBConnect();
            String consulta = "SELECT codigo,nombre,precio,disponible,imagen FROM productos";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                boolean disponible = rs.getBoolean("disponible");
                Blob imagen = rs.getBlob("imagen");

                Producto producto = new Producto(codigo,nombre,precio,disponible,imagen);

                productos.add(producto);
            }
            rs.close();
            connection.closeConnection();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return productos;
    }

    /**
     * Busca un producto por medio de su id
     *
     * @param codigo Codigo del producto a buscar
     * @return producto o null
     */
    public static Producto getProducto(String codigo) {
        DBConnect connection;
        Producto producto = null;
        try {
            connection = new DBConnect();
            String consulta = "SELECT codigo,nombre,precio,disponible,imagen FROM productos WHERE codigo = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, codigo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String codigoProducto = rs.getString("codigo");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                boolean disponible = rs.getBoolean("disponible");
                Blob imagen = rs.getBlob("imagen");
                producto = new Producto(codigo,nombre,precio,disponible,imagen);
            }
            rs.close();
            connection.closeConnection();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return producto;
    }

    /**
     * Crea un nuevo producto en la BD
     *
     * @param producto Producto con datos nuevos
     * @return true si se puede false si no
     */
    public  static boolean insertar(Producto producto) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "INSERT INTO productos (codigo,nombre,precio,disponible,imagen) VALUES (?,?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, producto.getCodigo());
            pstmt.setString(2, producto.getNombre());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setBoolean(4, producto.isDisponible());
            pstmt.setBlob(5, producto.getImagen());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println(filasAfectadas);
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Modifica los datos de un producto
     *
     * @param producto Producto
     * @param productoNuevo Nuevos datos del producto
     * @return  true -> Se puede, false -> No se puede
     */
    public static boolean modificar(Producto producto, Producto productoNuevo) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "UPDATE productos SET nombre = ?,precio = ?,disponible = ?,imagen = ? WHERE codigo = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, productoNuevo.getNombre());
            pstmt.setDouble(2, productoNuevo.getPrecio());
            pstmt.setBoolean(3, productoNuevo.isDisponible());
            pstmt.setBlob(4, productoNuevo.getImagen());
            pstmt.setString(5, producto.getCodigo());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println(filasAfectadas);
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un producto
     *
     * @param producto Producto a eliminar
     * @return boolean Si se puede eliminar o no
     */
    public  static boolean eliminar(Producto producto){
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "DELETE FROM productos WHERE codigo = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, producto.getCodigo());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Convierte un objeto File a Blob
     *
     * @param file fichero imagen
     * @return blob el blob de imagen
     * @throws SQLException Error de sql
     * @throws IOException Error de E/S de datos
     */
    public static Blob convertFileToBlob(File file) throws SQLException, IOException {
        DBConnect connection = new DBConnect();

        try (Connection conn = connection.getConnection();
             FileInputStream inputStream = new FileInputStream(file)) {

            Blob blob = conn.createBlob();

            byte[] buffer = new byte[1024];
            int bytesRead;

            try (var outputStream = blob.setBinaryStream(1)) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return blob;
        }
    }
}
