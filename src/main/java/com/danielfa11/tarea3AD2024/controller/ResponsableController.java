package com.danielfa11.tarea3AD2024.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.danielfa11.tarea3AD2024.config.StageManager;
import com.danielfa11.tarea3AD2024.modelo.ConjuntoContratado;
import com.danielfa11.tarea3AD2024.modelo.EnvioACasa;
import com.danielfa11.tarea3AD2024.modelo.Estancia;
import com.danielfa11.tarea3AD2024.modelo.Parada;
import com.danielfa11.tarea3AD2024.modelo.Peregrino;
import com.danielfa11.tarea3AD2024.modelo.Servicio;
import com.danielfa11.tarea3AD2024.modelo.Sesion;
import com.danielfa11.tarea3AD2024.services.DB4OService;
import com.danielfa11.tarea3AD2024.services.EstanciaService;
import com.danielfa11.tarea3AD2024.services.ParadaService;
import com.danielfa11.tarea3AD2024.services.PeregrinoService;
import com.danielfa11.tarea3AD2024.utils.Utils;
import com.danielfa11.tarea3AD2024.view.FxmlView;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

@Controller
public class ResponsableController implements Initializable{

	@FXML
	private Label lblBienvenido;
	
	@FXML
	private GridPane panelPrincipal;
	
	@FXML
	private GridPane panelSellar;
	
	@FXML
	private Label lblNombreParada;
		
	@FXML
	private Label lblRegionParada;
	
	@FXML
	private Label lblResponsableParada;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private CheckBox ckEstancia;
	
	@FXML
	private CheckBox ckVip;
	
	@FXML
	private Label lblServicios;
	
	@FXML
	private CheckBox ckServicios;
	
	@FXML
	private AnchorPane panelConjunto;
	
	@FXML
	private TableView<Servicio> tablaServicios;
	
		@FXML
		private TableColumn<Servicio, String> columnaServicio;
		
		@FXML
		private TableColumn<Servicio, Double> columnaPrecio;
	
	@FXML
	private ComboBox<String> cboxPago;
	
	@FXML
	private AnchorPane panelEnvio;
	
		@FXML
		private TextField txtDireccion;
		
		@FXML
		private TextField txtLocalidad;

		@FXML
		private TextField txtPeso;
		
		@FXML
		private TextField txtX;
		
		@FXML
		private TextField txtY;
		
		@FXML
		private TextField txtZ;
		
		@FXML
		private CheckBox ckUrgente;
	
	@FXML
	private TextArea txtExtra;
	
	@FXML
	private GridPane panelExportar;
	
	@FXML
	private Label lblNombreParadaExportar;
	
	@FXML
	private Label lblRegionParadaExportar;
	
	@FXML
	private Label lblResponsableParadaExportar;
	
	@FXML
	private DatePicker dpFecha1;
	
	@FXML
	private DatePicker dpFecha2;
		
	@FXML
    private TableView<Estancia> tablaEditar;
        
        @FXML
        private TableColumn<Estancia, Long> columnaID = new TableColumn<>("Id");
        
        @FXML
        private TableColumn<Estancia, LocalDate> columnaFecha = new TableColumn<>("Fecha");

        @FXML
        private TableColumn<Estancia, Boolean> columnaVip = new TableColumn<>("Vip");
        
        @FXML
        private TableColumn<Estancia, String> columnaPeregrino = new TableColumn<>("Peregrino");
        
        @FXML
        private TableColumn<Estancia, String> columnaParada = new TableColumn<>("Parada");
        
    @FXML
    private AnchorPane panelEnvios;
        
    @Autowired
    private DB4OService db4oService;
    
	@Autowired
	private EstanciaService estanciaService;

	@Autowired
	private ParadaService paradaService;
	
	@Autowired
	private PeregrinoService peregrinoService;	

	@Lazy
    @Autowired
    private StageManager stageManager;
	
	private Parada parada;	
	
	private List<Estancia> estancias = new ArrayList<>(); 
	
	private ObservableList<Estancia> estanciasTabla = FXCollections.observableArrayList(estancias);
	
	private List<Servicio> serviciosSeleccionados = new ArrayList<>();
	
	private ObservableList<Servicio> serviciosTabla = FXCollections.observableArrayList();
	
	private boolean visible = false;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		for(Servicio s : db4oService.retrieveAllServicio()) {
			
			if(s.getParadas().contains(Sesion.getSesion().getId())) {
				serviciosTabla.add(s);
				
			}			
		}
		
		tablaServicios.setItems(serviciosTabla);
		
		columnaServicio.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
		
