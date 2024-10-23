package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Produto;

public class ProdutoDAO {

    public boolean cadastrar(Produto produto) {
        String sql = "INSERT INTO produto (codigo, descricao, nome, quantidade, quantidadeCritica, imagem, fornecedor, preco, custo, status) VALUES (?,?,?,?,?,?,?,?,?,?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);

            pstm.setInt(1, produto.getCodigo());
            pstm.setString(2, produto.getDescricao());
            pstm.setString(3, produto.getNome());
            pstm.setInt(4, produto.getQuantidade());
            pstm.setInt(5, produto.getQuantidadeCritica());
            pstm.setString(6, produto.getImagem());
            pstm.setString(7, produto.getFornecedor());
            pstm.setDouble(8, produto.getPreco());
            pstm.setDouble(9, produto.getCusto());
            pstm.setBoolean(10, produto.isStatus());

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

    public List<Produto> listar() {
        String sql = "SELECT * FROM produto";
        List<Produto> produtos = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Produto produto = new Produto();
                produto.setCodigo(rset.getInt("codigo"));
                produto.setDescricao(rset.getString("descricao"));
                produto.setNome(rset.getString("nome"));
                produto.setQuantidade(rset.getInt("quantidade"));
                produto.setQuantidadeCritica(rset.getInt("quantidadeCritca"));
                produto.setImagem(rset.getString("imagem"));
                produto.setFornecedor(rset.getString("fornecedor"));
                produto.setPreco(rset.getDouble("preco"));
                produto.setCusto(rset.getDouble("custo"));
                produto.setStatus(rset.getBoolean("status"));

                produtos.add(produto);
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

        return produtos;
    }

    public Produto listarPorId(int codigo) {
        String sql = "SELECT * from produto WHERE codigo = ?";
        Produto produto = new Produto();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, codigo);
            rset = pstm.executeQuery();

            if (rset.next()) {
                produto.setCodigo(rset.getInt("codigo"));
                produto.setDescricao(rset.getString("descricao"));
                produto.setNome(rset.getString("nome"));
                produto.setQuantidade(rset.getInt("quantidade"));
                produto.setQuantidadeCritica(rset.getInt("quantidadeCritica"));
                produto.setImagem(rset.getString("imagem"));
                produto.setFornecedor(rset.getString("fornecedor"));
                produto.setPreco(rset.getDouble("preco"));
                produto.setCusto(rset.getDouble("custo"));
                produto.setStatus(rset.getBoolean("status"));

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

        return produto;
    }

    public boolean alterar(Produto produto) {
        String sql = "UPDATE produto SET codigo = ?, descricao = ?, nome = ? quantidade = ?, quantidadeCritica = ?, imagem = ?, fornecedor = ?, preco = ?, custo = ?, status = ? WHERE codigo = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);

            pstm.setInt(1, produto.getCodigo());
            pstm.setString(2, produto.getDescricao());
            pstm.setString(3, produto.getNome());
            pstm.setInt(4, produto.getQuantidade());
            pstm.setInt(5, produto.getQuantidadeCritica());
            pstm.setString(6, produto.getImagem());
            pstm.setString(7, produto.getFornecedor());
            pstm.setDouble(8, produto.getPreco());
            pstm.setDouble(9, produto.getCusto());
            pstm.setBoolean(10, produto.isStatus());

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

    public boolean ativar(Produto produto) {

        String sql = ""
                + " UPDATE produto"
                + " SET"
                + " status = ?"
                + " WHERE"
                + " codigo = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        
        try{
            
            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setBoolean(1, true);
            pstm.setInt(2, produto.getCodigo());
            
            pstm.execute();
            
            return true;
        }catch (Exception e) {

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
    public boolean desativar (Produto produto) {
        
        String sql = ""
                + " UPDATE produto"
                + " SET"
                + " status = ?"
                + " WHERE"
                + " codigo = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setBoolean(1, false);
            pstm.setInt(2, produto.getCodigo());
            
            pstm.execute();
            
            return true;
    }catch (Exception e) {

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
