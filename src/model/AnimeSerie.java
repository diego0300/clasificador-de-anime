package model;

public class AnimeSerie extends Anime {
    private int cantidadCapitulos;

    public AnimeSerie(String titulo, int anioLanzamiento, String estudio, int cantidadCapitulos) {
        super(titulo, anioLanzamiento, estudio); // Llama al constructor de Anime
        this.cantidadCapitulos = cantidadCapitulos;
    }

    public int getCantidadCapitulos() {
        return cantidadCapitulos;
    }

    @Override
    public int getDuracionTotal() {
        return cantidadCapitulos * 20;
    }

    @Override
    public String toString() {
        return super.toString() + " [Serie: " + cantidadCapitulos + " caps]";
    }
}