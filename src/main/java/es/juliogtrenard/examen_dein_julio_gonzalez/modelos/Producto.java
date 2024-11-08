package es.juliogtrenard.examen_dein_julio_gonzalez.modelos;

import java.sql.Blob;

public class Producto {
    /**
     * Codigo del producto
     */
    private String codigo;

    /**
     * Nombre del producto
     */
    private String nombre;

    /**
     * Precio del producto
     */
    private double precio;

    /**
     * Disponibilidad del producto
     */
    private boolean disponible;

    /**
     * Imagen del producto
     */
    private Blob imagen;

    /**
     * Constructor con parametros
     * @param codigo Codigo del producto
     * @param nombre Nombre del producto
     * @param precio Precio del producto
     * @param disponible Disponibilidad del producto
     * @param imagen Imagen del producto
     */
    public Producto(String codigo, String nombre, double precio, boolean disponible, Blob imagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
        this.imagen = imagen;
    }

    /**
     * Constructor vacio
     */
    public Producto() {

    }

    /**
     * Getter del codigo del producto
     * @return Codigo del producto
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código del producto.
     *
     * @param codigo El nuevo código del producto.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Getter del nombre del producto.
     *
     * @return Nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre El nuevo nombre del producto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter del precio del producto.
     *
     * @return El precio del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     *
     * @param precio El nuevo precio del producto.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Devuelve si el producto está disponible o no.
     *
     * @return true si el producto está disponible, false en caso contrario.
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Establece si el producto está disponible o no.
     *
     * @param disponible true si el producto está disponible, false en caso
     *                   contrario.
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Obtiene la imagen del producto.
     *
     * @return La imagen del producto como un objeto Blob.
     */
    public Blob getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen del producto.
     *
     * @param imagen La nueva imagen del producto como un objeto Blob.
     */
    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }
}
