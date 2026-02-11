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

        <script defer src="script/registrar_orcamento.js"></script>

        <link rel="stylesheet" href="style/main.css">
        <link rel="stylesheet" href="style/cadastrar_alterar.css">
        <link rel="stylesheet" href="style/orcamento.css">
        <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">

        <title>Registrar Orçamento</title>
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
            <section id="buscar_item" class="bloco">
                <input type="hidden" id="id_produto" name="" data-preco="" value="">
                <select id="seletor_produto" name="seletor_produto" class="seletor">
                    <!-- Produtos serão carregados via JavaScript -->
                </select>
                <button onclick="adicionarItem()">
                    ADICIONAR ITEM
                </button>
            </section>
            <div class="conjunto">
                <section id="dados_orcamento" class="bloco">
                    <h2>Dados do cliente</h2>
                    <form id="dados_cliente" style="margin-top: -20px;">
                        <input type="hidden" name="id_orcamento" id="id_orcamento" value="">
                        <input type="hidden" id="id_cliente" value="">

                        <div style="display: flex; flex-direction: row; width: 100%; min-width: 100%;">
                            <div class="campos">
                                <select id="seletor_cliente" name="seletor_cliente" class="seletor" required>
                                    <!-- Clientes serão carregados via JavaScript -->
                                </select>
                            </div>
                            
                            <button type="button" onclick="location.href='cadastrar_cliente.jsp'" title="Cadastrar Novo Cliente" style="font-size: 1.5em;">
                                Novo
                            </button>
                        </div>
                        <br>                     
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
                        <br>
                        <div class="campos">
                            <label for="informacao"> Informações:</label>
                            <textarea name="informacao" id="informacao"></textarea>
                        </div>
                        <button type="submit" style="font-size: 1.4em;">
                            Atualizar Informações
                        </button>
                    </form>
                </section>
                <section id="listar_itens" class="tabela bloco">
                    <h2>Itens do Orçamento</h2>
                    <dialog>
                        <div class="modal">
                            <form id="dados_item">
                                <input type="hidden" name="acao" id="acao_item" value="">

                                <input type="hidden" name="id_produto" id="produto_id">
                                <input type="hidden" name="id_orcamento" id="id_orcamento_modal" value="">
                                <input type="hidden" name="id_item" id="id_item" value="0">
                                <div class="campos">
                                    <label for="nome_produto" class="titulo_campo">Produto: </label>
                                    <input type="text" name="nome_produto" id="nome_produto" readonly disabled>
                                </div>
                                <div class="campos">
                                    <label for="preco_produto" class="titulo_campo">Preço (R$): </label>      
                                    <input type="number" name="preco_produto" step="0.001" id="preco_produto" required>       
                                </div>
                                <div class="campos">
                                    <label for="quantidade_produto" class="titulo_campo">Quantidade:</label>
                                    <input type="number" name="quantidade_produto" id="quantidade_produto" step="0.001" required>
                                </div>
                                <input type="submit" value="Confirmar" class="botao_confirma">
                            </form>
                            <button id="fechar" onclick="closeModal()" class="botao_fechar">           
                                X
                            </button>
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
                <button class="botao_cancela" onclick="confirmarExclusao(event, 'GerenciarOrcamento?id=' + new URLSearchParams(window.location.search).get('id') + '&acao=3')" style="position: absolute; left: 0;">
                    Cancelar
                </button>
                <button class="botao_confirma" id="botao_imprime" onclick="location.href = 'imprimir_orcamento.jsp?id=' + new URLSearchParams(window.location.search).get('id')">
                    Imprimir Orçamento
                </button> 
                <button class="botao_confirma" onclick="location.href = 'orcamentos.jsp'">
                    Guardar Orçamento
                </button>
                <button class="botao_confirma" id="botao_venda" onclick="location.href = 'registrar_venda.jsp?id=' + new URLSearchParams(window.location.search).get('id')">
                    Realizar Venda
                </button>
            </section>
        </div>
    </body>
</html>