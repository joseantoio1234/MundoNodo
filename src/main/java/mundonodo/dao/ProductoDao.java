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

/**
 * Clase Data Access Object (DAO) que gestiona el catálogo de productos de MundoNodo.
 * Proporciona funcionalidades de búsqueda, filtrado avanzado por categorías y precios,
 * y recuperación de metadatos para la interfaz de usuario (marcas y categorías).
 * * @author Jose Antonio
 * @version 1.0
 */
public class ProductoDao {

    /**
     * Recupera una lista aleatoria de productos para mostrar como novedades en la página de inicio.
     * * @param ds El {@link DataSource} para obtener la conexión.
     * @return Una {@link List} de hasta 8 objetos {@link Producto}.
     */
    public List<Producto> listarNovedades(DataSource ds) {
        String sql = "SELECT * FROM productos ORDER BY RAND() LIMIT 8";
        return ejecutarConsulta(ds, sql, null);
    }

    /**
     * Busca un producto específico mediante su identificador único.
     * * @param ds El {@link DataSource} para la conexión.
     * @param idProducto El ID del producto a buscar.
     * @return El objeto {@link Producto} encontrado o {@code null} si no existe.
     */
    public Producto obtenerPorId(DataSource ds, int idProducto) {
        String sql = "SELECT * FROM productos WHERE idproducto = ?";
        List<Producto> resultado = ejecutarConsulta(ds, sql, idProducto);
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    /**
     * Recupera todos los productos registrados en la base de datos.
     * * @param ds El {@link DataSource} para la conexión.
     * @return Una lista con todos los objetos {@link Producto}.
     */
    public List<Producto> listarTodo(DataSource ds) {
        String sql = "SELECT * FROM productos";
        return ejecutarConsulta(ds, sql, null);
    }

    /**
     * Realiza una búsqueda de productos por coincidencia de nombre.
     * * @param ds El {@link DataSource} para la conexión.
     * @param query La cadena de texto a buscar.
     * @return Lista de productos cuyo nombre contiene la cadena proporcionada.
     */
    public List<Producto> buscarPorNombre(DataSource ds, String query) {
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";
        return ejecutarConsulta(ds, sql, "%" + query + "%");
    }

    /**
     * Recupera el listado de nombres de categorías únicas disponibles.
     * Utilizado para poblar los filtros laterales de la tienda.
     * * @param ds El {@link DataSource} para la conexión.
     * @return Lista de cadenas de texto con los nombres de las categorías.
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
        } catch (SQLException e) {
            System.err.println(">>> ERROR en obtenerCategoriasUnicas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene el listado de todas las marcas distintas presentes en la tabla de productos.
     * * @param ds El {@link DataSource} para la conexión.
     * @return Lista de marcas ordenadas alfabéticamente.
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
        } catch (SQLException e) {
            System.err.println(">>> ERROR en obtenerMarcasUnicas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Calcula el valor mínimo y máximo de los precios de los productos actuales.
     * * @param ds El {@link DataSource} para la conexión.
     * @return Un array de double donde [0] es el precio mínimo y [1] el máximo.
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
        } catch (SQLException e) {
            System.err.println(">>> ERROR en obtenerRangoPrecios: " + e.getMessage());
        }
        return rango;
    }

    /**
     * Realiza un filtrado avanzado basado en múltiples categorías y un rango de precios.
     * * @param ds El {@link DataSource} para la conexión.
     * @param categoriasSeleccionadas Array con los nombres de las categorías a filtrar.
     * @param min Precio mínimo.
     * @param max Precio máximo.
     * @return Lista de productos que cumplen todos los criterios.
     */
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

    /**
     * Filtra la lista completa de productos únicamente por su rango de precio.
     * * @param ds El {@link DataSource} para la conexión.
     * @param min Precio mínimo.
     * @param max Precio máximo.
     * @return Lista de productos dentro del rango de precios.
     */
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

    /**
     * Método auxiliar privado para transformar las filas del {@link ResultSet} en objetos {@link Producto}.
     * * @param rs El conjunto de resultados de la base de datos.
     * @param lista La lista donde se añadirán los productos mapeados.
     * @throws SQLException Si ocurre un error al acceder a las columnas del ResultSet.
     */
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

    /**
     * Ejecuta una consulta SQL genérica y devuelve la lista de productos resultante.
     * Soporta parámetros de tipo Integer, String y Double.
     * * @param ds El {@link DataSource} para la conexión.
     * @param sql La sentencia SQL a ejecutar.
     * @param parametro El valor para el primer marcador '?' (opcional).
     * @return Una lista de objetos {@link Producto}.
     */
    private List<Producto> ejecutarConsulta(DataSource ds, String sql, Object parametro) {
        List<Producto> lista = new ArrayList<>();
        
        if (ds == null) {
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
            System.err.println(">>> ERROR en ejecutarConsulta: " + e.getMessage());
        }
        return lista;
    }
}