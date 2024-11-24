<%@page import="model.Produto"%>
<%@page import="dao.ProdutoDAO"%>
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

    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css">
    
    <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="stylesheet" href="style/cadastrar_alterar.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Alterar Produto</title>
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
        <h1 class="titulo">Alterar Produto</h1>
        <% 
            Produto produto = ProdutoDAO.listarPorId(Integer.parseInt(request.getParameter("codigo"))); 
        %>
        <form action="GerenciarProduto" method="post">
            <input type="hidden" name="acao" value="2"> 
            <input type="hidden" name="codigo" value="<%= produto.getCodigo() %>">

            <div class="campos">
                <label for="nome" class="titulo_campo">Nome: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="nome" value="<%= produto.getNome() %>" required>
            </div>

            <div class="campos">
                <label for="descricao" class="titulo_campo">Descrição:</label>
                <textarea name="descricao" rows="3" cols="30"><%= produto.getDescricao() %></textarea>
            </div>

            <div class="campos">
                <label for="quantidade" class="titulo_campo">Quantidade: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="number" name="quantidade" min="0" value="<%= produto.getQuantidade() %>" required>
            </div>

            <div class="campos">
                <label for="quantidadeCritica" class="titulo_campo">Quantidade Crítica: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="number" name="quantidadeCritica" min="0" value="<%= produto.getQuantidadeCritica() %>" required>
            </div>

            <div class="campos">
                <label for="preco" class="titulo_campo">Preço (R$): <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="number" name="preco" step="0.01" min="0" value="<%= produto.getPreco() %>" required>
            </div>

            <div class="campos">
                <label for="custo" class="titulo_campo">Custo (R$): <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="number" name="custo" step="0.01" min="0" value="<%= produto.getCusto() %>" required>
            </div>

            <div class="campos">
                <label for="fornecedor" class="titulo_campo">Fornecedor: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="fornecedor" value="<%= produto.getFornecedor() %>" required>
            </div>

            <div class="campos" style="gap: 5px;">
                <label for="status" class="titulo_campo">Status: </label>
                <div style="display: flex; justify-content: start; align-items: start; gap: 10px;">
                    <label for="ativo"  class="botao_radio">Ativo
                        <input type="radio" name="status" id="ativo" value="true" <%= produto.isStatus()? "checked" : " "%> >
                    </label>
                    <label for="desativo" class="botao_radio">Desativo
                        <input type="radio" name="status" id="desativo" value="false" <%= !produto.isStatus()? "checked" : " "%>>
                    </label>
                </div>
            </div>

            <div style="display: flex; gap: 10px; margin: 20px;">
                <button type="button" class="botao_cancela" onclick="location.href = 'produtos.jsp'"> Cancelar</button>
                <input type="submit" value="Alterar Produto" class="botao_confirma">
            </div>
        </form>
    </div>
</body>
</html>
