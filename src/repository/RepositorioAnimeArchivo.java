package repository;

import model.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioAnimeArchivo implements AnimeRepository {
    private String rutaArchivo;

    public RepositorioAnimeArchivo() {
        this.rutaArchivo = "animes.csv";
    }

    @Override
    public void guardar(Anime anime) throws PersistenciaException {
        // Estrategia: Leer todo, reemplazar/agregar y reescribir todo.
        List<Anime> todos = listarTodos();

        // Si ya existe (mismo título), lo saca para poner la versión nueva
        todos.removeIf(a -> a.getTitulo().equalsIgnoreCase(anime.getTitulo()));
        todos.add(anime);

        reescribirArchivo(todos);
    }

    @Override
    public void eliminar(String titulo) throws PersistenciaException {
        List<Anime> todos = listarTodos();
        boolean borrado = todos.removeIf(a -> a.getTitulo().equalsIgnoreCase(titulo));

        if (borrado) {
            reescribirArchivo(todos);
        }
    }

    @Override
    public Anime buscarPorTitulo(String titulo) throws PersistenciaException {
        return listarTodos().stream()
                .filter(a -> a.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Anime> listarTodos() throws PersistenciaException {
        List<Anime> lista = new ArrayList<>();
        File file = new File(rutaArchivo);

        if (!file.exists()) return lista; // Si no hay archivo, devuelve lista vacía

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    Anime a = parsearLinea(linea);
                    lista.add(a);
                } catch (Exception e) {
                    // Si una línea está corrupta, la ignora pero loguea
                    System.err.println("Error leyendo línea: " + linea);
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer el archivo de animes.");
        }
        return lista;
    }

    // --- Métodos privados de ayuda (parsing) ---

    private void reescribirArchivo(List<Anime> animes) throws PersistenciaException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Anime a : animes) {
                pw.println(generarLineaCSV(a));
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error escribiendo en el archivo.");
        }
    }

    private String generarLineaCSV(Anime a) {
        // Formato: TIPO,TITULO,ANIO,ESTUDIO,ESTADO,CALIFICACION,GENEROS,EXTRA
        String tipo = (a instanceof AnimeSerie) ? "SERIE" : "PELICULA";

        // Convertir set de géneros a un string unido por ";"
        String generosStr = a.getGeneros().stream()
                .map(Enum::name)
                .collect(Collectors.joining(";"));
        if (generosStr.isEmpty()) generosStr = "NINGUNO";

        StringBuilder sb = new StringBuilder();
        sb.append(tipo).append(",")
                .append(a.getTitulo()).append(",")
                .append(a.getAnioLanzamiento()).append(",")
                .append(a.getEstudio()).append(",")
                .append(a.getEstado()).append(",")
                .append(a.getCalificacion()).append(",")
                .append(generosStr).append(",");

        // Dato específico según el hijo
        if (a instanceof AnimeSerie) {
            sb.append(((AnimeSerie) a).getCantidadCapitulos());
        } else if (a instanceof AnimePelicula) {
            sb.append(((AnimePelicula) a).getDuracionMinutos());
        }

        return sb.toString();
    }

    private Anime parsearLinea(String linea) {
        String[] partes = linea.split(",");
        // Orden: 0:TIPO, 1:TITULO, 2:ANIO, 3:ESTUDIO, 4:ESTADO, 5:CALIF, 6:GENEROS, 7:EXTRA

        String tipo = partes[0];
        String titulo = partes[1];
        int anio = Integer.parseInt(partes[2]);
        String estudio = partes[3];
        EstadoAnime estado = EstadoAnime.valueOf(partes[4]);
        int calif = Integer.parseInt(partes[5]);

        // Reconstruir objeto
        Anime anime;
        if (tipo.equals("SERIE")) {
            int caps = Integer.parseInt(partes[7]);
            anime = new AnimeSerie(titulo, anio, estudio, caps);
        } else {
            int duracion = Integer.parseInt(partes[7]);
            anime = new AnimePelicula(titulo, anio, estudio, duracion);
        }

        anime.setEstado(estado);
        anime.setCalificacion(calif);

        // Recuperar géneros
        String generosStr = partes[6];
        if (!generosStr.equals("NINGUNO")) {
            String[] gArr = generosStr.split(";");
            for (String g : gArr) {
                anime.agregarGenero(GeneroAnime.valueOf(g));
            }
        }

        return anime;
    }
}