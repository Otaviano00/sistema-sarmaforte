/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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
        PrintWriter out = response.getWriter();
        String acao = request.getParameter("acao");
        if (acao.equals("registrar")) {
            Orcamento orcamento = new Orcamento();
            orcamento.setDataCriacao(LocalDateTime.now());
            orcamento.setDataValidade(LocalDateTime.now().plusDays(15));
            orcamento.setStatus("pendente");
            boolean ok = OrcamentoDAO.registrar(orcamento);
            
            List<Orcamento> orcamentos = OrcamentoDAO.listar();
            String idOrcamento = String.valueOf(orcamentos.get(orcamentos.size()-1).getId());
 
            response.sendRedirect("registrar_orcamento.jsp?id=" + idOrcamento + "&acao=registrar");
        } else if (acao.equals("mudarCliente")) {
            String acaoOrcamento = request.getParameter("acaoOrcamento");
            int idOrcamento = Integer.parseInt(request.getParameter("idOrcamento"));
            int idCliente = Integer.parseInt(request.getParameter("idCliente"));

            Orcamento orcamento = OrcamentoDAO.listarPorId(idOrcamento);
            orcamento.getCliente().setId(idCliente);
            OrcamentoDAO.alterar(orcamento);
            
            response.sendRedirect("registrar_orcamento.jsp?id=" + idOrcamento + "&acao="+ acaoOrcamento +"");
        } else if (acao.equals("adicionarItem")) {
            String acaoOrcamento = request.getParameter("acao_orcamento");
            
            int idProduto = Integer.parseInt(request.getParameter("id_produto"));
            Produto produto = ProdutoDAO.listarPorId(idProduto);
            int idOrcamento = Integer.parseInt(request.getParameter("id_orcamento"));
            Orcamento orcamento = OrcamentoDAO.listarPorId(idOrcamento);
            int quantidade = Integer.parseInt(request.getParameter("quantidade_produto"));
            double preco = ProdutoDAO.listarPorId(idProduto).getPreco();
            LocalDateTime dataHora = LocalDateTime.now();
            boolean statusVenda = false;

            ItemOrcamento item = new ItemOrcamento();
            
            item.setProduto(produto);
            item.setOrcamento(orcamento);
            item.setQuantidade(quantidade);
            item.setPreco(preco);
            item.setStatusVenda(statusVenda);
            item.setDataHora(dataHora);
            
            OrcamentoDAO.adicionarItem(item);
           
            response.sendRedirect("registrar_orcamento.jsp?id=" + idOrcamento + "&acao="+ acaoOrcamento +"");
          
        } else if (acao.equals("excluir")) {
            
            int idOrcamento = Integer.parseInt(request.getParameter("idOrcamento"));
            
            OrcamentoDAO.excluir(idOrcamento);
            
            response.sendRedirect("orcamentos.jsp");
        } else if (acao.equals("excluirItem")) {
            String acaoOrcamento = request.getParameter("acaoOrcamento");
            
            int idItem = Integer.parseInt(request.getParameter("idItem"));
            int idOrcamento = Integer.parseInt(request.getParameter("idOrcamento"));
            
            OrcamentoDAO.excluirItem(idItem);
            
            response.sendRedirect("registrar_orcamento.jsp?id=" + idOrcamento + "&acao="+ acaoOrcamento +"");
        } else if (acao.equals("alterarItem")) {
            String acaoOrcamento = request.getParameter("acao_orcamento");
            
            int idProduto = Integer.parseInt(request.getParameter("id_produto"));
            Produto produto = ProdutoDAO.listarPorId(idProduto);
            int idOrcamento = Integer.parseInt(request.getParameter("id_orcamento"));
            Orcamento orcamento = OrcamentoDAO.listarPorId(idOrcamento);
            int quantidade = Integer.parseInt(request.getParameter("quantidade_produto"));
            double preco = ProdutoDAO.listarPorId(idProduto).getPreco();
            LocalDateTime dataHora = LocalDateTime.now();
            boolean statusVenda = false;
            int id = Integer.parseInt(request.getParameter("id_item"));

            ItemOrcamento item = new ItemOrcamento();
            
            item.setProduto(produto);
            item.setOrcamento(orcamento);
            item.setQuantidade(quantidade);
            item.setPreco(preco);
            item.setStatusVenda(statusVenda);
            item.setDataHora(dataHora);
            item.setId(id);
            
            ItemOrcamentoDAO.alterar(item);
            
            response.sendRedirect("registrar_orcamento.jsp?id=" + idOrcamento + "&acao="+ acaoOrcamento +"");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        String acao = request.getParameter("acao");
        if (acao.equals("adicionarInformacao")) {
            String acaoOrcamento = request.getParameter("acaoOrcamento");
            int idOrcamento = Integer.parseInt(request.getParameter("id_orcamento"));
            String informacao = request.getParameter("informacao");
            
            out.print("<script>");
            out.print("alert('Informação: "+ informacao +"')");
            out.print("</script>");
            
            Orcamento orcamento = OrcamentoDAO.listarPorId(idOrcamento);
            orcamento.setInformacao(informacao);
                    
            OrcamentoDAO.alterar(orcamento);
            
            response.sendRedirect("registrar_orcamento.jsp?id=" + idOrcamento + "&acao="+ acaoOrcamento +"");
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private static void exibirMensagem(String mensagem, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.print("<script>");
        out.print("alert('" + mensagem + "');");
        out.print("location.href = 'orcamentos.jsp';");
        out.print("</script>");
        out.close();
    }
    
}
