<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>

<tr>
  <td>${label}<span style="color: red">*</span></td>
  <td>
    <c:set var="error" value="${errors[name]}" />
    <input name="${name}" value="${param[name]}" />
    <c:if test="${not empty error}">
      <p class="error-message">${error}</p>
    </c:if>
  </td>
</tr>