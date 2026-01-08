package controller;

import com.google.gson.Gson;
import dao.ClienteDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import model.Cliente;

public class GerenciarCliente extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarCliente(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarCliente(request, response);
    }

    private static void exibirMensagem(PrintWriter out, String mensagem, String url) throws IOException {
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = '" + url + "';");
        out.print("</script>");
        out.close();
    }

    private static boolean validarUnicidade(Cliente cliente, List<Cliente> clientes, PrintWriter out, String redirectUrl) throws IOException {
        if (cliente.getCpf() == null) {
            return false;
        }
        
        for (Cliente cl : clientes) {
            if (cl.getId() != cliente.getId()) { // Ignorar o cliente atual
                if (cliente.getCpf().equals(cl.getCpf())) {
                    exibirMensagem(out, "CPF já existente!", redirectUrl);
                    return true;
                }
                if (cliente.getTelefone().equals(cl.getTelefone())) {
                    exibirMensagem(out, "Telefone já existente!", redirectUrl);
                    return true;
                }
            }
        }
        return false;
    }

    public static void gerenciarCliente(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String acaoParam = request.getParameter("acao");
        int acao;
        try {
            acao = Integer.parseInt(acaoParam);
        } catch (NumberFormatException e) {
             if (acaoParam == null || acaoParam.isEmpty()) {
                acao = 4; // Default to list action
            } else {
                exibirMensagem(out, "Ação inválida!", "clientes.jsp");
                return;
            }
        }

        Cliente cliente = new Cliente();

        switch (acao) {
            case 1: // Cadastro
                try {
                    cliente.setNome(request.getParameter("nome"));
                    cliente.setTelefone(request.getParameter("telefone"));
                    cliente.setEndereco(request.getParameter("endereco"));
                    cliente.setCpf(request.getParameter("cpf"));
                    
                    if (cliente.getCpf() != null && cliente.getCpf().trim().isEmpty()) {
                        cliente.setCpf(null);
                    }
                    
                    if (cliente.getEndereco() != null && cliente.getEndereco().trim().isEmpty()) {
                        cliente.setEndereco(null);
                    }

             
                    // Verificação de unicidade
                    if (validarUnicidade(cliente, ClienteDAO.listar(), out, "cadastrar_cliente.jsp")) {
                        return;
                    }
  
                    if (ClienteDAO.cadastrar(cliente)) {
                        exibirMensagem(out, "Cliente cadastrado com sucesso!", "clientes.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao cadastrar cliente!", "clientes.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "cadastrar_cliente.jsp");
                }
                break;

            case 2: // Atualização
                try {
                    // Verificar se o id do cliente é 1
                    int clienteId = Integer.parseInt(request.getParameter("id"));
                    if (clienteId == 1) {
                        exibirMensagem(out, "Ação não permitida para esse cliente!", "clientes.jsp");
                        return; // Interrompe a execução se o id for 1
                    }

                    // Continue com o processo normal de atualização
                    cliente.setId(clienteId);
                    cliente.setNome(request.getParameter("nome"));
                    cliente.setTelefone(request.getParameter("telefone"));
                    cliente.setEndereco(request.getParameter("endereco"));
                    cliente.setCpf(request.getParameter("cpf"));

                    // Verificação de unicidade
                    if (validarUnicidade(cliente, ClienteDAO.listar(), out, "alterar_cliente.jsp?id=" + cliente.getId())) {
                        return;
                    }

                    if (ClienteDAO.alterar(cliente)) {
                        exibirMensagem(out, "Cliente atualizado com sucesso!", "clientes.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao atualizar cliente!", "clientes.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "clientes.jsp");
                }
                break;

            case 3: // Exclusão
                try {
                    int clienteIdExcluir = Integer.parseInt(request.getParameter("id"));

                    // Verificar se o id do cliente é 1
                    if (clienteIdExcluir == 1) {
                        exibirMensagem(out, "Ação não permitida para esse cliente!", "clientes.jsp");
                        return; // Interrompe a execução se o id for 1
                    }

                    if (ClienteDAO.excluir(clienteIdExcluir)) {
                        exibirMensagem(out, "Cliente excluído com sucesso!", "clientes.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao excluir cliente!", "clientes.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "clientes.jsp");
                }
                break;
            case 4: // Listar com paginação para DataTables
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                try {
                    int start = Integer.parseInt(request.getParameter("start"));
                    int length = Integer.parseInt(request.getParameter("length"));
                    String searchValue = request.getParameter("search[value]");
                    int draw = Integer.parseInt(request.getParameter("draw"));

                    List<Cliente> clientes = ClienteDAO.listarPaginado(start, length, searchValue);
                    int totalRecords = ClienteDAO.contarTodos();
                    int filteredRecords = ClienteDAO.contarFiltrados(searchValue);

                    Map<String, Object> jsonResponse = new HashMap<>();
                    jsonResponse.put("draw", draw);
                    jsonResponse.put("recordsTotal", totalRecords);
                    jsonResponse.put("recordsFiltered", filteredRecords);
                    jsonResponse.put("data", clientes);

                    String json = new Gson().toJson(jsonResponse);
                    out.print(json);
                    out.flush();
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"Erro ao listar clientes: " + e.getMessage() + "\"}");
                    out.flush();
                } finally {
                    out.close();
                }
                break;

            default:
                exibirMensagem(out, "Ação inválida!", "clientes.jsp");
                break;
        }
    }
}
