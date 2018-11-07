package org.safepln.plnmonitor.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.safepln.plnmonitor.NewAuthenticatedWebSession;
import org.safepln.plnmonitor.object.PLN;
import org.safepln.plnmonitor.object.User;
import org.safepln.plnmonitor.rdbms.DatabaseManager;
import org.safepln.plnmonitor.rdbms.TableRow;
import org.safepln.plnmonitor.rdbms.TableRowIterator;

public class UserDAO {
	private NewAuthenticatedWebSession userSession;
	
	private static User currentUser;
	public static final List<String> LISTROLES = new ArrayList<String>();
	public static final String ADMIN = "admin";
	static{
		LISTROLES.add(ADMIN);
	}
	
	public UserDAO(NewAuthenticatedWebSession session){
		this.userSession=session;
	}
	
	public User getUserData(String username) throws SQLException{
		
		//if(userSession==null || userSession.getUser()==null)throw new SQLException("Session not loaded in UserDao");

		String query = "SELECT * from plnmonitor.user where name=\'"+username+"\'";

		List<User> userList = new ArrayList<User>();
		TableRowIterator tri = null;
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				long plnId = row.getIntColumn("id");
				String name = row.getStringColumn("name");
				String passwordHash = row.getStringColumn("passwordHash");
				String role = row.getStringColumn("role");

				currentUser=new User(username, passwordHash, role);
			}
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return (currentUser);//log.debug("load user");
		
	}

	
	public NewAuthenticatedWebSession getMySession(){
		return this.userSession;
	}
	

}
