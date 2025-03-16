<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="order" required="true" %>

<a href="?sort=${field}&order=${order}&searchingQuery=${param.searchingQuery}" style="text-decoration: none; color: black;">
  <c:choose>
    <c:when test="${param.sort eq field and param.order eq order}">
      <c:choose>
        <c:when test="${order eq 'asc'}">&#9650;</c:when>
        <c:otherwise>&#9660;</c:otherwise>
      </c:choose>
    </c:when>
    <c:otherwise>
      <c:choose>
        <c:when test="${order eq 'asc'}">&#9651;</c:when>
        <c:otherwise>&#9661;</c:otherwise>
      </c:choose>
    </c:otherwise>
  </c:choose>
</a>