package repository;

import model.Anime;
import model.ListaPersonalizada;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioListas {
    private String archivo = "listas.csv";

    // GUARDAR: "NombreLista,Titulo1,Titulo2,Titulo3..."
    public void guardarListas(List<ListaPersonalizada> listas) throws PersistenciaException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (ListaPersonalizada lista : listas) {
                StringBuilder sb = new StringBuilder();
                sb.append(lista.getNombreLista());

                for (Anime a : lista.getAnimes()) {
                    sb.append(",").append(a.getTitulo());
                }
                pw.println(sb.toString());
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error guardando listas: " + e.getMessage());
        }
    }

    // CARGAR: Lee y busca los objetos reales en la "biblioteca"
    public List<ListaPersonalizada> cargarListas(List<Anime> bibliotecaGlobal) {
        List<ListaPersonalizada> misListas = new ArrayList<>();
        File f = new File(archivo);
        if (!f.exists()) return misListas;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 0) continue;

                // 1. Crea la lista con el nombre (posición 0)
                ListaPersonalizada lista = new ListaPersonalizada(partes[0]);

                // 2. Busca y agrega los animes (desde posición 1 en adelante)
                for (int i = 1; i < partes.length; i++) {
                    String tituloBuscado = partes[i];

                    // Buscamo el objeto real en la biblioteca
                    for (Anime a : bibliotecaGlobal) {
                        if (a.getTitulo().equalsIgnoreCase(tituloBuscado)) {
                            lista.agregarAnime(a);
                            break;
                        }
                    }
                }
                misListas.add(lista);
            }
        } catch (IOException e) {
            System.err.println("Error cargando listas personalizadas.");
        }
        return misListas;
    }
}