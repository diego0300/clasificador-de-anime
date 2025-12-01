package model;

import java.util.HashSet;
import java.util.Set;

public abstract class Anime {
    private String titulo;
    private int anioLanzamiento;
    private String estudio;
    private int calificacion; // 1 a 5
    private EstadoAnime estado;
    private Set<GeneroAnime> generos;

    // Constructor: Inicializa lo básico y la colección
    public Anime(String titulo, int anioLanzamiento, String estudio) {
        this.titulo = titulo;
        this.anioLanzamiento = anioLanzamiento;
        this.estudio = estudio;
        // Valores por defecto
        this.estado = EstadoAnime.POR_VER;
        this.generos = new HashSet<>(); // Set para evitar duplicados
    }

    // Método abstracto: las subclases está obligadas a implementarlo
    public abstract int getDuracionTotal();

    // Métodos de negocio básicos
    public void agregarGenero(GeneroAnime g) {
        this.generos.add(g);
    }

    // Getters y Setters necesarios
    public String getTitulo() {
        return titulo;
    }

    public int getAnioLanzamiento() {
        return anioLanzamiento;
    }

    public EstadoAnime getEstado() {
        return estado;
    }
    public void setEstado(EstadoAnime estado) {
        this.estado = estado;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        if (calificacion != 0 && (calificacion < 1 || calificacion > 5)) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
        this.calificacion = calificacion;
    }

    public Set<GeneroAnime> getGeneros() {
        return generos;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }

    // toString para que sea fácil imprimirlo en consola al probar
    @Override
    public String toString() {
        return titulo + " (" + anioLanzamiento + ") - " + estado;
    }
}