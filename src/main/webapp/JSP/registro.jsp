<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registro Profesional | MundoNodo</title>
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
                <a href="${pageContext.request.contextPath}/Carrito" class="cart-btn">
                    <i class="fas fa-shopping-cart"></i> 
                    Carrito (<span id="contador-carrito-ajax">${sessionScope.carrito != null ? sessionScope.carrito.size() : 0}</span>)
                </a>

                <a href="${pageContext.request.contextPath}/Inicio" class="auth-btn register" style="margin-right: 10px;">
                    <i class="fas fa-home"></i> Inicio
                </a>
                <a href="${pageContext.request.contextPath}/ValidarUsuario" class="auth-btn register">Iniciar Sesión</a>
            </div>
        </header>

        <main class="auth-main">
            <div class="auth-card" style="max-width: 650px;"> 
                <div class="auth-badge">Crear Cuenta de Cliente</div>

                <form id="formRegistro" action="${pageContext.request.contextPath}/RegistroUsuario" method="POST" enctype="multipart/form-data">

                    <div class="avatar-header" style="text-align: center; margin-bottom: 20px; color: #e67e22; font-weight: bold;">
                        <i class="fas fa-camera"></i> FOTO DE PERFIL
                    </div>

                    <div class="avatar-upload-container" style="display: flex; align-items: center; gap: 20px; padding: 20px; border: 1px dashed #e67e22; border-radius: 10px; background-color: #fffaf5;">
                        <div class="avatar-circle" style="width: 80px; height: 80px; border-radius: 50%; overflow: hidden; border: 3px solid #e67e22; flex-shrink: 0; background-color: #eee;">
                            <img id="imgPreview" 
                                 src="${pageContext.request.contextPath}/IMAGENES/avatar/pinguavatar.png" 
                                 alt="Vista previa" 
                                 style="width: 100%; height: 100%; object-fit: cover;">
                        </div>

                        <div class="upload-controls" style="flex: 1;">
                            <label style="display: block; font-weight: bold; margin-bottom: 5px;">Subir Avatar</label>
                            <input type="file" name="fotoAvatar" id="fotoAvatar" accept="image/*" style="width: 100%; padding: 5px; border: 1px solid #ddd; border-radius: 5px; background: white;">
                            <p style="font-size: 0.75rem; color: #888; margin-top: 8px;">Formatos: JPG, PNG. Máx: 2MB.</p>
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                        <div class="auth-field">
                            <label>Nombre</label>
                            <input type="text" name="nombre" placeholder="Tu nombre">
                        </div>
                        <div class="auth-field">
                            <label>Apellidos</label>
                            <input type="text" name="apellidos"  placeholder="Tus apellidos">
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                        <div class="auth-field">
                            <label>DNI / NIF</label>
                            <input type="text" id="dni" name="dni" maxlength="9"  placeholder="12345678Z">
                        </div>
                        <div class="auth-field">
                            <label>Teléfono</label>
                            <input type="tel" name="telefono" placeholder="Tiene que empezar por 6, 7 o 9" maxlength="9">
                        </div>
                    </div>

                    <div class="auth-field">    
                        <label>Correo Electrónico</label>
                        <input type="email" name="correo" id="correo" placeholder="ejemplo@correo.com">
                        <div id="emailFeedback" style="margin-top: 5px; font-size: 0.9rem; font-weight: bold; display: none;"></div>
                    </div>

                    <div style="margin: 20px 0 10px 0; border-bottom: 1px solid #eee; padding-bottom: 5px; color: #e67e22; font-weight: bold; font-size: 0.9rem;">
                        <i class="fas fa-truck"></i> DATOS DE ENVÍO
                    </div>

                    <div class="auth-field">
                        <label>Dirección</label>
                        <input type="text" name="direccion" id="direccion" placeholder="Calle, número, piso...">
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 15px;">
                        <div class="auth-field">
                            <label>Localidad</label>
                            <input type="text" name="localidad" id="localidad">
                        </div>
                        <div class="auth-field">
                            <label>Provincia</label>
                            <input type="text" name="provincia" id="provincia">
                        </div>
                        <div class="auth-field">
                            <label>Código Postal</label>
                            <input type="text" name="cp" id="cp" placeholder="06800" maxlength="5">
                        </div>
                    </div>

                    <div style="margin: 20px 0 10px 0; border-bottom: 1px solid #eee; padding-bottom: 5px; color: #e67e22; font-weight: bold; font-size: 0.9rem;">
                        <i class="fas fa-lock"></i> SEGURIDAD
                    </div>

                    <div class="auth-field">
                        <label>Contraseña</label>
                        <div class="password-wrapper">
                            <input type="password" id="pass1" name="password" placeholder="Mínimo 8 caracteres">
                            <i class="fa-solid fa-eye toggle-password" onclick="togglePass('pass1', this)"></i>
                        </div>
                    </div>

                    <div class="auth-field">
                        <label>Confirmar Contraseña</label>
                        <div class="password-wrapper">
                            <input type="password" id="pass2" placeholder="Repite la contraseña">
                            <i class="fa-solid fa-eye toggle-password" onclick="togglePass('pass2', this)"></i>
                        </div>
                    </div>

                    <button type="submit" id="btnRegistro" class="btn-submit-auth">Finalizar Registro</button>
                </form>

                <div class="auth-switch">
                    ¿Ya eres cliente? <a href="${pageContext.request.contextPath}/ValidarUsuario">Inicia sesión aquí</a>
                </div>
            </div>
        </main>

        <footer class="footer-regist">
            <p>&copy; 2026 MundoNodo Hardware S.L. Todos los derechos reservados.</p>
        </footer>

