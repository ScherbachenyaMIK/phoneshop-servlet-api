<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Advanced search
  </p>
  <tags:successDisplaying error="${error}"/>
  <tags:errorDisplaying error="${error}"/>
  <form>
    <table>
      <tr>
        <td>Description:</td>
        <td>
          <input class="${not empty error ? 'error-input' : ''}" name="description" value="${not empty error ? param.description : ''}"/>
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
          <input class="numeric-val ${not empty error ? 'error-input' : ''}" name="minPrice" value="${not empty error ? param.minPrice : ''}"/>
        </td>
      </tr>
      <tr>
        <td>Max price:</td>
        <td>
          <input class="numeric-val ${not empty error ? 'error-input' : ''}" name="maxPrice" value="${not empty error ? param.maxPrice : ''}"/>
        </td>
      </tr>
    </table>
    <button>Search</button>
  </form>
  <tags:recentlyViewedProducts/>
</tags:master>