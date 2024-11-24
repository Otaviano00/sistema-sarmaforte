
package utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
    public static String converteData(LocalDate data) {
        if (data == null) {
            return null; // Retorna null se a data for nula
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
    }
    
    public static String converteHora(LocalDateTime data) {
        if (data == null) {
            return null; // Retorna null se a data for nula
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return data.format(formatter);
    }

}
