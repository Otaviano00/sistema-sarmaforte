<%@page import="model.Usuario"%>
<%@page import="dao.UsuarioDAO"%>
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
    <title>Cadastrar Usuário</title>
    
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
        <a href="cadastrar_usuario.jsp">Cadastrar Usuario</a>
        <table border="1">
            <% if (nome.equals("Administrador")) {%>
            <tr>
                <td>Nome</td>   
                <td>Telefone</td>   
                <td>Login</td> 
                <td>Senha</td> 
                <td>CPF</td> 
                <td>Email</td>
                <td>Perfil</td>
                <td>Status</td>
            </tr>
            <% for (Usuario u : UsuarioDAO.listar()) {%>
            <tr>
                <% if (u.getPerfil().getNome().equals("Admin")) {%> 

                <td><%= u.getNome()%></td>   
                <td></td>   
                <td><%= u.getLogin()%></td>   
                <td><%= u.getSenha()%></td>   
                <td></td>   
                <td></td>   
                <td><%= u.getPerfil().getNome()%></td>   
                <td><%= u.isStatus()%></td>
                <td><a href="alterar_usuario.jsp?id=<%= u.getId()%>">Alterar Dados</a></td>

                <% } else {%> 

                <td><%= u.getNome()%></td>   
                <td><%= u.getTelefone()%></td>   
                <td><%= u.getLogin()%></td>   
                <td><%= u.getSenha()%></td>   
                <td><%= u.getCpf()%></td>   
                <td><%= u.getEmail()%></td>   
                <td><%= u.getPerfil().getNome()%></td>   
                <td><%= u.isStatus()%></td>
                <td><a href="alterar_usuario.jsp?id=<%= u.getId()%>">Alterar Dados</a></td>
                <% if (u.isStatus() == true) {%>
                <td><a href="GerenciarUsuario?id=<%= u.getId()%>&valor=4">Desativar Usuario</a></td>
                <% } else {%>
                <td><a href="GerenciarUsuario?id=<%= u.getId()%>&valor=3">Ativar Usuario</a></td>
                <% }%>

                <% }%>

            </tr>
            <% }%>
            <% } else {%>
            <tr>
                <td>Nome</td>   
                <td>Telefone</td>   
                <td>Login</td> 
                <td>Senha</td> 
                <td>CPF</td> 
                <td>Email</td>
                <td>Perfil</td>
                <td>Status</td>
            </tr>
            <% for (Usuario u : UsuarioDAO.listar()) {%>
            <tr>
                <% if (!u.getPerfil().getNome().equals("Admin")) {%>

                <td><%= u.getNome()%></td>   
                <td><%= u.getTelefone()%></td>   
                <td><%= u.getLogin()%></td>    
                <td><%= u.getSenha()%></td>    
                <td><%= u.getCpf()%></td>   
                <td><%= u.getEmail()%></td>   
                <td><%= u.getPerfil().getNome()%></td>   
                <td><%= u.isStatus()%></td>
                <td><a href="alterar_usuario.jsp?id=<%= u.getId()%>">Alterar Dados</a></td>
                <% if (u.isStatus() == true) {%>
                <td><a href="GerenciarUsuario?id=<%= u.getId()%>&valor=4">Desativar Usuario</a></td>
                <% } else {%>
                <td><a href="GerenciarUsuario?id=<%= u.getId()%>&valor=3">Ativar Usuario</a></td>
                <% }%>

                <% }%>
            </tr>
            <% }%>
            <% }%>
        </table>
    </div>
    <footer>
        <p> &copy; Samaforte - Natanel</p>
    </footer>
</body>
</html>

