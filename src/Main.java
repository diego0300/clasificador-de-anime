import repository.RepositorioAnimeArchivo;
import service.AnimeService;
import ui.VentanaPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 1. INYECCIÓN DE DEPENDENCIAS
                // Repo -> Service -> UI
                RepositorioAnimeArchivo repositorio = new RepositorioAnimeArchivo();
                AnimeService servicio = new AnimeService(repositorio);

                // 2. INICIAR VENTANA
                VentanaPrincipal ventana = new VentanaPrincipal(servicio);
                ventana.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error fatal iniciando la app.");
            }
        });
    }
}