		cboxPago.getItems().addAll("Efectivo", "Tarjeta", "Bizum");
		
		tablaServicios.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Servicio>) change -> {
			
			serviciosSeleccionados = tablaServicios.getSelectionModel().getSelectedItems();
			
			panelEnvio.setVisible(false);
			
			for(Servicio s : serviciosSeleccionados) {
				
				if(s.getNombre().equals("Envio a Casa")) {
					panelEnvio.setVisible(true);
				}
				
			}
			
        });
		
		ckServicios.setOnAction(event -> {
			
			visible = !visible;		
			panelConjunto.setVisible(visible);
			
		});
		
		lblBienvenido.setText("Bienvenido/a, "+Sesion.getSesion().getUsuario());
		
		parada = paradaService.find(Sesion.getSesion().getId());	
		
		// Sellar Parada
		
		lblNombreParada.setText("Nombre: "+parada.getNombre() );
		lblRegionParada.setText("Region: "+parada.getRegion() );
		lblResponsableParada.setText("Responsable: "+parada.getResponsable() );
		
		// Exportar Parada
		
		lblNombreParadaExportar.setText("Nombre: "+parada.getNombre() );
		lblRegionParadaExportar.setText("Region: "+parada.getRegion() );
		lblResponsableParadaExportar.setText("Responsable: "+parada.getResponsable() );
		
		tablaEditar.setItems(estanciasTabla);
		
		columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		columnaVip.setCellValueFactory(new PropertyValueFactory<>("vip"));
		columnaPeregrino.setCellValueFactory(cellData ->
			new SimpleStringProperty(cellData.getValue().getPeregrino().getNombre())		
		);
		
		columnaParada.setCellValueFactory(cellData ->
			new SimpleStringProperty(cellData.getValue().getParada().getNombre())
		);
		
	}
	
	public void clickSellar() {
		
		if(Utils.confirmarDatos().getResult().equals(ButtonType.OK)) {
			if(txtId.getText().isBlank()) {
				alertaIdVacio();
			}
			else if(!validarId(txtId.getText())){
				alertaIdIncorrecto();
			}
			else if(!peregrinoService.existsById(Long.valueOf(txtId.getText()))) {
				alertaIdInexistente();
			}
			else if(peregrinoService.existsById(Long.valueOf(txtId.getText()))) {
				
				Peregrino peregrino = peregrinoService.find(Long.valueOf(txtId.getText()));					
				
				
				if(parada.getPeregrinos().contains(peregrino)){	
					
					alertaPeregrino();
					}
				else {
					
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
			        alert.setTitle("Informacion del peregrino");
			        alert.setContentText("Nombre: "+peregrino.getNombre()+"\n"
			        		+"Nacionalidad: "+peregrino.getNacionalidad());
			        alert.showAndWait();
			        
					peregrino.getCarnet().setDistancia(peregrino.getCarnet().getDistancia()+5);		
					peregrino.getParadas().add(parada);							
					peregrinoService.save(peregrino);					
					parada.getPeregrinos().add(peregrino);
					paradaService.save(parada);	
					
					
					
					if(ckEstancia.isSelected()) {
						
						Estancia estancia = new Estancia();
						
						if(ckVip.isSelected()) {
							peregrino.getCarnet().setNvips(peregrino.getCarnet().getNvips()+1);

							estancia.setVip(true);
						}					
			
						estancia.setFecha(LocalDate.now());						
						estancia.setParada(parada);
						estancia.setPeregrino(peregrino);

						peregrino.getEstancias().add(estancia);
						peregrinoService.save(peregrino);
						
						if(visible) {
						
							ConjuntoContratado cc = new ConjuntoContratado();
							
							cc.setIdEstancia(estanciaService.findTopByOrderByIdDesc().getId());
							
							if(validarServicios() && validarPago()) {
								
								double precio = 0.00;
								
								switch(cboxPago.getValue()) {
								
									case "Tarjeta" -> cc.setModoPago('T');
									
									case "Efectivo" -> cc.setModoPago('E');
									
									case "Bizum" -> cc.setModoPago('B');
								
								}
								
								cc.setExtra(txtExtra.getText());
								
								for(Servicio s : serviciosSeleccionados) {
									
									precio+=s.getPrecio();
									cc.getServicios().add(s.getId());
								}
								
								DecimalFormat df = new DecimalFormat("#.00");
								
								precio = Double.valueOf(df.format(precio));
								
								cc.setPrecioTotal(precio);
								
								if(panelEnvio.isVisible()) {
									
									Servicio s = new Servicio(); 
									
									EnvioACasa e = new EnvioACasa();

									
								}
								
							}
						}
						
						

					}
				}
			}
		}
	}
	
	

	
	public void clickExportar() {
		
		if(Utils.confirmarDatos().getResult().equals(ButtonType.OK)) {
			
			estanciasTabla.clear();
			
			if(dpFecha1.getValue() == null || dpFecha2.getValue() == null){
				alertaFechasVacias();
			}		
			else if(dpFecha1.getValue().isAfter(LocalDate.now()) || dpFecha2.getValue().isAfter(LocalDate.now())){
				alertaFechasFuturas();
			}
			else if(dpFecha1.getValue().isAfter(dpFecha2.getValue())) {
				alertaFechasIncorrectas();
			}
			else if(dpFecha1.getValue().isBefore(dpFecha2.getValue())){

				estanciasTabla.addAll(estanciaService.findByFechaBetweenAndParada(dpFecha1.getValue(), dpFecha2.getValue(), parada));
				
			
				dpFecha1.setValue(null);
				dpFecha2.setValue(null);
				
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		        alert.setTitle("Atención");
		        alert.setContentText("¿Quiere volver a exportar estancias?");
		        alert.showAndWait();
		        
		        if(!alert.getResult().equals(ButtonType.OK)) {
		        	panelPrincipal.setVisible(true);
		        	panelSellar.setVisible(false);
		    		panelExportar.setVisible(false);		    		
		        }
			}
		}

	}
	
	
	
	public void clickMenuExportar() {	
		
		panelExportar.setVisible(true);
		panelPrincipal.setVisible(false);
		panelSellar.setVisible(false);
		panelEnvios.setVisible(false);
		
	}
	
	public void clickMenuSellar() {
		
		panelSellar.setVisible(true);
		panelExportar.setVisible(false);
		panelPrincipal.setVisible(false);
		panelEnvios.setVisible(false);
		
	}
	
	public void clickMenuEnvios() {
		panelEnvios.setVisible(true);
		panelSellar.setVisible(false);
		panelExportar.setVisible(false);
		panelPrincipal.setVisible(false);
	}
	
	
	public void clickCerrarSesion() {
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Atención");
        alert.setContentText("¿Desea cerrar sesion?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)) 
        {
			Sesion.getSesion().setId(-1L);
			Sesion.getSesion().setUsuario("");;
			Sesion.getSesion().setPerfil("");
			stageManager.switchScene(FxmlView.LOGIN);
        }
	}
	
	public void clickSalir()
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Atención");
        alert.setContentText("¿Desea salir de la aplicación?");
        alert.showAndWait();
        
        if (alert.getResult().equals(ButtonType.OK)) 
        {
            Platform.exit();
        }
	}
	
	private boolean validarId(String text) {

        String regex = "^[1-9]\\d*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if(matcher.matches()) {
        	return true;
        }
        else{
        	return false;
        }
    }
	
	private boolean validarServicios() {
		if(serviciosSeleccionados.size()>0) {
			return true;
		}
		else {
			alertaServiciosVacios();
			return false;
		}
	}
	
	private boolean validarPago() {
		if(cboxPago.getValue().isEmpty()) {
			alertaPagoVacio();
			return false;
		}
		else {
			return true;
		}
	}
	
	private void alertaPagoVacio() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Metodo de pago sin seleccionar");
		alerta.setContentText("Selecciona metodo de pago");
		alerta.show();
	}
	
	private void alertaIdVacio() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Id vacio");
		alerta.setContentText("Introduce el id del peregrino");
		alerta.show();
	}
	
	private void alertaServiciosVacios() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("No hay servicios");
		alerta.setContentText("Selecciona algun servicio");
		alerta.show();
	}
	
	
	private void alertaIdIncorrecto() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Id incorrecto");
		alerta.setContentText("Introduce un numero");
		alerta.show();
	}
	
	private void alertaIdInexistente() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Id incorrecto");
		alerta.setContentText("No existe peregrino relacionado con ese id");
		alerta.show();
	}
	
	private void alertaPeregrino() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Peregrino ya existente");
		alerta.setContentText("El peregrino ya ha sellado en esta parada");
		alerta.show();
	}
	
	private void alertaFechasVacias() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Fechas vacias");
		alerta.setContentText("Escoge las fechas");
		alerta.show();
	}
	
	private void alertaFechasFuturas() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Fechas incorrectas");
		alerta.setContentText("Una de las fechas es un dia despues del actual");
		alerta.show();
	}

	private void alertaFechasIncorrectas() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Fechas incorrectas");
		alerta.setContentText("La primera fecha es despues de la segunda fecha");
		alerta.show();
	}
	
	
	
}
