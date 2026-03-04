package mundonodo.model.dto;

import java.io.Serializable;

/**
 * Clase Data Transfer Object (DTO) que representa un producto del catálogo de MundoNodo.
 * Contiene información técnica, comercial y de inventario para la gestión de ventas
 * y visualización en la plataforma.
 * * @author Jose Antonio
 * @version 1.0
 */
public class Producto implements Serializable {

    private int idproducto;
    private String nombre;
    private String marca;
    private double precio;
    private String imagen;
    private String descripcion;
    private int stock;
    private String categoria;

    /**
     * Constructor por defecto de la clase Producto.
     * Permite la creación de una instancia vacía del producto.
     */
    public Producto() {
    }

    /**
     * Obtiene el identificador único del producto.
     * @return El ID numérico autoincremental de la base de datos.
     */
    public int getIdproducto() {
        return idproducto;
    }

    /**
     * Establece el identificador único del producto.
     * @param idproducto El ID numérico único del producto.
     */
    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    /**
     * Obtiene el nombre comercial del producto.
     * @return El nombre o título del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre comercial del producto.
     * @param nombre El nombre o título descriptivo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la marca del fabricante del producto.
     * @return El nombre de la marca.
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Establece la marca del fabricante del producto.
     * @param marca El nombre del fabricante o marca comercial.
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Obtiene el precio de venta del producto.
     * @return El valor numérico del precio.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio de venta del producto.
     * @param precio El importe monetario del producto.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el nombre del archivo de imagen del producto.
     * @return El nombre de la imagen ubicada en la carpeta de recursos.
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece el nombre del archivo de imagen del producto.
     * @param imagen El nombre o ruta del archivo de imagen.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtiene la descripción detallada del producto.
     * @return El texto con las especificaciones o detalles del artículo.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción detallada del producto.
     * @param descripcion El texto descriptivo del producto.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la cantidad de artículos disponibles en almacén.
     * @return El número de unidades en stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Establece la cantidad de artículos disponibles en almacén.
     * @param stock El número de unidades físicas disponibles.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Obtiene la categoría a la que pertenece el producto.
     * @return El nombre de la categoría.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría a la que pertenece el producto.
     * @param categoria El nombre de la categoría del catálogo.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}