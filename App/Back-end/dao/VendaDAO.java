package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import model.Venda;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import model.ItemOrcamento;
import model.Orcamento;
import model.Produto;
import model.Usuario;

public class VendaDAO {

    public static boolean registrar(Venda venda) {

        String sql = "INSERT INTO venda (data, valor, desconto, forma_pagamento, id_usuario, id_orcamento) VALUES (?,?,?,?,?,?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {

            conn = Conexao.criarConexaoMySQL();

            pstm = (PreparedStatement) conn.prepareStatement(sql);
            
            pstm.setTimestamp(1, Timestamp.valueOf(venda.getData())); 
            pstm.setDouble(2, venda.getValor());
            pstm.setDouble(3, venda.getDesconto());
            pstm.setString(4, venda.getFormaPagamento());
            pstm.setInt(5, venda.getUsuario().getId());
            pstm.setInt(6, venda.getOrcamento().getId());

            pstm.execute();

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static List<Venda> listar() {

        String sql = "SELECT * FROM venda";

        List<Venda> vendas = new ArrayList<Venda>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {

                try {

                    Usuario usuario = UsuarioDAO.listarPorId(rset.getInt("id_usuario"));
                    Orcamento orcamento = OrcamentoDAO.listarPorId(rset.getInt("id_orcamento"));
                    
                    Venda venda = new Venda();
                    
                    venda.setId(rset.getInt("id"));
                    venda.setData(rset.getTimestamp("data").toLocalDateTime());
                    venda.setValor(rset.getDouble("valor"));
                    venda.setDesconto(rset.getDouble("desconto"));
                    venda.setFormaPagamento(rset.getString("forma_pagamento"));
                    venda.setUsuario(usuario);
                    venda.setOrcamento(orcamento);

                    vendas.add(venda);

                } catch (Exception e) {

                    /*
                    
                    Deixar vazio ou colocar objetos com atributos nulos.
                    Acho que não vou colocar nada aqui mesmo.
                    
                     */
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return vendas;

    }

    public static Venda listarPorId(int id) {

        String sql = "SELECT * FROM venda WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        Venda venda = new Venda();

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setInt(1, id);
            pstm.execute();

            rset = pstm.executeQuery();

            if (rset.next()) {

                try {

                    Usuario usuario = UsuarioDAO.listarPorId(rset.getInt("id_usuario"));
                    Orcamento orcamento = OrcamentoDAO.listarPorId(rset.getInt("id_orcamento"));

                    venda.setId(rset.getInt("id"));
                    venda.setData(rset.getTimestamp("data").toLocalDateTime());
                    venda.setValor(rset.getDouble("valor"));
                    venda.setDesconto(rset.getDouble("desconto"));
                    venda.setFormaPagamento(rset.getString("forma_pagamento"));
                    venda.setUsuario(usuario);
                    venda.setOrcamento(orcamento);

                } catch (Exception e) {

                    return null;

                }

            } else {

                return null;

            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        return venda;

    }

    public static boolean alterar(Venda venda) {

        String sql = "UPDATE venda SET data = ?, valor = ?, desconto = ?, forma_pagamento = ?, id_usuario = ?, id_orcamento = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setTimestamp(1, Timestamp.valueOf(venda.getData()));
            pstm.setDouble(2, venda.getValor());
            pstm.setDouble(3, venda.getDesconto());
            pstm.setString(4, venda.getFormaPagamento());
            pstm.setInt(5, venda.getUsuario().getId());
            pstm.setInt(6, venda.getOrcamento().getId());
            pstm.setInt(7, venda.getId());

            pstm.execute();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean excluir(int id) {

        String sql = "DELETE FROM venda WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {

            conn = Conexao.criarConexaoMySQL();

            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setInt(1, id);

            pstm.execute();

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           
        }

    }
    
    public static List<ItemOrcamento> listarTodosItensVendidos() {

        String sql = "SELECT * FROM item_orcamento WHERE statusVenda = 1";

        List<ItemOrcamento> itens = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Timestamp sqlDataHora = rset.getTimestamp("dataHora");

                Orcamento orca = OrcamentoDAO.listarPorId(rset.getInt("id_orcamento"));

                Produto pro = ProdutoDAO.listarPorId(rset.getInt("id_produto"));

                ItemOrcamento item = new ItemOrcamento();

                item.setId(rset.getInt("id"));
                item.setQuantidade(rset.getInt("quantidade"));
                item.setPreco(rset.getDouble("preco"));
                item.setDataHora(sqlDataHora.toLocalDateTime());
                item.setStatusVenda(rset.getBoolean("statusVenda"));
                item.setOrcamento(orca);
                item.setProduto(pro);

                itens.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return itens;

    }

}
