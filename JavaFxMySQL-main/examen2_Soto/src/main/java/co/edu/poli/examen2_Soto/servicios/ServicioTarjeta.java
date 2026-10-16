package co.edu.poli.examen2_Soto.servicios;

import co.edu.poli.examen2_Soto.modelo.EstadoInvalidoException;
import co.edu.poli.examen2_Soto.modelo.EstadoTarjeta;
import co.edu.poli.examen2_Soto.modelo.Tarjeta;

/**
 * Capa de servicio que valida y coordina las operaciones relacionadas con
 * tarjetas, incluyendo creación, consulta y actualización de estado.
 */
public class ServicioTarjeta {

	private final DAOTarjeta dao = new DAOTarjeta();

	public String crearTarjeta(Tarjeta tarjeta) throws Exception {
		if (!validarTarjeta(tarjeta)) {
			throw new EstadoInvalidoException("No se puede guardar una tarjeta sin un estado válido.");
		}
		return dao.create(tarjeta);
	}

	public Tarjeta consultarTarjeta(String numero) throws Exception {
		return dao.readone(numero);
	}

	public String actualizarEstado(String numero, EstadoTarjeta estado) throws Exception {
		if (estado == null) {
			throw new EstadoInvalidoException("El estado es obligatorio.");
		}
		return dao.actualizarEstado(numero, estado);
	}

	public boolean validarTarjeta(Tarjeta tarjeta) {
		return tarjeta != null && tarjeta.validarEstado();
	}
}
