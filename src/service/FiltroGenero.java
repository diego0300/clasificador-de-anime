package service;

import model.Anime;
import model.GeneroAnime;

public class FiltroGenero implements CriterioBusqueda {
    private GeneroAnime generoBuscado;

    public FiltroGenero(GeneroAnime generoBuscado) {
        this.generoBuscado = generoBuscado;
    }

    @Override
    public boolean cumple(Anime anime) {
        // Usa el método 'getGeneros' del modelo
        // Contains devuelve true si el género está en el Set
        return anime.getGeneros().contains(generoBuscado);
    }
}