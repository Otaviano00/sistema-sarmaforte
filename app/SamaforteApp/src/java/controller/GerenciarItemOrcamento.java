package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.ItemOrcamentoDAO;
import dao.OrcamentoDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.ProdutoDAO;
import model.ItemOrcamento;
import utilities.LocalDateTimeAdapter;

public class GerenciarItemOrcamento extends HttpServlet {

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
        String idOrcamentoParam = request.getParameter("idOrcamento");
        String isPaginado = request.getParameter("isPaginado");

        try {
            // Validar que o idOrcamento é obrigatório (exceto para busca por ID de item específico)
            if (idOrcamentoParam == null && idParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Parâmetro 'idOrcamento' é obrigatório para listar itens\"}");
                return;
            }

            // Buscar item específico por ID
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                ItemOrcamento item = ItemOrcamentoDAO.listarPorId(id);

                if (item == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Item não encontrado com o id: " + id + "\"}");
                    return;
                }

                item.setOrcamento(null);
                out.print(gson.toJson(item));
                return;
            }

            int idOrcamento = Integer.parseInt(idOrcamentoParam);

            // Validar se o orçamento existe
            if (OrcamentoDAO.listarPorId(idOrcamento) == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Orçamento não encontrado com o id: " + idOrcamento + "\"}");
                return;
            }

            // Verificar se é uma requisição do DataTables serverSide
            if (Boolean.parseBoolean(isPaginado)) {
                // Requisição do DataTables serverSide
                int start = Integer.parseInt(request.getParameter("start"));
                int length = Integer.parseInt(request.getParameter("length"));
                String searchValue = request.getParameter("search[value]");
                int draw = Integer.parseInt(request.getParameter("draw"));
                String filterColumn = request.getParameter("filterColumn");
                String filterType = request.getParameter("filterType");

                List<ItemOrcamento> itens = ItemOrcamentoDAO.listarPaginadoPorOrcamento(
                    idOrcamento, start, length, searchValue, filterColumn, filterType
                );

                // Evitar referência circular removendo o orçamento dos itens
                itens.forEach(item -> item.setOrcamento(null));

                int totalRecords = ItemOrcamentoDAO.contarTodosPorOrcamento(idOrcamento);
                int filteredRecords = ItemOrcamentoDAO.contarFiltradosPorOrcamento(
                    idOrcamento, searchValue, filterColumn, filterType
                );

                double totalOrcamento = ItemOrcamentoDAO.calcularTotalOrcamento(idOrcamento);

                Map<String, Object> jsonResponse = new HashMap<>();
                jsonResponse.put("draw", draw);
                jsonResponse.put("recordsTotal", totalRecords);
                jsonResponse.put("recordsFiltered", filteredRecords);
                jsonResponse.put("data", itens);
                jsonResponse.put("totalOrcamento", totalOrcamento);

                out.print(gson.toJson(jsonResponse));
                return;
            }

            // Listagem completa dos itens do orçamento (sem paginação)
            List<ItemOrcamento> itens = ItemOrcamentoDAO.listarPorOrcamento(idOrcamento);

            // Evitar referência circular removendo o orçamento dos itens
            itens.forEach(item -> item.setOrcamento(null));

            out.print(gson.toJson(itens));

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Parâmetro inválido: " + e.getMessage() + "\"}");
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

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Adicionar item ao orçamento
            Map<String, Object> data = gson.fromJson(request.getReader(), Map.class);

            int idProduto = ((Double) data.get("idProduto")).intValue();
            int idOrcamento = ((Double) data.get("idOrcamento")).intValue();
            double quantidade = (Double) data.get("quantidade");
            double preco = (Double) data.get("preco");

            // Validar se o orçamento existe
            if (OrcamentoDAO.listarPorId(idOrcamento) == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Orçamento não encontrado com o id: " + idOrcamento + "\"}");
                return;
            }

            ItemOrcamento item = new ItemOrcamento();
            item.setProduto(ProdutoDAO.listarPorId(idProduto));
            item.setOrcamento(OrcamentoDAO.listarPorId(idOrcamento));
            item.setQuantidade(quantidade);
            item.setPreco(preco);
            item.setStatusVenda(false);
            item.setDataHora(LocalDateTime.now());

            if (OrcamentoDAO.adicionarItem(item)) {
                item.setOrcamento(null); // Evitar referência circular
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(gson.toJson(item));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Erro ao adicionar item\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Parâmetro inválido: " + e.getMessage() + "\"}");
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

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Alterar item do orçamento
            Map<String, Object> data = gson.fromJson(request.getReader(), Map.class);

            int idItem = ((Double) data.get("idItem")).intValue();
            int idProduto = ((Double) data.get("idProduto")).intValue();
            int idOrcamento = ((Double) data.get("idOrcamento")).intValue();
            double quantidade = (Double) data.get("quantidade");
            double preco = (Double) data.get("preco");

            // Validar se o orçamento existe
            if (OrcamentoDAO.listarPorId(idOrcamento) == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Orçamento não encontrado com o id: " + idOrcamento + "\"}");
                return;
            }

            // Validar se o item existe
            if (ItemOrcamentoDAO.listarPorId(idItem) == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Item não encontrado com o id: " + idItem + "\"}");
                return;
            }

            ItemOrcamento item = new ItemOrcamento();
            item.setId(idItem);
            item.setProduto(ProdutoDAO.listarPorId(idProduto));
            item.setOrcamento(OrcamentoDAO.listarPorId(idOrcamento));
            item.setQuantidade(quantidade);
            item.setPreco(preco);
            item.setStatusVenda(false);
            item.setDataHora(LocalDateTime.now());

            if (ItemOrcamentoDAO.alterar(item)) {
                item.setOrcamento(null); // Evitar referência circular
                out.print(gson.toJson(item));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Erro ao atualizar item\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Parâmetro inválido: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Erro no servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String idItemParam = request.getParameter("idItem");

            if (idItemParam == null || idItemParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Parâmetro 'idItem' é obrigatório\"}");
                return;
            }

            int idItem = Integer.parseInt(idItemParam);

            // Validar se o item existe
            if (ItemOrcamentoDAO.listarPorId(idItem) == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Item não encontrado com o id: " + idItem + "\"}");
                return;
            }

            if (OrcamentoDAO.excluirItem(idItem)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Erro ao excluir item\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Parâmetro inválido: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Erro no servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

}
