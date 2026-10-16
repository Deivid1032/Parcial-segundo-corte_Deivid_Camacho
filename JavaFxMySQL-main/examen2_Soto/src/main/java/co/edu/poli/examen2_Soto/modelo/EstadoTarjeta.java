package co.edu.poli.examen2_Soto.modelo;

/**
 * Define los estados posibles que puede tener una tarjeta dentro del sistema.
 *
 * <p>Los valores disponibles son activa, bloqueada y cancelada.</p>
 */
public enum EstadoTarjeta {

	ACTIVA("Activa"), BLOQUEADA("Bloqueada"), CANCELADA("Cancelada");

	private final String etiqueta;

	EstadoTarjeta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	/** Texto que ve el usuario (ComboBox, TextArea, etc.). */
	@Override
	public String toString() {
		return etiqueta;
	}

	/** Convierte el valor guardado en la BD ("ACTIVA", ...) al enum. */
	public static EstadoTarjeta fromString(String valor) {
		if (valor == null || valor.isBlank()) {
			throw new EstadoInvalidoException("El estado es obligatorio");
		}
		try {
			return valueOf(valor.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new EstadoInvalidoException("Estado no válido: " + valor);
		}
	}
}
