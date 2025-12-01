package service;

import model.Anime;

public class FiltroCalificacion implements CriterioBusqueda {
    private int calificacionMinima;

    public FiltroCalificacion(int calificacionMinima) {
        this.calificacionMinima = calificacionMinima;
    }

    @Override
    public boolean cumple(Anime anime) {
        // Devuelve true si la calificación del animé es mayor o igual a la buscada
        return anime.getCalificacion() >= calificacionMinima;
    }
}