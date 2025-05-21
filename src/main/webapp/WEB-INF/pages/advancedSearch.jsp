<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Advanced search
  </p>
  <form>
    <table>
      <tr>
        <td>Description:</td>
        <td>
          <input name="description" value="${param.description}"/>
        </td>
        <td>
          <select name="searchMode" id="searchMode">
            <option value="all words">all words</option>
            <option value="any word">any word</option>
          </select>
        </td>
      </tr>
      <tr>
        <td>Min price:</td>
        <td>
          <input class="numeric-val ${not empty minPriceError ? 'error-input' : ''}" name="minPrice" value="${param.minPrice}"/>
          <c:if test="${not empty minPriceError}">
            <p class="error-message">${minPriceError}</p>
          </c:if>
        </td>
      </tr>
      <tr>
        <td>Max price:</td>
        <td>
          <input class="numeric-val ${not empty maxPriceError ? 'error-input' : ''}" name="maxPrice" value="${param.maxPrice}"/>
          <c:if test="${not empty maxPriceError}">
            <p class="error-message">${maxPriceError}</p>
          </c:if>
        </td>
      </tr>
    </table>
    <button>Search</button>
  </form>
  <table style="display: ${not empty products ? 'block' : 'none'}">
      <thead>
        <tr>
          <td>Image</td>
          <td>
            Description
          </td>
          <td class="price">
            Price
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
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
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