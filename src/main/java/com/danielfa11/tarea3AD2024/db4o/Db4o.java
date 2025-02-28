package com.danielfa11.tarea3AD2024.db4o;

import org.springframework.stereotype.Component;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;


@Component
public class Db4o {

	private static Db4o instance = null;
	
	private final static String DB4OFILENAME = "./src/main/resources/db4o/db4o.db4o";
	private static ObjectContainer db = null;
	
	private Db4o() {
		
	}
	
	public static Db4o getDb4o() {
		if(instance==null) {
			instance=new Db4o();
			return instance;
		}
		return instance;
	}
	
	public ObjectContainer getDb() {
		if(db==null || db.ext().isClosed()) {
			db=Db4oEmbedded.openFile(Db4oEmbedded
					.newConfiguration(), DB4OFILENAME);
			return db;
		}
		return db;
	}
	
	public void closeDb() {
		db.close();
	}

	
}
