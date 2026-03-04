<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error del Servidor | MundoNodo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
</head>
<body class="error-page">
    <div class="error-container">
        <h1>500</h1>
        <h2>Algo salió mal en nuestros servidores</h2>
        <p>Estamos trabajando para solucionarlo. Inténtalo de nuevo más tarde.</p>
        <a href="${pageContext.request.contextPath}/Inicio" class="auth-btn">Volver al Inicio</a>
    </div>
</body>
</html>