package com.danielfa11.tarea3AD2024.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.danielfa11.tarea3AD2024.config.StageManager;
import com.danielfa11.tarea3AD2024.modelo.Parada;
import com.danielfa11.tarea3AD2024.modelo.Sesion;
import com.danielfa11.tarea3AD2024.modelo.Usuario;
import com.danielfa11.tarea3AD2024.services.ParadaService;
import com.danielfa11.tarea3AD2024.services.UsuarioService;
import com.danielfa11.tarea3AD2024.utils.Utils;
import com.danielfa11.tarea3AD2024.view.FxmlView;

import javafx.application.Platform;
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
import javafx.scene.control.TextField;
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
	
	@Lazy
    @Autowired
    private StageManager stageManager;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ParadaService paradaService;
	
	private List<Character> regiones = Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		
		String url = getClass().getResource("/help/ayuda_peregrino.html").toExternalForm();
		webView.getEngine().load(url);
	
	 	cboxRegion.getItems().addAll(regiones);
	}
	
	public void clickCancelar() {
		
		panelPrincipal.setVisible(true);
		panelRegistrar.setVisible(false);	
		panelAyuda.setVisible(false);
		
	}
	
//	@Transactional
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
				
			}
		}
	}
	
	
	
	public void clickMenuRegistrar() {
		panelPrincipal.setVisible(false);
		panelRegistrar.setVisible(true);
		panelAyuda.setVisible(false);
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
