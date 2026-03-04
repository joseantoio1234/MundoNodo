package mundonodo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import mundonodo.model.dto.Pedido;
import mundonodo.model.dto.Producto;
import mundonodo.model.dto.ItemCarrito;
import mundonodo.model.mysql.Conexion; 

public class PedidoDAO {

  
    public int insertarPedidoCompleto(DataSource ds, Pedido pedido, List<ItemCarrito> carrito) {
        String sqlPedido = "INSERT INTO pedidos (idusuario, fecha, total, estado) VALUES (?, NOW(), ?, 'PAGADO')";
        String sqlLinea = "INSERT INTO lineaspedidos (idpedido, idproducto, cantidad) VALUES (?, ?, ?)";
        
        int idGenerado = -1;
        Connection con = null;

        try {
            con = Conexion.getConexion(ds);
            con.setAutoCommit(false); 

            // 1. INSERTAR CABECERA DEL PEDIDO
            try (PreparedStatement psP = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                psP.setInt(1, pedido.getIdusuario());
                psP.setDouble(2, pedido.getTotal());
                psP.executeUpdate();

                try (ResultSet rs = psP.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                    }
                }
            }

            // 2. INSERTAR LÍNEAS DE DETALLE 
            if (idGenerado != -1) {
                try (PreparedStatement psL = con.prepareStatement(sqlLinea)) {
                    for (ItemCarrito item : carrito) {
                        psL.setInt(1, idGenerado);
                        psL.setInt(2, item.getProducto().getIdproducto());
                        psL.setInt(3, item.getCantidad());
                        psL.addBatch(); 
                    }
                    psL.executeBatch(); 
                }
                con.commit(); 
                System.out.println("Pedido " + idGenerado + " guardado con éxito.");
            }

        } catch (SQLException e) {
            if (con != null) {
                try { 
                    con.rollback(); 
                    System.err.println("Transacción deshecha (Rollback) debido a: " + e.getMessage());
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
            idGenerado = -1;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return idGenerado;
    }

   
    public List<Pedido> listarPedidosPorUsuario(DataSource ds, int idUsuario) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT idpedido, idusuario, fecha, total, estado FROM pedidos WHERE idusuario = ? ORDER BY fecha DESC";
        
        try (Connection con = Conexion.getConexion(ds);
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setIdpedido(rs.getInt("idpedido"));
                    p.setIdusuario(rs.getInt("idusuario"));
                    p.setFecha(rs.getTimestamp("fecha"));
                    p.setTotal(rs.getDouble("total"));
                    p.setEstado(rs.getString("estado"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
        }
        return lista;
    }


    public List<ItemCarrito> listarDetallesPorPedido(DataSource ds, int idPedido) {
        List<ItemCarrito> detalles = new ArrayList<>();
        String sql = "SELECT p.nombre, p.precio, p.imagen, lp.cantidad " +
                     "FROM lineaspedidos lp " +
                     "JOIN productos p ON lp.idproducto = p.idproducto " +
                     "WHERE lp.idpedido = ?";
        
        try (Connection con = Conexion.getConexion(ds);
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto prod = new Producto();
                    prod.setNombre(rs.getString("nombre"));
                    prod.setPrecio(rs.getDouble("precio"));
                    prod.setImagen(rs.getString("imagen"));
                    
                    detalles.add(new ItemCarrito(prod, rs.getInt("cantidad")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles del pedido: " + e.getMessage());
        }
        return detalles;
    }
}