<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="searchingQuery" value="${param.searchingQuery}">
    <button>Search</button>
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
          <a href="?sort=description&order=asc&searchingQuery=${param.searchingQuery}", style="text-decoration: none; color: black;">
            <c:choose>
              <c:when test="${param.sort eq 'description' and param.order eq 'asc'}">▲</c:when>
              <c:otherwise>△</c:otherwise>
            </c:choose>
          </a>
          <a href="?sort=description&order=desc&searchingQuery=${param.searchingQuery}", style="text-decoration: none; color: black;">
            <c:choose>
              <c:when test="${param.sort eq 'description' and param.order eq 'desc'}">▼</c:when>
              <c:otherwise>▽</c:otherwise>
            </c:choose>
          </a>
        </td>
        <td class="price">
          Price
          <a href="?sort=price&order=asc&searchingQuery=${param.searchingQuery}", style="text-decoration: none; color: black;">
            <c:choose>
              <c:when test="${param.sort eq 'price' and param.order eq 'asc'}">▲</c:when>
              <c:otherwise>△</c:otherwise>
            </c:choose>
          </a>
          <a href="?sort=price&order=desc&searchingQuery=${param.searchingQuery}", style="text-decoration: none; color: black;">
            <c:choose>
              <c:when test="${param.sort eq 'price' and param.order eq 'desc'}">▼</c:when>
              <c:otherwise>▽</c:otherwise>
            </c:choose>
          </a>
        </td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>${product.description}</td>
        <td class="price">
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>