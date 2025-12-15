package model;

public class AnimePelicula extends Anime {
    private int duracion; // duración en minutos

    public AnimePelicula(String titulo, int anioLanzamiento, String estudio, int duracion) {
        super(titulo, anioLanzamiento, estudio);
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0");
        }
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
//Se agregó una validación en el constructor para asegurar que la duración de la película sea mayor a cero, evitando la creación de objetos inválidos
