<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>MundoNodo | <c:out value="${not empty compraOk ? 'Factura Final' : 'Resumen de Pedido'}" /></title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/style.css">

        <style>
            body { margin: 0; padding: 0; font-family: 'Segoe UI', sans-serif; background-color: #f4f7f6; display: flex; flex-direction: column; min-height: 100vh; }
            .main-content { flex: 1; width: 100%; display: flex; justify-content: center; padding: 40px 0; }
            .invoice-box { width: 90%; max-width: 1100px; background: white; padding: 40px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.1); border-radius: 8px; border-top: 10px solid #e67e22; }
            .header-factura { display: flex; justify-content: space-between; border-bottom: 3px solid #e67e22; padding-bottom: 20px; }
            .details { margin-top: 30px; display: flex; justify-content: space-between; background: #fdfdfd; padding: 20px; border-radius: 5px; border: 1px solid #eee; }
            .table-factura { width: 100%; border-collapse: collapse; margin-top: 40px; }
            .table-factura th { background: #2c3e50; color: white; padding: 15px; text-align: left; }
            .table-factura td { padding: 15px; border-bottom: 1px solid #eee; }
            .total-section { text-align: right; margin-top: 40px; padding-right: 20px; }
            .tax-row { display: flex; justify-content: flex-end; gap: 50px; margin: 8px 0; color: #7f8c8d; font-size: 1.1rem; }
            .btn-confirmar { background: #2ecc71; color: white; padding: 15px 25px; border: none; border-radius: 8px; cursor: pointer; font-weight: bold; font-size: 1.3rem; width: 100%; margin-top: 30px; transition: 0.3s; text-decoration: none; display: block; text-align: center; }
            .btn-confirmar:hover { background: #27ae60; transform: translateY(-2px); }
            .success-msg { background-color: #d4edda; color: #155724; padding: 20px; border-radius: 8px; text-align: center; margin-bottom: 30px; border: 1px solid #c3e6cb; }
            
            @media print {
                .main-header, .invoice-actions, footer, .success-msg { display: none !important; }
                .invoice-box { box-shadow: none; border: none; width: 100%; padding: 0; }
                body { background: white; }
                .main-content { padding: 0; }
            }
        </style>
    </head>
    <body>


        <c:choose>
            <c:when test="${not empty carritoFinal}">
                <c:set var="listaItems" value="${carritoFinal}" />
            </c:when>
            <c:when test="${sessionScope.carrito.getClass().simpleName == 'ArrayList' or sessionScope.carrito.getClass().simpleName == 'List'}">
                <c:set var="listaItems" value="${sessionScope.carrito}" />
            </c:when>
            <c:otherwise>
                <c:set var="listaItems" value="${null}" />
            </c:otherwise>
        </c:choose>

        <%-- Función para calcular el getTotal dinámicamente --%>
        <c:set var="totalCalculado" value="0" />
        <c:forEach var="item" items="${listaItems}">
            <c:set var="totalCalculado" value="${totalCalculado + (item.producto.precio * item.cantidad)}" />
        </c:forEach>

        <c:set var="base" value="${totalCalculado / 1.21}" />
        <c:set var="iva" value="${totalCalculado - base}" />

        <header class="main-header">
            <div class="header-left">
                <a href="${pageContext.request.contextPath}/Inicio" class="logo-link">
                    <img src="${pageContext.request.contextPath}/IMAGENES/logo/MundoNodologo.png" alt="Logo" class="logo-img">
                </a>
                <h2 onclick="location.href = '${pageContext.request.contextPath}/Inicio'" style="cursor:pointer; margin:0; color:white;">MundoNodo</h2>
            </div>
            <div class="header-right" style="display: flex; align-items: center; gap: 15px;">
                <img src="${pageContext.request.contextPath}/IMAGENES/avatar/${sessionScope.usuarioLogueado.avatar}" 
                     alt="Avatar" style="width: 35px; height: 35px; border-radius: 50%; object-fit: cover; border: 2px solid #e67e22;"
                     onerror="this.src='${pageContext.request.contextPath}/IMAGENES/avatar/pinguavatar.png';">
                <span style="color: white;">Hola, <strong>${sessionScope.usuarioLogueado.nombre}</strong></span>
            </div>
        </header>

        <main class="main-content">
            <div class="invoice-box">

                <c:if test="${not empty compraOk}">
                    <div class="success-msg">
                        <i class="fas fa-check-circle fa-3x"></i>
                        <h2 style="margin: 10px 0;">¡GRACIAS POR TU COMPRA!</h2>
                        <p style="font-size: 1.2rem;">Tu pedido <strong>#MN-${numPedido}</strong> ha sido procesado con éxito.</p>
                    </div>
                </c:if>

                <div class="header-factura">
                    <div>
                        <h1 style="color: #e67e22; margin: 0; font-size: 2.5rem;">
                            <c:out value="${not empty compraOk ? 'FACTURA OFICIAL' : 'RESUMEN DE PEDIDO'}" />
                        </h1>
                        <p style="color: #7f8c8d;">MundoNodo Hardware S.L. - NIF: B-12345678</p>
                    </div>
                    <div style="text-align: right;">
                        <p><strong>Fecha:</strong> <fmt:formatDate value="<%= new java.util.Date()%>" pattern="dd/MM/yyyy" /></p>
                        <p><strong>Referencia:</strong> MN-2026-${sessionScope.usuarioLogueado.idusuario}</p>
                    </div>
                </div>

                <div class="details">
                    <div>
                        <h4 style="margin:0; color: #2c3e50;"><i class="fas fa-truck"></i> Envío a:</h4>
                        <address style="margin: 5px 0; line-height: 1.5; font-style: normal;">
                            <strong>${sessionScope.usuarioLogueado.nombre} ${sessionScope.usuarioLogueado.apellidos}</strong><br>
                            ${sessionScope.usuarioLogueado.direccion}<br>
                            ${sessionScope.usuarioLogueado.localidad}, ${sessionScope.usuarioLogueado.provincia} (${sessionScope.usuarioLogueado.cp})
                        </address>
                    </div>
                </div>

                <table class="table-factura">
                    <thead>
                        <tr>
                            <th>Producto</th>
                            <th style="text-align: center;">Cant.</th>
                            <th style="text-align: right;">Precio Unit.</th>
                            <th style="text-align: right;">Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${listaItems}">
                            <tr>
                                <td><strong>${item.producto.nombre}</strong></td>
                                <td style="text-align: center;">${item.cantidad}</td>
                                <td style="text-align: right;"><fmt:formatNumber value="${item.producto.precio}" type="currency" currencySymbol="€"/></td>
                                <td style="text-align: right; font-weight: bold;"><fmt:formatNumber value="${item.producto.precio * item.cantidad}" type="currency" currencySymbol="€"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>    
                </table>

                <div class="total-section">
                    <div class="tax-row">
                        <span>Base Imponible:</span>
                        <strong><fmt:formatNumber value="${base}" type="currency" currencySymbol="€"/></strong>
                    </div>
                    <div class="tax-row">
                        <span>IVA (21%):</span>
                        <strong><fmt:formatNumber value="${iva}" type="currency" currencySymbol="€"/></strong>
                    </div>
                    <div class="tax-row" style="color: #2ecc71; font-weight: bold;">
                        <span>Gastos de Envío:</span>
                        <span>¡GRATIS!</span>
                    </div>
                    <hr style="border: none; border-top: 2px solid #2c3e50; margin: 20px 0 10px 0; width: 35%; margin-left: auto;">
                    <h2 style="color: #e67e22; font-size: 3rem; margin: 0;">
                        TOTAL: <fmt:formatNumber value="${totalCalculado}" type="currency" currencySymbol="€"/>
                    </h2>
                </div>

                <div class="invoice-actions" style="margin-top: 40px;">
                    <c:choose>
                        <c:when test="${empty compraOk}">
                            <form action="${pageContext.request.contextPath}/ProcesarCompra" method="POST">
                                <button type="submit" class="btn-confirmar">
                                    <i class="fas fa-check-double"></i> CONFIRMAR Y FINALIZAR COMPRA
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div style="display: flex; gap: 20px;">
                                <a href="${pageContext.request.contextPath}/Inicio" class="btn-confirmar" style="background-color: #34495e; flex: 1;">
                                    <i class="fas fa-store"></i> SEGUIR COMPRANDO
                                </a>
                                <a href="${pageContext.request.contextPath}/HistorialPedidos" class="btn-confirmar" style="background-color: #e67e22; flex: 1;">
                                    <i class="fas fa-history"></i> VER MIS PEDIDOS
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
        
        <footer style="margin-top: auto; padding: 20px; text-align: center; color: #7f8c8d;">
            <p>&copy; 2026 MundoNodo Hardware S.L. - Mérida, España</p>
        </footer>
    </body>
</html>