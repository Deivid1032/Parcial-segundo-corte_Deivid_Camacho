package co.edu.poli.examen2_Soto.tests.unitaria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import co.edu.poli.examen2_Soto.modelo.Credito;
import co.edu.poli.examen2_Soto.modelo.EstadoInvalidoException;
import co.edu.poli.examen2_Soto.modelo.EstadoTarjeta;
import co.edu.poli.examen2_Soto.modelo.Titular;
import co.edu.poli.examen2_Soto.servicios.ServicioTarjeta;

/** Pruebas que NO tocan la base de datos (la validación ocurre antes del DAO). */
public class TestServicioTarjeta {

	private final ServicioTarjeta servicio = new ServicioTarjeta();

	@Test
	void crearTarjeta_null_noSeGuarda() {
		assertThrows(EstadoInvalidoException.class, () -> servicio.crearTarjeta(null));
	}

	@Test
	void actualizarEstado_null_noSeGuarda() {
		assertThrows(EstadoInvalidoException.class, () -> servicio.actualizarEstado("123", null));
	}

	@Test
	void validarTarjeta_conEstado_esValida() {
		Credito c = new Credito("123", "25/12/2025", EstadoTarjeta.ACTIVA, new Titular("1", "Test"), 1000);

		assertTrue(servicio.validarTarjeta(c));
		assertFalse(servicio.validarTarjeta(null));
	}
}
