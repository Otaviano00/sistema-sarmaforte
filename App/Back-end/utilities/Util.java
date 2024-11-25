
package utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
}
