package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.ItemOrcamentoDAO;
import dao.OrcamentoDAO;
import dao.UsuarioDAO;
import dao.VendaDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.ItemOrcamento;
import model.Orcamento;
import model.Venda;
import utilities.LocalDateTimeAdapter;

public class GerenciarVenda extends HttpServlet {

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

        try {
            if (idParam != null) {
                // Buscar venda por ID
                int id = Integer.parseInt(idParam);
                Venda venda = VendaDAO.listarPorId(id);
                if (venda != null && venda.getId() != 0) {
                    out.print(gson.toJson(venda));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Venda não encontrada\"}");
                }
            } else {
                // Listar vendas para DataTables
                int start = Integer.parseInt(request.getParameter("start"));
                int length = Integer.parseInt(request.getParameter("length"));
                String searchValue = request.getParameter("search[value]");
                int draw = Integer.parseInt(request.getParameter("draw"));
                String filterColumn = request.getParameter("filterColumn");
                String filterType = request.getParameter("filterType");

                List<Venda> vendas = VendaDAO.listarPaginado(start, length, searchValue, filterColumn, filterType);
                int totalRecords = VendaDAO.contarTodos();
                int filteredRecords = VendaDAO.contarFiltrados(searchValue, filterColumn, filterType);

                Map<String, Object> jsonResponse = new HashMap<>();
                jsonResponse.put("draw", draw);
                jsonResponse.put("recordsTotal", totalRecords);
                jsonResponse.put("recordsFiltered", filteredRecords);
                jsonResponse.put("data", vendas);

                out.print(gson.toJson(jsonResponse));
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        try {
            // Verificar autenticação
            if (session == null || session.getAttribute("hierarquia") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Sessão expirada. Faça login novamente.\"}");
                return;
            }

            Integer hierarquia = (Integer) session.getAttribute("hierarquia");

            // Verificar autorização (hierarquia 2 não pode cadastrar)
            if (hierarquia == 2) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print("{\"error\":\"Você não tem autorização para realizar esta ação\"}");
                return;
            }

            // Parse do JSON recebido
            JsonObject jsonObject = JsonParser.parseReader(request.getReader()).getAsJsonObject();

            Venda venda = new Venda();
            venda.setData(LocalDateTime.now());
            venda.setDesconto(jsonObject.get("desconto").getAsDouble());
            venda.setFormaPagamento(jsonObject.get("formaPagamento").getAsString());
            venda.setValor(jsonObject.get("valor").getAsDouble());

            Integer idUsuario = jsonObject.get("idUsuario").getAsInt();
            venda.setUsuario(UsuarioDAO.listarPorId(idUsuario));

            Integer idOrcamento = jsonObject.get("idOrcamento").getAsInt();
            venda.setOrcamento(OrcamentoDAO.listarPorId(idOrcamento));

            // Itens do orçamento a serem marcados como vendidos
            int[] itensIds = gson.fromJson(jsonObject.get("itensIds"), int[].class);

            List<ItemOrcamento> itensOrcamento = OrcamentoDAO.listarItensOrcamento(idOrcamento);

            // Cadastro da venda no banco
            if (VendaDAO.registrar(venda)) {
                // Marcar itens como vendidos
                for (ItemOrcamento item : itensOrcamento) {
                    for (int itemId : itensIds) {
                        if (item.getId() == itemId) {
                            item.setStatusVenda(true);
                            item.setDataHora(LocalDateTime.now());
                            ItemOrcamentoDAO.alterar(item);
                        }
                    }
                }

                // Atualizar status do orçamento
                Orcamento orcamento = OrcamentoDAO.listarPorId(idOrcamento);
                orcamento.setStatus("Pendente");

                // Verificar se todos os itens foram vendidos
                boolean todosVendidos = true;
                for (ItemOrcamento item : OrcamentoDAO.listarItensOrcamento(idOrcamento)) {
                    if (!item.isStatusVenda()) {
                        todosVendidos = false;
                        break;
                    }
                }

                if (todosVendidos) {
                    orcamento.setStatus("Concluído");
                }

                OrcamentoDAO.alterar(orcamento);

                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(gson.toJson(venda));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Erro ao registrar venda\"}");
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
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        try {
            // Verificar autenticação
            if (session == null || session.getAttribute("hierarquia") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Sessão expirada. Faça login novamente.\"}");
                return;
            }

            Integer hierarquia = (Integer) session.getAttribute("hierarquia");

            // Verificar autorização (hierarquia 2 não pode alterar)
            if (hierarquia == 2) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print("{\"error\":\"Você não tem autorização para realizar esta ação\"}");
                return;
            }

            // Parse do JSON recebido
            JsonObject jsonObject = JsonParser.parseReader(request.getReader()).getAsJsonObject();

            Venda venda = new Venda();
            venda.setId(jsonObject.get("id").getAsInt());
            venda.setData(LocalDateTime.now());
            venda.setDesconto(jsonObject.get("desconto").getAsDouble());
            venda.setFormaPagamento(jsonObject.get("formaPagamento").getAsString());
            venda.setValor(jsonObject.get("valor").getAsDouble());

            Integer idUsuario = jsonObject.get("idUsuario").getAsInt();
            venda.setUsuario(UsuarioDAO.listarPorId(idUsuario));

            Integer idOrcamento = jsonObject.get("idOrcamento").getAsInt();
            venda.setOrcamento(OrcamentoDAO.listarPorId(idOrcamento));

            // Itens do orçamento a serem marcados como vendidos
            int[] itensIds = gson.fromJson(jsonObject.get("itensIds"), int[].class);

            List<ItemOrcamento> itensOrcamento = OrcamentoDAO.listarItensOrcamento(idOrcamento);

            // Atualizar venda no banco
            if (VendaDAO.alterar(venda)) {
                // Atualizar status dos itens
                for (ItemOrcamento item : itensOrcamento) {
                    for (int itemId : itensIds) {
                        if (item.getId() == itemId) {
                            item.setStatusVenda(true);
                            item.setDataHora(LocalDateTime.now());
                            ItemOrcamentoDAO.alterar(item);
                        }
                    }
                }

                // Atualizar status do orçamento
                Orcamento orcamento = OrcamentoDAO.listarPorId(idOrcamento);
                orcamento.setStatus("Pendente");

                // Verificar se todos os itens foram vendidos
                boolean todosVendidos = true;
                for (ItemOrcamento item : itensOrcamento) {
                    if (!item.isStatusVenda()) {
                        todosVendidos = false;
                        break;
                    }
                }

                if (todosVendidos) {
                    orcamento.setStatus("Concluído");
                }

                OrcamentoDAO.alterar(orcamento);

                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(venda));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Erro ao atualizar venda\"}");
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
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        try {
            // Verificar autenticação
            if (session == null || session.getAttribute("hierarquia") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Sessão expirada. Faça login novamente.\"}");
                return;
            }

            Integer hierarquia = (Integer) session.getAttribute("hierarquia");

            // Verificar autorização (apenas hierarquia 0 pode excluir)
            if (hierarquia != 0) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print("{\"error\":\"Você não tem autorização para realizar esta ação\"}");
                return;
            }

            int id = Integer.parseInt(request.getParameter("id"));
            Venda venda = VendaDAO.listarPorId(id);

            if (venda == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Venda não encontrada\"}");
                return;
            }

            if (VendaDAO.excluir(id)) {
                OrcamentoDAO.excluir(venda.getOrcamento().getId());
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Erro ao excluir venda\"}");
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
