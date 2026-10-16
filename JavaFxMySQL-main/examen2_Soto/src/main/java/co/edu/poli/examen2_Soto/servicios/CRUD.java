package co.edu.poli.examen2_Soto.servicios;

import java.util.List;

/**
 * Contrato base para las operaciones CRUD que deben implementar los DAO del
 * sistema.
 *
 * @param <T> tipo de entidad que gestiona el repositorio.
 */
public interface CRUD<T> {

	String create(T t) throws Exception;

	<K> T readone(K id) throws Exception;
	
	List<T> readall() throws Exception;
}
