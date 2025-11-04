<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="sessao.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>

    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css">
    
    <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

    <script defer src="script/tabela.js"> </script>
    <script defer src="script/cadastrar_alterar.js"></script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="stylesheet" href="style/cadastrar_alterar.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Cadastrar Cliente</title>
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
        <h1 class="titulo">
            Cadastrar Cliente
        </h1>
        <form action="GerenciarCliente" method="post">
            <input type="hidden" name="acao" value="1">
            <div class="campos">
                <label for="nome" class="titulo_campo">Nome: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="nome" title="Apenas caracteres alfabéticos!" required>
            </div>

            <div class="campos">
                <label for="telefone" class="titulo_campo">Telefone: <abbr title="Campo obrigatório" style="color: red; font-weight: bolder; text-decoration: none;">*</abbr></label>
                <input type="text" name="telefone" title="Apenas caracteres numéricos!" required>
            </div>

            <div class="campos">
                <label for="cpf" class="titulo_campo">CPF:</label>
                <input type="text" name="cpf">
            </div>

            <div class="campos">
                <label for="endereco" class="titulo_campo">Endereço:</label>
                <input type="text" name="endereco">
            </div>

            <div style="display: flex; gap: 10px; margin: 20px;">
                <button type="button" class="botao_cancela" onclick="location.href = 'clientes.jsp'">Cancelar</button>
                <input type="submit" value="Cadastrar Dados" class="botao_confirma">
            </div>
        </form>
    </div>
</body>
</html>
