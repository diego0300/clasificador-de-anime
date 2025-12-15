package model;

public class AnimeSerie extends Anime {
    private int cantidadCapitulos; // cantidad total de capítulos de la serie

    public AnimeSerie(String titulo, int anioLanzamiento, String estudio, int cantidadCapitulos) {
        super(titulo, anioLanzamiento, estudio); // Llama al constructor de Anime
        if (cantidadCapitulos <= 0) {
            throw new IllegalArgumentException("La cantidad de capítulos debe ser mayor a 0");
        }
        this.cantidadCapitulos = cantidadCapitulos;
    }

    public int getCantidadCapitulos() {
        return cantidadCapitulos;
    }

    @Override
    public int getDuracionTotal() {
        // Se asume una duración promedio de 20 minutos por capítulo
        return cantidadCapitulos * 20;
    }

    @Override
    public String toString() {
        return super.toString() + " [Serie: " + cantidadCapitulos + " caps]";
    }
}
//Se agregó una validación en el constructor para asegurar que la cantidad de capítulos sea mayor a cero, evitando la creación de series inválidas
