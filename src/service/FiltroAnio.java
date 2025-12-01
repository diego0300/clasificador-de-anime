package service;

import model.Anime;

public class FiltroAnio implements CriterioBusqueda {
    private int anioMin;
    private int anioMax;

    public FiltroAnio(int anioMin, int anioMax) {
        this.anioMin = anioMin;
        this.anioMax = anioMax;
    }

    @Override
    public boolean cumple(Anime anime) {
        return anime.getAnioLanzamiento() >= anioMin && anime.getAnioLanzamiento() <= anioMax;
    }
}