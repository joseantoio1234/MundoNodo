package mundonodo.model.dto;

import java.io.Serializable;

/**
 * Clase Data Transfer Object (DTO) que representa una línea o elemento dentro del carrito de compras.
 * Vincula un objeto de la clase {@link Producto} con una cantidad específica, permitiendo 
 * gestionar los artículos seleccionados por el usuario antes de procesar el pedido.
 * * @author Jose Antonio
 * @version 1.0
 */
public class ItemCarrito implements Serializable {

    private Producto producto;
    private int cantidad;

    /**
     * Constructor por defecto de la clase ItemCarrito.
     * Crea una instancia vacía para ser configurada posteriormente.
     */
    public ItemCarrito() {
    }

    /**
     * Constructor parametrizado para crear un elemento del carrito con datos iniciales.
     * * @param producto El objeto {@link Producto} que se añade al carrito.
     * @param cantidad La cantidad de unidades seleccionadas de dicho producto.
     */
    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el objeto producto asociado a este elemento del carrito.
     * * @return El {@link Producto} seleccionado.
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Establece el producto para este elemento del carrito.
     * * @param producto El objeto {@link Producto} a asignar.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Obtiene la cantidad de unidades de este producto en el carrito.
     * * @return El número de unidades.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de unidades para este producto.
     * * @param cantidad El número de unidades (debe ser un valor positivo).
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Calcula el importe subtotal de esta línea del carrito.
     * Multiplica el precio unitario del producto por la cantidad seleccionada.
     * * @return El subtotal acumulado por este producto.
     */
    public double getSubtotal() {
        if (producto != null) {
            return producto.getPrecio() * cantidad;
        }
        return 0.0;
    }
}