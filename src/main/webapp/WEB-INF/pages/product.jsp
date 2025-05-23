<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
  <c:set var="cart" value="${sessionScope['com.es.phoneshop.cart.DefaultCartService.cart']}"/>
  <c:if test="${not empty cart}">
    <p>Cart: {${cart.items}, ${cart.totalQuantity}, ${cart.totalCost}}</p>
  </c:if>
  <p>
    ${product.description}
  </p>
  <img src="${product.imageUrl}">
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Code</th>
        <th>Description</th>
        <th>Price</th>
        <th>Stock</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>${product.id}</td>
        <td>${product.code}</td>
        <td>${product.description}</td>
        <td class="price">
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
        <td>${product.stock}</td>
    </tbody>
  </table>
  <form method="post" style="margin-top: 10px">
    <input class="numeric-val ${not empty error ? 'error-input' : ''}" name="quantity" value="${not empty error ? param.quantity : 1}"/>
    <button>Add to cart</button>
  </form>
  <c:if test="${not empty error}">
    <p class="error-message">${error.message}</p>
  </c:if>
  <tags:successDisplaying error="${error}"/>
  <tags:recentlyViewedProducts/>
</tags:master>