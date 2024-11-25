package controller;

import dao.ClienteDAO;
import dao.ItemOrcamentoDAO;
import dao.OrcamentoDAO;
import dao.ProdutoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import model.ItemOrcamento;
import model.Orcamento;
import model.Produto;

public class GerenciarOrcamento extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarOrcamento(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarOrcamento(request, response);
    }

    private static void exibirMensagem(PrintWriter out, String mensagem, String url) throws IOException {
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = '" + url + "';");
        out.print("</script>");
        out.close();
    }

    public static void gerenciarOrcamento(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        int acao;
        try {
            acao = Integer.parseInt(request.getParameter("acao"));
        } catch (NumberFormatException e) {
            exibirMensagem(out, "Ação inválida!", "orcamentos.jsp");
            return;
        }

        Orcamento orcamento = new Orcamento();

        switch (acao) {
            case 1: // Registrar
                try {
                    orcamento.setDataCriacao(LocalDateTime.now());
                    orcamento.setDataValidade(LocalDateTime.now().plusDays(15));
                    orcamento.setStatus("aberto");
                    orcamento.setCliente(ClienteDAO.listarPorId(5));

                    // Registrar no banco
                    if (OrcamentoDAO.registrar(orcamento)) {
                        List<Orcamento> orcamentos = OrcamentoDAO.listar();
                        int idOrcamento = orcamentos.get(orcamentos.size() - 1).getId();
                        response.sendRedirect("registrar_orcamento.jsp?id=" + idOrcamento);
                    } else {
                        exibirMensagem(out, "Erro ao registrar orçamento!", "orcamentos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "orcamentos.jsp");
                }
                break;

            case 2: // Alterar
                try {
                    int idOrcamento = Integer.parseInt(request.getParameter("id"));
                    orcamento = OrcamentoDAO.listarPorId(idOrcamento);

                    // Verificação de orçamento
                    if (orcamento == null) {
                        exibirMensagem(out, "Orçamento não encontrado!", "orcamentos.jsp");
                        return;
                    }

                    // Alterar no banco
                    if (OrcamentoDAO.alterar(orcamento)) {
                        response.sendRedirect("alterar_orcamento.jsp?id=" + idOrcamento);
                    } else {
                        exibirMensagem(out, "Erro ao alterar orçamento!", "orcamentos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "orcamentos.jsp");
                }
                break;

            case 3: // Excluir
                try {
                    int idOrcamento = Integer.parseInt(request.getParameter("id"));

                    // Excluir no banco
                    if (OrcamentoDAO.excluir(idOrcamento)) {
                        exibirMensagem(out, "Orçamento excluído com sucesso!", "orcamentos.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao excluir orçamento!", "orcamentos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "orcamentos.jsp");
                }
                break;

            case 4: // Atualizar Informação
                try {
                    int idOrcamento = Integer.parseInt(request.getParameter("id_orcamento"));
                    int idCliente = Integer.parseInt(request.getParameter("seletor_cliente"));
                    String informacao = request.getParameter("informacao");

                    orcamento = OrcamentoDAO.listarPorId(idOrcamento);

                    if (orcamento == null) {
                        exibirMensagem(out, "Orçamento não encontrado!", "orcamentos.jsp");
                        return;
                    }

                    orcamento.setInformacao(informacao);
                    orcamento.setCliente(ClienteDAO.listarPorId(idCliente));

                    // Alterar no banco
                    if (OrcamentoDAO.alterar(orcamento)) {
                        out.print("<script>");
                        out.print("location.href = document.referrer;");
                        out.print("</script>");
                        out.close();
                    } else {
                        exibirMensagem(out, "Erro ao atualizar informações do orçamento!", "orcamentos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "orcamentos.jsp");
                }
                break;

            case 5: // Adicionar Item
                try {
                    int idProduto = Integer.parseInt(request.getParameter("id_produto"));
                    int idOrcamento = Integer.parseInt(request.getParameter("id_orcamento"));
                    int quantidade = Integer.parseInt(request.getParameter("quantidade_produto"));
                    double preco = Double.parseDouble(request.getParameter("preco_produto"));

                    ItemOrcamento item = new ItemOrcamento();
                    item.setProduto(ProdutoDAO.listarPorId(idProduto));
                    item.setOrcamento(OrcamentoDAO.listarPorId(idOrcamento));
                    item.setQuantidade(quantidade);
                    item.setPreco(preco);
                    item.setStatusVenda(false);
                    item.setDataHora(LocalDateTime.now());

                    // Adicionar item no orçamento
                    if (OrcamentoDAO.adicionarItem(item)) {
                        out.print("<script>");
                        out.print("location.href = document.referrer;");
                        out.print("</script>");
                        out.close();
                    } else {
                        exibirMensagem(out, "Erro ao adicionar item no orçamento!", "orcamentos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "orcamentos.jsp");
                }
                break;

            case 6: // Alterar Item
                try {
                    int idItem = Integer.parseInt(request.getParameter("id_item"));
                    int idProduto = Integer.parseInt(request.getParameter("id_produto"));
                    int quantidade = Integer.parseInt(request.getParameter("quantidade_produto"));
                    int idOrcamento = Integer.parseInt(request.getParameter("id_orcamento"));
                    double preco = Double.parseDouble(request.getParameter("preco_produto"));

                    ItemOrcamento item = new ItemOrcamento();
                    item.setId(idItem);
                    item.setProduto(ProdutoDAO.listarPorId(idProduto));
                    item.setOrcamento(OrcamentoDAO.listarPorId(idOrcamento));
                    item.setQuantidade(quantidade);
                    item.setPreco(preco);
                    item.setStatusVenda(false);
                    item.setDataHora(LocalDateTime.now());

                    // Alterar item no orçamento
                    if (ItemOrcamentoDAO.alterar(item)) {
                        out.print("<script>");
                        out.print("location.href = document.referrer;");
                        out.print("</script>");
                        out.close();
                    } else {
                        exibirMensagem(out, "Erro ao alterar item do orçamento!", "orcamentos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "orcamentos.jsp");
                }
                break;

            case 7: // Excluir Item
                try {
                    int idItem = Integer.parseInt(request.getParameter("idItem"));

                    // Excluir item do orçamento
                    if (OrcamentoDAO.excluirItem(idItem)) {
                        out.print("<script>");
                        out.print("location.href = document.referrer;");
                        out.print("</script>");
                        out.close();
                    } else {
                        exibirMensagem(out, "Erro ao excluir item do orçamento!", "orcamentos.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "orcamentos.jsp");
                }
                break;

            default:
                exibirMensagem(out, "Ação inválida!", "orcamentos.jsp");
                break;
        }
    }
}
