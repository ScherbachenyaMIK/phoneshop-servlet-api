<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Error">
  <h1>Unexpected error</h1>
  <p>Ooops... Something went wrong!</p>
  <p>Error: ${pageContext.exception.message}</p>
  <a href="${pageContext.servletContext.contextPath}">Return to home page<a>
</tags:master>