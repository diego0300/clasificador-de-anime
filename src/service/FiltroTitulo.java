package service;

import model.Anime;

public class FiltroTitulo implements CriterioBusqueda {
    private String terminoBusqueda;

    public FiltroTitulo(String terminoBusqueda) {
        this.terminoBusqueda = terminoBusqueda.toLowerCase();
    }

    @Override
    public boolean cumple(Anime anime) {
        if (anime.getTitulo() == null) return false;
        // Verifica si el título del animé contiene el texto buscado
        return anime.getTitulo().toLowerCase().contains(terminoBusqueda);
    }
}
