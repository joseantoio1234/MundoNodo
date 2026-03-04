package mundonodo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource; 
import mundonodo.model.dto.Producto;
import mundonodo.model.mysql.Conexion;

public class ProductoDao {

    public List<Producto> listarNovedades(DataSource ds) {
        // RAND() para variedad en la landing page
        String sql = "SELECT * FROM productos ORDER BY RAND() LIMIT 8";
        return ejecutarConsulta(ds, sql, null);
    }

    public Producto obtenerPorId(DataSource ds, int idProducto) {
        String sql = "SELECT * FROM productos WHERE idproducto = ?";
        List<Producto> resultado = ejecutarConsulta(ds, sql, idProducto);
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    public List<Producto> listarTodo(DataSource ds) {
        String sql = "SELECT * FROM productos";
        return ejecutarConsulta(ds, sql, null);
    }

    public List<Producto> buscarPorNombre(DataSource ds, String query) {
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";
        return ejecutarConsulta(ds, sql, "%" + query + "%");
    }

    /**
     * Recupera las categorías únicas para el filtro lateral
     */
    public List<String> obtenerCategoriasUnicas(DataSource ds) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre FROM categorias ORDER BY nombre ASC";

        try (Connection con = Conexion.getConexion(ds);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                if (nombre != null) lista.add(nombre.trim());
            }
            // LOG DE DEPURACIÓN
            System.out.println(">>> DAO: Categorías cargadas: " + lista.size());
        } catch (SQLException e) {
            System.err.println(">>> ERROR en obtenerCategoriasUnicas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Recupera las marcas únicas (Método nuevo para corregir error en Inicio.java)
     */
    public List<String> obtenerMarcasUnicas(DataSource ds) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT marca FROM productos WHERE marca IS NOT NULL ORDER BY marca ASC";

        try (Connection con = Conexion.getConexion(ds);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String marca = rs.getString("marca");
                if (marca != null) lista.add(marca.trim());
            }
            // LOG DE DEPURACIÓN
            System.out.println(">>> DAO: Marcas cargadas: " + lista.size());
        } catch (SQLException e) {
            System.err.println(">>> ERROR en obtenerMarcasUnicas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Recupera el rango de precios real de la tabla productos
     */
    public double[] obtenerRangoPrecios(DataSource ds) {
        double[] rango = {0.0, 5000.0}; 
        String sql = "SELECT MIN(precio), MAX(precio) FROM productos";

        try (Connection con = Conexion.getConexion(ds);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                rango[0] = rs.getDouble(1);
                rango[1] = rs.getDouble(2);
            }
            System.out.println(">>> DAO: Rango precios: [" + rango[0] + " - " + rango[1] + "]");
        } catch (SQLException e) {
            System.err.println(">>> ERROR en obtenerRangoPrecios: " + e.getMessage());
        }
        return rango;
    }

    public List<Producto> filtrarAvanzado(DataSource ds, String[] categoriasSeleccionadas, double min, double max) {
        if (categoriasSeleccionadas == null || categoriasSeleccionadas.length == 0) {
            return filtrarSoloPrecio(ds, min, max);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < categoriasSeleccionadas.length; i++) {
            sb.append("?");
            if (i < categoriasSeleccionadas.length - 1) sb.append(",");
        }

        String sql = "SELECT p.* FROM productos p "
                   + "JOIN categorias c ON p.idcategoria = c.idcategoria "
                   + "WHERE p.precio BETWEEN ? AND ? "
                   + "AND c.nombre IN (" + sb.toString() + ")";

        List<Producto> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion(ds);
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            for (int i = 0; i < categoriasSeleccionadas.length; i++) {
                ps.setString(i + 3, categoriasSeleccionadas[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                mapearResultSet(rs, lista);
            }
        } catch (SQLException e) {
            System.err.println(">>> ERROR en filtrarAvanzado: " + e.getMessage());
        }
        return lista;
    }

    public List<Producto> filtrarSoloPrecio(DataSource ds, double min, double max) {
        String sql = "SELECT * FROM productos WHERE precio BETWEEN ? AND ?";
        List<Producto> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion(ds);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            try (ResultSet rs = ps.executeQuery()) {
                mapearResultSet(rs, lista);
            }
        } catch (SQLException e) {
            System.err.println(">>> ERROR en filtrarSoloPrecio: " + e.getMessage());
        }
        return lista;
    }


    private void mapearResultSet(ResultSet rs, List<Producto> lista) throws SQLException {
        while (rs.next()) {
            Producto p = new Producto();
            p.setIdproducto(rs.getInt("idproducto"));
            p.setNombre(rs.getString("nombre"));
            p.setMarca(rs.getString("marca"));
            p.setPrecio(rs.getDouble("precio"));
            p.setImagen(rs.getString("imagen"));
            p.setDescripcion(rs.getString("descripcion"));
            lista.add(p);
        }
    }

    private List<Producto> ejecutarConsulta(DataSource ds, String sql, Object parametro) {
        List<Producto> lista = new ArrayList<>();
        
        if (ds == null) {
            System.err.println(">>> ERROR CRÍTICO: El DataSource (ds) es nulo en ProductoDao.");
            return lista;
        }

        try (Connection con = Conexion.getConexion(ds);
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            if (parametro != null) {
                if (parametro instanceof Integer) ps.setInt(1, (Integer) parametro);
                else if (parametro instanceof String) ps.setString(1, (String) parametro);
                else if (parametro instanceof Double) ps.setDouble(1, (Double) parametro);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                mapearResultSet(rs, lista);
            }
        } catch (SQLException e) {
            System.err.println(">>> ERROR en ejecutarConsulta (SQL: " + sql + "): " + e.getMessage());
        }
        return lista;
    }
}