package mundonodo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource; 
import mundonodo.model.dto.Usuario;
import mundonodo.model.mysql.Conexion;

/**
 * Clase Data Access Object (DAO) que gestiona la persistencia de los usuarios.
 * Proporciona métodos para el registro, inicio de sesión, actualización de perfil
 * y validaciones de existencia de datos en la base de datos de MundoNodo.
 * * @author Jose Antonio
 * @version 1.0
 */
public class UsuarioDao {

    /**
     * Registra un nuevo usuario en el sistema.
     * * @param ds El {@link DataSource} para obtener la conexión del pool.
     * @param u El objeto {@link Usuario} que contiene los datos a registrar.
     * @return {@code true} si el registro fue exitoso, {@code false} en caso contrario.
     */
    public boolean registrar(DataSource ds, Usuario u) {
        String sql = "INSERT INTO usuarios (email, password, nombre, apellidos, nif, telefono, direccion, codigo_postal, localidad, provincia, avatar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion(ds); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getCorreo());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getApellidos());
            ps.setString(5, u.getDni());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getDireccion());
            ps.setString(8, u.getCp());
            ps.setString(9, u.getLocalidad());
            ps.setString(10, u.getProvincia());
            ps.setString(11, u.getAvatar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ERROR SQL en registrar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida las credenciales de un usuario para el inicio de sesión.
     * * @param ds El {@link DataSource} para la conexión.
     * @param correo El email introducido por el usuario.
     * @param password La contraseña introducida.
     * @return Un objeto {@link Usuario} poblado si las credenciales son válidas, {@code null} si no coinciden.
     */
    public Usuario login(DataSource ds, String correo, String password) {
        Usuario u = null;
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";
        try (Connection con = Conexion.getConexion(ds); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setIdusuario(rs.getInt("idusuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellidos(rs.getString("apellidos"));
                    u.setDni(rs.getString("nif"));
                    u.setTelefono(rs.getString("telefono"));
                    u.setCorreo(rs.getString("email"));
                    u.setDireccion(rs.getString("direccion"));
                    u.setLocalidad(rs.getString("localidad"));
                    u.setProvincia(rs.getString("provincia"));
                    u.setCp(rs.getString("codigo_postal"));
                    u.setPassword(rs.getString("password"));
                    u.setAvatar(rs.getString("avatar"));
                    u.setUltimo_acceso(rs.getTimestamp("ultimo_acceso"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en login(): " + e.getMessage());
        }
        return u;
    }

    /**
     * Actualiza la información del perfil de un usuario existente.
     * * @param ds El {@link DataSource} para la conexión.
     * @param u El objeto {@link Usuario} con los datos actualizados.
     * @return {@code true} si la actualización fue exitosa, {@code false} en caso contrario.
     */
    public boolean actualizar(DataSource ds, Usuario u) {
        String sql = "UPDATE usuarios SET nombre=?, apellidos=?, telefono=?, direccion=?, "
                + "localidad=?, provincia=?, codigo_postal=?, password=?, avatar=? WHERE idusuario=?";
        try (Connection con = Conexion.getConexion(ds); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellidos());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getDireccion());
            ps.setString(5, u.getLocalidad());
            ps.setString(6, u.getProvincia());
            ps.setString(7, u.getCp());
            ps.setString(8, u.getPassword());
            ps.setString(9, u.getAvatar());
            ps.setInt(10, u.getIdusuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en actualizar(): " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si un correo electrónico o un DNI ya están registrados en la base de datos.
     * Se utiliza principalmente durante el proceso de registro para evitar duplicados.
     * * @param ds El {@link DataSource} para la conexión.
     * @param campo El nombre del campo a verificar ("correo" o "nif").
     * @param valor El valor a buscar.
     * @return {@code true} si el valor ya existe, {@code false} si está disponible.
     */
    public boolean verificarExistencia(DataSource ds, String campo, String valor) {
        String columna = (campo.equalsIgnoreCase("correo") || campo.equalsIgnoreCase("email")) ? "email" : "nif";
        String sql = "SELECT COUNT(*) FROM usuarios WHERE LOWER(" + columna + ") = LOWER(?)";

        try (Connection con = Conexion.getConexion(ds); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, valor.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en verificarExistencia(): " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza la fecha y hora del último acceso del usuario a la fecha actual del sistema.
     * * @param ds El {@link DataSource} para la conexión.
     * @param idUsuario El identificador del usuario que ha iniciado sesión.
     */
    public void actualizarUltimoAcceso(DataSource ds, int idUsuario) {
        String sql = "UPDATE usuarios SET ultimo_acceso = NOW() WHERE idusuario = ?";
        try (Connection con = Conexion.getConexion(ds); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar ultimo_acceso: " + e.getMessage());
        }
    }
}