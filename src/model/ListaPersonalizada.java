package model;

import java.util.ArrayList;
import java.util.List;

public class ListaPersonalizada {
    private String nombreLista;
    private List<Anime> animes;

    public ListaPersonalizada(String nombreLista) {
        if (nombreLista == null || nombreLista.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la lista no puede estar vacío");
        }
        this.nombreLista = nombreLista.trim();
        this.animes = new ArrayList<>();
    }

    public void agregarAnime(Anime a) {
        // Valida que el animé no sea nulo ni esté repetido
        if (a != null && !animes.contains(a)) {
            animes.add(a);
        }
    }

    public void quitarAnime(Anime a) {
        animes.remove(a);
    }

    public List<Anime> getAnimes() {
        // Se devuelve una copia para proteger la lista interna
        return new ArrayList<>(animes);
    }

    public String getNombreLista() {
        return nombreLista;
    }

    @Override
    public String toString() {
        return "Lista: " + nombreLista + " (" + animes.size() + " animes)";
    }
}
//Se agregaron validaciones en el constructor para asegurar que la lista tenga un nombre válido. También se validó que no se puedan agregar animes nulos
