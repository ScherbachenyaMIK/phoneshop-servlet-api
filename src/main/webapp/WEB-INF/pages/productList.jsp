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
          <tags:sortButton field="description" order="asc"/>
          <tags:sortButton field="description" order="desc"/>
        </td>
        <td class="price">
          Price
          <tags:sortButton field="price" order="asc"/>
          <tags:sortButton field="price" order="desc"/>
        </td>
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