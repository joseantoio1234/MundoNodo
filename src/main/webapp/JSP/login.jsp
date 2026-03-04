<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Iniciar Sesión | MundoNodo</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
                    <input type="text" name="query" placeholder="Buscar productos...">
                    <button type="submit"><i class="fas fa-search"></i></button>
                </form>
            </div>

            <div class="header-right">
                <a href="${pageContext.request.contextPath}/Carrito" class="cart-btn">
                    <i class="fas fa-shopping-cart"></i> 
                    Carrito (${sessionScope.carrito != null ? sessionScope.carrito.size() : 0})
                </a>
                <a href="${pageContext.request.contextPath}/RegistroUsuario" class="auth-btn register">Registrarse</a>
                <a href="${pageContext.request.contextPath}/Inicio" class="auth-btn register" style="margin-right: 10px;">
                    <i class="fas fa-home"></i> Inicio
                </a>

            </div>
        </header>

        <main class="auth-main">
            <h1 class="auth-welcome">¡Hola de nuevo!</h1>

            <div class="auth-card">
                <div class="auth-badge">Iniciar Sesión</div>

                <form id="formLogin" action="${pageContext.request.contextPath}/ValidarUsuario" method="POST">
                    <div class="auth-field">
                        <label>Correo Electrónico</label>
                        <input type="email" name="correo" required placeholder="tu@correo.com">
                    </div>

                    <div class="auth-field">
                        <label>Contraseña</label>
                        <div class="password-wrapper">
                            <input type="password" id="passLogin" name="password" required placeholder="contraseña">
                            <i class="fa-solid fa-eye toggle-password" onclick="togglePass('passLogin', this)"></i>
                        </div>
                    </div>

                    <div id="loginError" class="error-text" style="text-align:center; margin-bottom:10px; display: none;">
                        ❌ Credenciales incorrectas. Inténtalo de nuevo.
                    </div>

                    <button type="submit" id="btnEntrar" class="btn-submit-auth">Entrar</button>
                </form>

                <div class="auth-switch">
                    ¿No tienes cuenta? <a href="registro.jsp">Regístrate aquí</a>
                </div>
            </div>
        </main>

        <footer class="footer-auth">
            <p>&copy; 2026 MundoNodo Hardware S.L. Todos los derechos reservados.</p>
        </footer>

        <script>
            document.getElementById('formLogin').addEventListener('submit', function (e) {
                e.preventDefault();

                const btn = document.getElementById('btnEntrar');
                const errorDiv = document.getElementById('loginError');
                const formData = new URLSearchParams(new FormData(this));

                btn.disabled = true;
                btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Verificando...';
                if (errorDiv)
                    errorDiv.style.display = 'none';

                fetch(this.action, {
                    method: 'POST',
                    body: formData,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                })
                        .then(response => response.text())
                        .then(data => {
                            if (data.trim() === "success") {
                                Swal.fire({
                                    title: '¡Acceso Correcto!',
                                    text: 'Entrando a mundoNodo...',
                                    icon: 'success',
                                    timer: 1500,
                                    showConfirmButton: false
                                }).then(() => {
                                    window.location.href = "${pageContext.request.contextPath}/Inicio";
                                });
                            } else {
                                Swal.fire('Error', 'Correo o contraseña incorrectos', 'error');
                                btn.disabled = false;
                                btn.innerText = "Entrar";
                            }
                        })
                        .catch(err => {
                            console.error("Error en el login:", err);
                            Swal.fire('Error', 'No se pudo conectar con el servidor', 'error');
                            btn.disabled = false;
                            btn.innerText = "Entrar";
                        });
            });

            function togglePass(id, icon) {
                const input = document.getElementById(id);
                if (input.type === "password") {
                    input.type = "text";
                    icon.classList.replace('fa-eye', 'fa-eye-slash');
                } else {
                    input.type = "password";
                    icon.classList.replace('fa-eye-slash', 'fa-eye');
                }
            }
        </script>
    </body>
</html>