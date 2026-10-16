package co.edu.poli.examen2_Soto.tests.unitaria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import co.edu.poli.examen2_Soto.modelo.Debito;
import co.edu.poli.examen2_Soto.modelo.EstadoInvalidoException;
import co.edu.poli.examen2_Soto.modelo.EstadoTarjeta;
import co.edu.poli.examen2_Soto.modelo.Tarjeta;
import co.edu.poli.examen2_Soto.modelo.Titular;

public class TestTarjeta {

	@Test
	void bloquear_cambiaEstadoABloqueada() {
		Titular titular = new Titular("1", "Test");

		Tarjeta t = new Debito("123", "2025-12-25", EstadoTarjeta.ACTIVA, titular, 1000);

		String mensaje = t.bloquear();

		assertEquals(EstadoTarjeta.BLOQUEADA, t.getEstado());
		assertTrue(mensaje.contains("BLOQUEADA"));
	}

	@Test
	void activar_cambiaEstadoAActiva() {
		Titular titular = new Titular("1", "Test");

		Tarjeta t = new Debito("123", "2025-12-25", EstadoTarjeta.BLOQUEADA, titular, 1000);

		String mensaje = t.activar();

		assertEquals(EstadoTarjeta.ACTIVA, t.getEstado());
		assertTrue(mensaje.contains("ACTIVADA"));
	}

	@Test
	void cancelar_cambiaEstadoACancelada() {
		Titular titular = new Titular("1", "Test");

		Tarjeta t = new Debito("123", "2025-12-25", EstadoTarjeta.ACTIVA, titular, 1000);

		String mensaje = t.cancelar();

		assertEquals(EstadoTarjeta.CANCELADA, t.getEstado());
		assertTrue(mensaje.contains("CANCELADA"));
	}

	@Test
	void constructor_sinEstado_lanzaExcepcion() {
		Titular titular = new Titular("1", "Test");

		assertThrows(EstadoInvalidoException.class, () -> new Debito("123", "2025-12-25", null, titular, 1000));
	}

	@Test
	void setEstado_null_lanzaExcepcion() {
		Titular titular = new Titular("1", "Test");
		Tarjeta t = new Debito("123", "2025-12-25", EstadoTarjeta.ACTIVA, titular, 1000);

		assertThrows(EstadoInvalidoException.class, () -> t.setEstado(null));
		assertEquals(EstadoTarjeta.ACTIVA, t.getEstado()); // no se modificó
	}

	@Test
	void validarEstado_conEstado_esVerdadero() {
		Titular titular = new Titular("1", "Test");
		Tarjeta t = new Debito("123", "2025-12-25", EstadoTarjeta.ACTIVA, titular, 1000);

		assertTrue(t.validarEstado());
	}

	@Test
	void fromString_valorInvalido_lanzaExcepcion() {
		assertEquals(EstadoTarjeta.BLOQUEADA, EstadoTarjeta.fromString("BLOQUEADA"));
		assertThrows(EstadoInvalidoException.class, () -> EstadoTarjeta.fromString("XYZ"));
		assertThrows(EstadoInvalidoException.class, () -> EstadoTarjeta.fromString(null));
	}

	@Test
	void getters_retornaValoresCorrectos() {
		Titular titular = new Titular("1", "Test");

		Tarjeta t = new Debito("123", "2025-12-25", EstadoTarjeta.ACTIVA, titular, 1000);

		assertEquals("123", t.getNumero());
		assertEquals("2025-12-25", t.getFechaExp());
		assertEquals(EstadoTarjeta.ACTIVA, t.getEstado());
		assertEquals(titular, t.getTitular());
	}

	@Test
	void setters_modificanValores() {
		Titular titular = new Titular("1", "Test");
		Titular nuevo = new Titular("2", "Nuevo");

		Tarjeta t = new Debito("123", "2025-12-25", EstadoTarjeta.ACTIVA, titular, 1000);

		t.setNumero("999");
		t.setFechaExp("2030-01-01");
		t.setEstado(EstadoTarjeta.CANCELADA);
		t.setTitular(nuevo);

		assertEquals("999", t.getNumero());
		assertEquals("2030-01-01", t.getFechaExp());
		assertEquals(EstadoTarjeta.CANCELADA, t.getEstado());
		assertEquals(nuevo, t.getTitular());
	}

	@Test
	void toString_contieneDatos() {
		Titular titular = new Titular("1", "Test");

		Tarjeta t = new Debito("123", "2025-12-25", EstadoTarjeta.ACTIVA, titular, 1000);

		String texto = t.toString();

		assertTrue(texto.contains("123"));
		assertTrue(texto.contains("2025-12-25"));
		assertTrue(texto.contains("Activa"));
	}
}
