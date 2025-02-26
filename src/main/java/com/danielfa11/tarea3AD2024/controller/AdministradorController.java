package com.danielfa11.tarea3AD2024.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.danielfa11.tarea3AD2024.config.StageManager;
import com.danielfa11.tarea3AD2024.modelo.Parada;
import com.danielfa11.tarea3AD2024.modelo.Servicio;
import com.danielfa11.tarea3AD2024.modelo.Sesion;
import com.danielfa11.tarea3AD2024.modelo.Usuario;
import com.danielfa11.tarea3AD2024.services.DB4OService;
import com.danielfa11.tarea3AD2024.services.ParadaService;
import com.danielfa11.tarea3AD2024.services.UsuarioService;
import com.danielfa11.tarea3AD2024.utils.Utils;
import com.danielfa11.tarea3AD2024.view.FxmlView;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

@Controller
public class AdministradorController implements Initializable{

	@FXML
	private MenuItem miRegistrar;
	
	@FXML
	private MenuItem miCSesion;
		
	@FXML
	private MenuItem miSalir;
	
	@FXML
	private MenuItem miAyuda;
	
	@FXML
	private GridPane panelPrincipal;
	
	@FXML
	private Label lblBienvenido;
	
	@FXML
	private GridPane panelRegistrar;
	
	@FXML
	private TextField txtNombre;
	
	@FXML
	private TextField txtUsuario;
	
	@FXML
	private PasswordField ptxtContraseña;
	
	@FXML
	private TextField txtCorreo;
	
	@FXML
	private ComboBox<Character> cboxRegion;
	
	@FXML
	private Button btnCancelar;
	
	@FXML
	private Button btnRegistrar;
	
	@FXML
	private Pane panelAyuda;
	
	@FXML
	private WebView webView;
	
	@FXML
	private AnchorPane panelServicio;
	
	@FXML
    private TableView<Parada> tablaServicio;
        
        @FXML
        private TableColumn<Parada, Long> columnaID = new TableColumn<>("Id");
        
        @FXML
        private TableColumn<Parada, String> columnaNombre = new TableColumn<>("Nombre");

        @FXML
        private TableColumn<Parada, Character> columnaRegion = new TableColumn<>("Region");     
        
        @FXML
        private TableColumn<Parada, String> columnaResponsable = new TableColumn<>("Responsable");     
        
    @FXML
    private TextField txtNombreServicio;
    
    @FXML
    private TextField txtPrecio;
        
	@Lazy
    @Autowired
    private StageManager stageManager;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ParadaService paradaService;
	
	@Autowired
	private DB4OService db4oService;
    	
    private ObservableList<Parada> paradasTabla = FXCollections.observableArrayList();
    	
    private List<Parada> paradasSeleccionadas = new ArrayList<>();
    	
	private List<Character> regiones = Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
			
		
		String url = getClass().getResource("/help/ayuda_peregrino.html").toExternalForm();
		webView.getEngine().load(url);
	
		
		
//		columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
//		columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
//		columnaRegion.setCellValueFactory(new PropertyValueFactory<>("region"));
//		columnaResponsable.setCellValueFactory(new PropertyValueFactory<>("responsable"));
		
		columnaID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
		columnaNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
		columnaRegion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRegion()));
		columnaResponsable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getResponsable()));
		
