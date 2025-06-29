

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<div style = "background-color: #ffffff; border-radius: 20px;">
    <canvas id="grafico_vendas"></canvas>
 </div>

<script>
  const ctx = document.getElementById('grafico_vendas');

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['Cimento', 'Tijolo', 'Prego', 'EPis'],
      datasets: [{
        label: 'R$ - Vendido',
        data: [100.50, 50.40, 5.5, 30.6],
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
