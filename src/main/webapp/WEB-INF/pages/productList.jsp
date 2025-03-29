<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <c:if test="${not empty param.message and empty error}">
    <p class="success-message">${param.message}</p>
  </c:if>
  <c:if test="${not empty error}">
    <p class="error-message">Error were found</p>
  </c:if>
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
          <tags:sortButton field="description" order="asc"/>
          <tags:sortButton field="description" order="desc"/>
        </td>
        <td class="price">
          Price
          <tags:sortButton field="price" order="asc"/>
          <tags:sortButton field="price" order="desc"/>
        </td>
        <td>Quantity</td>
        <td></td>
        <td style="border-bottom: none; border-right: none; border-top: none;"></td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
            ${product.description}
          </a>
        </td>
        <td class="price">
          <a href="?sort=${param.sort}&order=${param.order}&searchingQuery=${param.searchingQuery}&price=${product.id}#price-${product.id}">
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </a>
        </td>
        <form method="post">
          <td>
            <input class="numeric-val ${not empty error and id eq product.id ? 'error-input' : ''}" name="quantity" value="${not empty error and id eq product.id ? param.quantity : 1}"/>
            <c:if test="${param.id eq product.id and empty error}">
              <p class="success-message">Successfully added ${param.count} item(s)</p>
            </c:if>
            <c:if test="${id eq product.id and not empty error}">
              <p class="error-message">${error.message}</p>
            </c:if>
          </td>
          <td>
            <input type="hidden" name="productId" value="${product.id}"/>
            <input type="hidden" name="searchingQuery" value="${param.searchingQuery}"/>
            <input type="hidden" name="sort" value="${param.sort}"/>
            <input type="hidden" name="order" value="${param.order}"/>
            <button>Add to cart</button>
          </td>
        </form>
        <td style="border-bottom: none; border-right: none; border-top: none;">
          <c:if test="${product.id == param.price}">
            <div class="price-history-form" id="price-${product.id}">
              <div class="price-history-content">
                <p>Price history: ${product.description}</p>
                  <c:forEach var="history" items="${product.priceHistory}">
                    <p>
                      <fmt:formatDate value="${history.date}" pattern="dd MMM yyyy" />
                      -
                      <fmt:formatNumber value="${history.price}" type="currency" currencySymbol="${product.currency.symbol}" />
                    </p>
                  </c:forEach>
              </div>
            </div>
          </c:if>
        </td>
      </tr>
    </c:forEach>
  </table>
  <tags:recentlyViewedProducts/>
</tags:master>