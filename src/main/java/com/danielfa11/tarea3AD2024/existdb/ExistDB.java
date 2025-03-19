package com.danielfa11.tarea3AD2024.existdb;

import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Component;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

@Component
public class ExistDB {

	private static ExistDB existdb;
	
	private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
	private static Database database;
	private static Collection paradas;
	
	 
	private ExistDB() {
		 
	}
	 
	public static ExistDB getExistdb() {
		 if(existdb == null) {
			 existdb = new ExistDB();
				Class<?> cl;
				try {
					cl = Class.forName("org.exist.xmldb.DatabaseImpl");
					database = (Database) cl.getDeclaredConstructor().newInstance();
			        database.setProperty("create-database", "true");
			        DatabaseManager.registerDatabase(database);
		        	paradas = DatabaseManager.getCollection(existdb.getURI() + "/db/paradas");
		            if(paradas == null) {
	                        
                        Collection parent = DatabaseManager.getCollection(existdb.getURI() + "/db", "admin", "admin");
                        CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
                        paradas = mgt.createCollection("paradas");
                        paradas.close();
                        parent.close();
                        
                    }
			                   
			        
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XMLDBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		 }
		 return existdb;
	}
	 
	public String getURI() {
		return URI;
	}
	
	public Database getDatabase() {
		return database;
	}
	
	 
}
