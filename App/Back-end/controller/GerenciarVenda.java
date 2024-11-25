package controller;

import dao.ItemOrcamentoDAO;
import dao.OrcamentoDAO;
import dao.UsuarioDAO;
import dao.VendaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import model.ItemOrcamento;
import model.Orcamento;
import model.Venda;

public class GerenciarVenda extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarVenda(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        gerenciarVenda(request, response);
    }

    private static void exibirMensagem(PrintWriter out, String mensagem, String url) throws IOException {
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = '" + url + "';");
        out.print("</script>");
        out.close();
    }

    public static void gerenciarVenda(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, IOException {
        HttpSession sessao = request.getSession();
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Integer hierarquia = (Integer) sessao.getAttribute("hierarquia");
        Venda venda = new Venda();

        // Verificar se a sessão expirou
        if (hierarquia == null) {
            exibirMensagem(out, "Sessão expirada! Faça login novamente.", "login.jsp");
            return;
        }

        int acao;
        try {
            acao = Integer.parseInt(request.getParameter("acao"));
        } catch (NumberFormatException e) {
            exibirMensagem(out, "Ação inválida!", "vendas.jsp");
            return;
        }

        // Verificação de hierarquia
        if (hierarquia == 2) {
            acao = 0;
        }

        switch (acao) {
            case 0: // Sem autorização
                try {
                    response.sendRedirect("registrar_venda.jsp");     
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "vendas.jsp");
                }
                break;     

            case 1: // Create - Cadastro de venda
                try {
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
      
                    // Cadastro da venda no banco
                    if (VendaDAO.registrar(venda)) {
                        for (ItemOrcamento i : itensOrcamento) {
                            for (String j : itens) {
                                if (i.getId() == Integer.parseInt(j)) {
                                    i.setStatusVenda(true);
                                    i.setDataHora(LocalDateTime.now());
                                    ItemOrcamentoDAO.alterar(i);
                                }
                            }
                        }
                        
                        Orcamento o = OrcamentoDAO.listarPorId(idOrcamento);
                        o.setStatus("Pendente");
                        
                        int veri = 0;
                        for (ItemOrcamento i : itensOrcamento) {
                            if (!i.isStatusVenda()) {
                                veri = 1;
                            }
                        }
                        
                        if (veri == 0) {
                            o.setStatus("Concluído");
                        }
                        
                        OrcamentoDAO.alterar(o);
                        
                        exibirMensagem(out, "Venda registrada com sucesso!", "vendas.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao registrar venda!", "vendas.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "vendas.jsp");
                }
                break;

            case 2: // Update - Atualização de venda
                try {
                    venda.setId(Integer.parseInt(request.getParameter("id_venda")));
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
      
                    // Cadastro da venda no banco
                    if (VendaDAO.alterar(venda)) {
                        for (ItemOrcamento i : itensOrcamento) {
                            for (String j : itens) {
                                if (i.getId() == Integer.parseInt(j)) {
                                    i.setStatusVenda(true);
                                    i.setDataHora(LocalDateTime.now());
                                    ItemOrcamentoDAO.alterar(i);
                                }
                            }
                        }
                        
                        Orcamento o = OrcamentoDAO.listarPorId(idOrcamento);
                        o.setStatus("Pendente");
                        
                        int veri = 0;
                        for (ItemOrcamento i : itensOrcamento) {
                            if (!i.isStatusVenda()) {
                                veri = 1;
                            }
                        }
                        
                        if (veri == 0) {
                            o.setStatus("Concluído");
                        }
                        
                        OrcamentoDAO.alterar(o);
                        
                        exibirMensagem(out, "Venda atualizada com sucesso!", "vendas.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao atualizar a venda!", "vendas.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "vendas.jsp");
                }
                break;

            case 3: // Excluir venda
                try {
                    if (hierarquia != 0) {
                        exibirMensagem(out, "Você não tem autorização para realizar essa ação!", "vendas.jsp");
                        return;
                    }
                    
                    int id = Integer.parseInt(request.getParameter("id"));

                    venda = VendaDAO.listarPorId(id);
      
                    // Excluir a venda no banco
                    if (VendaDAO.excluir(id)) {
                        OrcamentoDAO.excluir(venda.getOrcamento().getId());
                        exibirMensagem(out, "Venda excluída com sucesso!", "vendas.jsp");
                    } else {
                        exibirMensagem(out, "Erro ao excluir venda!", "vendas.jsp");
                    }
                } catch (Exception e) {
                    exibirMensagem(out, "Erro interno! Contate o administrador do sistema.", "vendas.jsp");
                }
                break;

            default:
                exibirMensagem(out, "Ação inválida!", "vendas.jsp");
                break;
        }
    }
}
