package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Menu;
import model.Perfil;

public class PerfilDAO {

    public static boolean cadastrar(Perfil perfil) {
        String sql = "INSERT INTO perfil (nome, descricao, hierarquia, status) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, perfil.getNome());
            pstm.setString(2, perfil.getDescricao());
            pstm.setInt(3, perfil.getHierarquia());
            pstm.setBoolean(4, perfil.isStatus());

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

    public static List<Perfil> listar() {
        String sql = "SELECT * FROM perfil";
        List<Perfil> perfis = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Perfil perfil = new Perfil();
                perfil.setId(rset.getInt("id"));
                perfil.setNome(rset.getString("nome"));
                perfil.setDescricao(rset.getString("descricao"));
                perfil.setHierarquia(rset.getInt("hierarquia"));
                perfil.setStatus(rset.getBoolean("status"));

                perfis.add(perfil);
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

        return perfis;
    }

    public static Perfil listarPorId(int id) {
        String sql = "SELECT * FROM perfil WHERE id = ?";
        Perfil perfil = new Perfil();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);
            rset = pstm.executeQuery();

            if (rset.next()) {
                perfil.setId(rset.getInt("id"));
                perfil.setNome(rset.getString("nome"));
                perfil.setDescricao(rset.getString("descricao"));
                perfil.setHierarquia(rset.getInt("hierarquia"));
                perfil.setStatus(rset.getBoolean("status"));
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

        return perfil;
    }

    public static boolean alterar(Perfil perfil) {
        String sql = "UPDATE perfil SET nome = ?, descricao = ?, hierarquia = ?, status = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, perfil.getNome());
            pstm.setString(2, perfil.getDescricao());
            pstm.setInt(3, perfil.getHierarquia());
            pstm.setBoolean(4, perfil.isStatus());
            pstm.setInt(5, perfil.getId());

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

    public static boolean ativar(Perfil perfil) {

        String sql = ""
                + " UPDATE perfil"
                + " SET"
                + " status = ?"
                + " WHERE"
                + " id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setBoolean(1, true);
            pstm.setInt(2, perfil.getId());

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

    public static boolean desativar(Perfil perfil) {

        String sql = ""
                + " UPDATE perfil"
                + " SET"
                + " status = ?"
                + " WHERE"
                + " id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setBoolean(1, false);
            pstm.setInt(2, perfil.getId());

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
    
     public static boolean excluir(Perfil perfil) {

        String sqlUpdate = "UPDATE usuario SET id_perfil = 5 WHERE id_perfil = ?"; 
         
        String sql = " DELETE FROM perfil WHERE id = ?;";

        Connection conn = null;
        PreparedStatement pstm = null;
        PreparedStatement pstmUpdate = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            
            pstmUpdate = (PreparedStatement) conn.prepareStatement(sqlUpdate);
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            
            pstmUpdate.setInt(1, perfil.getId());
            pstm.setInt(1, perfil.getId());

            pstmUpdate.execute();
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
                if (pstmUpdate != null) {
                    pstmUpdate.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static boolean vincular(int id_perfil, int id_menu) {

        String sql = "INSERT INTO perfil_menu (id_perfil, id_menu) VALUES (?,?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);

            pstm.setInt(1, id_perfil);
            pstm.setInt(2, id_menu);
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

    public static boolean desvincular(int id_perfil, int id_menu) {

        String sql = "DELETE FROM perfil_menu WHERE id_perfil = ? AND id_menu = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);

            pstm.setInt(1, id_perfil);
            pstm.setInt(2, id_menu);
            pstm.execute();

            return true;

        } catch (Exception e) {

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

    public static List<Menu> listarMenus(int id_perfil) {

        String sql = "SELECT id_menu FROM perfil_menu WHERE id_perfil = ?";

        List<Menu> menus = new ArrayList<Menu>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);
            pstm.setInt(1, id_perfil);
            rset = pstm.executeQuery();

            while (rset.next()) {

                Menu menu = MenuDAO.listarPorId(rset.getInt("id_menu"));
                menus.add(menu);

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

        return menus;

    }
}
