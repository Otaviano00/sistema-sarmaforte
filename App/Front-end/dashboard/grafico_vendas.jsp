

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dao.VendaDAO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.ItemOrcamento"%>
<%@page import="java.util.Locale"%>
<%@page import="utilities.Util"%>
<%@page import="utilities.Item"%>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<div class="grafico" style = "background-color: #ffffff; border-radius: 20px;">
    <div class="campo">
        <label class="titulo_campo">PRODUTOS MAIS VENDIDOS</label>
    </div>
    <canvas id="grafico_vendas"></canvas>
 </div>

<%
    List<ItemOrcamento> itens = VendaDAO.listarTodosItensVendidos();
    
    List<Integer> ids = new ArrayList<>();
    List<String> nomes = new ArrayList<>();
    List<Integer> quantidades = new ArrayList<>();
    
    for (int i = 0; i < itens.size(); i++) {
        int veri = 0;
        for (int j = i; j < itens.size(); j++) {
            if (itens.get(i).getProduto().getCodigo() == itens.get(j).getProduto().getCodigo()) {
                veri++;
            }
        }
        
        if (veri != 0) {
            boolean v = true;
            for (Integer id : ids) {
                if (itens.get(i).getProduto().getCodigo() == id) {
                    v = false;
                }
            }
            if (v) {
                ids.add(itens.get(i).getProduto().getCodigo());
                nomes.add(itens.get(i).getProduto().getNome());
                quantidades.add(itens.get(i).getQuantidade());
            }
        }
    }
    
    List<Item> dados = Util.ordenarItem(ids, quantidades, nomes);
    
    ids = new ArrayList<>();
    nomes = new ArrayList<>();
    quantidades = new ArrayList<>();
 
    for (Item item : dados) {
        ids.add(item.id);
        quantidades.add(item.quantidade);
        nomes.add(item.nome);
    }
    
    String labels = "[";
    
    for (int i = 0; i < ids.size(); i++) {
        if (i != ids.size() - 1) {
            labels = labels + "'" + ids.get(i) + " - " + nomes.get(i).split(" ")[0] +"', ";
        } else {
            labels = labels + "'" + ids.get(i) + " - " + nomes.get(i).split(" ")[0] +"']";
        }
    }
    
    
    String data = "[";

    for (int i = 0; i < ids.size(); i++) {    
        if (i != ids.size() - 1) {
            data = data + quantidades.get(i) + ", ";
        } else {
            data = data + quantidades.get(i) + "]";
        }
    }
%>

<script>
  const ctx = document.getElementById('grafico_vendas');
  
  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: <%= labels%>,
      datasets: [{
        label: 'Quantidade Vendida',
        data: <%= data%>,
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
</script>
