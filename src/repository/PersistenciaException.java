package repository;

import model.AnimeException;

public class PersistenciaException extends AnimeException {
    public PersistenciaException(String msg) {
        super(msg);
    }
}