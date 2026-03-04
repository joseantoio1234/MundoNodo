<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>MundoNodo | Hardware</title>
        <link rel="stylesheet" type="text/css" href="CSS/style.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    </head>
    <body>
        <header class="main-header">
            <div class="header-left">
                <a href="Inicio" class="logo-link">
                    <img src="${pageContext.request.contextPath}/IMAGENES/logo/MundoNodologo.png" alt="MundoNodo Logo" class="logo-img">
                </a>
                <h2>MundoNodo</h2>
            </div>

            <div class="header-center">
                <form action="BusquedaServlet" method="GET" class="search-form">
                    <input type="text" name="query" placeholder="Buscar productos...">
                    <button type="submit"><i class="fas fa-search"></i></button>
                </form>
            </div>

            <div class="header-right">
                <a href="${pageContext.request.contextPath}/Carrito" class="cart-btn" style="position: relative; display: inline-flex; align-items: center; gap: 8px;">
                    <i class="fas fa-shopping-cart"></i> 
                    Carrito 
                    <span id="contador-carrito-ajax" class="cart-count" style="background-color: #e67e22; color: white; border-radius: 12px; padding: 2px 8px; font-size: 0.85rem; font-weight: bold; min-width: 20px; text-align: center; display: inline-block; transition: transform 0.2s ease;">
                        ${sessionScope.carrito != null ? sessionScope.carrito.size() : 0}
                    </span>
                </a>

                <c:choose>
                    <%-- CASO 1: El usuario está logueado --%>
                    <c:when test="${not empty sessionScope.usuarioLogueado}">
                        <div class="user-welcome" style="display: flex; align-items: center; gap: 10px;">

                            <%-- Enlace al perfil envolviendo la imagen del avatar --%>
                            <a href="${pageContext.request.contextPath}/ActualizarPerfil" title="Ir a mi perfil">
                                <c:choose>
                                    <%-- Intentamos cargar el avatar del usuario logueado --%>
                                    <c:when test="${not empty sessionScope.usuarioLogueado.avatar}">
                                        <img src="${pageContext.request.contextPath}/IMAGENES/avatar/${sessionScope.usuarioLogueado.avatar}" 
                                             alt="Avatar" 
                                             class="user-avatar-nav"
                                             onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/IMAGENES/avatar/pinguavatar.png';">
                                    </c:when>
                                    <%-- Si el objeto está vacío, cargamos directamente el pingu --%>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/IMAGENES/avatar/pinguavatar.png" 
                                             class="user-avatar-nav" alt="Avatar por defecto">
                                    </c:otherwise>
                                </c:choose>
                            </a>

                            <span class="welcome-text">
                                <strong>Hola ${sessionScope.usuarioLogueado.nombre}</strong>
                            </span>

                            <a href="${pageContext.request.contextPath}/CerrarSesion" class="auth-btn logout" style="background-color: #e74c3c; margin-left: 10px; padding: 5px 10px; border-radius: 4px; color: white; text-decoration: none; font-size: 0.9rem;">
                                <i class="fas fa-sign-out-alt"></i> Salir
                            </a>
                        </div>
                    </c:when>

                    <%-- CASO 2: No hay usuario (Anonimo) --%>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/RegistroUsuario" class="auth-btn register">Registrarse</a>
                        <a href="${pageContext.request.contextPath}/ValidarUsuario" class="auth-btn register">Iniciar Sesion</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>

        <main>
            <section class="hero-section">
                <div class="hero-content">
                    <h1>Hardware de Élite</h1>
                    <p>Configura tu equipo con los mejores componentes del mercado.</p>
                </div>
            </section>

            <div class="shop-container">

                <aside class="filter-sidebar">
                    <h3 class="sidebar-title"><i class="fas fa-sliders-h"></i> Filtros</h3>

                    <form action="FiltrarProductos" method="GET" id="formFiltros">

                        <div class="filter-section-sidebar">
                            <h4>Categorías Principales</h4>
                            <div class="category-scroll-container"> 
                                <ul class="category-list">
                                    <c:forEach var="cat" items="${applicationScope.listaCategorias}">
                                        <li>
                                            <label class="category-item-row">
                                                <span class="category-name">${cat}</span>
                                                <input type="checkbox" name="cat" value="${cat}" class="category-checkbox">
                                            </label>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>

                        <div class="filter-section-sidebar">
                            <h4>Rango de Precio</h4>
                            <div id="slider-range-side" style="margin: 20px 10px 10px 10px;"></div>

                            <div class="price-labels" style="display: flex; justify-content: space-between; margin-top: 15px;">
                                <span id="minValSide">${applicationScope.precioMinDB != null ? applicationScope.precioMinDB : 0}</span>€
                                <span id="maxValSide">${applicationScope.precioMaxDB != null ? applicationScope.precioMaxDB : 3000}</span>€
                            </div>

                            <input type="hidden" name="min_precio" id="min_precio_input" value="${applicationScope.precioMinDB}">
                            <input type="hidden" name="max_precio" id="max_precio_input" value="${applicationScope.precioMaxDB}">
                        </div>

                        <button type="submit" class="btn-aplicar-filtros">
                            <i class="fas fa-filter"></i> Aplicar Filtros
                        </button>
                    </form>
                </aside>
                <section class="main-catalog">

                    <div id="resultados-busqueda" style="display:none; width: 100%;">
                        <h2 class="section-label"><i class="fas fa-search"></i> Resultados</h2>
                        <div id="grid-resultados">
                        </div>
                    </div>

                    <div id="secciones-fijas">

                        <div class="product-section-compact">
                            <h2 class="section-label"><i class="fas fa-fire"></i> Novedades en MundoNodo</h2>
                            <div class="product-grid-compact">
                                <c:forEach var="prod" items="${listaNovedades}" varStatus="status" end="4">
                                    <div class="card-wide product-card-clickable" onclick="openModal('modal-${prod.idproducto}')">
                                        <div class="img-container-wide">
                                            <img src="${pageContext.request.contextPath}/IMAGENES/productos/${prod.imagen}.jpg" alt="${prod.nombre}" onerror="this.src='IMAGENES/productos/default.jpg'">
                                        </div>
                                        <div class="card-content-wide">
                                            <h3>${prod.nombre}</h3>
                                            <p class="product-price">
                                                <fmt:formatNumber value="${prod.precio}" type="number" minFractionDigits="2" maxFractionDigits="2" />€
                                            </p>
                                        </div>
                                    </div>

                                    <div id="modal-${prod.idproducto}" class="modal-overlay">
                                        <div class="modal-content">
                                            <span class="close-modal" onclick="closeModal('modal-${prod.idproducto}')">&times;</span>
                                            <div class="modal-grid">
                                                <div class="modal-img-side">
                                                    <img src="${pageContext.request.contextPath}/IMAGENES/productos/${prod.imagen}.jpg" alt="${prod.nombre}">
                                                </div>
                                                <div class="modal-info-side">
                                                    <h2>${prod.nombre}</h2>
                                                    <p class="modal-brand">Marca: ${prod.marca}</p>
                                                    <hr>
                                                    <p class="modal-description">${prod.descripcion}</p>
                                                    <div class="modal-footer-price">
                                                        <span class="modal-price-val">
                                                            <fmt:formatNumber value="${prod.precio}" type="number" minFractionDigits="2" maxFractionDigits="2" />€
                                                        </span>
                                                        <button type="button" 
                                                                class="btn-submit-auth" 
                                                                onclick="añadirAlCarritoAjax(${prod.idproducto})">
                                                            <i class="fas fa-shopping-cart"></i> Añadir al carrito
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="product-section-compact" style="margin-top: 30px;">
                            <h2 class="section-label"><i class="fas fa-chart-line"></i> Los Más Vendidos</h2>
                            <div class="product-grid-compact">
                                <c:forEach var="prod" items="${listaMasVendidos}" end="4">
                                    <div class="card-wide product-card-clickable" onclick="openModal('modal-vend-${prod.idproducto}')">
                                        <div class="img-container-wide">
                                            <img src="${pageContext.request.contextPath}/IMAGENES/productos/${prod.imagen}.jpg" alt="${prod.nombre}" onerror="this.src='IMAGENES/productos/default.jpg'">
                                        </div>
                                        <div class="card-content-wide">
                                            <h3>${prod.nombre}</h3>
                                            <p class="product-price">
                                                <fmt:formatNumber value="${prod.precio}" type="number" minFractionDigits="2" maxFractionDigits="2" />€
                                            </p>
                                        </div>
                                    </div>

                                    <div id="modal-vend-${prod.idproducto}" class="modal-overlay">
                                        <div class="modal-content">
                                            <span class="close-modal" onclick="closeModal('modal-vend-${prod.idproducto}')">&times;</span>
                                            <div class="modal-grid">
                                                <div class="modal-img-side">
                                                    <img src="${pageContext.request.contextPath}/IMAGENES/productos/${prod.imagen}.jpg" alt="${prod.nombre}">
                                                </div>
                                                <div class="modal-info-side">
                                                    <h2>${prod.nombre}</h2>
                                                    <p class="modal-brand">Marca: ${prod.marca}</p>
                                                    <hr>
                                                    <p class="modal-description">${prod.descripcion}</p>
                                                    <div class="modal-footer-price">
                                                        <span class="modal-price-val">
                                                            <fmt:formatNumber value="${prod.precio}" type="number" minFractionDigits="2" maxFractionDigits="2" />€
                                                        </span>
                                                        <button type="button" 
                                                                class="btn-submit-auth" 
                                                                onclick="añadirAlCarritoAjax(${prod.idproducto})">
                                                            <i class="fas fa-shopping-cart"></i> Añadir al carrito
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </main>

        <footer class="main-footer">
            <div class="container footer-grid">
                <div class="footer-section about">
                    <img src="${pageContext.request.contextPath}/IMAGENES/logo/MundoNodologo.png" alt="MundoNodo" class="footer-logo">
                    <p>Especialistas en hardware de alto rendimiento y configuraciones gaming a medida. Llevamos la potencia de los nodos a tu escritorio.</p>
                    <div class="social-links">
                        <a href="#"><i class="fab fa-facebook"></i></a>
                        <a href="#"><i class="fab fa-twitter"></i></a>
                        <a href="#"><i class="fab fa-instagram"></i></a>
                        <a href="#"><i class="fab fa-youtube"></i></a>
                    </div>
                </div>

                <div class="footer-section links">
                    <h3>Explorar</h3>
                    <ul>
                        <li><a href="Inicio">Inicio</a></li>
                        <li><a href="#novedades">Novedades</a></li>
                        <li><a href="#mas-vendidos">Más Vendidos</a></li>
                        <li><a href="#">Promociones</a></li>
                    </ul>
                </div>

                <div class="footer-section legal">
                    <h3>Soporte</h3>
                    <ul>
                        <li><a href="#">Centro de Ayuda</a></li>
                        <li><a href="#">Garantía de Productos</a></li>
                        <li><a href="#">Política de Devoluciones</a></li>
                        <li><a href="#">Aviso Legal</a></li>
                    </ul>
                </div>

                <div class="footer-section contact">
                    <h3>Contacto</h3>
                    <p><i class="fas fa-map-marker-alt"></i> Mérida, Badajoz, España</p>
                    <p><i class="fas fa-envelope"></i> soporte@mundonodo.com</p>
                    <p><i class="fas fa-phone"></i> +34 924 000 000</p>
                </div>
            </div>

            <div class="footer-bottom">
                <p>&copy; 2026 MundoNodo Hardware S.L. Todos los derechos reservados.</p>
            </div>
        </footer>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

 <script>
    // --- LÓGICA DE MODALES ---
    function openModal(id) {
        const modal = document.getElementById(id);
        if (modal) {
            modal.style.display = "block";
            document.body.style.overflow = "hidden";
        }
    }

    function closeModal(id) {
        const modal = document.getElementById(id);
        if (modal) {
            modal.style.display = "none";
            document.body.style.overflow = "auto";
        }
    }

    window.onclick = function (event) {
        if (event.target.classList.contains('modal-overlay')) {
            event.target.style.display = "none";
            document.body.style.overflow = "auto";
        }
    }

    // --- FUNCIÓN GENÉRICA PARA MOSTRAR RESULTADOS ---
    function mostrarResultadosAJAX(html) {
        const gridResultados = document.getElementById('grid-resultados');
        const seccionResultados = document.getElementById('resultados-busqueda');
        const seccionesFijas = document.getElementById('secciones-fijas');

        if (gridResultados) {
            gridResultados.innerHTML = html;
            gridResultados.style.opacity = "1";
        }

        if (seccionesFijas) seccionesFijas.style.display = "none";
        if (seccionResultados) seccionResultados.style.display = "block";

        if (seccionResultados) {
            seccionResultados.scrollIntoView({behavior: 'smooth', block: 'start'});
        }
    }

    // --- LÓGICA AJAX: FILTROS LATERALES ---
    function ejecutarFiltradoAjax() {
        const form = document.getElementById('formFiltros');
        if (!form) return;

        const datosForm = new URLSearchParams(new FormData(form)).toString();
        const gridResultados = document.getElementById('grid-resultados');

        if (gridResultados) gridResultados.style.opacity = "0.4";

        fetch('${pageContext.request.contextPath}/FiltrarProductos?' + datosForm)
            .then(response => {
                if (!response.ok) throw new Error('Error en el servidor');
                return response.text();
            })
            .then(html => mostrarResultadosAJAX(html))
            .catch(error => {
                console.error('Error en AJAX Filtros:', error);
                if (gridResultados) gridResultados.style.opacity = "1";
            });
    }

    // --- FUNCIÓN: AÑADIR AL CARRITO AJAX (VERSION FINAL) ---
    function añadirAlCarritoAjax(idProducto) {
        const url = "${pageContext.request.contextPath}/AnadirCarrito?id=" + idProducto;

        fetch(url, {
            headers: {"X-Requested-With": "XMLHttpRequest"}
        })
        .then(response => {
            if (!response.ok) throw new Error('Error en la petición');
            return response.text();
        })
        .then(data => {
            console.log("Respuesta del servidor:", data); // Esto debe decir success|X

            if (data.includes("success")) {
                const partes = data.split("|");
                const nuevaCantidad = partes[1];

                // Notificación visual Toast
                Swal.fire({
                    icon: 'success',
                    title: '¡Añadido!',
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: true
                });

                // ACTUALIZACIÓN DEL CONTADOR POR ID (Clave para que funcione)
                const cartBadge = document.getElementById('contador-carrito-ajax');
                
                if (cartBadge && nuevaCantidad !== undefined) {
                    cartBadge.innerText = nuevaCantidad;

                    // Efecto de animación para que se note el cambio
                    cartBadge.style.transition = "transform 0.2s cubic-bezier(0.175, 0.885, 0.32, 1.275)";
                    cartBadge.style.transform = "scale(1.5)";
                    
                    setTimeout(() => {
                        cartBadge.style.transform = "scale(1)";
                    }, 200);
                }
            }
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire('Error', 'No se pudo añadir al carrito', 'error');
        });
    }
    
    function añadirAlCarritoAjax(idProducto) {
    const url = "${pageContext.request.contextPath}/AnadirCarrito?id=" + idProducto;

    fetch(url, {
        headers: {"X-Requested-With": "XMLHttpRequest"}
    })
    .then(response => {
        if (!response.ok) throw new Error('Error en la petición');
        return response.text();
    })
    .then(data => {
        console.log("Respuesta del servidor:", data);

        if (data.includes("success")) {
            const partes = data.split("|");
            const nuevaCantidad = partes[1];

            // --- NUEVO: CERRAR EL MODAL AUTOMÁTICAMENTE ---
            // Buscamos cualquier modal que esté abierto y lo cerramos
            const modales = document.querySelectorAll('.modal-overlay');
            modales.forEach(modal => {
                modal.style.display = "none";
            });
            document.body.style.overflow = "auto"; // Devolvemos el scroll a la página

            // Notificación visual Toast
            Swal.fire({
                icon: 'success',
                title: '¡Añadido!',
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 2000,
                timerProgressBar: true
            });

            // Actualizar contador
            const cartBadge = document.getElementById('contador-carrito-ajax');
            if (cartBadge && nuevaCantidad !== undefined) {
                cartBadge.innerText = nuevaCantidad;
                cartBadge.style.transform = "scale(1.5)";
                setTimeout(() => {
                    cartBadge.style.transform = "scale(1)";
                }, 200);
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        Swal.fire('Error', 'No se pudo añadir al carrito', 'error');
    });
}

    // --- CONFIGURACIÓN INICIAL Y EVENTOS ---
    document.addEventListener('DOMContentLoaded', function () {
        const slider = document.getElementById('slider-range-side');
        const formFiltros = document.getElementById('formFiltros');
        const searchForm = document.querySelector('.search-form');

        if (slider) {
            const minDB = parseFloat("${applicationScope.precioMinDB}") || 0;
            const maxDB = parseFloat("${applicationScope.precioMaxDB}") || 3000;

            noUiSlider.create(slider, {
                start: [minDB, maxDB],
                connect: true,
                range: {'min': minDB, 'max': maxDB},
                step: 1,
                format: {
                    to: value => Math.round(value),
                    from: value => value
                }
            });

            const minLabel = document.getElementById('minValSide');
            const maxLabel = document.getElementById('maxValSide');
            const minInput = document.getElementById('min_precio_input');
            const maxInput = document.getElementById('max_precio_input');

            slider.noUiSlider.on('update', function (values, handle) {
                if (handle === 0) {
                    minLabel.innerText = values[0];
                    minInput.value = values[0];
                } else {
                    maxLabel.innerText = values[1];
                    maxInput.value = values[1];
                }
            });
        }

        if (formFiltros) {
            formFiltros.addEventListener('submit', function (e) {
                e.preventDefault();
                ejecutarFiltradoAjax();
            });
        }

        if (searchForm) {
            searchForm.addEventListener('submit', function (e) {
                e.preventDefault();
                const query = this.querySelector('input[name="query"]').value;
                const gridResultados = document.getElementById('grid-resultados');
                if (gridResultados) gridResultados.style.opacity = "0.4";

                fetch('${pageContext.request.contextPath}/Busqueda?query=' + encodeURIComponent(query))
                    .then(response => {
                        if (!response.ok) throw new Error('Error en búsqueda');
                        return response.text();
                    })
                    .then(html => mostrarResultadosAJAX(html))
                    .catch(error => {
                        console.error('Error en AJAX Busqueda:', error);
                        if (gridResultados) gridResultados.style.opacity = "1";
                    });
            });
        }
    });
</script>
    </body>
</html>