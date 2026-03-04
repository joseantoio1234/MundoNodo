package mundonodo.model.dto;

public class Producto {
    private int idproducto;
    private String nombre;
    private String marca;
    private double precio;
    private String imagen;
    private String descripcion; 
    private int stock;
    private String categoria;

    public Producto() {}

  
    public int getIdproducto() { return idproducto; }
    public void setIdproducto(int idproducto) { this.idproducto = idproducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getDescripcion() { 
        return descripcion; 
    }
    
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }

public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}