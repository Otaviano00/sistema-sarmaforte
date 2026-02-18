package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.ClienteDAO;
import dao.OrcamentoDAO;
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
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // Receber o orçamento do JSON
            Orcamento orcamentoRecebido = gson.fromJson(request.getReader(), Orcamento.class);

            if (orcamentoRecebido == null || orcamentoRecebido.getId() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Orçamento inválido ou sem ID\"}");
                return;
            }

            // Buscar o orçamento existente no banco
            Orcamento orcamentoExistente = OrcamentoDAO.listarPorId(orcamentoRecebido.getId());

            if (orcamentoExistente == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Orçamento não encontrado com o id: " + orcamentoRecebido.getId() + "\"}");
                return;
            }

            orcamentoExistente.setInformacao(orcamentoRecebido.getInformacao() != null ? orcamentoRecebido.getInformacao() : orcamentoExistente.getInformacao());
            orcamentoExistente.setCliente(orcamentoRecebido.getCliente() != null && orcamentoRecebido.getCliente().getId() != 0 ?
                    ClienteDAO.listarPorId(orcamentoRecebido.getCliente().getId()) : orcamentoExistente.getCliente());

            orcamentoExistente.setDataValidade(orcamentoRecebido.getDataValidade() != null ? orcamentoRecebido.getDataValidade() : orcamentoExistente.getDataValidade());
            orcamentoExistente.setDataCriacao(orcamentoRecebido.getDataCriacao() != null ? orcamentoRecebido.getDataCriacao() : orcamentoExistente.getDataCriacao());
            orcamentoExistente.setStatus(orcamentoRecebido.getStatus() != null ? orcamentoRecebido.getStatus() : orcamentoExistente.getStatus());

            if (OrcamentoDAO.alterar(orcamentoExistente)) {
                out.print(gson.toJson(orcamentoExistente));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Erro ao atualizar orçamento\"}");
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
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String idOrcamento = request.getParameter("id");

            if (idOrcamento == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Parâmetro 'id' é obrigatório\"}");
                return;
            }

            int id = Integer.parseInt(idOrcamento);

            if (OrcamentoDAO.excluir(id)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Erro ao excluir orçamento\"}");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Erro no servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }
}
