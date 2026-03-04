package mundonodo.model.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase Data Transfer Object (DTO) que representa la cabecera de un pedido en MundoNodo.
 * Contiene la información general de la transacción, vinculándola a un usuario
 * específico y registrando el importe total y el estado del envío.
 * * @author Jose Antonio
 * @version 1.0
 */
public class Pedido implements Serializable {

    private int idpedido;
    private int idusuario;
    private Date fecha;
    private double total;
    private String estado;

    /**
     * Constructor por defecto de la clase Pedido.
     * Inicializa una instancia vacía del pedido para ser gestionada por el DAO.
     */
    public Pedido() {
    }

    /**
     * Obtiene el identificador único del pedido.
     * @return El ID numérico autoincremental de la base de datos (clave primaria).
     */
    public int getIdpedido() {
        return idpedido;
    }

    /**
     * Establece el identificador único del pedido.
     * @param idpedido El ID generado por la base de datos tras la inserción.
     */
    public void setIdpedido(int idpedido) {
        this.idpedido = idpedido;
    }

    /**
     * Obtiene el identificador del usuario que realizó la compra.
     * @return El ID del usuario (clave foránea).
     */
    public int getIdusuario() {
        return idusuario;
    }

    /**
     * Vincula el pedido a un usuario específico mediante su ID.
     * @param idusuario El ID del usuario comprador.
     */
    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    /**
     * Obtiene la fecha y hora en la que se registró el pedido.
     * @return Objeto {@link Date} con el momento exacto de la transacción.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de registro del pedido.
     * @param fecha Objeto {@link Date} que representa el momento de la compra.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el importe total del pedido (base imponible + impuestos).
     * @return El valor total de la transacción.
     */
    public double getTotal() {
        return total;
    }

    /**
     * Establece el importe total de la compra.
     * @param total El importe total calculado en el proceso de compra.
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Obtiene el estado actual del pedido.
     * @return Cadena de texto que describe la situación (ej. Pendiente, Enviado, Cancelado).
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Define el estado actual en el que se encuentra el pedido.
     * @param estado Texto descriptivo del estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}