package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.ProdutoDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import model.Produto;
import utilities.LocalDateTimeAdapter;

public class GerenciarProduto extends HttpServlet {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String codigoParam = request.getParameter("codigo");
        String isPaginado = request.getParameter("isPaginado");

        try {
            if (codigoParam != null) {
                // Buscar produto por código
                int codigo = Integer.parseInt(codigoParam);
                Produto produto = ProdutoDAO.listarPorId(codigo);
                if (produto != null && produto.getCodigo() != 0) {
                    out.print(gson.toJson(produto));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Produto não encontrado\"}");
                }
            } else if (Boolean.parseBoolean(isPaginado)) {
                // Listar produtos para DataTables
                int start = Integer.parseInt(request.getParameter("start"));
                int length = Integer.parseInt(request.getParameter("length"));
                String searchValue = request.getParameter("search[value]");
                int draw = Integer.parseInt(request.getParameter("draw"));
                String filterColumn = request.getParameter("filterColumn");
                String filterType = request.getParameter("filterType");

                List<Produto> produtos = ProdutoDAO.listarPaginado(start, length, searchValue, filterColumn, filterType);
                int totalRecords = ProdutoDAO.contarTodos();
                int filteredRecords = ProdutoDAO.contarFiltrados(searchValue, filterColumn, filterType);

                Map<String, Object> jsonResponse = new HashMap<>();
                jsonResponse.put("draw", draw);
                jsonResponse.put("recordsTotal", totalRecords);
                jsonResponse.put("recordsFiltered", filteredRecords);
                jsonResponse.put("data", produtos);

                out.print(gson.toJson(jsonResponse));
            } else {
                // Listar todos os produtos
                List<Produto> produtos = ProdutoDAO.listar();
                out.print(gson.toJson(produtos));
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
        PrintWriter out = response.getWriter();

        try {
            // Cadastrar produto
            Produto produto = gson.fromJson(request.getReader(), Produto.class);

            if (ProdutoDAO.cadastrar(produto)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(gson.toJson(produto));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Erro ao cadastrar produto\"}");
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
            String statusParam = request.getParameter("status");

            // Se tem parâmetro status, é para alterar apenas o status
            if (statusParam != null) {
                String codigoParam = request.getParameter("codigo");

                if (codigoParam == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Parâmetro 'codigo' é obrigatório\"}");
                    return;
                }

                int codigo = Integer.parseInt(codigoParam);
                boolean novoStatus = Boolean.parseBoolean(statusParam);

                Produto produto = new Produto();
                produto.setCodigo(codigo);

                boolean sucesso = novoStatus ? ProdutoDAO.ativar(produto) : ProdutoDAO.desativar(produto);

                if (sucesso) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"message\":\"Status do produto alterado com sucesso\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Erro ao alterar status do produto\"}");
                }
            } else {
                // Alterar produto completo
                Produto produto = gson.fromJson(request.getReader(), Produto.class);

                if (ProdutoDAO.alterar(produto)) {
                    out.print(gson.toJson(produto));
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Erro ao atualizar produto\"}");
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
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            int codigo = Integer.parseInt(request.getParameter("codigo"));

            if (ProdutoDAO.excluir(codigo)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Erro ao excluir produto\"}");
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
