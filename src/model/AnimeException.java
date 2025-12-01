package model;

// Usamos Exception (Checked) para obligar a quien use el código a manejar el error
public class AnimeException extends Exception {
    public AnimeException(String msg) {
        super(msg);
    }
}