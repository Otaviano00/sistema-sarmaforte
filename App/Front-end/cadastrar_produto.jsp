<%@page import="model.Produto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="sessao.jsp" %>
<%@include file="infoAdmin.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css?family=Karla" rel="stylesheet">

    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css">
    
    <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="stylesheet" href="style/cadastrar_alterar.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Cadastrar Produto</title>
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
        <h1 class="titulo">Cadastrar Produto</h1>
        <form action="GerenciarProduto" method="post">
            <input type="hidden" name="acao" value="1">
            <div class="campos">
                <label for="imagem" class="titulo_campo">Imagem:</label>
                <input type="file" name="imagem" style="border: none; padding-top: 10px;">
            </div>
            <div class="campos">
                <label for="nome" class="titulo_campo">Nome: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="nome" required>
            </div>

            <div class="campos">
                <label for="descricao" class="titulo_campo">Descrição:</label>
                <textarea name="descricao" rows="3" cols="30"></textarea>
            </div>

            <div class="campos">
                <label for="quantidade" class="titulo_campo">Quantidade: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="number" name="quantidade" min="0" required>
            </div>

            <div class="campos">
                <label for="quantidade_critica" class="titulo_campo">Quantidade Crítica: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="number" name="quantidade_critica" min="0" required>
            </div>

            <div class="campos">
                <label for="preco" class="titulo_campo">Preço (R$): <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="number" name="preco" step="0.01" min="0" required>
            </div>

            <div class="campos">
                <label for="custo" class="titulo_campo">Custo (R$): <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="number" name="custo" step="0.01" min="0">
            </div>

            <div class="campos">
                <label for="fornecedor" class="titulo_campo">Fornecedor: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="fornecedor" required>
            </div>

            <div style="display: flex; gap: 10px; margin: 20px;">
                <button type="button" class="botao_cancela" onclick="location.href = 'produtos.jsp'"> Cancelar</button>
                <input type="submit" value="Cadastrar Dados" class="botao_confirma">
            </div>
        </form>
    </div>
</body>
</html>
