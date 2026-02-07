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

    public static List<Venda> listarPaginado(int start, int length, String searchValue, String filterColumn, String filterType) {
        StringBuilder sql = new StringBuilder(
            "SELECT v.* FROM venda v " +
            "LEFT JOIN usuario u ON v.id_usuario = u.id " +
            "LEFT JOIN orcamento o ON v.id_orcamento = o.id " +
            "LEFT JOIN cliente c ON o.id_cliente = c.id"
        );
        List<Object> params = new ArrayList<>();

        if (searchValue != null && !searchValue.isEmpty() && filterColumn != null && !filterColumn.isEmpty()) {
            // Colunas: 0=#(id), 1=Cliente, 2=Vendedor, 3=Data, 4=Valor, 5=Forma Pagamento
            String[] columns = {"v.id", "c.nome", "u.nome", "v.data", "v.valor", "v.forma_pagamento"};
            try {
                int columnIndex = Integer.parseInt(filterColumn);
                if (columnIndex >= 0 && columnIndex < columns.length) {
                    String column = columns[columnIndex];
                    sql.append(" WHERE ").append(column).append(" LIKE ?");

                    if ("0".equals(filterType)) { // Começa com
                        params.add(searchValue + "%");
                    } else { // Inclui
                        params.add("%" + searchValue + "%");
                    }
                }
            } catch (NumberFormatException e) {
                // Tratar erro se filterColumn não for um número válido
            }
        }

        sql.append(" ORDER BY v.id DESC LIMIT ? OFFSET ?");
        params.add(length);
        params.add(start);

        List<Venda> vendas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                pstm.setObject(i + 1, params.get(i));
            }

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
                    // Ignorar vendas com dados inválidos
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vendas;
    }

    public static int contarTodos() {
        String sql = "SELECT COUNT(id) FROM venda";
        int total = 0;

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();
            if (rset.next()) {
                total = rset.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public static int contarFiltrados(String searchValue, String filterColumn, String filterType) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(v.id) FROM venda v " +
            "LEFT JOIN usuario u ON v.id_usuario = u.id " +
            "LEFT JOIN orcamento o ON v.id_orcamento = o.id " +
            "LEFT JOIN cliente c ON o.id_cliente = c.id"
        );
        List<Object> params = new ArrayList<>();
        int total = 0;

        if (searchValue != null && !searchValue.isEmpty() && filterColumn != null && !filterColumn.isEmpty()) {
            // Colunas: 0=#(id), 1=Cliente, 2=Vendedor, 3=Data, 4=Valor, 5=Forma Pagamento
            String[] columns = {"v.id", "c.nome", "u.nome", "v.data", "v.valor", "v.forma_pagamento"};
            try {
                int columnIndex = Integer.parseInt(filterColumn);
                if (columnIndex >= 0 && columnIndex < columns.length) {
                    String column = columns[columnIndex];
                    sql.append(" WHERE ").append(column).append(" LIKE ?");

                    if ("0".equals(filterType)) { // Começa com
                        params.add(searchValue + "%");
                    } else { // Inclui
                        params.add("%" + searchValue + "%");
                    }
                }
            } catch (NumberFormatException e) {
                // Tratar erro se filterColumn não for um número válido
            }
        }

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                pstm.setObject(i + 1, params.get(i));
            }

            rset = pstm.executeQuery();
            if (rset.next()) {
                total = rset.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return total;
    }

}
