package com.danielfa11.tarea3AD2024.db4o;

import org.springframework.stereotype.Component;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;


@Component
public class Db4o {

	private final String DB4OFILENAME = "/db4o/db4o.db4o";
	private ObjectContainer db;
	
	
	public ObjectContainer getDb4o() {
		if(db==null) {
			db=Db4oEmbedded.openFile(Db4oEmbedded
					.newConfiguration(), DB4OFILENAME);
			return db;
		}
		return db;
	}
	
	public void closeDb4o() {
		db.close();
	}

	
}
