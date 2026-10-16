package co.edu.poli.examen2_Soto.modelo;

/**
 * Representa una tarjeta de pago genérica con número, fecha de expiración,
 * estado y titular asociado.
 *
 * <p>Esta clase sirve como base para los tipos específicos de tarjeta que
 * existen en el sistema, como débito y crédito.</p>
 */
public abstract class Tarjeta {

	private String numero;

	private String fechaExp;

	private EstadoTarjeta estado;

	private Titular titular;

	public Tarjeta(String numero, String fechaExp, EstadoTarjeta estado, Titular titular) {
		this.numero = numero;
		this.fechaExp = fechaExp;
		this.estado = exigirEstado(estado);
		this.titular = titular;
	}

	private static EstadoTarjeta exigirEstado(EstadoTarjeta estado) {
		if (estado == null) {
			throw new EstadoInvalidoException("El estado es obligatorio");
		}
		return estado;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getFechaExp() {
		return fechaExp;
	}

	public void setFechaExp(String fechaExp) {
		this.fechaExp = fechaExp;
	}

	public EstadoTarjeta getEstado() {
		return estado;
	}

	public void setEstado(EstadoTarjeta estado) {
		this.estado = exigirEstado(estado);
	}

	public Titular getTitular() {
		return titular;
	}

	public void setTitular(Titular titular) {
		this.titular = titular;
	}

	public void cambiarEstado(EstadoTarjeta estado) {
		this.estado = exigirEstado(estado);
	}

	public boolean validarEstado() {
		return estado != null;
	}

	public String bloquear() {
		this.estado = EstadoTarjeta.BLOQUEADA;
		return "Tarjeta " + numero + " BLOQUEADA.";
	}

	public String activar() {
		this.estado = EstadoTarjeta.ACTIVA;
		return "Tarjeta " + numero + " ACTIVADA.";
	}

	public String cancelar() {
		this.estado = EstadoTarjeta.CANCELADA;
		return "Tarjeta " + numero + " CANCELADA.";
	}

	@Override
	public String toString() {
		return "numero=" + numero + ", fechaExp=" + fechaExp + ", estado=" + estado + ", titular=" + titular;
	}
}
