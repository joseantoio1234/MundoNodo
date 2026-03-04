<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Mi Historial de Pedidos | MundoNodo</title>
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
            .history-container {
                max-width: 1000px;
                margin: 0 auto;
                background: white;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            .history-title {
                color: #2c3e50;
                border-bottom: 3px solid #e67e22;
                padding-bottom: 10px;
                margin-bottom: 30px;
                display: flex;
                align-items: center;
                gap: 15px;
            }

            .order-table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 10px;
            }
            .order-table th {
                background-color: #2c3e50;
                color: white;
                padding: 15px;
                text-align: left;
                font-weight: 600;
            }
            .order-table td {
                padding: 15px;
                border-bottom: 1px solid #eee;
                color: #555;
            }
            .order-table tr:hover {
                background-color: #f9f9f9;
            }

            .status-badge {
                padding: 5px 12px;
                border-radius: 20px;
                font-size: 0.85rem;
                font-weight: bold;
                text-transform: uppercase;
            }
            .status-pagado {
                background-color: #ffeaa7;
                color: #d35400;
            }
            .status-completado {
                background-color: #55efc4;
                color: #00b894;
            }

            .btn-detail {
                color: #e67e22;
                text-decoration: none;
                font-weight: bold;
                transition: 0.3s;
            }
            .btn-detail:hover {
                color: #d35400;
                text-decoration: underline;
            }

            .empty-history {
                text-align: center;
                padding: 50px 0;
            }
            .empty-history i {
                color: #ddd;
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>

        <header class="main-header">
            <div class="header-left">
                <a href="${pageContext.request.contextPath}/Inicio">
                    <img src="${pageContext.request.contextPath}/IMAGENES/logo/MundoNodologo.png" alt="Logo" class="logo-img" style="width: 45px;">
                </a>
                <h2 style="color: white; margin: 0; cursor: pointer;" onclick="location.href = '${pageContext.request.contextPath}/Inicio'">MundoNodo</h2>
            </div>
            <div class="header-right">
                <a href="${pageContext.request.contextPath}/Inicio" style="color: white; text-decoration: none;">
                    <i class="fas fa-home"></i> Volver a la tienda
                </a>
            </div>
        </header>

        <main class="main-content">
            <div class="history-container">
                <h1 class="history-title"><i class="fas fa-box-open"></i> Mis Pedidos Realizados</h1>

                <c:choose>
                    <c:when test="${not empty listaPedidos}">
                        <table class="order-table">
                            <thead>
                                <tr>
                                    <th>ID Pedido</th>
                                    <th>Fecha y Hora</th>
                                    <th>Total Pagado</th>
                                    <th>Estado</th>
                                    <th style="text-align: center;">Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="pedido" items="${listaPedidos}">
                                    <tr>
                                        <td><strong>#MN-${pedido.idpedido}</strong></td>
                                        <td>
                                            <fmt:formatDate value="${pedido.fecha}" pattern="dd/MM/yyyy HH:mm" />
                                        </td>
                                        <td style="font-weight: bold; color: #2c3e50;">
                                            <fmt:formatNumber value="${pedido.total}" type="currency" currencySymbol="€"/>
                                        </td>
                                        <td>
                                            <span class="status-badge ${pedido.estado == 'pendiente' ? 'status-pendiente' : 'status-pagado'}">
                                                ${pedido.estado}
                                            </span>
                                        </td>
                                        <td style="text-align: center;">
                                            <a href="${pageContext.request.contextPath}/DetallePedido?id=${pedido.idpedido}" class="btn-detail">
                                                <i class="fas fa-search-plus"></i> Ver detalles
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-history">
                            <i class="fas fa-receipt fa-5x"></i>
                            <h3>Aún no has realizado ninguna compra</h3>
                            <p>Cuando realices tu primer pedido, aparecerá detallado en esta sección.</p>
                            <a href="${pageContext.request.contextPath}/Inicio" class="auth-btn register" style="display: inline-block; margin-top: 20px;">
                                Explorar catálogo
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <footer style="background: #2c3e50; color: white; text-align: center; padding: 20px; margin-top: auto;">
            <p>&copy; 2026 MundoNodo Hardware S.L. - Mérida, España</p>
        </footer>

    </body>
</html>