package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Perfil;
import model.Usuario;

public class UsuarioDAO {

    public boolean cadastrar(Usuario usuario) {

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

    public List<Usuario> listar() {

        String sql = ""
                + " SELECT"
                + " u.id,"
                + " u.nome,"
                + " u.telefone,"
                + " u.login,"
                + " u.senha,"
                + " u.cpf,"
                + " u.email,"
                + " u.id_perfil,"
                + " u.status,"
                + " p.nome,"
                + " p.descricao,"
                + " p.hierarquia,"
                + " p.status"
                + " FROM"
                + " usuario u INNER JOIN perfil p"
                + " ON"
                + " u.id_perfil = p.id";

        List<Usuario> usuarios = new ArrayList<Usuario>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {

                Perfil perfil = new Perfil();

                perfil.setId(rset.getInt("u.id_perfil"));
                perfil.setNome(rset.getString("p.nome"));
                perfil.setDescricao(rset.getString("p.descricao"));
                perfil.setHierarquia(rset.getInt("p.hierarquia"));
                perfil.setStatus(rset.getBoolean("p.status"));

                Usuario usuario = new Usuario();

                usuario.setId(rset.getInt("u.id"));
                usuario.setNome(rset.getString("u.nome"));
                usuario.setTelefone(rset.getString("u.telefone"));
                usuario.setLogin(rset.getString("u.login"));
                usuario.setSenha(rset.getString("u.senha"));
                usuario.setCpf(rset.getString("u.cpf"));
                usuario.setEmail(rset.getString("u.email"));
                usuario.setPerfil(perfil);
                usuario.setStatus(rset.getBoolean("u.status"));

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

    public Usuario listarPorId(int id) {

        /*
        
        NOTA:
        Este está sucetivel á erro caso o id adicionado não existir no banco de dados.
        Acabei de corrigir o erro, caso o id não for encontrado no banco, retornará um objeto com os atributos vazios e nulos.
       
         */
        String sql = ""
                + " SELECT"
                + " u.id,"
                + " u.nome,"
                + " u.telefone,"
                + " u.login,"
                + " u.senha,"
                + " u.cpf,"
                + " u.email,"
                + " u.id_perfil,"
                + " u.status,"
                + " p.nome,"
                + " p.descricao,"
                + " p.hierarquia,"
                + " p.status"
                + " FROM"
                + " usuario u INNER JOIN perfil p"
                + " ON"
                + " u.id_perfil = p.id"
                + " WHERE"
                + " u.id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        Perfil perfil = new Perfil();
        Usuario usuario = new Usuario();

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setInt(1, id);
            pstm.execute();

            rset = pstm.executeQuery();

            if (rset.next()) {

                perfil.setId(rset.getInt("u.id_perfil"));
                perfil.setNome(rset.getString("p.nome"));
                perfil.setDescricao(rset.getString("p.descricao"));
                perfil.setHierarquia(rset.getInt("p.hierarquia"));
                perfil.setStatus(rset.getBoolean("p.status"));

                usuario.setId(rset.getInt("u.id"));
                usuario.setNome(rset.getString("u.nome"));
                usuario.setTelefone(rset.getString("u.telefone"));
                usuario.setLogin(rset.getString("u.login"));
                usuario.setSenha(rset.getString("u.senha"));
                usuario.setCpf(rset.getString("u.cpf"));
                usuario.setEmail(rset.getString("u.email"));
                usuario.setPerfil(perfil);
                usuario.setStatus(rset.getBoolean("u.status"));

            } else {

                perfil.setId(0);
                perfil.setNome(null);
                perfil.setDescricao(null);
                perfil.setHierarquia(0);
                perfil.setStatus(false);

                usuario.setId(0);
                usuario.setNome(null);
                usuario.setTelefone(null);
                usuario.setLogin(null);
                usuario.setSenha(null);
                usuario.setCpf(null);
                usuario.setEmail(null);
                usuario.setPerfil(perfil);
                usuario.setStatus(false);

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

    public boolean alterar(Usuario usuario) {

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

    public boolean ativar(Usuario usuario) {

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

    public boolean desativar(Usuario usuario) {

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

    public boolean efetuarLogin(String login, String senha) {

        String sql = ""
                + " SELECT"
                + " u.login,"
                + " u.senha"
                + " FROM"
                + " usuario u INNER JOIN perfil p"
                + " ON"
                + " u.id_perfil = p.id"
                + " WHERE"
                + " u.login = ?"
                + " AND"
                + " u.senha = ?"
                + " AND"
                + " u.status = ?"
                + " AND"
                + " p.status = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setString(1, login);
            pstm.setString(2, senha);
            pstm.setBoolean(3, true);
            pstm.setBoolean(4, true);
            pstm.execute();

            rset = pstm.executeQuery();

            if (rset.next()) {
                return true;
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

        return false;

    }

    public void efetuarLogOff() {
        // Não faço ideia do que esse metodo faz.
    }

}