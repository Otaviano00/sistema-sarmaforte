package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Perfil;
import model.Usuario;

public class UsuarioDAO {

    public static boolean cadastrar(Usuario usuario) {

        String sql = ""
                + " INSERT INTO"
                + " usuario (nome, telefone, login, senha, cpf, email, status, id_perfil)"
                + " VALUES"
                + " (?,?,?,?,?,?,?,?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {

            conn = Conexao.criarConexaoMySQL();

            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setString(1, usuario.getNome());
            pstm.setString(2, usuario.getTelefone());
            pstm.setString(3, usuario.getLogin());
            pstm.setString(4, usuario.getSenha());
            pstm.setString(5, usuario.getCpf());
            pstm.setString(6, usuario.getEmail());
            pstm.setBoolean(7, usuario.isStatus());
            pstm.setInt(8, usuario.getPerfil().getId());

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

    public static List<Usuario> listar() {

        String sql = ""
                + " SELECT"
                + " id,"
                + " nome,"
                + " telefone,"
                + " login,"
                + " senha,"
                + " cpf,"
                + " email,"
                + " id_perfil,"
                + " status "
                + " FROM"
                + " usuario";

        List<Usuario> usuarios = new ArrayList<Usuario>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {

                Perfil perfil = PerfilDAO.listarPorId(rset.getInt("id_perfil"));

                Usuario usuario = new Usuario();

                usuario.setId(rset.getInt("id"));
                usuario.setNome(rset.getString("nome"));
                usuario.setTelefone(rset.getString("telefone"));
                usuario.setLogin(rset.getString("login"));
                usuario.setSenha(rset.getString("senha"));
                usuario.setCpf(rset.getString("cpf"));
                usuario.setEmail(rset.getString("email"));
                usuario.setPerfil(perfil);
                usuario.setStatus(rset.getBoolean("status"));

                usuarios.add(usuario);

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
        
        return usuarios;
    }

    public static Usuario listarPorId(int id) {

        /*
        
        NOTA:
        Este está sucetivel á erro caso o id adicionado não existir no banco de dados.
        Acabei de corrigir o erro, caso o id não for encontrado no banco, retornará um objeto com os atributos vazios e nulos.
       
         */
        String sql = ""
                + " SELECT"
                + " id,"
                + " nome,"
                + " telefone,"
                + " login,"
                + " senha,"
                + " cpf,"
                + " email,"
                + " id_perfil,"
                + " status"
                + " FROM"
                + " usuario WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        Usuario usuario = new Usuario();

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setInt(1, id);
            pstm.execute();

            rset = pstm.executeQuery();

            if (rset.next()) {

                Perfil perfil = PerfilDAO.listarPorId(rset.getInt("id_perfil"));    
              
                usuario.setId(rset.getInt("id"));
                usuario.setNome(rset.getString("nome"));
                usuario.setTelefone(rset.getString("telefone"));
                usuario.setLogin(rset.getString("login"));
                usuario.setSenha(rset.getString("senha"));
                usuario.setCpf(rset.getString("cpf"));
                usuario.setEmail(rset.getString("email"));
                usuario.setPerfil(perfil);
                usuario.setStatus(rset.getBoolean("status"));

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

        return usuario;

    }

    public static boolean alterar(Usuario usuario) {

        /*
        
        NOTA:
        Para esse metodo dar certo, precisaremos do atributo Id, principalmente de um Id existente.
        
         */
        String sql = ""
                + " UPDATE usuario"
                + " SET"
                + " nome = ?,"
                + " telefone = ?,"
                + " login = ?,"
                + " senha = ?,"
                + " cpf = ?,"
                + " email = ?,"
                + " status = ?,"
                + " id_perfil = ?"
                + " WHERE"
                + " id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setString(1, usuario.getNome());
            pstm.setString(2, usuario.getTelefone());
            pstm.setString(3, usuario.getLogin());
            pstm.setString(4, usuario.getSenha());
            pstm.setString(5, usuario.getCpf());
            pstm.setString(6, usuario.getEmail());
            pstm.setBoolean(7, usuario.isStatus());
            pstm.setInt(8, usuario.getPerfil().getId());
            pstm.setInt(9, usuario.getId());

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

    public static boolean ativar(Usuario usuario) {

        String sql = ""
                + " UPDATE usuario"
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
            pstm.setInt(2, usuario.getId());

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

    public static boolean desativar(Usuario usuario) {

        String sql = ""
                + " UPDATE usuario"
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
            pstm.setInt(2, usuario.getId());

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

        String sql = " DELETE FROM usuario"
                + " WHERE"
                + " id = ?";

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
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static Usuario efetuarLogin(String login, String senha) {

        String sql = ""
                + " SELECT"
                + " id"
                + " FROM"
                + " usuario"
                + " WHERE"
                + " login = ?"
                + " AND"
                + " senha = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setString(1, login);
            pstm.setString(2, senha);
            pstm.execute();

            rset = pstm.executeQuery();

            if (rset.next()) {
                Usuario usuario = listarPorId(rset.getInt("id"));
      
                return usuario;

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

        return null;

    }

}
