<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Cesta de Productos | MundoNodo</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    </head>
    <body class="auth-page">

        <header class="main-header">
            <div class="header-left">
                <a href="${pageContext.request.contextPath}/Inicio" class="logo-link" title="Ir a Inicio">
                    <img src="${pageContext.request.contextPath}/IMAGENES/logo/MundoNodologo.png" alt="MundoNodo Logo" class="logo-img">
                </a>
                <h2 onclick="location.href = '${pageContext.request.contextPath}/Inicio'" style="cursor:pointer">MundoNodo</h2>
            </div>

            <div class="header-center">
                <form action="${pageContext.request.contextPath}/BusquedaServlet" method="GET" class="search-form">
                    <input type="text" name="query" placeholder="Buscar más productos...">
                    <button type="submit"><i class="fas fa-search"></i></button>
                </form>
            </div>

            <div class="header-right">
                <a href="${pageContext.request.contextPath}/Inicio" class="auth-btn register" style="margin-right: 10px;">
                    <i class="fas fa-home"></i> Inicio
                </a>

                <c:choose>
                    <c:when test="${not empty sessionScope.usuarioLogueado}">
                        <div class="user-welcome" style="display: flex; align-items: center; gap: 10px;">
                            <a href="${pageContext.request.contextPath}/ActualizarPerfil" style="display: flex; align-items: center;">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.usuarioLogueado.avatar}">
                                        <img src="${pageContext.request.contextPath}/IMAGENES/avatar/${sessionScope.usuarioLogueado.avatar}?t=${System.currentTimeMillis()}" 
                                             class="user-avatar-nav" alt="Avatar de ${sessionScope.usuarioLogueado.nombre}"
                                             onerror="this.src='${pageContext.request.contextPath}/IMAGENES/avatar/pinguavatar.png'">
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fas fa-user-circle" style="font-size: 1.8rem; color: #e67e22;"></i>
                                    </c:otherwise>
                                </c:choose>
                            </a>
                            <span class="welcome-text" style="color: white; font-size: 0.9rem;">
                                Hola, <strong>${sessionScope.usuarioLogueado.nombre}</strong>
                            </span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/RegistroUsuario" class="auth-btn register">Iniciar Sesión</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>

        <main class="container cart-container">
            <div style="flex: 2;" id="cart-items-container">
                <h2 class="cart-title">Cesta de Productos</h2>

                <c:choose>
                    <c:when test="${empty sessionScope.carrito}">
                        <div class="card" style="padding: 60px; text-align: center; width: 100%;">
                            <i class="fas fa-shopping-basket fa-4x" style="color: #ddd; margin-bottom: 20px;"></i>
                            <h3 style="color: #555;">Tu cesta está vacía</h3>
                            <p style="margin: 15px 0; color: #888;">¡Explora nuestros productos y añade algo a tu setup!</p>
                            <a href="${pageContext.request.contextPath}/Inicio" class="btn-submit-auth" style="display: inline-block; text-decoration: none; padding: 10px 25px; width: auto;">Ir a la tienda</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:set var="total" value="0" />
                        <c:forEach var="item" items="${sessionScope.carrito}">
                            <c:set var="total" value="${total + (item.producto.precio * item.cantidad)}" />

                            <div id="fila-${item.producto.idproducto}" class="card cart-item-card">
                                <img src="${pageContext.request.contextPath}/IMAGENES/productos/${item.producto.imagen}.jpg" 
                                     class="cart-item-img"
                                     onerror="this.src='${pageContext.request.contextPath}/IMAGENES/productos/default.jpg'">

                                <div class="cart-item-info">
                                    <div style="margin-bottom: 5px;">
                                        <span style="color: #888; font-size: 0.75em; margin-left: 10px;">ID: #${item.producto.idproducto}</span>
                                    </div>

                                    <h3>${item.producto.nombre}</h3>

                                    <p class="cart-item-price">
                                        <fmt:formatNumber value="${item.producto.precio}" type="number" minFractionDigits="2" maxFractionDigits="2" />€
                                    </p>
                                </div>

                                <div class="cart-controls">
                                    <div class="qty-selector">
                                        <button type="button" onclick="updateCart(${item.producto.idproducto}, 'restar')" 
                                                class="qty-btn" id="btn-restar-${item.producto.idproducto}"
                                                ${item.cantidad <= 1 ? 'style="display:none"' : ''}>-</button>

                                        <span class="qty-btn-disabled" id="placeholder-${item.producto.idproducto}" 
                                              ${item.cantidad > 1 ? 'style="display:none"' : ''}>-</span>

                                        <span class="qty-num" id="qty-${item.producto.idproducto}">${item.cantidad}</span>

                                        <button type="button" onclick="updateCart(${item.producto.idproducto}, 'sumar')" class="qty-btn">+</button>
                                    </div>

                                    <button type="button" onclick="updateCart(${item.producto.idproducto}, 'eliminar')" class="delete-icon" title="Eliminar producto" style="background:none; border:none; cursor:pointer;">
                                        <i class="fas fa-trash-alt"></i>
                                    </button>
                                </div>
                            </div>
                        </c:forEach>

                        <div style="margin-top: 20px;">
                            <a href="${pageContext.request.contextPath}/VaciarCarrito" style="color: #888; text-decoration: none; font-size: 0.9rem;">
                                <i class="fas fa-trash"></i> Vaciar Cesta
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="summary-wrapper">
                <div class="card summary-card">
                    <h3>Resumen</h3>
                    <div class="summary-line">
                        <span>Subtotal:</span>
                        <span id="subtotal-val"><fmt:formatNumber value="${not empty total ? total : 0}" type="number" minFractionDigits="2" maxFractionDigits="2" />€</span>
                    </div>
                    <div class="summary-line" style="color: #27ae60;">
                        <span>Envío:</span>
                        <span>Gratis</span>
                    </div>
                    <hr style="border: 0; border-top: 1px dotted #ccc; margin: 15px 0;">
                    <div class="summary-total">
                        <span>Total:</span>
                        <span style="color: #e67e22;" id="total-val">
                            <fmt:formatNumber value="${not empty total ? total : 0}" type="number" minFractionDigits="2" maxFractionDigits="2" />€
                        </span>
                    </div>
                    <button type="button" class="btn-checkout" onclick="verificarSesionAntesDeFinalizar()">
                        <i class="fas fa-credit-card"></i> FINALIZAR PEDIDO
                    </button>
                </div>
            </div>
        </main>

        <footer class="footer-auth">
            <p>&copy; 2026 MundoNodo Hardware S.L. Todos los derechos reservados.</p>
        </footer>



