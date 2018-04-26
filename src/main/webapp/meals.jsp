<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <h2>Meals</h2>
    <a href="meals?action=create">Add Meal</a>
    <hr/>
    <form action="meals" method="post">
        <label for="sDate">Enter a start date</label>
        <input id="sDate" type="date" name="startDate">
        <label for="eDate">Enter an end date</label>
        <input id="eDate" type="date" name="endDate">
        <label for="sTime">Enter a start time</label>
        <input id="sTime" type="time" name="startTime">
        <label for="eTime">Enter an end time</label>
        <input id="eTime" type="time" name="endTime">
        <input type="submit" value="Filter" name="filter">
    </form>


    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th colspan="2">Action</th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>