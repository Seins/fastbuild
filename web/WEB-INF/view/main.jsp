<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/8/4
  Time: 10:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>FastBuild</title>
    <meta http-equiv="Content-Type" charset="utf-8" content="text/html">
    <meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=edge" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/fastbuild/css/fastbuild.css" >
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/banner/css/style.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/banner/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/banner/js/slider.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/jquery.json.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/fastbuild/js/fastbuild.js"></script>
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/cloud_favicon.ico" type="image/gif" >
    <style>
        body{
            padding-top: 63px;
        }
    </style>
</head>
<body>
    <jsp:include page="tpl/header/nav-top.jsp"></jsp:include>
    <div id="banner_tabs" class="flexslider">
        <ul class="slides">
            <li>
                <a title="" target="_blank" href="#">
                    <img width="1920" height="482" alt="" style="background: url('${pageContext.request.contextPath}/resources/banner/images/banner1.jpg') no-repeat center;" src="${pageContext.request.contextPath}/resources/banner/images/alpha.png">
                </a>
            </li>
            <li>
                <a title="" href="#">
                    <img width="1920" height="482" alt="" style="background: url('${pageContext.request.contextPath}/resources/banner/images/banner2.jpg') no-repeat center;" src="${pageContext.request.contextPath}/resources/banner/images/alpha.png">
                </a>
            </li>
            <li>
                <a title="" href="#">
                    <img width="1920" height="482" alt="" style="background: url('${pageContext.request.contextPath}/resources/banner/images/banner3.jpg') no-repeat center;" src="${pageContext.request.contextPath}/resources/banner/images/alpha.png">
                </a>
            </li>
        </ul>
        <ul class="flex-direction-nav">
            <li><a class="flex-prev" href="javascript:;">Previous</a></li>
            <li><a class="flex-next" href="javascript:;">Next</a></li>
        </ul>
        <ol id="bannerCtrl" class="flex-control-nav flex-control-paging">
            <li><a>1</a></li>
            <li><a>2</a></li>
            <li><a>2</a></li>
        </ol>
    </div>
    <div style="min-height: 150px;display: block;">

    </div>
    <script type="text/javascript">
            $(function() {
                var bannerSlider = new Slider($('#banner_tabs'), {
                    time: 5000,
                    delay: 400,
                    event: 'hover',
                    auto: true,
                    mode: 'fade',
                    controller: $('#bannerCtrl'),
                    activeControllerCls: 'active'
                });

                $('#banner_tabs .flex-prev').click(function() {
                    bannerSlider.prev()
                });

                $('#banner_tabs .flex-next').click(function() {
                    bannerSlider.next()
                });
            })
</script>
<jsp:include page="tpl/footer/footer_default.jsp"></jsp:include>
</body>
</html>
