<%--
  Created by IntelliJ IDEA.
  User: dmitry
  Date: 9.10.21
  Time: 16:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <title>Add new meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Add/Update Meal</h2>
<br>
<%--@elvariable id="meal" type="ru.javawebinar.topjava.model.Meal"--%>
<form method="post" action="meals" name="frmAddMeal">
    <table>
        <tbody>
        <tr>
            <td> ID:</td>
            <td><label>
                <input type="text" readonly="readonly" name="mealId" value="<c:out value="${meal.mealId}"/>">
            </label></td>
        </tr>
        <tr>
            <td> Date and Time:</td>
            <td><label>
                <input type="datetime-local" name="dateTime"
                       value="<c:out value="${meal.dateTime.format(DateTimeFormatter.ofPattern(\"yyyy-MM-dd'T'HH:mm\"))}"/>">
            </label></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><label>
                <input type="text" name="description" value="<c:out value="${meal.description}"/>">
            </label></td>
        </tr>
        <tr>
            <td>Calories:</td>
            <td><label>
                <input type="text" name="calories" value="<c:out value="${meal.calories}"/>">
            </label></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit"></td>
        </tr>
        </tbody>
    </table>
</form>
<br>
<h2><a href="meals">Cancel</a></h2>
</body>
</html>
