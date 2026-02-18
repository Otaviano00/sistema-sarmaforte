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
        String itensParam = request.getParameter("itens");

        try {
            if (idParam != null && "true".equals(itensParam)) {
                // Buscar itens do orçamento da venda
                int id = Integer.parseInt(idParam);
                Venda venda = VendaDAO.listarPorId(id);
                if (venda != null) {
                    List<ItemOrcamento> itens = OrcamentoDAO.listarItensOrcamento(venda.getOrcamento().getId());
                    out.print(gson.toJson(itens));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Venda não encontrada\"}");
                }
            } else if (idParam != null) {
                // Buscar venda por ID
                int id = Integer.parseInt(idParam);
                Venda venda = VendaDAO.listarPorId(id);
                if (venda != null) {
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

        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();

        // Verificar autenticação
        if (session == null || session.getAttribute("hierarquia") == null) {
            out.print("<script>");
            out.print("alert('Sessão expirada! Faça login novamente.');");
            out.print("location.href = 'login.jsp';");
            out.print("</script>");
            out.close();
            return;
        }

        Integer hierarquia = (Integer) session.getAttribute("hierarquia");

        // Verificar autorização (hierarquia 2 não pode cadastrar/alterar)
        if (hierarquia >= 2) {
            out.print("<script>");
            out.print("alert('Você não tem autorização para realizar esta ação');");
            out.print("location.href = 'vendas.jsp';");
            out.print("</script>");
            out.close();
            return;
        }

        try {
            int acao = Integer.parseInt(request.getParameter("acao"));

            Venda venda = new Venda();
            venda.setData(LocalDateTime.now());
            venda.setDesconto(Double.parseDouble(request.getParameter("desconto")));
            venda.setFormaPagamento(request.getParameter("forma_pagamento"));
            venda.setValor(Double.parseDouble(request.getParameter("valor")));
            Integer idUsuario = Integer.parseInt(request.getParameter("id_usuario"));
            venda.setUsuario(UsuarioDAO.listarPorId(idUsuario));
            Integer idOrcamento = Integer.parseInt(request.getParameter("id_orcamento"));
            venda.setOrcamento(OrcamentoDAO.listarPorId(idOrcamento));
            String[] itens = request.getParameterValues("produto-checkbox");

            List<ItemOrcamento> itensOrcamento = OrcamentoDAO.listarItensOrcamento(idOrcamento);
            boolean sucesso = false;
            String mensagemSucesso = "";
            String mensagemErro = "";

            switch (acao) {
                case 1: // Criar venda
                    sucesso = VendaDAO.registrar(venda);
                    mensagemSucesso = "Venda registrada com sucesso!";
                    mensagemErro = "Erro ao registrar venda!";
                    break;

                case 2: // Alterar venda
                    venda.setId(Integer.parseInt(request.getParameter("id_venda")));
                    sucesso = VendaDAO.alterar(venda);
                    mensagemSucesso = "Venda atualizada com sucesso!";
                    mensagemErro = "Erro ao atualizar venda!";
                    break;

                default:
                    out.print("<script>");
                    out.print("alert('Ação inválida!');");
                    out.print("location.href = 'vendas.jsp';");
                    out.print("</script>");
                    out.close();
                    return;
            }

            if (sucesso) {
                // Atualizar status dos itens selecionados
                if (itens != null && itens.length > 0) {
                    // Timestamp base para garantir unicidade
                    LocalDateTime baseTimestamp = LocalDateTime.now();
                    int itemCounter = 0;

                    for (String itemIdStr : itens) {
                        int itemId = Integer.parseInt(itemIdStr);

                        for (ItemOrcamento item : itensOrcamento) {
                            if (item.getId() == itemId) {
                                item.setStatusVenda(true);
                                // Adicionar segundos incrementais para garantir unicidade
                                item.setDataHora(baseTimestamp.plusSeconds(itemCounter));
                                ItemOrcamentoDAO.alterar(item);
                                itemCounter++;
                                break;
                            }
                        }
                    }
                }

                // Verificar se todos os itens foram vendidos
                itensOrcamento = OrcamentoDAO.listarItensOrcamento(idOrcamento); // Recarregar para pegar status atualizado
                boolean todasVendidas = itensOrcamento.stream()
                        .allMatch(ItemOrcamento::isStatusVenda);

                if (todasVendidas) {
                    venda.getOrcamento().setStatus("Concluído");
                } else {
                    venda.getOrcamento().setStatus("Pendente");
                }

                OrcamentoDAO.alterar(venda.getOrcamento());

                // Redirecionar com mensagem de sucesso
                out.print("<script>");
                out.print("alert('" + mensagemSucesso + "');");
                out.print("location.href = 'vendas.jsp';");
                out.print("</script>");
                out.close();
            } else {
                out.print("<script>");
                out.print("alert('" + mensagemErro + "');");
                out.print("history.back();");
                out.print("</script>");
                out.close();
            }
        } catch (Exception e) {
            out.print("<script>");
            out.print("alert('Erro interno! Contate o administrador do sistema. Erro: " + e.getMessage() + "');");
            out.print("history.back();");
            out.print("</script>");
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
            if (hierarquia >= 2) {
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
