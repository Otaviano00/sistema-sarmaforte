package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.Orcamento;

public class ClienteDAO {

    public static boolean cadastrar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, telefone, cpf, endereco) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstm.setString(1, cliente.getNome());
            pstm.setString(2, cliente.getTelefone());
            if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
                pstm.setNull(3, java.sql.Types.VARCHAR);
            } else {
                pstm.setString(3, cliente.getCpf());
            }
            if (cliente.getEndereco() == null || cliente.getEndereco().trim().isEmpty()) {
                pstm.setNull(4, java.sql.Types.VARCHAR);
            } else {
                pstm.setString(4, cliente.getEndereco());
            }

            pstm.execute();

            // Recuperar o ID gerado
            rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                cliente.setId(rs.getInt(1));
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (rs != null) {
                    rs.close();
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
    }

    public static List<Cliente> listar() {
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rset.getInt("id"));
                cliente.setNome(rset.getString("nome"));
                cliente.setTelefone(rset.getString("telefone"));
                cliente.setCpf(rset.getString("cpf"));
                cliente.setEndereco(rset.getString("endereco"));

                clientes.add(cliente);
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

        return clientes;
    }

    public static Cliente listarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        Cliente cliente = new Cliente();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);
            rset = pstm.executeQuery();

            if (rset.next()) {
                cliente.setId(rset.getInt("id"));
                cliente.setNome(rset.getString("nome"));
                cliente.setTelefone(rset.getString("telefone"));
                cliente.setCpf(rset.getString("cpf"));
                cliente.setEndereco(rset.getString("endereco"));
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

        return cliente;
    }

    public static boolean alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, telefone = ?, cpf = ?, endereco = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, cliente.getNome());
            pstm.setString(2, cliente.getTelefone());
            if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
                pstm.setNull(3, java.sql.Types.VARCHAR);
            } else {
                pstm.setString(3, cliente.getCpf());
            }
            if (cliente.getEndereco() == null || cliente.getEndereco().trim().isEmpty()) {
                pstm.setNull(4, java.sql.Types.VARCHAR);
            } else {
                pstm.setString(4, cliente.getEndereco());
            }
            pstm.setInt(5, cliente.getId());

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
        String sql = "DELETE FROM cliente WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);
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
    
    public static List<Orcamento> listarOrcamentos(int id) {
        String sql = "SELECT * FROM orcamento WHERE id_cliente = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        List<Orcamento> orcamentos = new ArrayList<>();

        try {
            conn = Conexao.criarConexaoMySQL();
            pstm = conn.prepareStatement(sql);

            pstm.setInt(1, id);

            rset = pstm.executeQuery();
            
            while(rset.next()) {
                Orcamento orcamento = OrcamentoDAO.listarPorId(rset.getInt("id"));
                orcamentos.add(orcamento);
            }

        } catch (Exception e) {
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
        return orcamentos;
    }

    public static List<Cliente> listarPaginado(int start, int length, String searchValue, String filterColumn, String filterType) {
        StringBuilder sql = new StringBuilder("SELECT * FROM cliente");
        List<Object> params = new ArrayList<>();

        if (searchValue != null && !searchValue.isEmpty() && filterColumn != null && !filterColumn.isEmpty()) {
            String[] columns = {"id", "nome", "telefone", "endereco", "cpf"};
            try {
                int columnIndex = Integer.parseInt(filterColumn);
                if (columnIndex >= 0 && columnIndex < columns.length) {
                    String column = columns[columnIndex];
                    sql.append(" WHERE ").append(column).append(" LIKE ?");

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

        List<Cliente> clientes = new ArrayList<>();
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
                Cliente cliente = new Cliente();
                cliente.setId(rset.getInt("id"));
                cliente.setNome(rset.getString("nome"));
                cliente.setTelefone(rset.getString("telefone"));
                cliente.setCpf(rset.getString("cpf"));
                cliente.setEndereco(rset.getString("endereco"));
                clientes.add(cliente);
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
        return clientes;
    }

    public static int contarTodos() {
        String sql = "SELECT COUNT(id) FROM cliente";
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
        StringBuilder sql = new StringBuilder("SELECT COUNT(id) FROM cliente");
        List<Object> params = new ArrayList<>();
        int total = 0;

        if (searchValue != null && !searchValue.isEmpty() && filterColumn != null && !filterColumn.isEmpty()) {
            String[] columns = {"id", "nome", "telefone", "endereco", "cpf"};
            try {
                int columnIndex = Integer.parseInt(filterColumn);
                if (columnIndex >= 0 && columnIndex < columns.length) {
                    String column = columns[columnIndex];
                    sql.append(" WHERE ").append(column).append(" LIKE ?");

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
}
