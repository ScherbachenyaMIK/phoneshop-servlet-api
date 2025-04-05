<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
  <h1>
    Checkout
  </h1>
  <tags:errorDisplaying error="${errors}"/>
  <form method="post">
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
            <td class="numeric-val">
              <fmt:formatNumber value="${cartItem.quantity}"/>
          </tr>
        </tbody>
      </c:forEach>
      <tr>
        <td></td>
        <td></td>
        <td>
          <fmt:formatNumber value="${order.subtotal}" type="currency" currencyCode="${cart.items[0].product.currency}" var="subtotal"/>
          Subtotal: ${subtotal}
        </td>
        <td>
          <fmt:formatNumber value="${order.totalQuantity}" var="totalQuantity"/>
          Total quantity: ${totalQuantity}
        </td>
      </tr>
      <tr>
        <td></td>
        <td></td>
        <td>
          <fmt:formatNumber value="${order.deliveryCost}" type="currency" currencyCode="${cart.items[0].product.currency}" var="deliveryCost"/>
          Delivery cost: ${deliveryCost}
        </td>
        <td></td>
      </tr>
      <tr>
        <td></td>
        <td></td>
        <td>
          <fmt:formatNumber value="${order.totalCost}" type="currency" currencyCode="${cart.items[0].product.currency}" var="totalCost"/>
          Total cost: ${totalCost}
        </td>
        <td></td>
      </tr>
    </table>
    <h2>Your information:</h2>
    <table>
      <tags:orderFormRow name="firstName" label="First name" order="${order}" errors="${errors}" />
      <tags:orderFormRow name="lastName" label="Last name" order="${order}" errors="${errors}" />
      <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}" />
      <tr>
        <td>Delivery date<span style="color: red">*</span></td>
        <td>
          <c:set var="error" value="${errors['deliveryDate']}" />
          <input name="deliveryDate" value="${param['deliveryDate']}" />
          <c:if test="${not empty error}">
            <p class="error-message">${error}</p>
          </c:if>
        </td>
      </tr>
      <tags:orderFormRow name="deliveryAddress" label="Delivery address" order="${order}" errors="${errors}" />
      <tr>
        <td>Payment method<span style="color: red">*</span></td>
        <td>
          <c:set var="error" value="${errors['paymentMethod']}" />
          <select name="paymentMethod">
            <option></option>
            <c:forEach var="paymentMethod" items="${paymentMethods}">
              <option <c:if test="${paymentMethod == param['paymentMethod']}">selected</c:if>>${paymentMethod}</option>
            </c:forEach>
          </select>
          <c:if test="${not empty error}">
            <p class="error-message">${error}</p>
          </c:if>
        </td>
      </tr>
    </table>
    <p>
      <button>Place order</button>
    </p>
  </form>
</tags:master>