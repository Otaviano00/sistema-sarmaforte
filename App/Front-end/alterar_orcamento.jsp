<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="model.Produto"%>
<%@page import="dao.ProdutoDAO"%>
<%@page import="model.Cliente"%>
<%@page import="dao.ClienteDAO"%>
<%@page import="model.Orcamento"%>
<%@page import="dao.OrcamentoDAO"%>
<%@page import="model.ItemOrcamento"%>
<%@page import="utilities.Util" %>

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

        <script defer src="script/orcamento.js"></script>
        <script defer src="script/tabela.js"> </script>

        <link rel="stylesheet" href="style/main.css">
        <link rel="stylesheet" href="style/cadastrar_alterar.css">
        <link rel="stylesheet" href="style/orcamento.css">
        <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">

        <title>Alterar Orçamento</title>
    </head>
    <body>
        <%
            int id = Integer.parseInt(request.getParameter("id"));
            Orcamento orcamento = OrcamentoDAO.listarPorId(id);
        %>
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
            <section id="buscar_item" class="bloco">
                <input type="hidden" id="id_produto" name="AREIA LAVADA MEDIA SACO 20 KG" data-preco="6.9" value="2">
                <select id="seletor_produto" name="seletor_produto" class="seletor">
                    <%
                        List<Produto> produtos = ProdutoDAO.listar();
                        for (Produto produto : produtos) {
                            if (produto.isStatus()) {
                    %>
                    <option value="<%= produto.getCodigo()%>" data-preco="<%= produto.getPreco()%>">
                        <%= produto.getNome()%>
                    </option>
                    <% 
                            }
                        }
                    %>
                </select>
                <button onclick="adicionarItem()">
                    ADICIONAR ITEM
                </button>
            </section>
            <div class="conjunto">
                <section id="dados_orcamento" class="bloco">
                    <h2>Dados do Cliente</h2>
                    <form method="post" action="GerenciarOrcamento" id="dados_cliente" style="margin-top: -20px;">
                        <input type="hidden" name="id_orcamento" id="id_orcamento" value="<%= id%>">
                        <input type="hidden" id="id_cliente" value="<%= orcamento.getCliente().getId()%>">
                        <input type="hidden" name="acao" value="4">


                        <div style="display: flex; flex-direction: row; width: 100%; min-width: 100%;">
                            <div class="campos">
                                <select id="seletor_cliente" name="seletor_cliente" class="seletor" required>
                                    <%
                                        List<Cliente> clientes = ClienteDAO.listar();
                                        for (Cliente cliente : clientes) {
                                
                                         if (cliente.getId() == orcamento.getCliente().getId()) {
                                    %>
                                        <option value="<%= cliente.getId()%>" selected>
                                            <%= cliente.getNome()%>
                                        </option>
                                    <%
                                            } else {
                                    %>
                                        <option value="<%= cliente.getId()%>">
                                            <%= cliente.getNome()%>
                                        </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            
                            <button type="button" onclick="location.href='cadastrar_cliente.jsp'" title="Cadastrar Novo Cliente" style="font-size: 1.5em;">
                                Novo
                            </button>
                        </div>

                        <br>                     
                        <div class="campo_cliente campos">
                            <label>Nome:</label>
                            <input type="text" value="<%= orcamento.getCliente().getNome()%>" disabled readonly>
                        </div>
                        <div class="campo_cliente campos">
                            <label>Telefone:</label>
                            <input type="text" value="<%= orcamento.getCliente().getTelefone()%>" disabled readonly>
                        </div>
                        <div class="campo_cliente campos">
                            <label>CPF:</label>
                            <input type="text" value="<%= orcamento.getCliente().getCpf() != null? orcamento.getCliente().getCpf() : "---"%>" disabled readonly>
                        </div>
                        <div class="campo_cliente campos">
                            <label>Endereço:</label>
                            <input type="text" value="<%= orcamento.getCliente().getEndereco() != null? orcamento.getCliente().getEndereco() : "---"%>" disabled readonly>
                        </div>
                        <br>
                        <div class="campos">
                            <label for="informacao"> Informações:</label>
                            <textarea name="informacao" id="informacao"><%= orcamento.getInformacao() == null? "" : orcamento.getInformacao().trim()%></textarea>
                        </div>
                        <button style="font-size: 1.4em;">
                            Atualizar Informações
                        </button>
                    </form>
                </section>
                <section id="listar_itens" class="tabela bloco">
                    <h2>Itens do Orçamento</h2>
                    <dialog>
                        <div class="modal">
                            <form action="GerenciarOrcamento" method="get">
                                <input type="hidden" name="acao" id="acao_item" value="5">
                            
                                <input type="hidden" name="id_produto" id="produto_id">
                                <input type="hidden" name="id_orcamento" value="<%= id%>">
                                <input type="hidden" name="id_item" id="id_item" value="0">
                                <div class="campos">
                                    <label for="nome_produto" class="titulo_campo">Produto: </label>
                                    <input type="text" name="nome_produto" id="nome_produto" readonly disabled>
                                </div>
                                <div class="campos">
                                    <label for="preco_produto" class="titulo_campo">Preço (R$): </label>      
                                    <input type="number" name="preco_produto" step="0.01" id="preco_produto" required>       
                                </div>
                                <div class="campos">
                                    <label for="quantidade_produto" class="titulo_campo">Quantidade:</label>
                                    <input type="number" name="quantidade_produto" id="quantidade_produto" required>
                                </div>
                                <input type="submit" value="Confirmar" class="botao_confirma">
                            </form>
                            <button id="fechar" onclick="closeModal()" class="botao_fechar">           
                                X
                            </button>
                        </div>
                    </dialog>
                    <table class="table table-striped" style="background-color: white;">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Código</th>
                                <th>Data</th>
                                <th>Nome</th>
                                <th>Quantidade</th>
                                <th>Preço Unitário</th>
                                <th>Valor</th>
                                <th>Status</th>
                                <th>Opções</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<ItemOrcamento> itens = OrcamentoDAO.listarItensOrcamento(id);

                                double total = 0;
                                for (int i = 0; i < itens.size(); i++) {
                                    double precoTotal = itens.get(i).getPreco() * itens.get(i).getQuantidade();

                                    total += precoTotal;

                                    String data = Util.converteData(itens.get(i).getDataHora().toLocalDate());
                                    String hora = Util.converteHora(itens.get(i).getDataHora());
                                    String dataHora = data + " - " + hora;
                            %>
                                <tr>
                                    <td>
                                        <%= i+1%>
                                    </td>
                                    <td>
                                        <%= itens.get(i).getProduto().getCodigo()%>
                                    </td>
                                    <td>
                                        <%= dataHora%>
                                    </td>
                                    <td>
                                        <%= itens.get(i).getProduto().getNome()%>
                                    </td>
                                    <td>
                                        <%= itens.get(i).getQuantidade()%>
                                    </td>
                                    <td>
                                        <%= String.format("%,.2f", itens.get(i).getPreco())%>
                                    </td>
                                    <td>
                                        <%= String.format("%,.2f", precoTotal)%>
                                    </td>
                                    <td>
                                        <%= itens.get(i).isStatusVenda()? "Vendido" : "Não Vendido"%>
                                    </td>
                                    <td>
                                        <button onclick="alterarItem(parseInt('<%= i%>'), parseInt('<%= itens.get(i).getId()%>'))" class="botao_acao" title="Alterar <%= itens.get(i).getProduto().getNome()%>">
                                            <img src="images/icone_alterar.svg" alt="Alterar">
                                        </button>
                                        <button onclick="location.href = 'GerenciarOrcamento?acao=7&idOrcamento=<%= itens.get(i).getOrcamento().getId()%>&idItem=<%= itens.get(i).getId()%>'" class="botao_acao" title="Excluir <%= itens.get(i).getProduto().getNome()%>">
                                            <img src="images/icone_excluir.svg" alt="Excluir">
                                        </button>
                                    </td>
                                </tr>
                            <%
                                }
                            %>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="6" >
                                    TOTAL
                                </td>
                                <td colspan="3">
                                    <%= String.format("R$ %,.2f", total)%>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                    
                </section>
            </div> 

            <section id="finalizar" class="bloco" >
                <div style="position: absolute; left: 0;">
                    <button class="botao_cancela" onclick="location.href = 'orcamentos.jsp'" >
                        Voltar
                    </button>
                    <button class="botao_cancela" onclick="confirmarExclusao(event, 'GerenciarOrcamento?id=<%= id%>&acao=3')" >
                        Excluir
                    </button>
                </div>
                <button class="botao_confirma" id="botao_imprime" onclick="location.href = 'orcamentos.jsp'">
                    Imprimir Orçamento
                </button>
                <button class="botao_confirma" id="botao_venda" onclick="location.href = 'registrar_venda.jsp?id=<%= id%>'">
                    Realizar Venda
                </button>     
            </section>
        </div>
    </body>
</html>