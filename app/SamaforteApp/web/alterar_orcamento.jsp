<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="model.Produto"%>
<%@page import="dao.ProdutoDAO"%>
<%@page import="model.Cliente"%>
<%@page import="dao.ClienteDAO"%>
<%@page import="model.Orcamento"%>
<%@page import="dao.OrcamentoDAO"%>
<%@page import="model.Venda"%>
<%@page import="dao.VendaDAO"%>
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

        <script defer src="script/registrar_orcamento.js"></script>

        <link rel="stylesheet" href="style/main.css">
        <link rel="stylesheet" href="style/modal.css">
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
                <input type="hidden" id="id_produto" name="" data-preco="" value="">
                <div style="display: flex; flex-direction: column; gap: 8px; flex: 1;">
                    <label for="seletor_produto" style="font-weight: 600; color: #142E50; font-size: 1.1em;">Selecione o Produto:</label>
                    <select id="seletor_produto" name="seletor_produto" class="seletor">
                        <!-- Produtos serão carregados via JavaScript -->
                    </select>
                </div>
                <button onclick="adicionarItem()" style="align-self: flex-end;">
                    ADICIONAR ITEM
                </button>
            </section>
            <div class="conjunto">
                <section id="dados_orcamento" class="bloco">
                    <h2>Dados do cliente</h2>
                    <form id="dados_cliente">
                        <input type="hidden" name="id_orcamento" id="id_orcamento" value="">
                        <input type="hidden" id="id_cliente" value="">

                        <div style="display: flex; gap: 10px; align-items: flex-end; width: 100%;">
                            <div class="campos" style="flex: 1; margin: 0;">
                                <label for="seletor_cliente">Cliente:</label>
                                <select id="seletor_cliente" name="seletor_cliente" class="seletor" required>
                                    <!-- Clientes serão carregados via JavaScript -->
                                </select>
                            </div>

                            <button type="button" onclick="abrirModalCriarCliente()" title="Cadastrar Novo Cliente" style="height: 50px; margin-bottom: 0;">
                                + Novo
                            </button>
                        </div>
                        <div class="campo_cliente campos">
                            <label>Nome:</label>
                            <input type="text" value="" disabled readonly>
                        </div>
                        <div class="campo_cliente campos">
                            <label>Telefone:</label>
                            <input type="text" value="" disabled readonly>
                        </div>
                        <div class="campo_cliente campos">
                            <label>CPF:</label>
                            <input type="text" value="" disabled readonly>
                        </div>
                        <div class="campo_cliente campos">
                            <label>Endereço:</label>
                            <input type="text" value="" disabled readonly>
                        </div>

                        <div class="campos">
                            <label for="informacao">Informações:</label>
                            <textarea name="informacao" id="informacao"></textarea>
                        </div>
                    </form>
                </section>
                <section id="listar_itens" class="tabela bloco">
                    <h2>Itens do Orçamento</h2>

                    <!-- Modal para ADICIONAR item -->
                    <dialog id="modal_adicionar">
                        <div class="modal">
                            <button class="botao_fechar_modal" onclick="closeModalAdicionar()" title="Fechar">×</button>
                            <div class="modal-content">
                                <div class="modal-body">
                                    <h2>Adicionar item</h2>
                                    <input type="hidden" id="add_produto_id">

                                    <div class="form-group">
                                        <label for="add_nome_produto" class="form-label">Produto</label>
                                        <input type="text" id="add_nome_produto" class="form-input" readonly disabled>
                                    </div>

                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="add_preco_produto" class="form-label">Preço Unitário (R$) <span style="color: red;">*</span></label>
                                            <input type="number" id="add_preco_produto" class="form-input" step="0.001" min="0" onkeypress="handleEnterAdicionar(event)">
                                        </div>

                                        <div class="form-group">
                                            <label for="add_quantidade_produto" class="form-label">Quantidade <span style="color: red;">*</span></label>
                                            <input type="number" id="add_quantidade_produto" class="form-input" step="0.001" min="0.001" onkeypress="handleEnterAdicionar(event)">
                                        </div>
                                    </div>
                                </div>

                                <div class="modal-footer">
                                    <button onclick="closeModalAdicionar()" class="btn btn-secondary">Cancelar</button>
                                    <button onclick="confirmarAdicionar()" class="btn btn-primary">
                                        <span>+</span> Adicionar Item
                                    </button>
                                </div>
                            </div>
                        </div>
                    </dialog>

                    <!-- Modal para ALTERAR item -->
                    <dialog id="modal_alterar">
                        <div class="modal">
                            <button class="botao_fechar_modal" onclick="closeModalAlterar()" title="Fechar">×</button>
                            <div class="modal-content">
                                <div class="modal-body">
                                    <h2>Alterar item</h2>

                                    <input type="hidden" id="edit_item_id">
                                    <input type="hidden" id="edit_produto_id">

                                    <div class="form-group">
                                        <label for="edit_nome_produto" class="form-label">Produto</label>
                                        <input type="text" id="edit_nome_produto" class="form-input" readonly disabled>
                                    </div>

                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="edit_preco_produto" class="form-label">Preço Unitário (R$) <span style="color: red;">*</span></label>
                                            <input type="number" id="edit_preco_produto" class="form-input" step="0.001" min="0" onkeypress="handleEnterAlterar(event)">
                                        </div>

                                        <div class="form-group">
                                            <label for="edit_quantidade_produto" class="form-label">Quantidade <span style="color: red;">*</span></label>
                                            <input type="number" id="edit_quantidade_produto" class="form-input" step="0.001" min="0.001" onkeypress="handleEnterAlterar(event)">
                                        </div>
                                    </div>
                                </div>

                                <div class="modal-footer">
                                    <button onclick="closeModalAlterar()" class="btn btn-secondary">Cancelar</button>
                                    <button onclick="confirmarAlterar()" class="btn btn-primary">
                                        <span>✓</span> Salvar Alterações
                                    </button>
                                </div>
                            </div>
                        </div>
                    </dialog>

                    <!-- Modal para CRIAR CLIENTE -->
                    <dialog id="modal_criar_cliente">
                        <div class="modal">
                            <button class="botao_fechar_modal" onclick="closeModalCriarCliente()" title="Fechar">×</button>
                            <div class="modal-content">
                                <div class="modal-body">
                                    <h2>Novo Cliente</h2>
                                    <form id="createClienteForm">
                                        <div class="form-group">
                                            <label for="cliente_nome" class="form-label">Nome <span style="color: red;">*</span></label>
                                            <input type="text" name="nome" id="cliente_nome" class="form-input" required>
                                        </div>
                                        <div class="form-group">
                                            <label for="cliente_telefone" class="form-label">Telefone</label>
                                            <input type="text" name="telefone" id="cliente_telefone" class="form-input">
                                        </div>
                                        <div class="form-group">
                                            <label for="cliente_cpf" class="form-label">CPF</label>
                                            <input type="text" name="cpf" id="cliente_cpf" class="form-input">
                                        </div>
                                        <div class="form-group">
                                            <label for="cliente_endereco" class="form-label">Endereço</label>
                                            <input type="text" name="endereco" id="cliente_endereco" class="form-input">
                                        </div>
                                    </form>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" onclick="closeModalCriarCliente()">Cancelar</button>
                                    <button type="submit" form="createClienteForm" class="btn btn-primary">
                                        <span>+</span> Cadastrar
                                    </button>
                                </div>
                            </div>
                        </div>
                    </dialog>

                    <table id="tabela-itens" class="table table-striped" style="background-color: white;">
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
                        <!-- Itens serão carregados via JavaScript -->
                        </tbody>
                        <tfoot>
                        <tr>
                            <td colspan="6">TOTAL</td>
                            <td colspan="3">R$ 0,000</td>
                        </tr>
                        </tfoot>
                    </table>

                </section>
            </div>

            <section id="finalizar" class="bloco">
                <div style="display: flex; gap: -10px;">
                    <button class="botao_cancela" onclick="location.href = 'orcamentos.jsp'">
                        Voltar
                    </button>
                    <button class="botao_cancela" onclick="confirmarExclusao()">
                        Excluir Orçamento
                    </button>
                </div>
                <div style="display: flex; gap: 10px;">
                    <button class="botao_confirma" id="botao_imprime" onclick="location.href = 'imprimir_orcamento.jsp?id=<%= id%>'">
                        Imprimir Orçamento
                    </button>
                    <% if (orcamento.getStatus().equals("Pendente")) {
                        Venda venda = VendaDAO.listarPorOrcamento(id);
                    %>
                    <button class="botao_confirma" id="botao_venda" onclick="location.href = 'alterar_venda.jsp?id=<%= venda.getId()%>'">
                        Realizar Venda
                    </button>
                    <% } else if (!orcamento.getStatus().equals("Concluído")) { %>
                    <button class="botao_confirma" id="botao_venda" onclick="location.href = 'registrar_venda.jsp?id=<%= id%>'">
                        Realizar Venda
                    </button>
                    <%}%>
                </div>
            </section>
        </div>
    </body>
</html>
