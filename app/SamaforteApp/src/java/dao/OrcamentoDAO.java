package dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import model.Cliente;
import model.ItemOrcamento;
import model.Orcamento;
import model.Produto;
import utilities.Util;

public class OrcamentoDAO {

    public static boolean registrar(Orcamento orcamento) {
        String sql = ""
                + " INSERT INTO"
                + " orcamento(dataCriacao, dataValidade, status)"
                + " VALUES (?,?,?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            
            
            pstm.setTimestamp(1, Timestamp.valueOf(orcamento.getDataCriacao()));
            pstm.setTimestamp(2, Timestamp.valueOf(orcamento.getDataValidade()));
            pstm.setString(3, orcamento.getStatus());

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
                + " id,"
                + " dataCriacao,"
                + " dataValidade,"
                + " status,"
                + " informacoes,"
                + " id_cliente"
                + " FROM"
                + " orcamento";

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

                    Timestamp sqlCriacao = rset.getTimestamp("dataCriacao");
                    Timestamp sqlValidade = rset.getTimestamp("dataValidade");

                    Cliente cliente = ClienteDAO.listarPorId(rset.getInt("id_cliente"));
                    Orcamento orcamento = new Orcamento();
                    
                    orcamento.setId(rset.getInt("id"));
                    orcamento.setDataCriacao(sqlCriacao.toLocalDateTime());
                    orcamento.setDataValidade(sqlValidade.toLocalDateTime());
                    orcamento.setStatus(rset.getString("status"));
                    orcamento.setInformacao(rset.getString("informacoes"));
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
                + " id,"
                + " dataCriacao,"
                + " dataValidade,"
                + " status,"
                + " informacoes,"
                + " id_cliente"
                + " FROM"
                + " orcamento"
                + " WHERE"
                + " id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        
        Orcamento orcamento = new Orcamento();

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setInt(1, id);
            pstm.execute();

            rset = pstm.executeQuery();

            if (rset.next()) {

                try {

                    Timestamp sqlCriacao = rset.getTimestamp("dataCriacao");
                    Timestamp sqlValidade = rset.getTimestamp("dataValidade");

                    Cliente cliente = ClienteDAO.listarPorId(rset.getInt("id_cliente"));
                    
                    orcamento.setId(rset.getInt("id"));
                    orcamento.setDataCriacao(sqlCriacao.toLocalDateTime());
                    orcamento.setDataValidade(sqlValidade.toLocalDateTime());
                    orcamento.setStatus(rset.getString("status"));
                    orcamento.setInformacao(rset.getString("informacoes"));
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
    
    public static List<Orcamento> listarPorStatus(String status) {

        status = status.toLowerCase();
        status = Util.capitalize(status);
        
        String sql = ""
                + " SELECT"
                + " id,"
                + " dataCriacao,"
                + " dataValidade,"
                + " status,"
                + " informacoes,"
                + " id_cliente"
                + " FROM"
                + " orcamento"
                + " WHERE"
                + " status = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        
        List<Orcamento> orcamentos = new ArrayList<>();

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);

            pstm.setString(1, status);
            pstm.execute();

            rset = pstm.executeQuery();

            while (rset.next()) {

                try {
                    Orcamento orcamento = new Orcamento();

                    Timestamp sqlCriacao = rset.getTimestamp("dataCriacao");
                    Timestamp sqlValidade = rset.getTimestamp("dataValidade");

                    Cliente cliente = ClienteDAO.listarPorId(rset.getInt("id_cliente"));
                    
                    orcamento.setId(rset.getInt("id"));
                    orcamento.setDataCriacao(sqlCriacao.toLocalDateTime());
                    orcamento.setDataValidade(sqlValidade.toLocalDateTime());
                    orcamento.setStatus(rset.getString("status"));
                    orcamento.setInformacao(rset.getString("informacoes"));
                    orcamento.setCliente(cliente);
                    
                    orcamentos.add(orcamento);

                } catch (Exception e) {

                    return null;

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

            pstm.setDouble(1, item.getQuantidade());
            pstm.setDouble(2, item.getPreco());
            pstm.setTimestamp(3, Timestamp.valueOf(item.getDataHora()));
            pstm.setBoolean(4, item.isStatusVenda());
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

        String sql = " SELECT * FROM item_orcamento WHERE id_orcamento = ?";

        List<ItemOrcamento> itens = new ArrayList<ItemOrcamento>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {

            conn = Conexao.criarConexaoMySQL();
            pstm = (PreparedStatement) conn.prepareStatement(sql);
            pstm.setInt(1, idOrcamento);
            rset = pstm.executeQuery();

            Orcamento orca = OrcamentoDAO.listarPorId(idOrcamento);

            while (rset.next()) {

                Timestamp sqlDataHora = rset.getTimestamp("dataHora");

                Produto pro = ProdutoDAO.listarPorId(rset.getInt("id_produto"));

                ItemOrcamento item = new ItemOrcamento();

                item.setId(rset.getInt("id"));
                item.setQuantidade(rset.getDouble("quantidade"));
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

    public static List<Orcamento> listarPaginado(int start, int length, String searchValue, String filterColumn, String filterType) {
        StringBuilder sql = new StringBuilder(
            "SELECT o.* FROM orcamento o " +
            "LEFT JOIN cliente c ON o.id_cliente = c.id"
        );

        List<Object> params = new ArrayList<>();

        if (searchValue != null && !searchValue.isEmpty() && filterColumn != null && !filterColumn.isEmpty()) {
            // Colunas: 0=ID, 1=Cliente, 2=Status, 3=Data de Criação, 4=Data de Validade
            String[] columns = {"o.id", "c.nome", "o.status", "o.dataCriacao", "o.dataValidade"};
            try {
                int columnIndex = Integer.parseInt(filterColumn);
                if (columnIndex >= 0 && columnIndex < columns.length) {
                    String column = columns[columnIndex];

                    // Para datas (colunas 3 e 4), converter para formato MySQL e usar DATE()
                    if (columnIndex == 3 || columnIndex == 4) {
                        // Formato esperado: dd/mm/aaaa
                        // Converter para yyyy-mm-dd para comparação no MySQL
                        sql.append(" WHERE DATE_FORMAT(").append(column).append(", '%d/%m/%Y')");

                        if ("0".equals(filterType)) { // Começa com
                            sql.append(" LIKE ?");
                            params.add(searchValue + "%");
                        } else { // Inclui
                            sql.append(" LIKE ?");
                            params.add("%" + searchValue + "%");
                        }
                    }
                    // Para status, usar LOWER para busca case-insensitive
                    else if (columnIndex == 2) {
                        sql.append(" WHERE LOWER(").append(column).append(") LIKE LOWER(?)");
                        if ("0".equals(filterType)) { // Começa com
                            params.add(searchValue + "%");
                        } else { // Inclui
                            params.add("%" + searchValue + "%");
                        }
                    }
                    // Para ID e Cliente
                    else {
                        sql.append(" WHERE ").append(column).append(" LIKE ?");
                        if ("0".equals(filterType)) { // Começa com
                            params.add(searchValue + "%");
                        } else { // Inclui
                            params.add("%" + searchValue + "%");
                        }
                    }
                }
            } catch (NumberFormatException e) {
                // Tratar erro se filterColumn não for um número válido
            }
        }

        sql.append(" ORDER BY o.id DESC LIMIT ? OFFSET ?");
        params.add(length);
        params.add(start);

        List<Orcamento> orcamentos = new ArrayList<>();
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
                    Timestamp sqlCriacao = rset.getTimestamp("dataCriacao");
                    Timestamp sqlValidade = rset.getTimestamp("dataValidade");

                    Cliente cliente = ClienteDAO.listarPorId(rset.getInt("id_cliente"));
                    Orcamento orcamento = new Orcamento();

                    orcamento.setId(rset.getInt("id"));
                    orcamento.setDataCriacao(sqlCriacao.toLocalDateTime());
                    orcamento.setDataValidade(sqlValidade.toLocalDateTime());
                    orcamento.setStatus(rset.getString("status"));
                    orcamento.setInformacao(rset.getString("informacoes"));
                    orcamento.setCliente(cliente);

                    orcamentos.add(orcamento);
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
        return orcamentos;
    }

    public static int contarTodos() {
        String sql = "SELECT COUNT(id) FROM orcamento";
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
            "SELECT COUNT(o.id) FROM orcamento o " +
            "LEFT JOIN cliente c ON o.id_cliente = c.id"
        );
        List<Object> params = new ArrayList<>();
        int total = 0;

        if (searchValue != null && !searchValue.isEmpty() && filterColumn != null && !filterColumn.isEmpty()) {
            // Colunas: 0=ID, 1=Cliente, 2=Status, 3=Data de Criação, 4=Data de Validade
            String[] columns = {"o.id", "c.nome", "o.status", "o.dataCriacao", "o.dataValidade"};
            try {
                int columnIndex = Integer.parseInt(filterColumn);
                if (columnIndex >= 0 && columnIndex < columns.length) {
                    String column = columns[columnIndex];

                    // Para datas (colunas 3 e 4), converter para formato MySQL e usar DATE()
                    if (columnIndex == 3 || columnIndex == 4) {
                        // Formato esperado: dd/mm/aaaa
                        // Converter para yyyy-mm-dd para comparação no MySQL
                        sql.append(" WHERE DATE_FORMAT(").append(column).append(", '%d/%m/%Y')");

                        if ("0".equals(filterType)) { // Começa com
                            sql.append(" LIKE ?");
                            params.add(searchValue + "%");
                        } else { // Inclui
                            sql.append(" LIKE ?");
                            params.add("%" + searchValue + "%");
                        }
                    }
                    // Para status, usar LOWER para busca case-insensitive
                    else if (columnIndex == 2) {
                        sql.append(" WHERE LOWER(").append(column).append(") LIKE LOWER(?)");
                        if ("0".equals(filterType)) { // Começa com
                            params.add(searchValue + "%");
                        } else { // Inclui
                            params.add("%" + searchValue + "%");
                        }
                    }
                    // Para ID e Cliente
                    else {
                        sql.append(" WHERE ").append(column).append(" LIKE ?");
                        if ("0".equals(filterType)) { // Começa com
                            params.add(searchValue + "%");
                        } else { // Inclui
                            params.add("%" + searchValue + "%");
                        }
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
