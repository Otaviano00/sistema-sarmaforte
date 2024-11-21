<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Menu"%>
<%@page import="dao.MenuDAO"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>

    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css">
    
    <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

    <script defer src="script/tabela.js"> </script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Alterar Menu</title>
    
</head>
<body>
    <header>
         <div class="logo">
            <img id="bloco1_logo" src="images/blocos_esquerda.svg" alt="blocos">
            <a href="home.jsp">
                <img id="samaforte" src="images/logo.svg" alt="logo da Samaforte">
            </a>
            <img id="bloco2_logo" src="images/blocos_direita.svg" alt="">
        </div>
        <%@include file="infoLogin.jsp" %>
        <%@include file="infoAdmin.jsp" %>
    </header>
    <nav>
        <%@include file="nav_list.jsp"%>
    </nav>
    <div class="flex">
        <%
            int id = Integer.parseInt(request.getParameter("id"));
            Menu menu = MenuDAO.listarPorId(id);
        %>
        <h1 class="titulo">
            Alterar Menu
        </h1>
        <form action="GerenciarMenu" method="post">
            <input type="hidden" name="acao" value="2">
            <input type="hidden" name="status" value="<%= menu.isStatus()%>">
            <input type="hidden" name="id" value="<%= menu.getId()%>">
            <p>Nome: <input type="text" name="nome" maxlength="10" title="Apenas caracters alfabéticos!" size="8" value="<%= menu.getNome()%>" required></p>
            <p>Link:<input type="text" name="link" placeholder="pagina.jsp" value="<%= menu.getLink()%>"
                           size="15" title="Apenas caracters alfabéticos minusculos!"  required></p>
            <p>Imagem:<input type="file" name = "imagem" ></p>
            <input type="submit" value="Alterar Menu">

        </form>
    </div>
</body>
</html>

