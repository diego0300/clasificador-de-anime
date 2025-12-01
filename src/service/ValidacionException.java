package service;

import model.AnimeException;

public class ValidacionException extends AnimeException {
    public ValidacionException(String msg) {
        super(msg);
    }
}