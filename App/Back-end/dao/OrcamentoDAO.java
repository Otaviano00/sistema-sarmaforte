package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import model.Orcamento;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import model.Cliente;
import model.ItemOrcamento;
import model.Produto;

public class OrcamentoDAO {

    public static boolean registrar(Orcamento orcamento) {
        String sql = ""
                + " INSERT INTO"
                + " orcamento(dataCriacao, dataValidade, status, informacoes, id_cliente)"
                + " VALUES (?,?,?,?,?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            
            
            pstm.setTimestamp(1, Timestamp.valueOf(orcamento.getDataCriacao()));
            pstm.setTimestamp(2, Timestamp.valueOf(orcamento.getDataValidade()));
            pstm.setString(3, orcamento.getStatus());
            pstm.setString(4, orcamento.getInformacao());
            pstm.setInt(5, orcamento.getCliente().getId());

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

    public static List<Orcamento> listar() {

        String sql = ""
                + " SELECT"
                + " o.id,"
                + " o.dataCriacao,"
                + " o.dataValidade,"
                + " o.status,"
                + " o.informacoes,"
                + " o.id_cliente,"
                + " c.nome,"
                + " c.telefone,"
                + " c.cpf,"
                + " c.endereco"
                + " FROM"
                + " cliente c INNER JOIN orcamento o"
                + " ON"
                + " c.id = o.id_cliente";

        List<Orcamento> orcamentos = new ArrayList<Orcamento>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {

                /*
                
                AVISO:
                
                Caso exista um dificil cenário onde os dados como dataCriacao e dataValidade sejam mal cadastrados e, no banco ficarem 
                assim "0000-00-00 00:00:00", haverá erro ao tentar listar os Orcamentos. Por isso eu criei um tratamento que captura os 
                objetos defeituosos e só mostra os sem erros.
                
                 */
                try {

                    Timestamp sqlCriacao = rset.getTimestamp("o.dataCriacao");
                    Timestamp sqlValidade = rset.getTimestamp("o.dataValidade");
                    
                    Cliente cliente = new Cliente();

                    cliente.setId(rset.getInt("o.id_cliente"));
                    cliente.setNome(rset.getString("c.nome"));
                    cliente.setTelefone(rset.getString("c.telefone"));
                    cliente.setCpf(rset.getString("c.cpf"));
                    cliente.setEndereco(rset.getString("c.endereco"));
                    
                    Orcamento orcamento = new Orcamento();

                    orcamento.setId(rset.getInt("o.id"));
                    orcamento.setDataCriacao(sqlCriacao.toLocalDateTime());
                    orcamento.setDataValidade(sqlValidade.toLocalDateTime());
                    orcamento.setStatus(rset.getString("o.status"));
                    orcamento.setInformacao(rset.getString("o.informacoes"));
                    orcamento.setCliente(cliente);

                    orcamentos.add(orcamento);

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

        return orcamentos;

    }

    public static Orcamento listarPorId(int id) {

        String sql = ""
                + " SELECT"
                + " o.id,"
                + " o.dataCriacao,"
                + " o.dataValidade,"
                + " o.status,"
                + " o.informacoes,"
                + " o.id_cliente,"
                + " c.nome,"
                + " c.telefone,"
                + " c.cpf,"
                + " c.endereco"
                + " FROM"
                + " cliente c INNER JOIN orcamento o"
                + " ON"
                + " c.id = o.id_cliente"
                + " WHERE"
                + " o.id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        Cliente cliente = new Cliente();
        Orcamento orcamento = new Orcamento();

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setInt(1, id);
            pstm.execute();

            rset = pstm.executeQuery();

            if (rset.next()) {

                try {

                    Timestamp sqlCriacao = rset.getTimestamp("o.dataCriacao");
                    Timestamp sqlValidade = rset.getTimestamp("o.dataValidade");

                    cliente.setId(rset.getInt("o.id_cliente"));
                    cliente.setNome(rset.getString("c.nome"));
                    cliente.setTelefone(rset.getString("c.telefone"));
                    cliente.setCpf(rset.getString("c.cpf"));
                    cliente.setEndereco(rset.getString("c.endereco"));

                    orcamento.setId(rset.getInt("o.id"));
                    orcamento.setDataCriacao(sqlCriacao.toLocalDateTime());
                    orcamento.setDataValidade(sqlValidade.toLocalDateTime());
                    orcamento.setStatus(rset.getString("o.status"));
                    orcamento.setInformacao(rset.getString("o.informacoes"));
                    orcamento.setCliente(cliente);

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

        return orcamento;

    }

    public static boolean alterar(Orcamento orcamento) {
        String sql = "UPDATE orcamento SET dataCriacao = ?, dataValidade = ?, status = ?, informacoes = ?, id_cliente = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setTimestamp(1, Timestamp.valueOf(orcamento.getDataCriacao()));
            pstm.setTimestamp(2, Timestamp.valueOf(orcamento.getDataValidade()));
            pstm.setString(3, orcamento.getStatus());
            pstm.setString(4, orcamento.getInformacao());
            pstm.setInt(5, orcamento.getCliente().getId());
            pstm.setInt(6, orcamento.getId());

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

        /*
        
        Só cuidado para não apagar os orcamentos que estão sendo usados como chave estranjeira
        em item_orcamento, nesse caso esse metodo retornará false.
        
         */
        String sql = "DELETE FROM orcamento WHERE id = ?";

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

    public static boolean adicionarItem(ItemOrcamento item) {

        String sql = "INSERT INTO item_orcamento (quantidade, preco, dataHora, statusVenda, id_orcamento, id_produto) VALUES (?,?,?,?,?,?)";

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

    public static boolean excluirItem(int id) {

        String sql = "DELETE FROM item_orcamento WHERE id = ?";

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

    public static List<ItemOrcamento> listarItensOrcamento(int idOrcamento) {

        String sql = ""
                + " SELECT"
                + " i.id, i.quantidade, i.preco, i.dataHora, i.statusVenda, i.id_orcamento, i.id_produto,"
                + " p.codigo, p.descricao, p.nome, p.quantidade, p.quantidadeCritica, p.imagem, p.fornecedor, p.preco, p.custo, p.status"
                + " FROM"
                + " produto p INNER JOIN item_orcamento i ON p.codigo = i.id_produto"
                + " WHERE"
                + " i.id_orcamento = ?";

        List<ItemOrcamento> itens = new ArrayList<ItemOrcamento>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);
            pstm.setInt(1, idOrcamento);
            rset = pstm.executeQuery();

            while (rset.next()) {

                Timestamp sqlDataHora = rset.getTimestamp("i.dataHora");

                Orcamento orca = OrcamentoDAO.listarPorId(idOrcamento);

                Produto pro = new Produto();

                pro.setCodigo(rset.getInt("p.codigo"));
                pro.setDescricao(rset.getString("p.descricao"));
                pro.setNome(rset.getString("p.nome"));
                pro.setQuantidade(rset.getInt("p.quantidade"));
                pro.setQuantidadeCritica(rset.getInt("p.quantidadeCritica"));
                pro.setImagem(rset.getString("p.imagem"));
                pro.setFornecedor(rset.getString("p.fornecedor"));
                pro.setPreco(rset.getDouble("p.preco"));
                pro.setCusto(rset.getDouble("p.custo"));
                pro.setStatus(rset.getBoolean("p.status"));

                ItemOrcamento item = new ItemOrcamento();

                item.setId(rset.getInt("i.id"));
                item.setQuantidade(rset.getInt("i.quantidade"));
                item.setPreco(rset.getDouble("i.preco"));
                item.setDataHora(sqlDataHora.toLocalDateTime());
                item.setStatusVenda(rset.getBoolean("i.statusVenda"));
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
