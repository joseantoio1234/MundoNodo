<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
    <c:when test="${not empty listaFiltrada}">
        <div class="product-grid-compact">
            <c:forEach var="prod" items="${listaFiltrada}">
                <div class="card-landing-style" onclick="openModal('modal-filter-${prod.idproducto}')">
                    <div class="img-container-landing">
                        <img src="IMAGENES/productos/${prod.imagen}.jpg" alt="${prod.nombre}" onerror="this.src='IMAGENES/productos/default.jpg'">
                    </div>
                    <div>
                        <h3>${prod.nombre}</h3>
                        <p class="price-orange">
                            <fmt:formatNumber value="${prod.precio}" type="number" minFractionDigits="2" maxFractionDigits="2" />?
                        </p>
                    </div>
                </div>

                <div id="modal-filter-${prod.idproducto}" class="modal-overlay">
                    <div class="modal-content">
                        <span class="close-modal" onclick="closeModal('modal-filter-${prod.idproducto}')">&times;</span>
                        <div class="modal-grid">
                            <div class="modal-img-side">
                                <img src="IMAGENES/productos/${prod.imagen}.jpg" alt="${prod.nombre}">
                            </div>
                            <div class="modal-info-side">
                                <h2>${prod.nombre}</h2>
                                <p class="modal-brand">Marca: ${prod.marca}</p>
                                <hr>
                                <p class="modal-description">${prod.descripcion}</p>
                                <div class="modal-footer-price">
                                    <span class="modal-price-val" style="color:#f39c12">
                                        <fmt:formatNumber value="${prod.precio}" type="number" minFractionDigits="2" maxFractionDigits="2" />?
                                    </span>
                                    <a href="${pageContext.request.contextPath}/AnadirCarrito?id=${prod.idproducto}" class="btn-submit-auth">
                                        <i class="fas fa-shopping-cart"></i> Añadir al carrito
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:when>
    <c:otherwise>
        <div class="no-results-msg">
            <p><i class="fas fa-info-circle"></i> No se han encontrado productos con estos filtros.</p>
        </div>
    </c:otherwise>
</c:choose>