<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
  <h1>
    Cart
  </h1>
  <tags:successDisplaying error="${errors}"/>
  <tags:errorDisplaying error="${errors}"/>
  <form method="post">
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
          <c:forEach var="cartItem" items="${cart.items}" varStatus="status">
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
                  <c:set var="error" value="${errors[cartItem.product.id]}"/>
                  <input class="numeric-val ${not empty error ? 'error-input' : ''}" name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : quantity}"/>
                  <c:if test="${not empty error}">
                    <p class="error-message">${error}</p>
                  </c:if>
                  <input type="hidden" name="productId" value="${cartItem.product.id}"/>
                </td>
                <td>
                  <button form="deleteCartItem" formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${cartItem.product.id}">Delete</button>
                </td>
              </tr>
            </tbody>
          </c:forEach>
          <tr>
            <td></td>
            <td></td>
            <td>
              <fmt:formatNumber value="${cart.totalCost}" type="currency" currencyCode="${cart.items[0].product.currency}" var="totalCost"/>
              Total cost: ${totalCost}
            </td>
            <td>
              <fmt:formatNumber value="${cart.totalQuantity}" var="totalQuantity"/>
              Total quantity: ${totalQuantity}
            </td>
            <td></td>
          </tr>
        </table>
      </c:when>
      <c:otherwise>Cart is empty</c:otherwise>
    </c:choose>
    <p>
      <button>Update</button>
    </p>
  </form>
  <form id="deleteCartItem" method="post"></form>
  <c:if test="${not empty cart.items}">
    <form action="${pageContext.servletContext.contextPath}/checkout">
      <button>Checkout</button>
    </form>
  </c:if>
  <tags:recentlyViewedProducts/>
</tags:master>