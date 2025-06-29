<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Menu"%>
<%@page import="dao.MenuDAO"%>

<%@include file="sessao.jsp" %>
<%@include file="infoAdmin.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css?family=Karla" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css">
    
    <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

    <script defer src="script/tabela.js"> </script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="stylesheet" href="style/cadastrar_alterar.css">
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
    </header>
    <nav>
        <%@include file="nav_list.jsp"%>
    </nav>
    <div class="flex">
        <%
            int id = Integer.parseInt(request.getParameter("id"));
            Menu menu = MenuDAO.listarPorId(id);
        %>
        <h1 class="titulo">Alterar Menu</h1>
        <form action="GerenciarMenu" method="post">
            <input type="hidden" name="acao" value="2">
            <input type="hidden" name="id" value="<%= menu.getId() %>">

            <div class="campos">
                <label for="imagem" class="titulo_campo">Imagem:</label>
                <input type="file" name="imagem" style="border: none; padding-top: 10px;">
            </div>
            
            <div class="campos">
                <label for="nome" class="titulo_campo">Nome: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="nome" value="<%= menu.getNome() %>" required maxlength="100">
            </div>
            
            <div class="campos">
                <label for="link" class="titulo_campo">Link: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="link" value="<%= menu.getLink() %>" placeholder="pagina.jsp" required maxlength="100">
            </div>

            <div class="campos" style="gap: 5px;">
                <label for="status" class="titulo_campo">Status:</label>
                <div style="display: flex; justify-content: start; align-items: start; gap: 10px;">
                    <label for="ativo" class="botao_radio">Ativo
                        <input type="radio" name="status" id="ativo" value="true" <%= menu.isStatus() ? "checked" : "" %> >
                    </label>
                    <label for="desativo" class="botao_radio">Desativo
                        <input type="radio" name="status" id="desativo" value="false" <%= !menu.isStatus() ? "checked" : "" %> >
                    </label>
                </div>
            </div>

            <div style="display: flex; gap: 10px; margin: 20px;">
                <button type="button" class="botao_cancela" onclick="location.href = 'menus.jsp'"> Cancelar</button>
                <input type="submit" value="Alterar Menu" class="botao_confirma">
            </div>
        </form>
    </div>
</body>
</html>
