package repository;

import model.Anime;
import java.util.List;

public interface AnimeRepository {
    void guardar(Anime anime) throws PersistenciaException;
    void eliminar(String titulo) throws PersistenciaException;
    Anime buscarPorTitulo(String titulo) throws PersistenciaException;
    List<Anime> listarTodos() throws PersistenciaException;
}
