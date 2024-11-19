<%@page import="model.Perfil"%>
<%@page import="dao.PerfilDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>
    <link rel="stylesheet" href="style/main.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Home</title>
    
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
    </header>
    <nav>
        <%@include file="nav_list.jsp"%>
    </nav>
    <div class="flex">
        <form action="GerenciarUsuario" method="post">
            <input type="hidden" name="acao" value="1">
            <p>
                Nome:<input type="text" name="nome" 
                            maxlength="50" required>
            </p> 
            <p>
                Telefone:<input type="text" name="telefone" 
                                placeholder="(00)90000-0000" maxlength="11" 
                                title="Apenas caracters numericos!" size="11" required>
            </p>
            <p>
                Login:<input type="text" name="login" pattern="[a-z]+" maxlength="6" 
                             title="Apenas caracters alfabéticos minusculos!" size="6" required>
            </p>
            <p>
                Senha:<input type="password" name="senha" maxlength="8" size="8" required>
            </p>
            <p>
                CPF:<input type="text" name="cpf" 
                           placeholder="000.000.000-00" maxlength="11" 
                           title="Apenas caracters numericos!" size="11" required>
            </p>
            <p>
                Email:<input type="email" name="email" placeholder="seuemail@exemplo.com" required>
            </p>
            <p>
                Perfil:
                <select name="perfil">
                    <% for (Perfil p : PerfilDAO.listar()) {%> 
                    <% if (!p.getNome().equals("Admin") && p.isStatus() != false) {%>

                    <option value="<%= p.getId()%>"><%= p.getNome()%></option>

                    <% }%> 
                    <% }%>
                </select>
            </p>
            <p>
                <input type="submit" value="Cadastrar Usuario">
            </p>
        </form> 
    </div>
    <footer>
        <p> &copy; Samaforte - Natanel</p>
    </footer>
</body>
</html>