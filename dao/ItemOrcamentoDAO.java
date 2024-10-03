package dao;

import java.sql.Connection;
import java.sql.Timestamp;
// localdate now =  localdatetime.now; setTimestamp(1, Timestamp.valueOF(now))
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import model.ItemOrcamento;

public class ItemOrcamentoDAO {

    /*
    
    NOTA:
    
    Não estou satisfeito com a implementação do LocalDateTime. 
    Acredito que exista alguma outra forma de implementar isso.
    
     */
    public boolean alterar(ItemOrcamento item) {
        String sql = ""
                + " UPDATE itemorcamento"
                + " SET"
                + " produto = ?"
                + " quantidade = ?"
                + " preco = ?"
                + " datahora = ?"
                + " statusvenda = ?"
                + " WHERE"
                + " id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            LocalDateTime data = item.getDataHora();
            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setInt(1, item.getProduto().getCodigo());
            pstm.setInt(2, item.getQuantidade());
            pstm.setDouble(3, item.getPreco());
            pstm.setTimestamp(4, Timestamp.valueOf(data));
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
