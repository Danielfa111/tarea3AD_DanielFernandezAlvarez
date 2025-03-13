package com.danielfa11.tarea3AD2024.controller;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import com.danielfa11.tarea3AD2024.config.StageManager;
import com.danielfa11.tarea3AD2024.modelo.Carnet;
import com.danielfa11.tarea3AD2024.modelo.Parada;
import com.danielfa11.tarea3AD2024.modelo.Peregrino;
import com.danielfa11.tarea3AD2024.modelo.Sesion;
import com.danielfa11.tarea3AD2024.modelo.Usuario;
import com.danielfa11.tarea3AD2024.services.EXISTDBService;
import com.danielfa11.tarea3AD2024.services.ParadaService;
import com.danielfa11.tarea3AD2024.services.PeregrinoService;
import com.danielfa11.tarea3AD2024.services.UsuarioService;
import com.danielfa11.tarea3AD2024.utils.Utils;
import com.danielfa11.tarea3AD2024.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Controller
public class RegistroController implements Initializable{

	
	@FXML
	private TextField txtNombre;
	
	@FXML
	private TextField txtUsuario;
	
	@FXML
	private PasswordField ptxtContraseña;
	
	@FXML
	private PasswordField ptxtConfirmar;
	
	@FXML
	private TextField txtCorreo;
	
	@FXML
	private ComboBox<String> cboxNacionalidad;
	
	@FXML
	private ComboBox<Parada> cboxParada;
	
	@FXML
	private Button btnCancelar;
	
	@FXML
	private Button btnRegistrar;

	@Lazy
    @Autowired
    private StageManager stageManager;
	
	@Autowired
	private ParadaService paradaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EXISTDBService existdbService;
	
	@Autowired
	private PeregrinoService peregrinoService;
	
	private ObservableList<String> paises; 
	
	private ObservableList<Parada> paradas;
	
	@SuppressWarnings("unused")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		paises = FXCollections.observableArrayList(Utils.getDiccionarioPaises().keySet());
		
		paradas =  FXCollections.observableArrayList(paradaService.findAll());
		
		cboxNacionalidad.setItems(paises);
		
		cboxNacionalidad.setCellFactory(listView -> new ListCell<String>() {
			@Override
            protected void updateItem(String pais, boolean empty) {
                super.updateItem(pais, empty);
                if (empty || pais == null) {
                    setText(null);
                } else {
                    setText(pais+" : "+Utils.getDiccionarioPaises().get(pais));
                }
            }
        });
		
		cboxParada.setItems(paradas);
		
		cboxParada.setCellFactory(listView -> new ListCell<Parada>() {
			@Override
            protected void updateItem(Parada parada, boolean empty) {
                super.updateItem(parada, empty);
                if (empty || parada == null) {
                    setText(null);
                } else {
                    setText(parada.getNombre());
                }
            }
        });
		
	}
	
	public void clickRegistrar() {
		
		if(Utils.confirmarDatos().getResult().equals(ButtonType.OK)) {
			if(Utils.validarNombre(txtNombre.getText())
				&& usuarioExistente(txtUsuario.getText())
				&& Utils.validarContraseña(ptxtContraseña.getText())
				&& contraseñasIguales()
				&& Utils.validarEmail(txtCorreo.getText())
				&& validarNacionalidad()
				) {

				Parada parada = paradaService.find(cboxParada.getValue().getId());
				
				Carnet carnet = new Carnet();
				carnet.setFechaexp(LocalDate.now());
				
				Peregrino peregrino = new Peregrino();
				peregrino.setNombre(txtNombre.getText());
				peregrino.setNacionalidad(Utils.getDiccionarioPaises().get(cboxNacionalidad.getValue()));
				peregrino.setCarnet(carnet);
				peregrino.getParadas().add(parada);
				peregrino = peregrinoService.save(peregrino);
				
				parada.getPeregrinos().add(peregrino);
				parada = paradaService.save(parada);						
				
				Usuario usuario = new Usuario();
				usuario.setUsuario(txtUsuario.getText());
				usuario.setContraseña(ptxtContraseña.getText());
				usuario.setCorreo(txtCorreo.getText());
				usuario.setRol("Peregrino");
				if(peregrinoService.findTopByOrderByIdDesc()==null) {
					usuario.setId(1L);
					carnet.setId(1L);
					Sesion.getSesion().setId(1L);
				}
				else {
					usuario.setId(peregrinoService.findTopByOrderByIdDesc().getId());
					carnet.setId(peregrinoService.findTopByOrderByIdDesc().getId());
					Sesion.getSesion().setId(peregrinoService.findTopByOrderByIdDesc().getId());
				}
				
				usuario = usuarioService.save(usuario);				
				
				Sesion.getSesion().setPerfil(usuario.getRol());
				Sesion.getSesion().setUsuario(usuario.getUsuario());

				carnet.setParadaInicial(parada);
				carnet.setPropietario(peregrino);
				
				clickExportar(carnet);
				
				stageManager.switchScene(FxmlView.PEREGRINO);
			}
		}
	}
	
