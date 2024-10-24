package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import model.Relatorio;

public class RelatorioDAO {

    public static boolean criar(Relatorio relatorio) {
        String sql = "INSER INTO relatorio (id, nome, descricao, dataCriacao, arquivo) VALUES (?,?,?,?,?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);

            pstm.setInt(1, relatorio.getId());
            pstm.setString(2, relatorio.getNome());
            pstm.setString(3, relatorio.getDescricao());
            pstm.setDate(4, Date.valueOf(relatorio.getDataCriacao()));
            pstm.setString(5, relatorio.getArquivo());
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

    public static List<Relatorio> listar() {

        String sql = "SELECT * FROM relatorio";
        List<Relatorio> relatorios = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Relatorio relatorio = new Relatorio();
                relatorio.setId(rset.getInt("id"));
                relatorio.setNome(rset.getString("nome"));
                relatorio.setDescricao(rset.getString("descricao"));
                relatorio.setDataCriacao(rset.getDate("dataCriacao").toLocalDate());
                relatorio.setArquivo(rset.getString("arquivo"));

                relatorios.add(relatorio);

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

        return relatorios;
    }

    public static Relatorio listarPorId(int id) {
        String sql = "SELECT * FROM relatorio WHERE id = ?";
        Relatorio relatorio = new Relatorio();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);
            rset = pstm.executeQuery();

            if (rset.next()) {
                relatorio.setId(rset.getInt("id"));
                relatorio.setNome(rset.getString("nome"));
                relatorio.setDescricao(rset.getString("descricao"));
                relatorio.setDataCriacao(rset.getDate("dataCriacao").toLocalDate());
                relatorio.setArquivo(rset.getString("arquivo"));

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
        return relatorio;
    }

    public static boolean excluir(int id) {
        String sql = "DELETE FROM relatorio WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setInt(1, id);
            pstm.execute();

        }catch (Exception e) {
            e.printStackTrace();
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
        return false;
       
    }
   
}
