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

        <title>Detalhes do Orçamento</title>
    </head>
    <body>
        <%
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendRedirect("orcamentos.jsp");
                return;
            }

            Integer id = null;

            try {
                id = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                response.sendRedirect("orcamentos.jsp");
                return;
            }
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
            <div class="conjunto">
                <section id="dados_orcamento" class="bloco">
                    <h2>Dados do Cliente</h2>
                    <form id="dados_cliente">
                        <input type="hidden" name="id_orcamento" id="id_orcamento" value="">
                        <input type="hidden" id="id_cliente" value="">

                        <div style="display: flex; gap: 10px; align-items: flex-end; width: 100%;">
                            <div class="campos" style="flex: 1; margin: 0;">
                                <label for="seletor_cliente">Cliente:</label>
                                <select id="seletor_cliente" name="seletor_cliente" class="seletor" disabled>
                                    <!-- Clientes serão carregados via JavaScript -->
                                </select>
                            </div>
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
                            <textarea name="informacao" id="informacao" disabled readonly></textarea>
                        </div>
                    </form>
                </section>

                <section id="listar_itens" class="tabela bloco">
                    <h2>Itens do Orçamento</h2>
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
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Itens serão carregados via JavaScript -->
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="6">TOTAL</td>
                                <td colspan="2">R$ 0,000</td>
                            </tr>
                        </tfoot>
                    </table>
                </section>
            </div>

            <section id="finalizar" class="bloco">
                <div>
                    <button type="button" class="botao_cancela" onclick="location.href = 'orcamentos.jsp'">
                        Voltar
                    </button>
                </div>
                <div>
                    <button type="button" class="botao_confirma" id="botao_imprime" onclick="location.href = 'imprimir_orcamento.jsp?id=<%= id%>'">
                        Imprimir Orçamento
                    </button>
                    <button type="button" class="botao_confirma" id="botao_venda" onclick="location.href = 'registrar_venda.jsp?id=<%= id%>'">
                        Realizar Venda
                    </button>
                </div>
            </section>
        </div>

        <script>
            // Flag para indicar que esta é a página de detalhes (somente leitura)
            window.isDetalhesPage = true;
        </script>
    </body>
</html>