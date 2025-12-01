package service;

import model.Anime;

public interface CriterioBusqueda {
    boolean cumple(Anime anime);
}