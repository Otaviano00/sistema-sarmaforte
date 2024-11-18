<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Perfil"%>
<%@page import="dao.PerfilDAO"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>
    <link rel="stylesheet" href="style/main.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Cadastrar Perfil</title>
    
</head>
<body>
    <%@include file="infoAdmin.jsp" %>
    <header>
         <div class="logo">
            <img id="bloco1_logo" src="images/blocos_esquerda.svg" alt="blocos">
            <a href="home.jsp">
                <img id="samaforte" src="images/logo.svg" alt="logo da Samaforte">
            </a>
            <img id="bloco2_logo" src="images/blocos_direita.svg" alt="">
        </div>
        <%@include file="infoLogin.jsp" %>
    </header>
    <nav>
        <%@include file="nav_list.jsp"%>
    </nav>
    <div class="flex">
        <a href="cadastrar_perfil.jsp">Cadastrar Perfil</a>
        <table  border="1">
            <tr>
                <td>Nome</td>
                <td>Descrição</td>
                <td>Hierarquia</td>
                <td>Status</td>
            </tr>
            <% for (Perfil p : PerfilDAO.listar()) {%>
            <tr>
                <% if (p.getNome().equals("Admin")) {%> 

                <td><%= p.getNome()%></td>
                <td><%= p.getDescricao()%></td>
                <td><%= p.getHierarquia()%></td>
                <td><%= p.isStatus()%></td>

                <% } else {%> 

                    <td><%= p.getNome()%></td>
                    <td><%= p.getDescricao()%></td>
                    <td><%= p.getHierarquia()%></td>
                    <td><%= p.isStatus()%></td>
                    <td><a href="alterar_perfil.jsp?idPerfil=<%= p.getId()%>">Alterar dados</a></td>

                    <% if (p.isStatus() == true) {%>
                        <td><a href="GerenciarPerfil?id=<%= p.getId()%>&valor=4">Desativar Perfil</a></td>
                    <% } else {%>
                        <td><a href="GerenciarPerfil?id=<%= p.getId()%>&valor=3">Ativar Perfil</a></td>
                    <% }%>

                <% }%>
            </tr>
            <% }%>
        </table>
    </div>
    <footer>
        <p> &copy; Samaforte - Natanel</p>
    </footer>
</body>
</html>
