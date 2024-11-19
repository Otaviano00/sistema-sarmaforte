
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
    <title>Cadastrar Perfil</title>
    
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
        <form action="GerenciarPerfil" method="post">
            <input type="hidden" name="valor" value="1">
            <p> Nome <input type="text" name="nome" maxlength="10" title="Apenas caracters alfabéticos!" required></p>
            <p>Descricao</p><textarea name="descricao" rows="3" cols="30"></textarea>
            <p>
                Hierarquia 
                1<input type="radio" name="hierarquia" value="1">
                2<input type="radio" name="hierarquia" value="2" checked>
            </p>

            <p><input type="submit" value="Cadastrar Dados"></p>
        </form>
    </div>
    <footer>
        <p> &copy; Samaforte - Natanel</p>
    </footer>
</body>
</html>