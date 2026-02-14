<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Orcamento"%>
<%@page import="dao.OrcamentoDAO"%>
<%@page import="model.ItemOrcamento"%>
<%@page import="java.time.LocalDate"%>
<%@page import="utilities.Util" %>

<%@include file="sessao.jsp" %>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>

        <!-- jsPDF para geração de PDF -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>

        <link rel="stylesheet" href="style/main.css">
        <link rel="shortcut icon" href="images/favicon/favicon(1).ico" type="image/x-icon">

        <style>
            /* Estilos específicos para impressão */
            body {
                font-family: 'Karla', sans-serif;
                background-color: #f5f5f5;
            }

            .print-container {
                max-width: 1000px;
                margin: 20px auto;
                background: white;
                padding: 40px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }

            .print-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                border-bottom: 3px solid #142E50;
                padding-bottom: 20px;
                margin-bottom: 30px;
            }

            .print-logo {
                display: flex;
                align-items: center;
                gap: 20px;
            }

            .print-logo img {
                width: 280px;
            }

            .company-info {
                text-align: left;
            }

            .company-info h1 {
                color: #142E50;
                margin: 0;
                font-size: 1.8em;
            }

            .company-info p {
                margin: 5px 0;
                color: #666;
            }

            .print-date {
                text-align: right;
                color: #666;
                font-weight: bold;
            }

            .section-title {
                background: #142E50;
                color: #FFE9BD;
                padding: 10px 15px;
                margin: 20px 0 15px 0;
                border-radius: 5px;
                font-weight: bold;
                font-size: 1.1em;
            }

            .client-data {
                display: grid;
                grid-template-columns: repeat(2, 1fr);
                gap: 15px;
                margin-bottom: 20px;
            }

            .data-field {
                padding: 10px;
                background: #f9f9f9;
                border-left: 3px solid #142E50;
            }

            .data-field label {
                display: block;
                font-weight: bold;
                color: #142E50;
                margin-bottom: 5px;
                font-size: 0.9em;
            }

            .data-field span {
                color: #333;
            }

            .data-field.full-width {
                grid-column: 1 / -1;
            }

            .items-table {
                width: 100%;
                border-collapse: collapse;
                margin: 20px 0;
            }

            .items-table th {
                background: #142E50;
                color: #FFE9BD;
                padding: 12px;
                text-align: left;
                font-weight: bold;
            }

            .items-table td {
                padding: 10px 12px;
                border-bottom: 1px solid #ddd;
            }

            .items-table tbody tr:hover {
                background: #f5f5f5;
            }

            .items-table tfoot td {
                background: #f0f0f0;
                font-weight: bold;
                font-size: 1.1em;
                padding: 15px 12px;
                border-top: 2px solid #142E50;
            }

            .text-right {
                text-align: right;
            }

            .text-center {
                text-align: center;
            }

            .action-buttons {
                position: fixed;
                top: 20px;
                right: 20px;
                display: flex;
                gap: 10px;
                z-index: 1000;
            }

            .btn {
                padding: 12px 24px;
                border: none;
                border-radius: 8px;
                font-weight: bold;
                cursor: pointer;
                transition: all 0.3s;
                font-family: 'Karla', sans-serif;
                font-size: 1em;
            }

            .btn-primary {
                background: #142E50;
                color: #FFB729;
            }

            .btn-primary:hover {
                background: #366cb4;
                transform: translateY(-2px);
            }

            .btn-secondary {
                background: #6c757d;
                color: white;
            }

            .btn-secondary:hover {
                background: #5a6268;
                transform: translateY(-2px);
            }

            .btn-success {
                background: #28a745;
                color: white;
            }

            .btn-success:hover {
                background: #218838;
                transform: translateY(-2px);
            }

            /* Estilos para impressão via navegador */
            @media print {
                body {
                    background: white;
                }

                .action-buttons {
                    display: none !important;
                }

                .print-container {
                    box-shadow: none;
                    margin: 0;
                    padding: 20px;
                }
            }
        </style>

        <%
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendRedirect("orcamentos.jsp");
                return;
            }

            Integer id = null;
            Orcamento orcamento = null;

            try {
                id = Integer.parseInt(idParam);
                orcamento = OrcamentoDAO.listarPorId(id);

                if (orcamento == null) {
                    response.sendRedirect("orcamentos.jsp");
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("orcamentos.jsp");
                return;
            }
        %>

        <title>Orçamento #<%= id %> - <%= orcamento.getCliente().getNome()%></title>
    </head>
    <body>
    <body>
        <!-- Botões de ação (não aparecem na impressão) -->
        <div class="action-buttons">
            <button class="btn btn-secondary" onclick="window.history.back()">
                ← Voltar
            </button>
            <button class="btn btn-success" onclick="gerarPDF()">
                📄 Baixar PDF
            </button>
        </div>

        <!-- Conteúdo para impressão -->
        <div class="print-container" id="printArea">
            <!-- Cabeçalho -->
            <div class="print-header">
                <div class="print-logo">
                    <img src="images/logo.svg" alt="Logo Samaforte">
                    <div class="company-info">
                        <h1>SAMAFORTE</h1>
                        <p>Qr 215 Conjunto 1 lote 29</p>
                        <p>Samambaia Norte - DF</p>
                    </div>
                </div>
                <div class="print-date">
                    <p>Data de Emissão:</p>
                    <p><%= Util.converteData(LocalDate.now())%></p>
                    <p style="margin-top: 10px;">Orçamento #<%= id %></p>
                </div>
            </div>

            <!-- Dados do Cliente -->
            <div class="section-title">DADOS DO CLIENTE</div>
            <div class="client-data">
                <div class="data-field">
                    <label>Nome:</label>
                    <span><%= orcamento.getCliente().getNome()%></span>
                </div>
                <div class="data-field">
                    <label>Telefone:</label>
                    <span><%= orcamento.getCliente().getTelefone() != null ? orcamento.getCliente().getTelefone() : "---"%></span>
                </div>
                <div class="data-field">
                    <label>CPF:</label>
                    <span><%= orcamento.getCliente().getCpf() != null ? orcamento.getCliente().getCpf() : "---"%></span>
                </div>
                <div class="data-field">
                    <label>Endereço:</label>
                    <span><%= orcamento.getCliente().getEndereco() != null ? orcamento.getCliente().getEndereco() : "---"%></span>
                </div>
            </div>

            <!-- Dados do Orçamento -->
            <div class="section-title">DADOS DO ORÇAMENTO</div>
            <div class="client-data">
                <div class="data-field">
                    <label>Funcionário Responsável:</label>
                    <span><%= nome%></span>
                </div>
                <div class="data-field">
                    <label>Cargo:</label>
                    <span><%= cargo%></span>
                </div>
                <div class="data-field">
                    <label>Data de Criação:</label>
                    <span><%= Util.converteData(orcamento.getDataCriacao().toLocalDate())%></span>
                </div>
                <div class="data-field">
                    <label>Status:</label>
                    <span><%= orcamento.getStatus() != null ? orcamento.getStatus() : "Pendente" %></span>
                </div>
                <% if (orcamento.getInformacao() != null && !orcamento.getInformacao().trim().isEmpty()) { %>
                <div class="data-field full-width">
                    <label>Informações Adicionais:</label>
                    <span><%= orcamento.getInformacao().trim()%></span>
                </div>
                <% } %>
            </div>

            <!-- Itens do Orçamento -->
            <div class="section-title">ITENS DO ORÇAMENTO</div>
            <table class="items-table">
                <thead>
                    <tr>
                        <th style="width: 10%;">Cód.</th>
                        <th style="width: 40%;">Nome do Produto</th>
                        <th class="text-center" style="width: 12%;">Qtd.</th>
                        <th class="text-right" style="width: 18%;">Valor Unit.</th>
                        <th class="text-right" style="width: 20%;">Valor Total</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<ItemOrcamento> itens = OrcamentoDAO.listarItensOrcamento(id);
                        double total = 0;
                        for (ItemOrcamento item : itens) {
                            double precoTotal = item.getPreco() * item.getQuantidade();
                            total += precoTotal;
                    %>
                        <tr>
                            <td><%= item.getProduto().getCodigo()%></td>
                            <td><%= item.getProduto().getNome()%></td>
                            <td class="text-center"><%= String.format("%.3f", item.getQuantidade())%></td>
                            <td class="text-right">R$ <%= String.format("%,.3f", item.getPreco())%></td>
                            <td class="text-right">R$ <%= String.format("%,.3f", precoTotal)%></td>
                        </tr>
                    <%
                        }
                    %>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="4" class="text-right">VALOR TOTAL:</td>
                        <td class="text-right">R$ <%= String.format("%,.3f", total)%></td>
                    </tr>
                </tfoot>
            </table>

            <!-- Rodapé -->
            <div style="margin-top: 50px; padding-top: 20px; border-top: 1px solid #ddd; text-align: center; color: #666; font-size: 0.9em;">
                <p>Este orçamento tem validade de 15 dias a partir da data de emissão.</p>
                <p style="margin-top: 10px;">SAMAFORTE - Materiais de Construção | Telefone: (61) XXXX-XXXX</p>
            </div>
        </div>

        <script>

            // Função para gerar PDF usando jsPDF e html2canvas
            async function gerarPDF() {
                const { jsPDF } = window.jspdf;
                const printArea = document.getElementById('printArea');

                try {
                    // Ocultar botões temporariamente
                    const buttons = document.querySelector('.action-buttons');
                    if (buttons) buttons.style.display = 'none';

                    // Capturar o conteúdo como imagem
                    const canvas = await html2canvas(printArea, {
                        scale: 2,
                        useCORS: true,
                        logging: false,
                        backgroundColor: '#ffffff'
                    });

                    // Restaurar botões
                    if (buttons) buttons.style.display = 'flex';

                    // Criar PDF
                    const imgData = canvas.toDataURL('image/png');
                    const pdf = new jsPDF({
                        orientation: 'portrait',
                        unit: 'mm',
                        format: 'a4'
                    });

                    const imgWidth = 210; // A4 width in mm
                    const pageHeight = 297; // A4 height in mm
                    const imgHeight = (canvas.height * imgWidth) / canvas.width;
                    let heightLeft = imgHeight;
                    let position = 0;

                    pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
                    heightLeft -= pageHeight;

                    // Adicionar páginas extras se necessário
                    while (heightLeft >= 0) {
                        position = heightLeft - imgHeight;
                        pdf.addPage();
                        pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
                        heightLeft -= pageHeight;
                    }

                    // Salvar PDF
                    const filename = 'Orcamento_<%= id %>_<%= orcamento.getCliente().getNome().replaceAll(" ", "_") %>_<%= Util.converteData(LocalDate.now()).replaceAll("/", "-") %>.pdf';
                    pdf.save(filename);

                } catch (error) {
                    console.error('Erro ao gerar PDF:', error);
                    alert('Erro ao gerar PDF. Tente usar a opção de imprimir.');
                }
            }
        </script>
    </body>
</html>