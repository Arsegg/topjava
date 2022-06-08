<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
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
        <tr style="color:${meal.excess ? "red" : "green"}">
            <td>
                ${meal.dateTime.format(requestScope.dateTimeFormatter)}
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