//		tablaServicio.setItems(paradasTabla);
		
		tablaServicio.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		tablaServicio.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Parada>) change -> {

			paradasSeleccionadas = tablaServicio.getSelectionModel().getSelectedItems();
			
        });
		
		cboxRegion.getItems().addAll(regiones);
	}
	
	public void clickCancelar() {
		
		panelPrincipal.setVisible(true);
		panelRegistrar.setVisible(false);	
		panelAyuda.setVisible(false);
		panelServicio.setVisible(false);	
		
	}
	

	public void clickRegistrar() {
		
		if(Utils.confirmarDatos().getResult().equals(ButtonType.OK)) {
			if(Utils.validarNombre(txtNombre.getText())
				&& usuarioExistente(txtUsuario.getText())
				&& Utils.validarContraseña(ptxtContraseña.getText())
				&& Utils.validarEmail(txtCorreo.getText())
				&& validarRegion()
				) {

				Parada parada = new Parada();
				parada.setNombre(txtNombre.getText());
				parada.setRegion(cboxRegion.getValue());
				parada.setResponsable(txtUsuario.getText());
				
				parada = paradaService.save(parada);
				
				Usuario usuario = new Usuario();
				usuario.setUsuario(txtUsuario.getText());
				usuario.setContraseña(ptxtContraseña.getText());
				usuario.setCorreo(txtCorreo.getText());
				usuario.setRol("Parada");
				usuario.setId(parada.getId());
				
				usuario = usuarioService.save(usuario);
				
				Sesion.getSesion().setId(usuario.getId());
				Sesion.getSesion().setPerfil(usuario.getRol());
				Sesion.getSesion().setUsuario(usuario.getUsuario());
				
				txtNombre.clear();
				txtUsuario.clear();
				ptxtContraseña.clear();
				txtCorreo.clear();
				cboxRegion.valueProperty().set(null);
				
				panelPrincipal.setVisible(true);
				panelRegistrar.setVisible(false);	
				panelAyuda.setVisible(false);
				panelServicio.setVisible(false);	
				
			}
		}
	}
	
	
	
	public void clickCrearServicio() {
		
		Servicio servicio = new Servicio();
		
		if(Utils.validarNombre(txtNombreServicio.getText()) 
			&& Utils.validarPrecio(txtPrecio.getText())) {
			
			
			servicio.setId(db4oService.findServicioLastId());
			servicio.setNombre(txtNombreServicio.getText());
			servicio.setPrecio(Double.valueOf(txtPrecio.getText()));			
			
			for(Parada p : paradasSeleccionadas) {
				servicio.getParadas().add(p.getId());
			}
			
			db4oService.storeServicio(servicio);
		}
		
	}
	
	
	public void clickMenuRegistrar() {
		panelPrincipal.setVisible(false);
		panelRegistrar.setVisible(true);
		panelAyuda.setVisible(false);
		panelServicio.setVisible(false);	
	}
	
	public void clickMenuServicio() {
		
		List<Parada> paradas = paradaService.findAll();
	
		paradasTabla.clear();
		paradasTabla.setAll(paradas);
//		paradasTabla.addAll(paradas); 
		tablaServicio.setItems(paradasTabla);
		tablaServicio.refresh();
		
		panelPrincipal.setVisible(false);
		panelRegistrar.setVisible(false);
		panelAyuda.setVisible(false);
		panelServicio.setVisible(true);	
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
	
	public void clickAyuda() {
		
		panelAyuda.setVisible(true);
		panelPrincipal.setVisible(false);
		panelRegistrar.setVisible(false);
		panelServicio.setVisible(false);	
		
	}
	
	
	
	private boolean validarRegion() {
		if(cboxRegion.getValue() == null) {
			alertaRegionVacia();
			return false;
		}
		else{
			return true;
		}
	}
	
	private void alertaRegionVacia() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Region vacia");
		alerta.setContentText("Escoge region");
		alerta.show();
	}

	private boolean usuarioExistente(String usuario) {
		
		if(usuario.isBlank()) {
			alertaUsuarioVacio();
		}	
		else if(usuarioService.existsBy(usuario)) {
			alertaUsuario();
			return false;
		}
		return true;
	}
	
	private void alertaUsuario() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Usuario existente");
		alerta.setContentText("Ese usuario ya existe");
		alerta.show();
	}
	
	private void alertaUsuarioVacio() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Usuario vacio");
		alerta.setContentText("Introduce un usuario");
		alerta.show();
	}
	
}