public void clickExportar(Carnet carnetPeregrino) {
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Atención");
        alert.setContentText("Su carnet sera exportado en la carpeta Carnets dentro de Resources del proyecto actual");
        alert.showAndWait();
        
        if (alert.getResult().equals(ButtonType.OK)) 
        {

			DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	        try {
	            // Creando: Fábrica de Constructores de Documento, Constructor de Documento e Implementación DOM
	            DocumentBuilderFactory fabricaConstructorDocumento = DocumentBuilderFactory.newInstance();
	            DocumentBuilder constructorDocumento = fabricaConstructorDocumento.newDocumentBuilder();
	            DOMImplementation implementacion = constructorDocumento.getDOMImplementation();
	
	            // Creando el documento XML y el elemento documento
	            Document documento = implementacion.createDocument(null, "carnet", null);
	            Element carnet = documento.getDocumentElement();
	
	            // Modificando y creando instrucciones de procesamiento
	            documento.setXmlVersion("1.0");
	            ProcessingInstruction ip = documento.createProcessingInstruction("xml-stylesheet", "type=\"text/xml\" href=\"test.xsl\"");
	            documento.insertBefore(ip, carnet);
	
	            Element id, fechaexp, expedidoen, peregrino, nacionalidad, hoy, distanciatotal, paradas, parada, orden, nombre, region, estancias, estancia, fecha, vip;
	            Text idValor, fechaexpValor, expedidoenValor, nombreValor, nacionalidadValor, hoyValor, distanciatotalValor, ordenValor, regionValor, fechaValor, paradaValor;
	
	            // Creando un elemento id
	            id = documento.createElement("id");
	            carnet.appendChild(id);
	            idValor = documento.createTextNode(String.valueOf(carnetPeregrino.getId()));
	            id.appendChild(idValor);
	            
	            // Creando un elemento fechaexp
	            fechaexp = documento.createElement("fechaexp");
	            carnet.appendChild(fechaexp);
	            fechaexpValor = documento.createTextNode(String.valueOf(carnetPeregrino.getFechaexp().format(formato)));
	            fechaexp.appendChild(fechaexpValor);
	            
	            // Creando un elemento expedidoen
	            expedidoen = documento.createElement("expedidoen");
	            carnet.appendChild(expedidoen);
	            expedidoenValor = documento.createTextNode("parada "+String.valueOf(carnetPeregrino.getParadaInicial().getNombre()));
	            expedidoen.appendChild(expedidoenValor);
	            
	            // Creando un elemento peregrino
	            peregrino = documento.createElement("peregrino");
	            carnet.appendChild(peregrino);
	            
	                // Creando un elemento nombre dentro de peregrino
	                nombre = documento.createElement("nombre");
	                peregrino.appendChild(nombre);
	                nombreValor = documento.createTextNode(String.valueOf(carnetPeregrino.getPropietario().getNombre()));
	                nombre.appendChild(nombreValor);
	                
	                // Creando un elemento nacionalidad dentro de peregrino
	                nacionalidad = documento.createElement("nacionalidad");
	                peregrino.appendChild(nacionalidad);
	                nacionalidadValor = documento.createTextNode(String.valueOf(carnetPeregrino.getPropietario().getNacionalidad()));
	                nacionalidad.appendChild(nacionalidadValor);
	                
	            // Creando un elemento hoy
	            hoy = documento.createElement("hoy");
	            carnet.appendChild(hoy);
	            hoyValor = documento.createTextNode(String.valueOf(LocalDate.now().format(formato)));
	            hoy.appendChild(hoyValor);
	            
	            // Creando un elemento distanciatotal
	            distanciatotal = documento.createElement("distanciatotal");
	            carnet.appendChild(distanciatotal);
	            distanciatotalValor = documento.createTextNode(String.format("%.1f",carnetPeregrino.getDistancia()));
	            distanciatotal.appendChild(distanciatotalValor);
	            
	            // Creando un elemento paradas
	            paradas = documento.createElement("paradas");
	            carnet.appendChild(paradas);
	            
	                // Creando los elementos parada                              
	                for(int i=0;i<carnetPeregrino.getPropietario().getParadas().size(); i++)
	                {
	                    
	                    // Creando elemento parada dentro de paradas
	                    parada = documento.createElement("parada");
	                    paradas.appendChild(parada);
	                        // Creando elemento orden dentro de parada
	                        orden = documento.createElement("orden");
	                        parada.appendChild(orden);
	                        ordenValor = documento.createTextNode(String.valueOf(i+1));
	                        orden.appendChild(ordenValor);
	                        // Creando elemento nombre dentro de parada
	                        nombre = documento.createElement("nombre");
	                        parada.appendChild(nombre);
	                        nombreValor = documento.createTextNode(carnetPeregrino.getPropietario().getParadas().get(i).getNombre());
	                        nombre.appendChild(nombreValor);
	                        // Creando elemento region dentro de parada
	                        region = documento.createElement("region");
	                        parada.appendChild(region);
	                        regionValor = documento.createTextNode(String.valueOf(carnetPeregrino.getPropietario().getParadas().get(i).getRegion()));
	                        region.appendChild(regionValor);
	                }
	            
	            // Creando un elemento estancias
	            estancias = documento.createElement("estancias");           
	            carnet.appendChild(estancias);
	            
	                // Creando los elemento estancia
	                for(int i=0;i<carnetPeregrino.getPropietario().getEstancias().size(); i++)
	                {
	                    
	                    // Creando elemento estancia dentro de estancias
	                    estancia = documento.createElement("estancia");
	                    estancias.appendChild(estancia);
	                        // Creando elemento id dentro de estancia
	                        id = documento.createElement("id");
	                        estancia.appendChild(id);
	                        idValor = documento.createTextNode(String.valueOf(carnetPeregrino.getPropietario().getEstancias().get(i).getId()));
	                        id.appendChild(idValor);
	                        // Creando elemento fecha dentro de estancia
	                        fecha = documento.createElement("fecha");
	                        estancia.appendChild(fecha);
	                        fechaValor = documento.createTextNode(String.valueOf(carnetPeregrino.getPropietario().getEstancias().get(i).getFecha().format(formato)));
	                        fecha.appendChild(fechaValor);
	                        // Creando elemento parada dentro de estancia
	                        parada = documento.createElement("parada");
	                        estancia.appendChild(parada);
	                        paradaValor = documento.createTextNode(String.valueOf(carnetPeregrino.getPropietario().getEstancias().get(i).getParada().getNombre()));
	                        parada.appendChild(paradaValor);
	                        // Creando elemento vip dentro de estancia
	                        if(carnetPeregrino.getPropietario().getEstancias().get(i).isVip())
	                        {
	                            vip = documento.createElement("vip");
	                            estancia.appendChild(vip);
	                        }
	                        
	                }
	             
	                
	            Source fuente = new DOMSource(documento);
	            File fichero = new File("src/main/resources/Carnets/"+carnetPeregrino.getPropietario().getNombre()+"_carnet.xml");	            
	            Result resultado = new StreamResult(fichero);
	            TransformerFactory fabricaTransformador = TransformerFactory.newInstance();
	            Transformer transformador = fabricaTransformador.newTransformer();
	            transformador.transform(fuente, resultado);
	            
	            transformador.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
				transformador.setOutputProperty(OutputKeys.METHOD, "xml");
				transformador.setOutputProperty(OutputKeys.INDENT, "yes");
				transformador.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

				StringWriter escribe = new StringWriter();
				transformador.transform(new DOMSource(documento), new StreamResult(escribe));
	            
	            existdbService.store(escribe.toString(), "/"+carnetPeregrino.getParadaInicial().getNombre(), carnetPeregrino.getPropietario().getNombre());
	            
	        } catch (ParserConfigurationException ex) {
	            System.out.println("Error: " + ex.getMessage());
	        } catch (TransformerConfigurationException ex) {
	            System.out.println("Error: " + ex.getMessage());
	        } catch (TransformerException ex) {
	            System.out.println("Error: " + ex.getMessage());
	        }
		}
	}
	
	
	public void clickCancelar() {
		stageManager.switchScene(FxmlView.LOGIN);
	}
	
	
	
	private boolean contraseñasIguales() {
		if(ptxtContraseña.getText().equals(ptxtConfirmar.getText())) {
			return true;
		}
		else
		{
			alertaContraseñasIguales();
			return false;
		}
		
	}	

	private void alertaContraseñasIguales() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Contraseñas no coinciden");
		alerta.setContentText("Las contraseñas no coinciden");
		alerta.show();
	}
	
	private boolean validarNacionalidad() {
		if(cboxNacionalidad.getValue() == null) {
			alertaNacionalidadVacia();
			return false;
		}
		else{
			return true;
		}
	}
	
	private void alertaNacionalidadVacia() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Nacionalidad vacia");
		alerta.setContentText("Escoge nacionalidad");
		alerta.show();
	}
	
	private boolean usuarioExistente(String usuario) {
		if(usuario.isBlank()) {
			alertaUsuarioVacio();
			return false;
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
