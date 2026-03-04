package mundonodo.model.dto;

import java.util.Date;

public class Pedido {
    private int idpedido;
    private int idusuario;
    private Date fecha;
    private double total;
    private String estado;

    public Pedido() {
    }

    public int getIdpedido() { return idpedido; }
    public void setIdpedido(int idpedido) { this.idpedido = idpedido; }

    public int getIdusuario() { return idusuario; }
    public void setIdusuario(int idusuario) { this.idusuario = idusuario; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}