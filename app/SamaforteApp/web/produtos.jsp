<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="sessao.jsp" %>

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

    <script defer src="script/produtos.js"></script>

    <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

    <link rel="stylesheet" href="style/main.css">
    <link rel="stylesheet" href="style/modal.css">
    <link rel="stylesheet" href="style/cadastrar_alterar.css">
    <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">
    <title>Produto</title>
    
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
            PRODUTOS
        </h1>
        <%
            if (hierarquia < 2) {
        %>
        <button class="novo" onclick="openModal('createModal', null, 'create')">
            <div style="display: flex; justify-content: center; align-items: center; margin: auto; gap: 10px;">
                <span style="font-size: 2em;">+</span>
                Novo Produto
            </div>
        </button>
        <%
            } 
        %>
        
        <div class="filtro"> 
            <div class="item-filtro">
                <p>Coluna de pesquisa</p>
                <select id="input-filter">
                        <option value="0"> 
                            Código
                        </option>
                        <option value="1" selected>
                            Nome
                        </option>
                        <option value="2">
                            Fornecedor
                        </option>
                        <option value="3">
                            Quantidade
                        </option>
                        <option value="4">
                            Preço
                        </option>
                </select>
            </div>
            <div class="item-filtro">
                <p>
                    Tipo de pesquisa
                </p>
                <select id="input-type">
                    <option  value="0" selected> 
                            Começa com
                    </option>
                    <option  value="1"> 
                            Inclui
                    </option>
                </select>
            </div>
        </div>
        <div class="tabela">
            <table id="lista-produtos" class="table table-striped" style="background-color: white;">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Código</th>
                        <th>Nome</th>
                        <th>Fornecedor</th>
                        <th>Quantidade</th>
                        <th>Preço</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>

    <!-- Hidden input to store hierarquia value for JavaScript -->
    <input type="hidden" id="hierarquia-value" value="<%= hierarquia %>">

    <!-- Modal de Detalhes -->
    <!-- Modal de Detalhes -->
    <dialog id="detailsModal">
        <div class="modal" id="detailsModalContent"></div>
    </dialog>

    <!-- Modal de Alterar -->
    <dialog id="editModal">
        <div class="modal" id="editModalContent"></div>
    </dialog>

    <!-- Modal de Criar -->
    <dialog id="createModal">
        <div class="modal" id="createModalContent"></div>
    </dialog>

</body>
</html>
