package mundonodo.model.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Clase Data Transfer Object (DTO) que representa a un usuario en el sistema MundoNodo.
 * Contiene la información personal, datos de contacto, credenciales de acceso 
 * y metadatos de sesión del usuario.
 * * @author Jose Antonio
 * @version 1.0
 */
public class Usuario implements Serializable {

    private int idusuario;
    private String nombre;
    private String apellidos;
    private String dni;
    private String telefono;
    private String correo;
    private String password;
    private String direccion;
    private String localidad;
    private String provincia;
    private String cp;
    private String avatar;
    private Timestamp ultimo_acceso;

    /**
     * Constructor por defecto de la clase Usuario.
     * Crea una instancia vacía para ser poblada mediante métodos setter.
     */
    public Usuario() {
    }

    /**
     * Obtiene la marca de tiempo del último inicio de sesión o actividad.
     * @return El {@link Timestamp} del último acceso registrado.
     */
    public Timestamp getUltimo_acceso() {
        return ultimo_acceso;
    }

    /**
     * Establece la marca de tiempo del último acceso del usuario.
     * @param ultimo_acceso El {@link Timestamp} a registrar.
     */
    public void setUltimo_acceso(Timestamp ultimo_acceso) {
        this.ultimo_acceso = ultimo_acceso;
    }

    /**
     * Obtiene la localidad de residencia del usuario.
     * @return Nombre de la localidad.
     */
    public String getLocalidad() {
        return localidad;
    }

    /**
     * Establece la localidad de residencia del usuario.
     * @param localidad Nombre de la localidad.
     */
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    /**
     * Obtiene el identificador único del usuario.
     * @return El ID numérico autoincremental de la base de datos.
     */
    public int getIdusuario() {
        return idusuario;
    }

    /**
     * Establece el identificador único del usuario.
     * @param idusuario El ID numérico de la base de datos.
     */
    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return El nombre de pila.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     * @param nombre El nombre de pila.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del usuario.
     * @return Los apellidos completos.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del usuario.
     * @param apellidos Los apellidos completos.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el DNI/NIE del usuario.
     * @return El documento nacional de identidad.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI/NIE del usuario.
     * @param dni El documento nacional de identidad.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el número de teléfono de contacto.
     * @return El número telefónico.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono de contacto.
     * @param telefono El número telefónico.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el correo electrónico.
     * @return La dirección de email.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo electrónico.
     * @param correo La dirección de email.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return La contraseña encriptada.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * @param password La contraseña (preferiblemente ya encriptada).
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene la dirección postal del usuario.
     * @return La calle, número y piso.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección postal del usuario.
     * @param direccion La calle, número y piso.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene la provincia de residencia.
     * @return El nombre de la provincia.
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Establece la provincia de residencia.
     * @param provincia El nombre de la provincia.
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Obtiene el código postal.
     * @return El código postal de 5 dígitos.
     */
    public String getCp() {
        return cp;
    }

    /**
     * Establece el código postal.
     * @param cp El código postal de 5 dígitos.
     */
    public void setCp(String cp) {
        this.cp = cp;
    }

    /**
     * Obtiene el nombre del archivo de imagen del avatar.
     * @return Nombre del archivo ubicado en la carpeta /IMAGENES/avatar/.
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Establece el nombre del archivo de imagen del avatar.
     * @param avatar Nombre del archivo de imagen.
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}