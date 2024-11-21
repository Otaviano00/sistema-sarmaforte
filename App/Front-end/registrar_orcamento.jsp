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
        <link rel="stylesheet" href="style/orcamento.css">
        <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">

        <title>Home</title>
    </head>
    <body>
        <%
            int id = Integer.parseInt(request.getParameter("id"));
            String acaoOrcamento = request.getParameter("acao");
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
                    %>
                    <option value="<%= produto.getCodigo()%>" data-preco="<%= produto.getPreco()%>">
                        <%= produto.getNome()%>
                    </option>
                    <% }%>
                </select>
                <button id="adicionarItem()" onclick="adicionarItem()">
                    ADICIONAR ITEM
                </button>
            </section>
            <div style="display: flex; justify-content: left; align-items: flex-start; width: calc(97% - 20px); margin: 10px; margin-left: -40px; box-sizing: border-box; ">
                <section id="dados_orcamento" class="bloco">
                    <input type="hidden" id="id_orcamento" value="<%= id%>">
                    <input type="hidden" id="acao_orcamento" value="<%= acaoOrcamento%>">

                    <section id="dados_cliente">
                        <input type="hidden" id="id_cliente" value="<%= orcamento.getCliente().getId()%>">
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
                                }%>
                        </select>
                        
                        <a href="#" onclick="mudarCliente()">
                            MUDAR CLIENTE
                        </a>
                        <a href="cadastrar_cliente.jsp?acao=voltar">
                            CADASTRAR NOVO CLIENTE
                        </a>

                        <br>                     
                        <div class="campo_cliente">
                            <label>Nome:</label>
                            <p><%= orcamento.getCliente().getNome() != null? orcamento.getCliente().getNome() : "---"%></p>
                        </div>
                        <div class="campo_cliente">
                            <label>Telefone:</label>
                            <p><%= orcamento.getCliente().getTelefone() != null? orcamento.getCliente().getTelefone() : "---"%></p>
                        </div>
                        <div class="campo_cliente">
                            <label>CPF:</label>
                            <p><%= orcamento.getCliente().getCpf() != null? orcamento.getCliente().getCpf() : "---"%></p>
                        </div>
                        <div class="campo_cliente">
                            <label>Endereço:</label>
                            <p><%= orcamento.getCliente().getEndereco() != null? orcamento.getCliente().getEndereco() : "---"%></p>
                        </div>
                    </section>
                        <form action="GerenciarOrcamento" method="POST">
                            <input type="hidden" name="acao" value="adicionarInformacao">
                            <input type="hidden" name="acaoOrcamento" value="<%= acaoOrcamento%>">
                            <input type="hidden" name="id_orcamento" value="<%= id%>">
                            <textarea name="informacao" id="" style="width: 100%; max-width: 100%; min-width: 100%; height: 150px; max-height: 150px; min-height: 150px;"><%= orcamento.getInformacao() == null? " " : orcamento.getInformacao().trim()%></textarea>
                            <input type="submit" value="Adicionar Informação">
                        </form>
                    <section id="dados_vendedor">
                    </section>
                </section>
                <section id="listar_itens" class="tabela bloco">
                    <dialog>
                        <form action="GerenciarOrcamento" method="get">
                            <input type="hidden" name="acao" id="acao_item" value="adicionarItem">
                            <input type="hidden" name="acao_orcamento" value="<%= acaoOrcamento%>">
                            
                            <input type="hidden" name="id_produto" id="produto_id">
                            <input type="hidden" name="id_orcamento" value="<%= id%>">
                            <input type="hidden" name="id_item" id="id_item" value="0">

                            <input type="text" name="nome_produto" id="nome_produto" readonly disabled>
                            <input type="number" name="preco_produto" id="preco_produto" readonly disabled> 
                            <input type="number" name="quantidade_produto" id="quantidade_produto" required>
                            <input type="submit" value="Confirmar">
                        </form>
                        <button onclick="closeModal()">
                            Fechar
                        </button>
                    </dialog>
                    <table class="table table-striped" style="background-color: white;">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Opções</th>
                                <th>Código</th>
                                <th>Nome</th>
                                <th>Quantidade</th>
                                <th>Preço Unitário</th>
                                <th>Valor</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<ItemOrcamento> itens = OrcamentoDAO.listarItensOrcamento(id);

                                double total = 0;
                                for (int i = 0; i < itens.size(); i++) {
                                    double precoTotal = itens.get(i).getPreco() * itens.get(i).getQuantidade();

                                    total += precoTotal;
                            %>
                                <tr>
                                    <td>
                                        <%= i+1%>
                                    </td>
                                    <td>
                                        <button onclick="alterarItem(parseInt(<%= i%>), parseInt(<%= itens.get(i).getId()%>))" class="botao_acao" title="Alterar <%= itens.get(i).getProduto().getNome()%>">
                                            <img src="images/icone_alterar.svg" alt="Alterar">
                                        </button>
                                        <button onclick="location.href = 'GerenciarOrcamento?acao=excluirItem&idOrcamento=<%= itens.get(i).getOrcamento().getId()%>&idItem=<%= itens.get(i).getId()%>&acaoOrcamento=<%= acaoOrcamento%>'" class="botao_acao" title="Excluir <%= itens.get(i).getProduto().getNome()%>">
                                            <img src="images/icone_excluir.svg" alt="Excluir">
                                        </button>
                                    </td>
                                    <td>
                                        <%= itens.get(i).getProduto().getCodigo()%>
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
                                </tr>
                            <%
                                }
                            %>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="5" >
                                    TOTAL
                                </td>
                                <td colspan="2">
                                    <%= String.format("R$ %,.2f", total)%>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                    
                </section>
            </div> 

            <%
                if (acaoOrcamento.equals("registrar")) {
            %>

                <section id="finalizar" class="bloco">
                    <button onclick="location.href = 'registrar_venda.jsp'">
                        Realizar Venda
                    </button>
                    <button onclick="location.href = 'orcamentos.jsp'">
                        Guardar Orçamento
                    </button>
                    <button onclick="location.href = 'orcamentos.jsp'">
                        Imprimir Orçamento
                    </button>
                    <span>aaa</span>
                    <button onclick="cancelarOrcamento(parseInt(<%= id%>))">
                        Cancelar
                    </button>
                </section>
            <%
                } else if (acaoOrcamento.equals("alterar")) {
            %>
                <section id="finalizar" class="bloco">
                    <button>
                        Realizar Venda
                    </button>
                    <button>
                        Imprimir Orçamento
                    </button>
                    <button onclick="location.href = 'orcamentos.jsp'">
                        Voltar
                    </button>
                </section>
            <%
                } else {
            %>
            <section id="finalizar" class="bloco">
                <button onclick="location.href = 'orcamentos.jsp'">
                    Voltar
                </button>
            </section>
            <%
                }
            %>
        </div>
    </body>
</html>