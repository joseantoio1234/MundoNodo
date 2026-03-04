package mundonodo.model.dto;

import java.sql.Timestamp; 

public class Usuario {
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

    public Usuario() {}

    public Timestamp getUltimo_acceso() { return ultimo_acceso; }
    public void setUltimo_acceso(Timestamp ultimo_acceso) { this.ultimo_acceso = ultimo_acceso; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public int getIdusuario() { return idusuario; }
    public void setIdusuario(int idusuario) { this.idusuario = idusuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public String getCp() { return cp; }
    public void setCp(String cp) { this.cp = cp; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}