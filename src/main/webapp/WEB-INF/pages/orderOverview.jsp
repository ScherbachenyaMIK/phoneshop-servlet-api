<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order Overview">
  <h1>
    Order overview
  </h1>
  <table>
    <thead>
      <tr>
        <th>Image</th>
        <th>Description</th>
        <th>Price</th>
        <th>Quantity</th>
      </tr>
    </thead>
    <c:forEach var="orderItem" items="${order.items}" varStatus="status">
      <tbody>
        <tr>
          <td>
            <img class="product-tile" src="${orderItem.product.imageUrl}">
          </td>
          <td>${orderItem.product.description}</td>
          <td class="price">
            <fmt:formatNumber value="${orderItem.product.price}" type="currency" currencySymbol="${orderItem.product.currency.symbol}"/>
          </td>
          <td class="numeric-val">
            <fmt:formatNumber value="${orderItem.quantity}"/>
        </tr>
      </tbody>
    </c:forEach>
    <tr>
      <td></td>
      <td></td>
      <td>
        <fmt:formatNumber value="${order.subtotal}" type="currency" currencyCode="${order.items[0].product.currency}" var="subtotal"/>
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
        <fmt:formatNumber value="${order.deliveryCost}" type="currency" currencyCode="${order.items[0].product.currency}" var="deliveryCost"/>
        Delivery cost: ${deliveryCost}
      </td>
      <td></td>
    </tr>
    <tr>
      <td></td>
      <td></td>
      <td>
        <fmt:formatNumber value="${order.totalCost}" type="currency" currencyCode="${order.items[0].product.currency}" var="totalCost"/>
        Total cost: ${totalCost}
      </td>
      <td></td>
    </tr>
  </table>
  <h2>Your information:</h2>
  <table>
    <tags:orderOverviewRow name="firstName" label="First name" order="${order}" />
    <tags:orderOverviewRow name="lastName" label="Last name" order="${order}" />
    <tags:orderOverviewRow name="phone" label="Phone" order="${order}" />
    <tags:orderOverviewRow name="deliveryDate" label="Delivery date" order="${order}" />
    <tags:orderOverviewRow name="deliveryAddress" label="Delivery address" order="${order}" />
    <tags:orderOverviewRow name="paymentMethod" label="Payment method" order="${order}" />
    </table>
</tags:master>