<script>
   
    function verificarSesionAntesDeFinalizar() {
        
        const usuarioLogueado = ${sessionScope.usuarioLogueado != null ? 'true' : 'false'};
        const carritoVacio = ${empty sessionScope.carrito ? 'true' : 'false'};

        if (carritoVacio) {
            Swal.fire('Cesta vacía', 'Añade productos antes de finalizar la compra.', 'warning');
            return;
        }

        if (usuarioLogueado) {
           
            window.location.href = "${pageContext.request.contextPath}/ProcesarPedido";
        } else {
            // Si es anónimo, mostramos el modal con opciones hacia Servlets 
            Swal.fire({
                title: '<strong>¡Identifícate para continuar!</strong>',
                icon: 'info',
                html: `
                     <p style="margin-bottom: 25px; font-size: 1.1rem; color: #555;">
                         Estás a un paso de completar tu pedido. Por favor, elige una opción:
                     </p>
                     <div style="display: flex; flex-direction: column; gap: 15px; padding: 0 10px;">
                         <a href="${pageContext.request.contextPath}/RegistroUsuario" 
                            class="swal2-confirm swal2-styled" 
                            style="background-color: #e67e22; text-decoration: none; padding: 12px; border-radius: 8px; color: white; font-weight: bold; font-size: 1rem; border: none;">
                            <i class="fas fa-user-plus"></i> Crear una cuenta nueva
                         </a>
                         <a href="${pageContext.request.contextPath}/ValidarUsuario" 
                            class="swal2-cancel swal2-styled" 
                            style="background-color: #34495e; text-decoration: none; padding: 12px; border-radius: 8px; color: white; font-weight: bold; font-size: 1rem; border: none;">
                            <i class="fas fa-sign-in-alt"></i> Ya tengo cuenta, iniciar sesión
                         </a>
                     </div>
                `,
                showConfirmButton: false,
                showCancelButton: true,
                cancelButtonText: 'Seguir comprando',
                cancelButtonColor: '#95a5a6',
                background: '#fff',
                borderRadius: '15px'
            });
        }
    }

    // --- LÓGICA DE ACTUALIZACIÓN DEL CARRITO (AJAX) ---
    function updateCart(id, action) {
        const url = "${pageContext.request.contextPath}/ActualizarCantidad?id=" + id + "&accion=" + action;

        fetch(url)
            .then(response => {
                if (!response.ok) throw new Error('Error en la red');
                return response.json();
            })
            .then(data => {
                if (data.status === 'success') {
                    // Si se eliminó el producto o el carrito quedó vacío, recargamos para limpiar la vista
                    if (action === 'eliminar' || data.carritoVacio) {
                        location.reload();
                        return;
                    }

                    // 1. Actualizar cantidad en la fila
                    const qtyElement = document.getElementById('qty-' + id);
                    if (qtyElement) qtyElement.innerText = data.nuevaQty;

                    // 2. Gestionar visibilidad del botón restar
                    const btnRestar = document.getElementById('btn-restar-' + id);
                    const placeholder = document.getElementById('placeholder-' + id);
                    if (btnRestar && placeholder) {
                        if (data.nuevaQty <= 1) {
                            btnRestar.style.display = 'none';
                            placeholder.style.display = 'inline-block';
                        } else {
                            btnRestar.style.display = 'inline-block';
                            placeholder.style.display = 'none';
                        }
                    }

                    // 3. Actualizar Totales e Impuestos (IVA 21%)
                    const total = parseFloat(data.nuevoTotal);
                    const baseImponible = total / 1.21;
                    const iva = total - baseImponible;

                    const formatConfig = { style: 'currency', currency: 'EUR' };
                    const formatter = new Intl.NumberFormat('es-ES', formatConfig);

                    if(document.getElementById('subtotal-val')) document.getElementById('subtotal-val').innerText = formatter.format(total);
                    if(document.getElementById('total-val')) document.getElementById('total-val').innerText = formatter.format(total);
                    if(document.getElementById('base-val')) document.getElementById('base-val').innerText = formatter.format(baseImponible);
                    if(document.getElementById('iva-val')) document.getElementById('iva-val').innerText = formatter.format(iva);

                    // 4. Actualizar contador del Header
                    const cartBadge = document.getElementById('contador-carrito-ajax');
                    if (cartBadge) cartBadge.innerText = data.itemsTotales;
                }
            })
            .catch(error => console.error('Error:', error));
    }

    // --- LÓGICA DE AUTO-COMPLETAR DNI ---
    document.addEventListener('DOMContentLoaded', function() {
        const dniField = document.getElementById('dni');
        if (dniField) {
            dniField.addEventListener('input', function () {
                let dniInput = this.value.replace(/[^0-9]/g, '');
                if (dniInput.length === 8) {
                    fetch('${pageContext.request.contextPath}/CalcularLetraDNI?dni=' + dniInput)
                        .then(response => response.text())
                        .then(letra => {
                            if (letra.length === 1) {
                                this.value = dniInput + letra.toUpperCase();
                                this.style.borderColor = "#2ecc71";
                            }
                        });
                }
            });
        }
    });
</script>
    </body>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</html> 