package co.edu.poli.examen2_Soto.controlador;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import co.edu.poli.examen2_Soto.modelo.Credito;
import co.edu.poli.examen2_Soto.modelo.Debito;
import co.edu.poli.examen2_Soto.modelo.EstadoInvalidoException;
import co.edu.poli.examen2_Soto.modelo.EstadoTarjeta;
import co.edu.poli.examen2_Soto.modelo.Tarjeta;
import co.edu.poli.examen2_Soto.modelo.Titular;
import co.edu.poli.examen2_Soto.servicios.DAOTitular;
import co.edu.poli.examen2_Soto.servicios.ServicioTarjeta;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * Controlador de la vista principal que permite consultar, crear y actualizar
 * el estado de las tarjetas del sistema.
 */
public class ControlFormCard {

	// ----- Pestaña Consultar -----
	@FXML
	private Button bttConsulta;
	@FXML
	private TextField txtTarjeta1;
	@FXML
	private TextArea txtAreaResultado;
	@FXML
	private Label lblEstadoConsulta;
	@FXML
	private ComboBox<EstadoTarjeta> cmbNuevoEstado;
	@FXML
	private Button bttActualizarEstado;

	// ----- Pestaña Crear -----
	@FXML
	private Button bttCreacion;
	@FXML
	private TextField txtTarjeta2;
	@FXML
	private DatePicker datepk1;
	@FXML
	private ComboBox<Titular> cmbTitular;
	@FXML
	private ComboBox<EstadoTarjeta> cmbEstado;
	@FXML
	private RadioButton radio1; // Débito
	@FXML
	private RadioButton radio2; // Crédito
	@FXML
	private ToggleGroup tipo;

	private ServicioTarjeta servicio;

	private DAOTitular daoTitular;

	private Tarjeta tarjetaConsultada;

	@FXML
	private void initialize() {
		servicio = new ServicioTarjeta();
		daoTitular = new DAOTitular();

		datepk1.setValue(LocalDate.now());

		cargarEstados();

		List<Titular> lista = null;
		try {
			lista = daoTitular.readall();
		} catch (Exception e) {
			mostrarAlerta(e.getMessage());
		}
		if (lista != null)
			cmbTitular.getItems().setAll(lista);

		txtTarjeta1.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal)
				validarSoloNumeros(txtTarjeta1);
		});

		txtTarjeta2.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal)
				validarSoloNumeros(txtTarjeta2);
		});

	}

	// ------------------------------------------------------------------
	// Estados
	// ------------------------------------------------------------------

	private void cargarEstados() {
		cmbEstado.getItems().setAll(EstadoTarjeta.values());
		cmbNuevoEstado.getItems().setAll(EstadoTarjeta.values());
		configurarTextoVacio(cmbEstado, "Estado (obligatorio)");
		configurarTextoVacio(cmbNuevoEstado, "Nuevo estado");
	}

	/** Muestra un texto guía cuando el ComboBox no tiene selección. */
	private void configurarTextoVacio(ComboBox<EstadoTarjeta> combo, String texto) {
		combo.setButtonCell(new ListCell<EstadoTarjeta>() {
			@Override
			protected void updateItem(EstadoTarjeta item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? texto : item.toString());
			}
		});
	}

	private EstadoTarjeta seleccionarEstado() {
		return cmbEstado.getValue();
	}

	private void mostrarEstadoTarjeta(Tarjeta t) {
		if (t == null) {
			lblEstadoConsulta.setText("");
			cmbNuevoEstado.setValue(null);
		} else {
			lblEstadoConsulta.setText("Estado: " + t.getEstado());
			cmbNuevoEstado.setValue(t.getEstado());
		}
	}

	// ------------------------------------------------------------------
	// Acciones
	// ------------------------------------------------------------------

	@FXML
	private void pressConsulta(ActionEvent event) {
		txtAreaResultado.setText("");
		tarjetaConsultada = null;
		mostrarEstadoTarjeta(null);

		String numero = txtTarjeta1.getText().trim();
		if (numero.isEmpty()) {
			mostrarAlerta("Ingrese número de tarjeta");
			return;
		}

		try {
			Tarjeta t = servicio.consultarTarjeta(numero);

			if (t != null) {
				tarjetaConsultada = t;
				txtAreaResultado.setText(t.toString());
				mostrarEstadoTarjeta(t);
			} else
				mostrarAlerta("No existe el numero de tarjeta");
		} catch (Exception e) {
			mostrarAlerta(e.getMessage());
		}
	}

	@FXML
	private void pressActualizarEstado(ActionEvent event) {
		if (tarjetaConsultada == null) {
			mostrarAlerta("⚠ Primero consulte una tarjeta.");
			return;
		}

		EstadoTarjeta nuevo = cmbNuevoEstado.getValue();
		if (nuevo == null) {
			mostrarAlerta("⚠ Seleccione el nuevo estado.");
			return;
		}

		try {
			String resultado = servicio.actualizarEstado(tarjetaConsultada.getNumero(), nuevo);
			mostrarAlerta(resultado);

			Tarjeta t = servicio.consultarTarjeta(tarjetaConsultada.getNumero());
			if (t != null) {
				tarjetaConsultada = t;
				txtAreaResultado.setText(t.toString());
				mostrarEstadoTarjeta(t);
			}
		} catch (Exception e) {
			mostrarAlerta(e.getMessage());
		}
	}

	@FXML
	private void pressCreacion(ActionEvent event) {

		String numero = txtTarjeta2.getText().trim();
		if (numero.isEmpty()) {
			mostrarAlerta("⚠ Ingrese el número de tarjeta.");
			return;
		}

		if (datepk1.getValue() == null) {
			mostrarAlerta("⚠ Seleccione la fecha de expedición.");
			return;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String fechaExp = datepk1.getValue().format(formatter);

		Titular titular = cmbTitular.getValue();
		if (titular == null) {
			mostrarAlerta("⚠ Seleccione un titular.");
			return;
		}

		EstadoTarjeta estado = seleccionarEstado();
		if (estado == null) {
			mostrarAlerta("⚠ Seleccione el estado de la tarjeta (campo obligatorio).");
			return;
		}

		Tarjeta nueva;

		if (radio1.isSelected()) {
			nueva = new Debito(numero, fechaExp, estado, titular, 0.0);
		} else {
			nueva = new Credito(numero, fechaExp, estado, titular, 1000000);
		}

		try {
			String resultado = servicio.crearTarjeta(nueva);
			mostrarAlerta(resultado);
			if (resultado.startsWith("✔")) {
				limpiarFormCrear();
			}
		} catch (EstadoInvalidoException e) {
			mostrarAlerta("⚠ " + e.getMessage());
		} catch (Exception e) {
			mostrarAlerta(e.getMessage());
		}

	}

	private void mostrarAlerta(String mensaje) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Resultado");
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}

	private void limpiarFormCrear() {
		txtTarjeta2.clear();
		datepk1.setValue(null);
		cmbTitular.setValue(null);
		cmbEstado.setValue(null);
		radio1.setSelected(true);
	}

	private void validarSoloNumeros(TextField txt) {
		String texto = txt.getText().trim();
		if (!texto.isBlank()) {
			if (!texto.matches("\\d+")) {
				txtAreaResultado.setText("");
				txt.setStyle("-fx-border-color: red;");
				mostrarAlerta("Solo números son permitidos!");
				txt.setText("");
				Platform.runLater(() -> txt.requestFocus());
			} else {
				txt.setStyle("");
			}
		} else
			txt.setStyle("");
	}

}
