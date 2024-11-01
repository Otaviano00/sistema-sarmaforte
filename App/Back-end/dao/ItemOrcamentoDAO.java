package dao;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import model.ItemOrcamento;

public class ItemOrcamentoDAO {

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

            pstm.setInt(1, item.getQuantidade());
            pstm.setDouble(2, item.getPreco());
            pstm.setTimestamp(3, Timestamp.valueOf(item.getDataHora()));
            pstm.setBoolean(4, item.getStatusVenda());
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

}
