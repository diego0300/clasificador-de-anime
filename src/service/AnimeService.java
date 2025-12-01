package service;

import model.*;
import repository.AnimeRepository;
import repository.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

public class AnimeService {
    private AnimeRepository repositorio;
    private repository.RepositorioListas repoListas;

    public AnimeService(AnimeRepository repositorio) {
        this.repositorio = repositorio;
        this.repoListas = new repository.RepositorioListas();
    }

    // --- GRASP CREATOR y CONTROLLER ---
    // Recibe datos primitivos, valida y crea el objeto

    public void registrarSerie(String titulo, int anio, String estudio, int caps) throws ValidacionException, PersistenciaException {
        validarDatosComunes(titulo, anio);
        if (caps <= 0) {
            throw new ValidacionException("La cantidad de capítulos debe ser mayor a 0.");
        }

        // 1. Valida que no exista ya (regla de negocio)
        if (repositorio.buscarPorTitulo(titulo) != null) {
            throw new ValidacionException("Ya existe un animé con el título: " + titulo);
        }

        // 2. Crea (Creator)
        AnimeSerie serie = new AnimeSerie(titulo, anio, estudio, caps);

        // 3. Guarda
        repositorio.guardar(serie);
    }

    public void registrarPelicula(String titulo, int anio, String estudio, int duracion) throws ValidacionException, PersistenciaException {
        validarDatosComunes(titulo, anio);
        if (duracion <= 0) {
            throw new ValidacionException("La duración debe ser mayor a 0.");
        }

        if (repositorio.buscarPorTitulo(titulo) != null) {
            throw new ValidacionException("Ya existe un animé con el título: " + titulo);
        }

        AnimePelicula pelicula = new AnimePelicula(titulo, anio, estudio, duracion);
        repositorio.guardar(pelicula);
    }

