
package utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    public static String converteData(LocalDate data) {
        if (data == null) {
            return null; //
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
    }
    
    public static String converteHora(LocalDateTime data) {
        if (data == null) {
            return null; // 
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return data.format(formatter);
    }

    public static String pegarMes() {
         int mes = LocalDate.now().getMonthValue();
        
        String[] meses = {
            "Janeiro", "Fevereiro", "Março", "Abril", "maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        };

        if (mes >= 1 && mes <= 12) {
            return meses[mes - 1]; //
        } else {
            return "Mês inválido"; //
        }
    }
    
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str; // 
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    public static List<Item> ordenarItem(List<Integer> ids, List<Integer> quantidades, List<String> nomes) {
        
        List<Item> itens = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            itens.add(new Item(ids.get(i), quantidades.get(i), nomes.get(i)));
        }

        itens.sort((item1, item2) -> Integer.compare(item2.quantidade, item1.quantidade));
        
        return itens;
    }
}
