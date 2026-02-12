package dao;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.ItemOrcamento;
import model.Orcamento;
import model.Produto;

public class ItemOrcamentoDAO {

    public static List<ItemOrcamento> listarPorOrcamento(int idOrcamento) {
        String sql = "SELECT * FROM item_orcamento WHERE id_orcamento = ?";
        List<ItemOrcamento> itens = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, idOrcamento);
            rset = pstm.executeQuery();

            Orcamento orcamento = OrcamentoDAO.listarPorId(idOrcamento);

            while (rset.next()) {
                try {
                    Timestamp sqlDataHora = rset.getTimestamp("dataHora");
                    Produto produto = ProdutoDAO.listarPorId(rset.getInt("id_produto"));
                    ItemOrcamento item = new ItemOrcamento();

                    item.setId(rset.getInt("id"));
                    item.setQuantidade(rset.getDouble("quantidade"));
                    item.setPreco(rset.getDouble("preco"));
                    item.setDataHora(sqlDataHora.toLocalDateTime());
                    item.setStatusVenda(rset.getBoolean("statusVenda"));
                    item.setOrcamento(orcamento);
                    item.setProduto(produto);

                    itens.add(item);
                } catch (Exception e) {
                    // Ignorar registros com erro
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
        return itens;
    }

    public static ItemOrcamento listarPorId(int id) {
        String sql = "SELECT * FROM item_orcamento WHERE id = ?";
        ItemOrcamento item = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);
            rset = pstm.executeQuery();

            if (rset.next()) {
                Timestamp sqlDataHora = rset.getTimestamp("dataHora");
                Produto produto = ProdutoDAO.listarPorId(rset.getInt("id_produto"));
                Orcamento orcamento = OrcamentoDAO.listarPorId(rset.getInt("id_orcamento"));

                item = new ItemOrcamento();
                item.setId(rset.getInt("id"));
                item.setQuantidade(rset.getDouble("quantidade"));
                item.setPreco(rset.getDouble("preco"));
                item.setDataHora(sqlDataHora.toLocalDateTime());
                item.setStatusVenda(rset.getBoolean("statusVenda"));
                item.setOrcamento(orcamento);
                item.setProduto(produto);
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
        return item;
    }

    public static List<ItemOrcamento> listarPaginadoPorOrcamento(int idOrcamento, int start, int length, String searchValue, String filterColumn, String filterType) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM item_orcamento WHERE id_orcamento = ?");

        List<Object> params = new ArrayList<>();
        params.add(idOrcamento);

        if (searchValue != null && !searchValue.isEmpty() && filterColumn != null && !filterColumn.isEmpty()) {
            String[] columns = {"id", "quantidade", "preco", "dataHora", "statusVenda", "id_produto"};
            try {
                int columnIndex = Integer.parseInt(filterColumn);
                if (columnIndex >= 0 && columnIndex < columns.length) {
                    String column = columns[columnIndex];
                    sql.append(" AND ").append(column).append(" LIKE ?");

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

        sql.append(" ORDER BY id LIMIT ? OFFSET ?");
        params.add(length);
        params.add(start);

        List<ItemOrcamento> itens = new ArrayList<>();
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

            Orcamento orcamento = OrcamentoDAO.listarPorId(idOrcamento);

            while (rset.next()) {
                try {
                    Timestamp sqlDataHora = rset.getTimestamp("dataHora");
                    Produto produto = ProdutoDAO.listarPorId(rset.getInt("id_produto"));
                    ItemOrcamento item = new ItemOrcamento();

                    item.setId(rset.getInt("id"));
                    item.setQuantidade(rset.getDouble("quantidade"));
                    item.setPreco(rset.getDouble("preco"));
                    item.setDataHora(sqlDataHora.toLocalDateTime());
                    item.setStatusVenda(rset.getBoolean("statusVenda"));
                    item.setOrcamento(orcamento);
                    item.setProduto(produto);

                    itens.add(item);
                } catch (Exception e) {
                    // Ignorar registros com erro
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
        return itens;
    }

    public static int contarTodosPorOrcamento(int idOrcamento) {
        String sql = "SELECT COUNT(id) FROM item_orcamento WHERE id_orcamento = ?";
        int total = 0;

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, idOrcamento);
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

    public static int contarFiltradosPorOrcamento(int idOrcamento, String searchValue, String filterColumn, String filterType) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(id) FROM item_orcamento WHERE id_orcamento = ?");
        List<Object> params = new ArrayList<>();
        params.add(idOrcamento);
        int total = 0;

        if (searchValue != null && !searchValue.isEmpty() && filterColumn != null && !filterColumn.isEmpty()) {
            String[] columns = {"id", "quantidade", "preco", "dataHora", "statusVenda", "id_produto"};
            try {
                int columnIndex = Integer.parseInt(filterColumn);
                if (columnIndex >= 0 && columnIndex < columns.length) {
                    String column = columns[columnIndex];
                    sql.append(" AND ").append(column).append(" LIKE ?");

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

    public static boolean alterar(ItemOrcamento item) {

        String sql = ""
                + " UPDATE item_orcamento"
                + " SET"
                + " quantidade = ?,"
                + " preco = ?,"
                + " datahora = ?,"
                + " statusvenda = ?,"
                + " id_orcamento = ?,"
                + " id_produto = ?"
                + " WHERE"
                + " id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {

            conn = Conexao.criarConexaoMySQL();

            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setDouble(1, item.getQuantidade());
            pstm.setDouble(2, item.getPreco());
            pstm.setTimestamp(3, Timestamp.valueOf(item.getDataHora()));
            pstm.setBoolean(4, item.isStatusVenda());
            pstm.setInt(5, item.getOrcamento().getId());
            pstm.setInt(6, item.getProduto().getCodigo());
            pstm.setInt(7, item.getId());

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

    public static double calcularTotalOrcamento(int idOrcamento) {
        String sql = "SELECT SUM(quantidade * preco) as total FROM item_orcamento WHERE id_orcamento = ?";
        double total = 0.0;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, idOrcamento);
            rset = pstm.executeQuery();

            if (rset.next()) {
                total = rset.getDouble("total");
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


