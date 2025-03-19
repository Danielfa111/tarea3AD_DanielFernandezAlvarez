package com.danielfa11.tarea3AD2024.repositorios;

import java.util.ArrayList;
import java.util.List;

import org.exist.xmldb.EXistResource;
import org.exist.xmldb.RemoteBinaryResource;
import org.springframework.stereotype.Repository;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import com.danielfa11.tarea3AD2024.existdb.ExistDB;

@Repository
public class EXISTDBRepository {

	private static ExistDB existdb = ExistDB.getExistdb();
	
	public void store(String fichero, String coleccion, String peregrino) {
		Collection col = null;
        XMLResource res = null;
        try { 
            col = getOrCreateCollection(coleccion);
            
            res = (XMLResource) col.createResource(peregrino, "XMLResource");

            res.setContent(fichero);
            col.storeResource(res);
            
        } catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if(res != null) {
                try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
            
            if(col != null) {
                try { col.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
        }
    }
	    
	public Collection getOrCreateCollection(String collectionUri) throws XMLDBException {
	        
        Collection col = DatabaseManager.getCollection(existdb.getURI()+"/db/paradas" + collectionUri, "admin", "admin");
        if(col == null) {
            if(collectionUri.startsWith("/")) {
                collectionUri = collectionUri.substring(1);
            }

            Collection parent = DatabaseManager.getCollection(existdb.getURI() + "/db/paradas", "admin", "admin");
            CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
            col = mgt.createCollection(collectionUri);

        }
		return col;
	}
	
	public List<String> getAllResourcesFromCollection(String coleccion) {
	    List<String> recursos = new ArrayList<>();
	    Collection col = null;
	    try {
	        col = DatabaseManager.getCollection(existdb.getURI()+"/db/paradas" + coleccion);
	        if (col == null) {
	            System.out.println("La colección no existe: " + coleccion);
	            return recursos;
	        }

	        String[] resourceNames = col.listResources();

	        for (String resourceName : resourceNames) {

	            Resource resource = col.getResource(resourceName);
	            if (resource instanceof RemoteBinaryResource) {

	                RemoteBinaryResource binaryResource = (RemoteBinaryResource) resource;
	                byte[] binaryContent = (byte[]) binaryResource.getContent();

	                String contentAsString = new String(binaryContent);
	                recursos.add(contentAsString);
	                binaryResource.close();
	            } else {
	                System.out.println("El recurso " + resourceName + " es de un tipo no manejado: " + resource.getClass().getName());
	            }
	        }
	        
	    } catch (XMLDBException e) {
	        e.printStackTrace();
	    }
	    return recursos;
	}
	
	
}
