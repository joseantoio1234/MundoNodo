<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- SEGURIDAD: Si no hay usuario en sesión, lo mandamos al login --%>
<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="/ValidarUsuario" />
</c:if>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Mi Perfil | MundoNodo</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
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
                <a href="${pageContext.request.contextPath}/Carrito" class="cart-btn" style="position: relative;">
                    <i class="fas fa-shopping-cart"></i> 
                    Carrito (<span id="contador-carrito-ajax">${sessionScope.carrito != null ? sessionScope.carrito.size() : 0}</span>)
                </a>

                <a href="${pageContext.request.contextPath}/Inicio" class="auth-btn register">
                    <i class="fas fa-home"></i> Inicio
                </a>

                <div class="user-nav-profile" style="display: flex; align-items: center; gap: 10px; margin-left: 10px;">
                    <div class="avatar-circle nav-avatar" style="width: 35px; height: 35px; border: 2px solid #e67e22; border-radius: 50%; overflow: hidden;">
                        <img src="${pageContext.request.contextPath}/IMAGENES/avatars/${not empty sessionScope.usuarioLogueado.avatar ? sessionScope.usuarioLogueado.avatar : 'pinguavatar.png'}?t=${System.currentTimeMillis()}" 
                             alt="Avatar" 
                             class="user-avatar-nav"
                             style="width: 100%; height: 100%; object-fit: cover;"
                             onerror="this.src='${pageContext.request.contextPath}/IMAGENES/avatars/pinguavatar.png';">
                    </div>
                    <span style="color: white; font-size: 0.85rem;">
                        <strong> Hola ${sessionScope.usuarioLogueado.nombre}</strong>
                    </span>
                </div>

                <a href="${pageContext.request.contextPath}/CerrarSesion" class="auth-btn logout" style="background-color: #e74c3c; margin-left: 10px; padding: 5px 10px; border-radius: 4px; color: white; text-decoration: none; font-size: 0.9rem;">
                    <i class="fas fa-sign-out-alt"></i> Salir
                </a>
            </div>
        </header>

        <main class="auth-main">
            <div class="auth-card" style="max-width: 700px;">
                <div class="auth-badge">Gestionar Mi Perfil</div>

                <p style="text-align: center; color: #666; font-size: 0.9rem; margin-bottom: 20px;">
                    Actualiza tus datos de envío. Por seguridad, el NIF y el Email son fijos.
                </p>

                <form id="formPerfil" enctype="multipart/form-data">

                    <div style="margin: 0 0 10px 0; border-bottom: 2px solid #e67e22; padding-bottom: 5px; color: #e67e22; font-weight: bold;">
                        <i class="fas fa-camera"></i> MI AVATAR
                    </div>

                    <div class="auth-field" style="display: flex; align-items: center; gap: 20px; margin-bottom: 25px; background: #fdfdfd; padding: 15px; border-radius: 8px; border: 1px solid #eee;">
                        <div style="width: 100px; height: 100px; border-radius: 50%; overflow: hidden; border: 3px solid #e67e22; background: #eee; flex-shrink: 0;">
                            <img id="imgPreview" 
                                 src="${pageContext.request.contextPath}/IMAGENES/avatars/${sessionScope.usuarioLogueado.avatar}" 
                                 style="width: 100%; height: 100%; object-fit: cover;"
                                 onerror="this.src='${pageContext.request.contextPath}/IMAGENES/avatars/pinguavatar.png';">
                        </div>
                        <div style="flex: 1;">
                            <label style="display: block; margin-bottom: 8px;">Cambiar foto de perfil</label>
                            <input type="file" name="fotoAvatar" id="fotoAvatar" accept="image/*" style="font-size: 0.85rem;">
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px; background: #f9f9f9; padding: 15px; border-radius: 8px; margin-bottom: 20px;">
                        <div class="auth-field">
                            <label>DNI / NIF</label>
                            <input type="text" value="${sessionScope.usuarioLogueado.dni}" disabled style="background: #eee;">
                        </div>
                        <div class="auth-field">
                            <label>Email</label>
                            <input type="text" value="${sessionScope.usuarioLogueado.correo}" disabled style="background: #eee;">
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                        <div class="auth-field">
                            <label>Nombre</label>
                            <input type="text" name="nombre" value="${sessionScope.usuarioLogueado.nombre}" required>
                        </div>
                        <div class="auth-field">
                            <label>Apellidos</label>
                            <input type="text" name="apellidos" value="${sessionScope.usuarioLogueado.apellidos}" required>
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                        <div class="auth-field">
                            <label>Teléfono</label>
                            <input type="tel" name="telefono" value="${sessionScope.usuarioLogueado.telefono}" maxlength="9">
                        </div>
                        <div class="auth-field">
                            <label>Dirección</label>
                            <input type="text" name="direccion" value="${sessionScope.usuarioLogueado.direccion}" required>
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 15px;">
                        <div class="auth-field">
                            <label>Localidad</label>
                            <input type="text" name="localidad" value="${sessionScope.usuarioLogueado.localidad}" required>
                        </div>
                        <div class="auth-field">
                            <label>Provincia</label>
                            <input type="text" name="provincia" value="${sessionScope.usuarioLogueado.provincia}" required>
                        </div>
                        <div class="auth-field">
                            <label>C.P.</label>
                            <input type="text" name="cp" value="${sessionScope.usuarioLogueado.cp}" maxlength="5" required>
                        </div>
                    </div>

                    <div style="margin: 25px 0 10px 0; border-bottom: 2px solid #e67e22; padding-bottom: 5px; color: #e67e22; font-weight: bold;">
                        <i class="fas fa-shield-alt"></i> CAMBIAR CONTRASEÑA
                    </div>

                    <div class="auth-field">
                        <label>Contraseña Actual</label>
                        <input type="password" name="passActual" id="passActual">
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                        <div class="auth-field">
                            <label>Nueva Contraseña</label>
                            <input type="password" name="passNueva1" id="passNueva1">
                        </div>
                        <div class="auth-field">
                            <label>Confirmar Nueva</label>
                            <input type="password" id="passNueva2">
                        </div>
                    </div>

                    <button type="submit" id="btnGuardarPerfil" class="btn-submit-auth" style="margin-top: 20px;">
                        Guardar Cambios
                    </button>
                </form>

                <div class="perfil-footer" style="margin-top: 30px; display: flex; justify-content: space-between; align-items: center;">
                    <a href="${pageContext.request.contextPath}/HistorialPedidos" class="btn-historial" style="text-decoration: none; color: #2c3e50; font-weight: bold;">
                        <i class="fas fa-history"></i> Mis Pedidos
                    </a>
                    <div style="font-size: 0.85rem; color: #888;">
                        Sesión: <fmt:formatDate value="${sessionScope.fechaLogin}" pattern="HH:mm:ss" />
                    </div>
                </div>
            </div>
        </main>

        <footer class="footer-regist">
            <p>&copy; 2026 MundoNodo Hardware S.L.</p>
        </footer>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const formPerfil = document.getElementById('formPerfil');
                const btnGuardar = document.getElementById('btnGuardarPerfil');
                const avatarInput = document.getElementById('fotoAvatar');
                const imgPreview = document.getElementById('imgPreview'); // Foto grande
                const imgNavHeader = document.querySelector('.user-avatar-nav'); // Foto pequeña (header)

                const pinguPath = "${pageContext.request.contextPath}/IMAGENES/avatar/pinguavatar.png";

                // --- 1. PREVISUALIZACIÓN DUAL ---
                if (avatarInput) {
                    avatarInput.addEventListener('change', function () {
                        const file = this.files[0];
                        if (file && file.type.startsWith('image/')) {
                            const urlNueva = URL.createObjectURL(file);
                            // Actualizamos ambas imágenes al mismo tiempo
                            if (imgPreview)
                                imgPreview.src = urlNueva;
                            if (imgNavHeader)
                                imgNavHeader.src = urlNueva;
                        } else if (file) {
                            Swal.fire('Error', 'Por favor, selecciona una imagen válida.', 'error');
                            this.value = '';
                        }
                    });
                }

                // --- 2. CONTROL DE ERRORES GLOBAL ---
                [imgPreview, imgNavHeader].forEach(img => {
                    if (img) {
                        img.onerror = function () {
                            this.onerror = null;
                            this.src = pinguPath;
                            console.log("Avatar no encontrado, cargando pingu por defecto.");
                        };
                    }
                });

                // --- 3. ENVÍO AJAX ---
                if (formPerfil) {
                    formPerfil.addEventListener('submit', function (e) {
                        e.preventDefault();

                        // Validaciones de contraseña
                        const passActual = document.getElementById('passActual').value;
                        const passNueva1 = document.getElementById('passNueva1').value;
                        const passNueva2 = document.getElementById('passNueva2').value;

                        if (passNueva1 !== "" || passNueva2 !== "") {
                            if (passActual === "") {
                                Swal.fire('Atención', 'Introduce tu contraseña actual.', 'warning');
                                return;
                            }
                            if (passNueva1 !== passNueva2) {
                                Swal.fire('Error', 'Las contraseñas no coinciden.', 'error');
                                return;
                            }
                            if (passNueva1.length < 8) {
                                Swal.fire('Error', 'Mínimo 8 caracteres.', 'error');
                                return;
                            }
                        }

                        btnGuardar.disabled = true;
                        btnGuardar.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Guardando...';

                        fetch('${pageContext.request.contextPath}/ActualizarPerfil', {
                            method: 'POST',
                            body: new FormData(formPerfil)
                        })
                                .then(response => response.text())
                                .then(resText => {
                                    if (resText.trim() === "success") {
                                        Swal.fire({
                                            title: '¡Hecho!',
                                            text: 'Perfil actualizado.',
                                            icon: 'success',
                                            timer: 1500,
                                            showConfirmButton: false
                                        }).then(() => window.location.reload());
                                    } else {
                                        Swal.fire('Error', resText, 'error');
                                        resetBoton();
                                    }
                                })
                                .catch(err => {
                                    Swal.fire('Error', 'Fallo de conexión.', 'error');
                                    resetBoton();
                                });
                    });
                }

                function resetBoton() {
                    btnGuardar.disabled = false;
                    btnGuardar.innerHTML = 'Guardar Cambios';
                }
            });
        </script>
    </body>
</html>