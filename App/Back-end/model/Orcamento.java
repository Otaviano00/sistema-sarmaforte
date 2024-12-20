package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Orcamento {

    private int id;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataValidade;
    private String status;
    private Cliente cliente;
    private String informacao;
    private List<ItemOrcamento> itens;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDateTime dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public List<ItemOrcamento> getItens() {
        return itens;
    }

    public void setItens(List<ItemOrcamento> itens) {
        this.itens = itens;
    }

    public Orcamento(int id, LocalDateTime dataCriacao, LocalDateTime dataValidade, String status, Cliente cliente, String informacao, List<ItemOrcamento> itens) {
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.dataValidade = dataValidade;
        this.status = status;
        this.cliente = cliente;
        this.informacao = informacao;
        this.itens = itens;
    }

    public Orcamento() {
    }

}
