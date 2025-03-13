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
import com.danielfa11.tarea3AD2024.modelo.Direccion;
import com.danielfa11.tarea3AD2024.modelo.EnvioACasa;
import com.danielfa11.tarea3AD2024.modelo.Estancia;
import com.danielfa11.tarea3AD2024.modelo.Parada;
import com.danielfa11.tarea3AD2024.modelo.Peregrino;
import com.danielfa11.tarea3AD2024.modelo.Servicio;
import com.danielfa11.tarea3AD2024.modelo.Sesion;
import com.danielfa11.tarea3AD2024.services.DB4OService;
import com.danielfa11.tarea3AD2024.services.EXISTDBService;
import com.danielfa11.tarea3AD2024.services.EstanciaService;
import com.danielfa11.tarea3AD2024.services.ODBService;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
	private Label lblDatosPeregrino;
	
	@FXML
	private Label lblPeregrinoNombre;
	
	@FXML
	private Label lblPeregrinoNacionalidad;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private CheckBox ckEstancia;
	
	@FXML
	private Label lblCkVip;
	
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
    
    @FXML
    private TableView<EnvioACasa> tablaEnvios;
        
        @FXML
        private TableColumn<EnvioACasa, Long> columnaEnviosId = new TableColumn<>("Id");
        
        @FXML
        private TableColumn<EnvioACasa, Double> columnaEnviosPeso = new TableColumn<>("Peso");

        @FXML
        private TableColumn<EnvioACasa, String> columnaEnviosVolumen = new TableColumn<>("Volumen");
        
        @FXML
        private TableColumn<EnvioACasa, Boolean> columnaEnviosUrgente = new TableColumn<>("Urgente");
        
        @FXML
        private TableColumn<EnvioACasa, String> columnaEnviosDireccion = new TableColumn<>("Direccion");
        
        @FXML
        private TableColumn<EnvioACasa, String> columnaEnviosLocalidad = new TableColumn<>("Localidad");

    @FXML
    private AnchorPane panelCarnets;
    
    @FXML
    private TreeView<String> treeView;
    
    @Autowired
    private DB4OService db4oService;
    
    @Autowired
    private ODBService odbService;

	@Autowired
	private EstanciaService estanciaService;

	@Autowired
	private ParadaService paradaService;
	
	@Autowired
	private PeregrinoService peregrinoService;	

	@Autowired
	private EXISTDBService existdbService;
	
	
	@Lazy
    @Autowired
    private StageManager stageManager;
	
	private Parada parada;	
	
	private List<Estancia> estancias = new ArrayList<>(); 
	
	private ObservableList<Estancia> estanciasTabla = FXCollections.observableArrayList(estancias);
	
	private List<Servicio> serviciosSeleccionados = null;
	
	private ObservableList<Servicio> serviciosTabla = FXCollections.observableArrayList();
	
	private ObservableList<EnvioACasa> enviosTabla = FXCollections.observableArrayList();
	
	private boolean visible = false;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		visible = false;
		panelEnvio.setVisible(false);
		
		
		tablaEnvios.setItems(enviosTabla);
		
		columnaEnviosId.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
		columnaEnviosPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
		columnaEnviosUrgente.setCellValueFactory(new PropertyValueFactory<>("urgente"));
		
		columnaEnviosVolumen.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().volumenToString()));
		columnaEnviosDireccion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDireccion().getDireccion()));
		columnaEnviosLocalidad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDireccion().getLocalidad()));
		
		
		tablaServicios.setItems(serviciosTabla);
		
		columnaServicio.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
		
		cboxPago.getItems().addAll("Efectivo", "Tarjeta", "Bizum");
		
		tablaServicios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);	

		tablaServicios.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Servicio>) change -> {
			
			serviciosSeleccionados = tablaServicios.getSelectionModel().getSelectedItems();
			
			panelEnvio.setVisible(false);
			
			for(Servicio s : serviciosSeleccionados) {
				
				if(s.getNombre().equals("Envio a Casa")) {
					panelEnvio.setVisible(true);
				}
				
			}
			
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
		
		txtId.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.isBlank()) {
				lblDatosPeregrino.setVisible(false);
				lblPeregrinoNombre.setVisible(false);
				lblPeregrinoNacionalidad.setVisible(false);
			}
			else if(peregrinoService.existsById(Long.valueOf(newValue))){
				
				Peregrino peregrino = peregrinoService.find(Long.valueOf(txtId.getText()));	
				
				lblPeregrinoNombre.setText("Nombre: "+peregrino.getNombre());
				lblPeregrinoNacionalidad.setText("Nacionalidad: "+peregrino.getNacionalidad());			
				
				lblDatosPeregrino.setVisible(true);
				lblPeregrinoNombre.setVisible(true);
				lblPeregrinoNacionalidad.setVisible(true);
				
				
			}
			else {
				lblDatosPeregrino.setVisible(false);
				lblPeregrinoNombre.setVisible(false);
				lblPeregrinoNacionalidad.setVisible(false);
			}
			
		});
		
		ckEstancia.selectedProperty().addListener((observable, oldValue, newValue) -> {
		
			if(ckEstancia.isSelected()) {
				ckVip.setVisible(true);
				lblCkVip.setVisible(true);			
			}
			else {
				ckVip.setSelected(false);
				ckVip.setVisible(false);
				lblCkVip.setVisible(false);
			}
			
		});
		
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
				boolean peregrinoYaSellado = false;
				
				for(Peregrino p : parada.getPeregrinos()) {
					if(p.getId().equals(peregrino.getId())) {
						peregrinoYaSellado = true;
					}
				}
				
				
				if(peregrinoYaSellado){	
					
					alertaPeregrino();
					}
				else {
			        
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
				
						if(serviciosTabla.size()>0 && !visible) {
							
							visible = !visible;		
							panelConjunto.setVisible(visible);
							contratar();
						}
						else if(serviciosTabla.size()<=0){
							noHayServiciosParada();
						}
						peregrinoSellado();
					}
				}
			}
		}
	}
	
	public void contratarServicios() {
		
		if(Utils.confirmarDatos().getResult().equals(ButtonType.OK)) {
			Servicio envio = new Servicio();
			
			if(visible) {
				
				ConjuntoContratado cc = new ConjuntoContratado();
				
				cc.setId(db4oService.findConjuntoContratadoLastId());
				
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
						
						if(s.getNombre().equals("Envio a Casa")) {
							envio = s;
						}
					}
					
					DecimalFormat df = new DecimalFormat("#.00");
					
					precio = Double.valueOf(df.format(precio).replace(',', '.'));
					
					cc.setPrecioTotal(precio);
					
					if(!panelEnvio.isVisible()) {
					
						db4oService.storeConjuntoContratado(cc);
						
						for(Servicio s : serviciosSeleccionados) {
							
							s.getConjuntos().add(cc.getId());
							db4oService.updateServicio(s.getId(), s);
						}
					}				
					else if(panelEnvio.isVisible()) {
						
						
						
						if(validarDireccion(txtDireccion.getText())
							&& Utils.validarNombre(txtLocalidad.getText())
							&& validarPeso(txtPeso.getText())
							&& validarVolumen(txtX.getText())
							&& validarVolumen(txtY.getText())
							&& validarVolumen(txtZ.getText())
								) {
							
							db4oService.storeConjuntoContratado(cc);
							
							for(Servicio s : serviciosSeleccionados) {
								
								s.getConjuntos().add(cc.getId());
								db4oService.updateServicio(s.getId(), s);
							}
							
							
							EnvioACasa e = new EnvioACasa();
							
							int[] volumen = new int[3];
							
							volumen[0] = Integer.valueOf(txtX.getText());
							volumen[1] = Integer.valueOf(txtY.getText());
							volumen[2] = Integer.valueOf(txtZ.getText());
							
							e.setId(envio.getId());
							e.setNombre(envio.getNombre());
							e.setParadas(envio.getParadas());
							e.setPrecio(envio.getPrecio());
							e.setConjuntos(envio.getConjuntos());
							e.getConjuntos().add(cc.getId());
							e.setParada(Sesion.getSesion().getId());
							
							Direccion d = new Direccion(txtDireccion.getText(), txtLocalidad.getText());
									
							e.setPeso(Double.valueOf(txtPeso.getText()));
							e.setDireccion(d);
							e.setVolumen(volumen);
							
							if(ckUrgente.isSelected()) {
								e.setUrgente(true);
							}
							
							odbService.storeEnvio(e);
							
							
							
							visible = false;
							panelConjunto.setVisible(false);
							panelEnvio.setVisible(false);
							
							tablaServicios.getSelectionModel().clearSelection();
							
							serviciosSeleccionados = null;
							
							cboxPago.setValue(null);
							txtDireccion.setText("");
							txtExtra.setText("");
							txtLocalidad.setText("");
							txtPeso.setText("");
							txtX.setText("");
							txtY.setText("");
							txtZ.setText("");
							
							serviciosContratados();
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
		    		panelCarnets.setVisible(false);
		    		estanciasTabla.clear();
		        }
			}
		}

	}
	
	
	
	public void clickMenuExportar() {
		
		panelEnvios.setVisible(false);
		panelExportar.setVisible(true);
		panelPrincipal.setVisible(false);
		panelSellar.setVisible(false);
		panelCarnets.setVisible(false);
	}
	
	public void clickMenuSellar() {
		
		if(db4oService.retrieveAllServicio().size()>0) {
			
			serviciosTabla.clear();
			
			for(Servicio s : db4oService.retrieveAllServicio()) {
				
				if(s.getParadas().contains(Sesion.getSesion().getId())) {
					serviciosTabla.add(s);					
				}			
			}
			if(!(serviciosTabla.size()>0)) {
				noHayServiciosParada();
			}
			
		}
		else {
			Utils.noHayServicios();
		}
		
		
		
		panelEnvios.setVisible(false);
		panelSellar.setVisible(true);
		panelExportar.setVisible(false);
		panelPrincipal.setVisible(false);
		panelCarnets.setVisible(false);
		
	}
	
	public void clickMenuCarnets() {
		
		panelEnvios.setVisible(false);
		panelSellar.setVisible(false);
		panelExportar.setVisible(false);
		panelPrincipal.setVisible(false);
		panelCarnets.setVisible(true);
				
		treeView.setRoot(null);
		
		int contador = 0;
		
	    List<String> xmls = existdbService.getAllResourcesFromCollection("/"+parada.getNombre());

	    TreeItem<String> rootItem = new TreeItem<>("Carnets");
		rootItem.setExpanded(true);

	    for (String xml : xmls) {
	    	contador++;
	        TreeItem<String> xmlItem = new TreeItem<>("Carnet "+contador);
	        
	        String[] lineas = xml.split("\n");
	        for (String linea : lineas) {
	            xmlItem.getChildren().add(new TreeItem<>(linea));
	        }

	        rootItem.getChildren().add(xmlItem);
	    }

	    treeView.setRoot(rootItem);
	    
		
		
		
	}
	
	public void clickMenuEnvios() {
		
		if(odbService.retrieveAllEnvios(Sesion.getSesion().getId())!=null) {
			enviosTabla.addAll(odbService.retrieveAllEnvios(Sesion.getSesion().getId()));
		}		
		
		panelEnvios.setVisible(true);
		panelSellar.setVisible(false);
		panelExportar.setVisible(false);
		panelCarnets.setVisible(false);
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
	
	private boolean validarDireccion(String direccion) {
		
		String regex = "^(?i)(Calle|Avda\\.?|Avenida|Plaza|Paseo|Ronda|Carretera)\\s+[A-Za-zÁÉÍÓÚáéíóúÑñ\\s\\.\\-]+,\\s*\\d+[A-Za-z]?\\s*,\\s*\\d{5}\\s+[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$";
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(direccion);
        
        if(direccion.isBlank()){
        	alertaDireccionVacia();
        	return false;
        }    
        else if(matcher.matches()) {
        	return true;
        }
        else{
        	alertaDireccion();
        	return false;
        }
				
	}
	
	public boolean validarPeso(String peso) {
		String pesoRegex = "^[1-9]\\d*\\.\\d{2}$";
        Pattern pattern = Pattern.compile(pesoRegex);
        Matcher matcher = pattern.matcher(peso);
        
        if(peso.isBlank()) {
        	alertaPesoVacio();
        	return false;
        }        
        else if(matcher.matches()) {
        	return true;
        }
        else {
        	alertaPeso();
        	return false;
        }
	}
	
	private boolean validarVolumen(String volumen) {
		
		String regex = "^[1-9]\\d*$";
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(volumen);
        
        if(volumen.isBlank()){
        	alertaVolumenVacio();
        	return false;
        }    
        else if(matcher.matches()) {
        	return true;
        }
        else{
        	alertaVolumen();
        	return false;
        }
				
	}
	
	private boolean validarServicios() {
		if(serviciosSeleccionados != null) {
			return true;
		}
		else {
			alertaServiciosVacios();
			return false;
		}
	}
	
	private boolean validarPago() {
		if(cboxPago.getValue()==null) {
			alertaPagoVacio();
			return false;
		}
		else {
			return true;
		}
	}
	
	public static void noHayServiciosParada() {
		Alert alerta = new Alert(AlertType.INFORMATION);
		alerta.setTitle("Parada sin servicios");
		alerta.setContentText("Esta parada no tiene servicios");
		alerta.show();
	}
	
	private void alertaDireccionVacia() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Direccion vacia");
		alerta.setContentText("Introduce una direccion");
		alerta.show();
	}
	
	private void alertaDireccion() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Direccion invalida");
		alerta.setContentText("Introduce una direccion valida \n"
				+ "Calle o parecidos [Nombre de la calle], [numero de portal], [codigo postal] [Localidad]");
		alerta.show();
	}
	
	
	public static void alertaPesoVacio() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Peso vacio");
		alerta.setContentText("No dejes el peso vacio");
		alerta.show();
	}
	
	public static void alertaPeso() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Peso invalido");
		alerta.setContentText("Introduzca un peso valido con dos decimales");
		alerta.show();
	}
	
	private void alertaVolumenVacio() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Volumen vacio");
		alerta.setContentText("Uno de los valores del volumen esta vacio");
		alerta.show();
	}
	
	private void alertaVolumen() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Volumen invalido");
		alerta.setContentText("Introduce un entero positivo en cada valor del volumen");
		alerta.show();
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
	
	private void serviciosContratados() {
		Alert alerta = new Alert(AlertType.INFORMATION);
		alerta.setTitle("Servicios contratados");
		alerta.setContentText("Servicios contratados");
		alerta.show();
	}
	
	private void peregrinoSellado() {
		Alert alerta = new Alert(AlertType.INFORMATION);
		alerta.setTitle("Peregrino sellado");
		alerta.setContentText("Peregrino sellado");
		alerta.show();
	}
	
	private void contratar() {
		Alert alerta = new Alert(AlertType.INFORMATION);
		alerta.setTitle("Servicios contratables");
		alerta.setContentText("Si el peregrino quiere contratar algun servicio, seleccionalos, rellena los datos y confirma la contratacion");
		alerta.show();
	}
	
}
