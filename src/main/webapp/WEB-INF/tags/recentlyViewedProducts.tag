<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty recentlyViewed}">
  <h3>Recently Viewed Products</h3>
  <table>
    <thead>
      <tr>
        <th>Image</th>
        <th>Description</th>
     </tr>
    </thead>
    <c:forEach var="product" items="${recentlyViewed}">
      <tbody>
        <tr>
          <td>
            <a href="${pageContext.request.contextPath}/products/${product.id}">
              <img src="${product.imageUrl}" alt="${product.description}" width="50"/>
            </a>
          </td>
          <td>
            <a href="${pageContext.request.contextPath}/products/${product.id}">
              ${product.description}
            </a>
          </td>
        </tr>
      </tbody>
    </c:forEach>
  </table>
</c:if>