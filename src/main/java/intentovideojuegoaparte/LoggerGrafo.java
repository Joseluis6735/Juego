package intentovideojuegoaparte;

import java.util.ArrayList;
import java.util.List;

public class LoggerGrafo {
    private List<String> eventos;

    public LoggerGrafo() {
        eventos = new ArrayList<>();
    }

    public void log(String evento) {
        System.out.println("[LOG] " + evento);
        eventos.add(evento);
    }

    public List<String> getEventos() {
        return eventos;
    }
}
