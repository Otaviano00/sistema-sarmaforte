<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
        <title>JSP Page</title>
    </head>
    <style>
        .grafico {
            padding: 50px;
            background-color: #F0F0F0;
        }
    </style>
    <body>
        <h1>Hello World!</h1>
        <div class="grafico">
            <%@include file="grafico.jsp"%>
        </div>
        <div class="grafico">
            <%@include file="tabela.html"%>
        </div>
    </body>
</html>
