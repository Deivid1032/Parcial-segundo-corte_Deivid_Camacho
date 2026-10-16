package co.edu.poli.examen2_Soto.modelo;

/**
 * Excepción lanzada cuando se intenta asignar o validar un estado de tarjeta
 * nulo o no permitido.
 */
public class EstadoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EstadoInvalidoException(String mensaje) {
		super(mensaje);
	}
}
