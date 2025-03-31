<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.cart.Cart" scope="request"/>
<fmt:formatNumber value="${cart.totalCost}" type="currency" currencyCode="${cart.items[0].product.currency}" var="totalCost"/>
<a class="minicart" href="${pageContext.servletContext.contextPath}/cart">
  Cart: ${cart.totalQuantity} items for ${totalCost}
</a>