<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
  <h1>
    Cart
  </h1>
  <c:choose>
    <c:when test="${not empty cart.items}">
      <table>
        <thead>
          <tr>
            <th>Image</th>
            <th>Description</th>
            <th>Price</th>
            <th>Quantity</th>
          </tr>
        </thead>
        <c:forEach var="cartItem" items="${cart.items}">
          <tbody>
            <tr>
              <td>
                <img class="product-tile" src="${cartItem.product.imageUrl}">
              </td>
              <td>${cartItem.product.description}</td>
              <td class="price">
                <fmt:formatNumber value="${cartItem.product.price}" type="currency" currencySymbol="${cartItem.product.currency.symbol}"/>
              </td>
              <td>
                <fmt:formatNumber value="${cartItem.quantity}" var="quantity"/>
                <input class="numeric-val" name="quantity" value="${quantity}"/>
              </td>
            </tr>
          </tbody>
        </c:forEach>
      </table>
    </c:when>
    <c:otherwise>Cart is empty</c:otherwise>
  </c:choose>
  <tags:recentlyViewedProducts/>
</tags:master>