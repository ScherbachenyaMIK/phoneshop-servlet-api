<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="error" required="true" %>

<c:if test="${not empty param.message and empty error}">
  <p class="success-message">${param.message}</p>
</c:if>