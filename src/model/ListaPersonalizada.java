package model;

import java.util.ArrayList;
import java.util.List;

public class ListaPersonalizada {
    private String nombreLista;
    private List<Anime> animes;

    public ListaPersonalizada(String nombreLista) {
        this.nombreLista = nombreLista;
        this.animes = new ArrayList<>(); // Inicialización de la lista vacía
    }

    public void agregarAnime(Anime a) {
        // Valida que el animé no se encuentre en la lista
        if (!animes.contains(a)) {
            animes.add(a);
        }
    }

    public void quitarAnime(Anime a) {
        animes.remove(a);
    }

    public List<Anime> getAnimes() {
        return animes;
    }

    public String getNombreLista() {
        return nombreLista;
    }

    @Override
    public String toString() {
        return "Lista: " + nombreLista + " (" + animes.size() + " animes)";
    }
}
