<%--
  Created by IntelliJ IDEA.
  User: dmitry
  Date: 8.10.21
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2 style="text-align: center">Meals</h2>
<table style="margin: auto" border="1">
    <tbody>
    <tr>
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Action</th>
    </tr>
    <%--@elvariable id="mealsList" type="java.util.List"--%>
    <c:forEach var="meal" items="${mealsList}">
        <tr style="color: ${meal.excess ? 'Red' : 'LimeGreen'}; text-align: center">
            <td>
                <c:out value="${meal.dateTime.format(DateTimeFormatter.ofPattern(\"dd.MM.yyyy HH:mm\"))}"/>
            </td>
            <td>
                <c:out value="${meal.description}"/>
            </td>
            <td>
                <c:out value="${meal.calories}"/>
            </td>
            <td>
                <a href="meals?action=edit&mealId=<c:out value="${meal.mealToId}"/>">Update</a>
            </td>
            <td>
                <a href="meals?action=delete&mealId=<c:out value="${meal.mealToId}"/>">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p style="margin: auto"><a href="meals?action=add">Add Meal</a></p>
</body>
</html>
