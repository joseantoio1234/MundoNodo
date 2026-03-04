<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Página no encontrada | MundoNodo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
</head>
<body class="error-page">
    <div class="error-container">
        <h1>404</h1>
        <img src="${pageContext.request.contextPath}/IMAGENES/avatar/pinguavatar.png" width="150">
        <h2>¡Ups! Parece que te has perdido</h2>
        <p>No hemos podido encontrar la página que buscas en MundoNodo.</p>
        <a href="${pageContext.request.contextPath}/Inicio" class="auth-btn">Volver al Inicio</a>
    </div>
</body>
</html>