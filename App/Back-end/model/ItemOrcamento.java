package model;

import java.time.LocalDateTime;

public class ItemOrcamento {

    private int id;
    private Produto produto;
    private int quantidade;
    private double preco;
    private LocalDateTime dataHora;
    private boolean statusVenda;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public boolean getStatusVenda() {
        return statusVenda;
    }

    public void setStatusVenda(boolean statusVenda) {
        this.statusVenda = statusVenda;
    }

    public ItemOrcamento(int id, Produto produto, int quantidade, double preco, LocalDateTime dataHora, boolean statusVenda) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = preco;
        this.dataHora = dataHora;
        this.statusVenda = statusVenda;
    }

    public ItemOrcamento() {
    }

}
