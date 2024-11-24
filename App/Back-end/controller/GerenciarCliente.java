package controller;

import dao.ClienteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
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
        PrintWriter out = response.getWriter();

        int acao;
        try {
            acao = Integer.parseInt(request.getParameter("acao"));
        } catch (NumberFormatException e) {
            exibirMensagem(out, "Ação inválida!", "clientes.jsp");
            return;
        }

        Cliente cliente = new Cliente();

        switch (acao) {
            case 1: // Cadastro
                try {
                    cliente.setNome(request.getParameter("nome"));
                    cliente.setTelefone(request.getParameter("telefone"));
                    cliente.setEndereco(request.getParameter("endereco"));
                    cliente.setCpf(request.getParameter("cpf"));

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
                    // Verificar se o id do cliente é 5
                    int clienteId = Integer.parseInt(request.getParameter("id"));
                    if (clienteId == 5) {
                        exibirMensagem(out, "Ação não permitida para esse cliente!", "clientes.jsp");
                        return; // Interrompe a execução se o id for 5
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

                    // Verificar se o id do cliente é 5
                    if (clienteIdExcluir == 5) {
                        exibirMensagem(out, "Ação não permitida para esse cliente!", "clientes.jsp");
                        return; // Interrompe a execução se o id for 5
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

            default:
                exibirMensagem(out, "Ação inválida!", "clientes.jsp");
                break;
        }
    }
}