<script>
    // 1. Definir la función GLOBALMENTE para asegurar visibilidad desde el onclick
    function togglePass(id, icon) {
        const input = document.getElementById(id);
        if (input) {
            if (input.type === "password") {
                input.type = "text";
                icon.classList.replace("fa-eye", "fa-eye-slash");
            } else {
                input.type = "password";
                icon.classList.replace("fa-eye-slash", "fa-eye");
            }
        }
    }

    document.addEventListener('DOMContentLoaded', function () {
        // --- SELECCIÓN DE ELEMENTOS ---
        const form = document.getElementById('formRegistro');
        const btnSubmit = document.getElementById('btnRegistro');
        const dniInput = document.getElementById('dni');
        const cpInput = document.getElementById('cp');
        const telInput = document.querySelector('input[name="telefono"]');
        const localidadInput = document.getElementById('localidad');
        const p1 = document.getElementById('pass1');
        const p2 = document.getElementById('pass2');
        const emailInput = document.getElementById('correo');
        const feedback = document.getElementById('emailFeedback');
        const avatarInput = document.getElementById('fotoAvatar');
        const imgPreview = document.getElementById('imgPreview');
        
        // Contexto para rutas AJAX
        const contextPath = "${pageContext.request.contextPath}";
        const defaultAvatar = contextPath + "/IMAGENES/avatar/pinguavatar.png";

        function aplicarValidacionVisual(input, esValido) {
            if (!input) return;
            input.style.borderColor = esValido ? "#2ecc71" : "#e74c3c";
            input.style.boxShadow = esValido ? "0 0 5px rgba(46, 204, 113, 0.2)" : "0 0 5px rgba(231, 76, 60, 0.2)";
        }

        // --- 1. VALIDACIÓN DE CORREO POR AJAX ---
        if (emailInput && feedback) {
            emailInput.addEventListener('blur', function () {
                const valorActual = this.value.trim();
                if (valorActual === "" || !valorActual.includes('@')) return;

                const url = contextPath + "/ValidarRegistroAjax?campo=correo&valor=" + encodeURIComponent(valorActual);

                fetch(url)
                    .then(res => res.json())
                    .then(data => {
                        feedback.style.display = 'block';
                        if (data.existe) {
                            aplicarValidacionVisual(this, false);
                            feedback.style.color = "#e74c3c";
                            feedback.innerHTML = '<i class="fas fa-times-circle"></i> Correo ya registrado.';
                            btnSubmit.disabled = true;
                        } else {
                            aplicarValidacionVisual(this, true);
                            feedback.style.color = "#2ecc71";
                            feedback.innerHTML = '<i class="fas fa-check-circle"></i> Correo disponible.';
                            btnSubmit.disabled = false;
                        }
                    })
                    .catch(err => console.error("Error en validación:", err));
            });
        }

        // --- 2. AUTO-CÁLCULO DE DNI (NUEVO) ---
        if (dniInput) {
            dniInput.addEventListener('input', function () {
                let valor = this.value.trim();
                
                // Limpiar letras mientras escribe para quedarse solo con números
                if (valor.length <= 8) {
                    this.value = valor.replace(/[^0-9]/g, '');
                    valor = this.value;
                }

                // Disparar AJAX exactamente a los 8 números
                if (valor.length === 8 && !isNaN(valor)) {
                    const urlDni = contextPath + "/CalcularLetraDNI?dni=" + valor;

                    fetch(urlDni)
                        .then(res => {
                            if (!res.ok) throw new Error("Error en servidor DNI");
                            return res.text();
                        })
                        .then(letra => {
                            const letraLimpia = letra.trim().toUpperCase();
                            if (letraLimpia.length === 1) {
                                this.value = valor + letraLimpia; // Concatenar número + letra
                                aplicarValidacionVisual(this, true);
                            }
                        })
                        .catch(err => {
                            console.error("Error DNI AJAX:", err);
                            aplicarValidacionVisual(this, false);
                        });
                } else if (valor.length > 9) {
                    this.value = valor.substring(0, 9);
                }
            });
        }

        // --- 3. RESTRICCIONES DE ENTRADA (CP Y TEL) ---
        if (cpInput) {
            cpInput.addEventListener('input', function() {
                this.value = this.value.replace(/[^0-9]/g, '').substring(0, 5);
                aplicarValidacionVisual(this, this.value.length === 5);
            });
        }

        if (telInput) {
            telInput.addEventListener('input', function() {
                let v = this.value.replace(/[^0-9]/g, '').substring(0, 9);
                if (v.length > 0 && !['6','7','9'].includes(v[0])) v = '';
                this.value = v;
                aplicarValidacionVisual(this, v.length === 9);
            });
        }

        // --- 4. PREVISUALIZACIÓN DE AVATAR ---
        if (avatarInput && imgPreview) {
            avatarInput.addEventListener('change', function () {
                const file = this.files[0];
                if (file && file.type.startsWith('image/')) {
                    imgPreview.src = URL.createObjectURL(file);
                } else {
                    this.value = '';
                    imgPreview.src = defaultAvatar;
                }
            });
        }

        // --- 5. ENVÍO DEL FORMULARIO ---
        if (form) {
            form.addEventListener('submit', function (e) {
                if (p1.value !== p2.value) {
                    e.preventDefault();
                    Swal.fire('Error', 'Las contraseñas no coinciden', 'error');
                    return;
                }
                if (p1.value.length < 8) {
                    e.preventDefault();
                    Swal.fire('Error', 'La contraseña debe tener al menos 8 caracteres', 'error');
                    return;
                }
            });
        }
    });
</script>
    </body>
</html>