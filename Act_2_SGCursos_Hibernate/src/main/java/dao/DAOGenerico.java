package dao;

import java.io.Serializable;
import java.util.List;

public interface DAOGenerico<T, ID extends Serializable> {
    // CREATE: Guardar una entidad (nueva o actualizada).
    void save(T entity); //

    // READ: Obtener una entidad por su ID.
    T getById(ID id);

    // READ: Obtener todas las entidades.
    List<T> getAll();

    // UPDATE: Actualizar una entidad.
    void update(T entity); // ¡Nuevo!

    // DELETE: Eliminar una entidad por su ID.
    void delete(ID id);
}