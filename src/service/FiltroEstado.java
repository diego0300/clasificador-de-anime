package service;

import model.Anime;
import model.EstadoAnime;

public class FiltroEstado implements CriterioBusqueda {
    private EstadoAnime estadoBuscado;

    public FiltroEstado(EstadoAnime estadoBuscado) {
        this.estadoBuscado = estadoBuscado;
    }

    @Override
    public boolean cumple(Anime anime) {
        return anime.getEstado() == estadoBuscado;
    }
}