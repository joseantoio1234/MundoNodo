package mundonodo.util;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import mundonodo.dao.ProductoDao;
import mundonodo.model.dto.ItemCarrito;
import mundonodo.model.dto.Producto;

/**
 * Utilidad para transformar la cesta de la compra entre objetos Java y texto plano (Cookies).
 */
public class CestaUtils {

    /**
     * Convierte la lista de productos en un texto para la Cookie.
     * Formato resultante: id1_cant1-id2_cant2 (Ejemplo: "14_2-5_1")
     */
    public static String serializarCesta(List<ItemCarrito> carrito) {
        if (carrito == null || carrito.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (ItemCarrito item : carrito) {
            // Verificamos que el producto no sea nulo antes de acceder a su ID
            if (item != null && item.getProducto() != null) {
                sb.append(item.getProducto().getIdproducto())
                  .append("_")
                  .append(item.getCantidad())
                  .append("-");
            }
        }
        
        // Si el StringBuilder está vacío tras el bucle, retornamos cadena vacía
        if (sb.length() == 0) return "";

        // Quitamos el último guion sobrante
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * Convierte el texto recuperado de la Cookie de nuevo en una lista de objetos ItemCarrito.
     * Requiere el DataSource para buscar la información actualizada de los productos en la BD.
     */
    public static List<ItemCarrito> deserializarCesta(String datos, DataSource ds) {
        List<ItemCarrito> carrito = new ArrayList<>();
        
        // Validación inicial: si no hay datos o el DataSource es nulo, devolvemos lista vacía
        if (datos == null || datos.isEmpty() || ds == null) {
            return carrito;
        }

        ProductoDao pDao = new ProductoDao();
        
        try {
            // 1. Separamos los bloques de productos (usando el guion '-')
            String[] items = datos.split("-"); 

            for (String itemStr : items) {
                // 2. Separamos el ID del producto y su cantidad (usando el guion bajo '_')
                String[] partes = itemStr.split("_"); 
                
                if (partes.length == 2) {
                    try {
                        int id = Integer.parseInt(partes[0]);
                        int cant = Integer.parseInt(partes[1]);
                        
                        // 3. Buscamos el producto en la BD para tener los datos reales (precio, imagen, nombre)
                        Producto p = pDao.obtenerPorId(ds, id);
                        
                        // Solo añadimos si el producto aún existe en nuestro catálogo
                        if (p != null) {
                            carrito.add(new ItemCarrito(p, cant));
                        }
                    } catch (NumberFormatException nfe) {
                        System.err.println("Error de formato en segmento de cookie: " + itemStr);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error general al deserializar la cesta: " + e.getMessage());
        }
        
        return carrito;
    }
}