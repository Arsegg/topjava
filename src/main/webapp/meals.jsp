<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8"
         import="java.time.format.DateTimeFormatter" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <tr>
        <th>DateTime</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${requestScope.meals}" var="meal">
        <c:set value="green" var="color"/>
        <c:if test="${meal.excess}">
            <c:set value="red" var="color"/>
        </c:if>
        <tr style="color:${color}">
            <td>
                ${meal.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))}
            </td>
            <td>
                ${meal.description}
            </td>
            <td>
                ${meal.calories}
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>