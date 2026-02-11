package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.ClienteDAO;
import dao.ItemOrcamentoDAO;
import dao.OrcamentoDAO;
import dao.ProdutoDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import enums.OrcamentoAttributes;
import model.ItemOrcamento;
import model.Orcamento;
import utilities.LocalDateTimeAdapter;

public class GerenciarOrcamento extends HttpServlet {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String idParam = request.getParameter("id");
        String isPaginado = request.getParameter("isPaginado");

        try {
            if (idParam == null) {
                if (!Boolean.parseBoolean(isPaginado)) {
                    List<Orcamento> orcamentos = OrcamentoDAO.listar();
                    out.print(gson.toJson(orcamentos));
                }

                // Requisição do DataTables serverSide
                int start = Integer.parseInt(request.getParameter("start"));
                int length = Integer.parseInt(request.getParameter("length"));
                String searchValue = request.getParameter("search[value]");
                int draw = Integer.parseInt(request.getParameter("draw"));
                String filterColumn = request.getParameter("filterColumn");
                String filterType = request.getParameter("filterType");

                List<Orcamento> orcamentos = OrcamentoDAO.listarPaginado(start, length, searchValue, filterColumn, filterType);

                int totalRecords = OrcamentoDAO.contarTodos();
                int filteredRecords = OrcamentoDAO.contarFiltrados(searchValue, filterColumn, filterType);

                Map<String, Object> jsonResponse = new HashMap<>();
                jsonResponse.put("draw", draw);
                jsonResponse.put("recordsTotal", totalRecords);
                jsonResponse.put("recordsFiltered", filteredRecords);
                jsonResponse.put("data", orcamentos);

                out.print(gson.toJson(jsonResponse));

                return;
            }

            Integer idOrcamento = Integer.parseInt(idParam);
            Orcamento orcamento = OrcamentoDAO.listarPorId(idOrcamento);

            if (orcamento == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Orçamento não encontrado com o id: " + idOrcamento + "\"}");
                return;
            }

            out.print(gson.toJson(orcamento));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Erro no servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String action = request.getParameter("action");

            if (action != null && action.equals("addItem")) {
                // Adicionar item ao orçamento
                Map<String, Object> data = gson.fromJson(request.getReader(), Map.class);

                int idProduto = ((Double) data.get("idProduto")).intValue();
                int idOrcamento = ((Double) data.get("idOrcamento")).intValue();
                double quantidade = (Double) data.get("quantidade");
                double preco = (Double) data.get("preco");

                ItemOrcamento item = new ItemOrcamento();
                item.setProduto(ProdutoDAO.listarPorId(idProduto));
                item.setOrcamento(OrcamentoDAO.listarPorId(idOrcamento));
                item.setQuantidade(quantidade);
                item.setPreco(preco);
                item.setStatusVenda(false);
                item.setDataHora(LocalDateTime.now());

                if (OrcamentoDAO.adicionarItem(item)) {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    out.print(gson.toJson(item));
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Erro ao adicionar item\"}");
                }
            } else {
                // Criar novo orçamento
                Orcamento orcamento = new Orcamento();
                orcamento.setDataCriacao(LocalDateTime.now());
                orcamento.setDataValidade(LocalDateTime.now().plusDays(15));
                orcamento.setStatus("Aberto");
                orcamento.setCliente(ClienteDAO.listarPorId(1));

                if (OrcamentoDAO.registrar(orcamento)) {
                    List<Orcamento> orcamentos = OrcamentoDAO.listar();
                    Orcamento novoOrcamento = orcamentos.get(orcamentos.size() - 1);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    out.print(gson.toJson(novoOrcamento));
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Erro ao criar orçamento\"}");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Erro no servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRestPut(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRestDelete(request, response);
    }

    private void handleRestPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String action = request.getParameter("action");

            if (action != null && action.equals("updateInfo")) {
                // Atualizar informações do orçamento
                Map<String, Object> data = gson.fromJson(request.getReader(), Map.class);

                int idOrcamento = ((Double) data.get("idOrcamento")).intValue();
                int idCliente = ((Double) data.get("idCliente")).intValue();
                String informacao = (String) data.get("informacao");

                Orcamento orcamento = OrcamentoDAO.listarPorId(idOrcamento);
                if (orcamento == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Orçamento não encontrado\"}");
                    return;
                }

                orcamento.setInformacao(informacao);
                orcamento.setCliente(ClienteDAO.listarPorId(idCliente));

                if (OrcamentoDAO.alterar(orcamento)) {
                    out.print(gson.toJson(orcamento));
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Erro ao atualizar orçamento\"}");
                }
            } else if (action != null && action.equals("updateItem")) {
                // Atualizar item do orçamento
                Map<String, Object> data = gson.fromJson(request.getReader(), Map.class);

                int idItem = ((Double) data.get("idItem")).intValue();
                int idProduto = ((Double) data.get("idProduto")).intValue();
                int idOrcamento = ((Double) data.get("idOrcamento")).intValue();
                double quantidade = (Double) data.get("quantidade");
                double preco = (Double) data.get("preco");

                ItemOrcamento item = new ItemOrcamento();
                item.setId(idItem);
                item.setProduto(ProdutoDAO.listarPorId(idProduto));
                item.setOrcamento(OrcamentoDAO.listarPorId(idOrcamento));
                item.setQuantidade(quantidade);
                item.setPreco(preco);
                item.setStatusVenda(false);
                item.setDataHora(LocalDateTime.now());

                if (ItemOrcamentoDAO.alterar(item)) {
                    out.print(gson.toJson(item));
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Erro ao atualizar item\"}");
                }
            } else {
                // Atualizar orçamento
                Orcamento orcamento = gson.fromJson(request.getReader(), Orcamento.class);

                if (OrcamentoDAO.alterar(orcamento)) {
                    out.print(gson.toJson(orcamento));
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Erro ao atualizar orçamento\"}");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Erro no servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    private void handleRestDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String idOrcamento = request.getParameter("id");
            String action = request.getParameter("action");

            if (action != null && action.equals("deleteItem")) {
                // Excluir item do orçamento
                int idItem = Integer.parseInt(request.getParameter("idItem"));

                if (OrcamentoDAO.excluirItem(idItem)) {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Erro ao excluir item\"}");
                }
            } else if (idOrcamento != null) {
                // Excluir orçamento
                int id = Integer.parseInt(idOrcamento);

                if (OrcamentoDAO.excluir(id)) {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Erro ao excluir orçamento\"}");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Erro no servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    // Legacy method for backward compatibility with old JSP-based requests
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
                    orcamento.setStatus("Aberto");
                    orcamento.setCliente(ClienteDAO.listarPorId(1));

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
                    double quantidade = Double.parseDouble(request.getParameter("quantidade_produto"));
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
                    double quantidade = Double.parseDouble(request.getParameter("quantidade_produto"));
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
