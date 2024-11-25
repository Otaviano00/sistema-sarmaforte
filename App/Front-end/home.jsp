<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="utilities.Util"%>

<%@include file="sessao.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>

    <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css">
    
    <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

    <script defer src="script/tabela.js"> </script>
    <script defer src="script/cadastrar_alterar.js"></script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="stylesheet" href="style/cadastrar_alterar.css">
    <link rel="stylesheet" href="style/orcamento.css">
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
        <h1 class="titulo">
            DASHBOARD
        </h1>
        <section class="content">
            <section class="dados bloco" id="info_vendas">
                <div>
                    <img src="images/icone_vendas.svg" alt="Vendas">
                    Vendas - 
                    <span><%= Util.pegarMes()%></span>
                    <hr>
                    <%@include file="testes/grafico.jsp" %>
                </div>

            </section>
            <section class="dados bloco" id="info_orcamento">
                <div>
                    <img src="images/icone_orcamento.svg" alt="Orçamentos">
                    Orçamentos - 
                    <span><%= Util.pegarMes()%></span>
                    <hr>
                </div>
            </section>
            <section class="dados bloco" id="info_produtos">
                <div>
                    <img src="images/icone_produto.svg" alt="Produtos">
                    Produtos - 
                    <span><%= Util.pegarMes()%></span>
                    <hr>
                </div>
            </section>
        </section>
    </div>
</body>
</html>