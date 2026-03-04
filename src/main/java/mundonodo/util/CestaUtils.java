package mundonodo.util;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import mundonodo.dao.ProductoDao;
import mundonodo.model.dto.ItemCarrito;
import mundonodo.model.dto.Producto;

/**
 * Clase de utilidad encargada de la persistencia del carrito de compras.
 * Proporciona métodos para transformar la lista de objetos {@link ItemCarrito} en 
 * cadenas de texto (Serialización) y viceversa (Deserialización), permitiendo 
 * almacenar la cesta en las cookies del cliente.
 * * @author Jose Antonio
 * @version 1.0
 */
public class CestaUtils {

    /**
     * Transforma la lista de items del carrito en una cadena de texto compacta.
     * El formato resultante es: "ID1_CANT1-ID2_CANT2-IDn_CANTn".
     * Ejemplo: "14_2-5_1" (Dos unidades del producto 14 y una del producto 5).
     * * @param carrito Lista de objetos {@link ItemCarrito} a procesar.
     * @return {@link String} formateado para su almacenamiento en cookies. 
     * Retorna una cadena vacía si el carrito es nulo o no contiene elementos.
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

        // Quitamos el último guion sobrante para que el formato sea limpio
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * Reconstruye la lista de objetos del carrito a partir de una cadena de texto.
     * Realiza consultas a la base de datos para recuperar la información actualizada 
     * (precio, imagen, stock) de cada producto basándose en su ID.
     * * @param datos Cadena de texto recuperada de la cookie.
     * @param ds El {@link DataSource} necesario para que el DAO realice las consultas.
     * @return {@link List} de {@link ItemCarrito} poblada con datos reales de la BD.
     */
    public static List<ItemCarrito> deserializarCesta(String datos, DataSource ds) {
        List<ItemCarrito> carrito = new ArrayList<>();
        
        // Validación inicial: si no hay datos o el DataSource es nulo, devolvemos lista vacía
        if (datos == null || datos.isEmpty() || ds == null) {
            return carrito;
        }

        // Instanciamos el DAO de productos para recuperar la info comercial
        ProductoDao pDao = new ProductoDao();
        
        try {
            // 1. Separamos los bloques de productos (usando el delimitador de item '-')
            String[] items = datos.split("-"); 

            for (String itemStr : items) {
                // 2. Separamos el ID del producto y su cantidad (usando el delimitador de campo '_')
                String[] partes = itemStr.split("_"); 
                
                if (partes.length == 2) {
                    try {
                        int id = Integer.parseInt(partes[0]);
                        int cant = Integer.parseInt(partes[1]);
                        
                        // 3. Buscamos el producto en la BD para tener los datos reales (precio, imagen, nombre)
                        // Esto garantiza que si el precio cambió mientras el usuario estaba ausente, el carrito se actualice.
                        Producto p = pDao.obtenerPorId(ds, id);
                        
                        // Solo añadimos si el producto aún existe en nuestro catálogo (integridad referencial)
                        if (p != null) {
                            carrito.add(new ItemCarrito(p, cant));
                        }
                    } catch (NumberFormatException nfe) {
                        System.err.println(">>> Error de formato en segmento de cookie: " + itemStr);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(">>> Error general al deserializar la cesta: " + e.getMessage());
        }
        
        return carrito;
    }
}