package model;

public class AnimePelicula extends Anime {
    private int duracion;

    public AnimePelicula(String titulo, int anioLanzamiento, String estudio, int duracion) {
        super(titulo, anioLanzamiento, estudio);
        this.duracion = duracion;
    }

    public int getDuracionMinutos() {
        return duracion;
    }

    @Override
    public int getDuracionTotal() {
        return duracion;
    }

    @Override
    public String toString() {
        return super.toString() + " [Película: " + duracion + " min]";
    }
}