    // Validación auxiliar privada
    private void validarDatosComunes(String titulo, int anio) throws ValidacionException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ValidacionException("El título no puede estar vacío.");
        }
        if (anio < 1900 || anio > 2100) {
            throw new ValidacionException("El año parece incorrecto.");
        }
    }

    // --- MODIFICACIÓN (RF1.b) ---
    public void modificarEstado(String titulo, EstadoAnime nuevoEstado) throws ValidacionException, PersistenciaException {
        // 1. Busca si existe
        Anime anime = repositorio.buscarPorTitulo(titulo);

        if (anime == null) {
            throw new ValidacionException("No se encontró ningún animé con el título: " + titulo);
        }

        // 2. Modifica el estado en memoria
        anime.setEstado(nuevoEstado);

        repositorio.guardar(anime);
    }

    public void modificarCalificacion(String titulo, int nuevaCalificacion) throws ValidacionException, PersistenciaException {
        Anime anime = repositorio.buscarPorTitulo(titulo);

        if (anime == null) {
            throw new ValidacionException("No se encontró el animé: " + titulo);
        }

        anime.setCalificacion(nuevaCalificacion);
        repositorio.guardar(anime);
    }

    public void agregarGenero(String titulo, GeneroAnime genero) throws ValidacionException, PersistenciaException {
        Anime anime = repositorio.buscarPorTitulo(titulo);

        if (anime == null) {
            throw new ValidacionException("No se encontró el animé: " + titulo);
        }

        anime.agregarGenero(genero);
        repositorio.guardar(anime);
    }

    public void eliminarAnime(String titulo) throws ValidacionException, PersistenciaException {
        // 1. Valida que exista antes de intentar borrar
        if (repositorio.buscarPorTitulo(titulo) == null) {
            throw new ValidacionException("No se encontró el animé: " + titulo);
        }

        repositorio.eliminar(titulo);
    }

    public void modificarEstudio(String titulo, String nuevoEstudio) throws ValidacionException, PersistenciaException {
        // 1. Valida input
        if (nuevoEstudio == null || nuevoEstudio.trim().isEmpty()) {
            throw new ValidacionException("El nombre del estudio no puede estar vacío.");
        }

        // 2. Busca
        Anime anime = repositorio.buscarPorTitulo(titulo);
        if (anime == null) {
            throw new ValidacionException("No se encontró el animé: " + titulo);
        }

        // 3. Modifica y guarda
        anime.setEstudio(nuevoEstudio);
        repositorio.guardar(anime);
    }

    // --- FUNCIONALIDAD DE BUSQUEDA (STRATEGY) ---
    public List<Anime> filtrarAnimes(CriterioBusqueda criterio) throws PersistenciaException {
        List<Anime> todos = repositorio.listarTodos();
        List<Anime> filtrados = new ArrayList<>();

        for (Anime a : todos) {
            // Se delega la decisión al objeto criterio (Polimorfismo)
            if (criterio.cumple(a)) {
                filtrados.add(a);
            }
        }
        return filtrados;
    }

    public List<Anime> obtenerTodos() throws PersistenciaException {
        return repositorio.listarTodos();
    }

    // --- GESTIÓN DE LISTAS PERSONALIZADAS ---

    public void crearListaPersonalizada(String nombre) throws ValidacionException, PersistenciaException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre de la lista no puede estar vacío.");
        }

        // 1. Cargar las listas existentes
        List<Anime> todosLosAnimes = repositorio.listarTodos();
        List<ListaPersonalizada> listas = repoListas.cargarListas(todosLosAnimes);

        // 2. Validar duplicados
        for (ListaPersonalizada l : listas) {
            if (l.getNombreLista().equalsIgnoreCase(nombre)) {
                throw new ValidacionException("Ya existe una lista con ese nombre.");
            }
        }

        // 3. Crear y guardar
        listas.add(new ListaPersonalizada(nombre));
        repoListas.guardarListas(listas);
    }

    public void agregarAnimeALista(String nombreLista, String tituloAnime) throws ValidacionException, PersistenciaException {
        // 1. Cargar todo el contexto
        List<Anime> todosLosAnimes = repositorio.listarTodos();
        List<ListaPersonalizada> listas = repoListas.cargarListas(todosLosAnimes);

        // 2. Buscar la lista
        ListaPersonalizada listaDestino = null;
        for (ListaPersonalizada l : listas) {
            if (l.getNombreLista().equalsIgnoreCase(nombreLista)) {
                listaDestino = l;
                break;
            }
        }
        if (listaDestino == null) throw new ValidacionException("Lista no encontrada.");

        // 3. Buscar el animé
        Anime animeAgregable = repositorio.buscarPorTitulo(tituloAnime);
        if (animeAgregable == null) throw new ValidacionException("Animé no encontrado.");

        // 4. Agregar y Guardar
        listaDestino.agregarAnime(animeAgregable);
        repoListas.guardarListas(listas);
    }

    public List<ListaPersonalizada> obtenerListas() throws PersistenciaException {
        return repoListas.cargarListas(repositorio.listarTodos());
    }

    public void quitarAnimeDeLista(String nombreLista, String tituloAnime) throws ValidacionException, PersistenciaException {
        // 1. Cargar contexto
        List<Anime> todos = repositorio.listarTodos();
        List<ListaPersonalizada> listas = repoListas.cargarListas(todos);

        // 2. Buscar la lista
        ListaPersonalizada listaDestino = null;
        for (ListaPersonalizada l : listas) {
            if (l.getNombreLista().equalsIgnoreCase(nombreLista)) {
                listaDestino = l;
                break;
            }
        }

        if (listaDestino == null) {
            throw new ValidacionException("No se encontró la lista: " + nombreLista);
        }

        // 3. Busca el animé dentro de esa lista específica
        Anime animeAQuitar = null;
        for (Anime a : listaDestino.getAnimes()) {
            if (a.getTitulo().equalsIgnoreCase(tituloAnime)) {
                animeAQuitar = a;
                break;
            }
        }

        if (animeAQuitar == null) {
            throw new ValidacionException("El animé '" + tituloAnime + "' no está en la lista '" + nombreLista + "'.");
        }

        // 4. Quitar y guardar
        listaDestino.quitarAnime(animeAQuitar);
        repoListas.guardarListas(listas);
    }

    // Ordena alfabéticamente (A-Z)
    public List<Anime> listarOrdenadoPorTitulo() throws PersistenciaException {
        List<Anime> lista = repositorio.listarTodos();
        // Usamos CASE_INSENSITIVE_ORDER para que 'a' y 'A' cuenten igual
        lista.sort((a1, a2) -> String.CASE_INSENSITIVE_ORDER.compare(a1.getTitulo(), a2.getTitulo()));
        return lista;
    }

    // Ordena por calificación (mayor a menor) - BONUS
    public List<Anime> listarOrdenadoPorCalificacion() throws PersistenciaException {
        List<Anime> lista = repositorio.listarTodos();
        // compare(y, x) invierte el orden para que sea descendente (5 a 1)
        lista.sort((a1, a2) -> Integer.compare(a2.getCalificacion(), a1.getCalificacion()));
        return lista;
    }

    // Ordena por año (más reciente primero) - BONUS
    public List<Anime> listarOrdenadoPorAnio() throws PersistenciaException {
        List<Anime> lista = repositorio.listarTodos();
        lista.sort((a1, a2) -> Integer.compare(a2.getAnioLanzamiento(), a1.getAnioLanzamiento()));
        return lista;
    }

    // --- ESTADÍSTICAS ---

    // 1. Cantidad de animes por Estado
    public java.util.Map<model.EstadoAnime, Long> getEstadisticasPorEstado() throws PersistenciaException {
        List<Anime> todos = repositorio.listarTodos();

        // Agrupa por Estado y cuenta cuántos hay
        return todos.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Anime::getEstado,
                        java.util.stream.Collectors.counting()
                ));
    }

    // 2. Promedio global de calificaciones
    public double getPromedioCalificacion() throws PersistenciaException {
        List<Anime> todos = repositorio.listarTodos();

        // Filtra los que tienen calificación 0 para no bajar el promedio
        return todos.stream()
                .filter(a -> a.getCalificacion() > 0)
                .mapToInt(Anime::getCalificacion)
                .average()
                .orElse(0.0); // Si no hay nada calificado, devuelve 0
    }

    // --- RECOMENDACIONES ---
    public List<Anime> generarRecomendacion(GeneroAnime genero, int topN) throws PersistenciaException {
        List<Anime> todos = repositorio.listarTodos();

        return todos.stream()
                // 1. Filtra (si se eligió un género, sino pasan todos)
                .filter(a -> genero == null || a.getGeneros().contains(genero))
                // 2. Ordena por calificación (mayor a menor)
                .sorted((a1, a2) -> Integer.compare(a2.getCalificacion(), a1.getCalificacion()))
                // 3. Toma solo los primeros N
                .limit(topN)
                .collect(java.util.stream.Collectors.toList());
    }
}