<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Detalle del Pedido #MN-${idPedido} | MundoNodo</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/style.css">

        <style>
            body {
                background-color: #f4f7f6;
                min-height: 100vh;
                display: flex;
                flex-direction: column;
            }
            .main-content {
                flex: 1;
                padding: 40px 20px;
            }
            .detail-container {
                max-width: 900px;
                margin: 0 auto;
                background: white;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            .detail-title {
                color: #2c3e50;
                border-bottom: 3px solid #e67e22;
                padding-bottom: 10px;
                margin-bottom: 30px;
                display: flex;
                align-items: center;
                justify-content: space-between;
            }

            .product-img {
                width: 50px;
                height: 50px;
                object-fit: cover;
                border-radius: 5px;
                border: 1px solid #ddd;
            }
            .table-details {
                width: 100%;
                border-collapse: collapse;
            }
            .table-details th {
                background-color: #2c3e50;
                color: white;
                padding: 15px;
                text-align: left;
            }
            .table-details td {
                padding: 15px;
                border-bottom: 1px solid #eee;
                vertical-align: middle;
            }

            .summary-box {
                margin-top: 30px;
                text-align: right;
                padding: 20px;
                background: #f9f9f9;
                border-radius: 8px;
            }
            .btn-back {
                display: inline-block;
                background-color: #34495e;
                color: white;
                padding: 12px 25px;
                border-radius: 8px;
                text-decoration: none;
                transition: 0.3s;
                margin-top: 20px;
            }
            .btn-back:hover {
                background-color: #2c3e50;
                transform: translateX(-5px);
            }
        </style>
    </head>
    <body>

        <header class="main-header">
            <div class="header-left">
                <img src="${pageContext.request.contextPath}/IMAGENES/logo/MundoNodologo.png" alt="Logo" class="logo-img" style="width: 45px;">
                <h2 style="color: white; margin: 0;">MundoNodo</h2>
            </div>
            <div class="header-right">
                <span style="color: white;">Detalle de Compra</span>
            </div>
        </header>

        <main class="main-content">
            <div class="detail-container">
                <div class="detail-title">
                    <h1><i class="fas fa-receipt"></i> Pedido #MN-${idPedido}</h1>
                    <a href="${pageContext.request.contextPath}/HistorialPedidos" class="btn-back">
                        <i class="fas fa-arrow-left"></i> Volver
                    </a>
                </div>

                <table class="table-details">
                    <thead>
                        <tr>
                            <th>Producto</th>
                            <th style="text-align: center;">Cantidad</th>
                            <th style="text-align: right;">Precio Unit.</th>
                            <th style="text-align: right;">Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="totalAcumulado" value="0" />
                        <c:forEach var="item" items="${detalles}">
                            <c:set var="subtotal" value="${item.producto.precio * item.cantidad}" />
                            <c:set var="totalAcumulado" value="${totalAcumulado + subtotal}" />
                            <tr>
                                <td style="display: flex; align-items: center; gap: 15px;">
                                    <img src="${pageContext.request.contextPath}/IMAGENES/productos/${item.producto.imagen}.jpg" 
                                         alt="${item.producto.nombre}" class="product-img"
                                         onerror="this.src='${pageContext.request.contextPath}/IMAGENES/productos/no-image.png';">
                                    <strong>${item.producto.nombre}</strong>
                                </td>
                                <td style="text-align: center;">${item.cantidad}</td>
                                <td style="text-align: right;">
                                    <fmt:formatNumber value="${item.producto.precio}" type="currency" currencySymbol="€"/>
                                </td>
                                <td style="text-align: right; font-weight: bold;">
                                    <fmt:formatNumber value="${subtotal}" type="currency" currencySymbol="€"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <div class="summary-box">
                    <p style="font-size: 1.1rem; color: #7f8c8d; margin: 5px 0;">Base Imponible: 
                        <fmt:formatNumber value="${totalAcumulado / 1.21}" type="currency" currencySymbol="€"/>
                    </p>
                    <p style="font-size: 1.1rem; color: #7f8c8d; margin: 5px 0;">IVA (21%): 
                        <fmt:formatNumber value="${totalAcumulado - (totalAcumulado / 1.21)}" type="currency" currencySymbol="€"/>
                    </p>
                    <h2 style="color: #e67e22; font-size: 2rem; margin-top: 10px;">
                        Total Pagado: <fmt:formatNumber value="${totalAcumulado}" type="currency" currencySymbol="€"/>
                    </h2>
                </div>
            </div>
        </main>

        <footer style="background: #2c3e50; color: white; text-align: center; padding: 20px; margin-top: auto;">
            <p>&copy; 2026 MundoNodo Hardware S.L. - Mérida, España</p>
        </footer>

    </body>
</html>