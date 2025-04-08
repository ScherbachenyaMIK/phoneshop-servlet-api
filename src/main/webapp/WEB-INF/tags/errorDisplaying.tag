<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="error" required="true" %>

<c:if test="${not empty error}">
  <p class="error-message">Error(s) were found</p>
</c:if>