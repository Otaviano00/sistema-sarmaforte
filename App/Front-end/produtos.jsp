<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Produto"%>
<%@page import="dao.ProdutoDAO"%>
<%@page import="java.util.List"%>

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
    
    <script defer src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script defer src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>

    <script defer src="script/tabela.js"> </script>

    <link rel="stylesheet" href="style/main.css">
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
        <button class="novo" onclick="location.href = ('cadastrar_produto.jsp')">
            <div style="display: flex; justify-content: center; align-items: center; margin: auto; gap: 10px;">
                <span style="font-size: 2em;">+</span>
                Novo Produto
            </div>
        </button>
        <div class="tabela">
            <table class="table table-striped" style="background-color: white;">
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
                    <%
                        List<Produto> produtos = ProdutoDAO.listar();
                        for (int i = 0; i < produtos.size(); i++) {%>
                        <tr>
                            <td><%= i+1%></td>
                            <td><%= produtos.get(i).getCodigo()%></td>
                            <td><%= produtos.get(i).getNome()%></td> 
                            <td><%= produtos.get(i).getFornecedor()%></td>   
                            <td><%= produtos.get(i).getQuantidade()%></td>    
                            <td><%= String.format("R$  %,.2f", produtos.get(i).getPreco())%></td>
                            <td>
                                <% if (hierarquia < 2) {%>
                                    <% if (produtos.get(i).isStatus() == true) {%>
                                        <button onclick="location.href = 'GerenciarProduto?codigo=<%= produtos.get(i).getCodigo()%>&acao=4'" class="botao_acao botao_ativo" title="Clique para desativar o produto <%= produtos.get(i).getNome()%>">
                                            Ativo
                                        </button>
                                        
                                    <% } else {%>
                                        <button onclick="location.href = 'GerenciarProduto?codigo=<%= produtos.get(i).getCodigo()%>&acao=3'" class="botao_acao botao_desativo" title="Clique para ativar o produto <%= produtos.get(i).getNome()%>">
                                            Desativo
                                        </button>
                                    <% }%>
                                <%
                                    } else {
                                %>
                                    <button class="botao_acao botao_ativo">
                                        Ativo
                                    </button>
                                <%
                                    }
                                %>
                            </td>  
                            <td>
                                <%
                                    if (hierarquia < 2) {
                                %>
                                    <button onclick="location.href = 'alterar_produto.jsp?codigo=<%= produtos.get(i).getCodigo()%>'" class="botao_acao" title="Alterar dados do produto <%= produtos.get(i).getNome()%>">
                                        <img src="images/icone_alterar.svg" alt="Alterar">
                                    </button>
                                    <button onclick="location.href = 'GerenciarProduto?codigo=<%= produtos.get(i).getCodigo()%>'" class="botao_acao" title="Excluir o produto <%= produtos.get(i).getNome()%>">
                                        <img src="images/icone_excluir.svg" alt="Excluir">
                                    </button>
                                <%
                                    }
                                %>
                                <button onclick="location.href = 'detalhes_produto?codigo=<%= produtos.get(i).getCodigo()%>'" class="botao_acao" title="Detalhes sobre o produto <%= produtos.get(i).getNome()%>">
                                    <img src="images/icone_detalhes.svg" alt="Detalhes">
                                </button>
                            </td>
                        </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
