package be.ulb.plnmonitor.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import be.ulb.plnmonitor.datacontroller.UserSession;
import be.ulb.plnmonitor.object.User;
import be.ulb.plnmonitor.rdbms.DatabaseManager;
import be.ulb.plnmonitor.rdbms.TableRow;
import be.ulb.plnmonitor.rdbms.TableRowIterator;

public class UserDAO {
	private UserSession userSession;
	
	private static List<User> userList;
	//private static Logger log = Logger.getLogger(UserDao.class);
	public static final List<String> LISTROLES = new ArrayList<String>();
	public static final String SECRETAIRE = "secretaire";
	public static final String DEMANDEUR = "demandeur";
	public static final String DELEGUE = "delegue";
	public static final String ACQUISITEUR = "acquisiteur";
	public static final String COMMANDITAIRE = "commanditaire"; // plus utilisé
	public static final String SUPER_ADMIN = "admin";
	static{
		LISTROLES.add(SECRETAIRE);
		LISTROLES.add(DEMANDEUR);
		LISTROLES.add(DELEGUE);
		LISTROLES.add(ACQUISITEUR);
		LISTROLES.add(COMMANDITAIRE);
		LISTROLES.add(SUPER_ADMIN);
	}
	
	public UserDAO(UserSession session){
		this.userSession=session;
	}
	
	public User loadUserPerson() throws SQLException{
		//log.debug("load user");
		if(userSession==null || userSession.getUser()==null)throw new SQLException("Session not loaded in UserDao");
		if(userList!=null)return findPerson(userSession.getUser().getMatricule());
		userList = new ArrayList<User>();
		String query = "SELECT * from person where matricule='"+userSession.getUser().getMatricule()+"'"; //where id_person = (select distinct personrole.id_person from personrole)";
		List<User> list = loadPersons(query);
		if(list.isEmpty()) return null;
		if(!userList.contains(list.get(0)))	userList.add(list.get(0));
		return list.get(0);
	}
	
	public User reloadUserPerson() throws SQLException{
		//log.debug("reload user");
		if(userSession==null || userSession.getUser()==null)throw new SQLException("Session not loaded in UserDao");
		String query = "SELECT * from person where matricule='"+userSession.getUser().getMatricule()+"'"; //where id_person = (select distinct personrole.id_person from personrole)";
		List<User> list = loadPersons(query);
		if(list.isEmpty()) return null;
		if(userList==null){
			userList = new ArrayList<User>();
		}
		if(!userList.contains(list.get(0)))
			userList.add(list.get(0));
		else {
			userList.remove(list.get(0));
			userList.add(list.get(0));
		}
		return list.get(0);
	}
	
	public List<User> loadPersons(String query) throws SQLException{
		List<User> list = new ArrayList<User>();
		TableRowIterator tri = null;
		//log.debug("loadPerson begin with query: "+query);
        try{
            tri = DatabaseManager.query(userSession, query);
            while(tri.hasNext()){
                TableRow row = tri.next();
                int id = row.getIntColumn("id_person");
                String firstname = row.getStringColumn("firstname");
                String lastname = row.getStringColumn("lastname");
                String email = row.getStringColumn("email");
                String matricule = row.getStringColumn("matricule");
                User user = new User();
                user.setEmail(email);
                user.setFirstName(firstname);
                user.setLastName(lastname);
                user.setId(id);
                user.setMatricule(matricule);
                TableRowIterator tri_role = null;
				try{
					String query_role = "select * from personrole where id_person = "+id;
					//log.debug("loadPersonrole begin with query: "+query_role);
					tri_role = DatabaseManager.query(userSession, query_role);
					List<String> list_roles = new ArrayList<String>();
					list_roles.add(DEMANDEUR);
					while(tri_role.hasNext()){
						TableRow row_role = tri_role.next();
						int id_role = row_role.getIntColumn("id_role");
						list_roles.add(LISTROLES.get(id_role-1));
					}
					user.setRoles(list_roles);
				}catch(SQLException e){
					userSession.closeDBConnection();
					throw new SQLException("Error to rappatriate delegate in Faculty: "+e);
				}finally {
		            if (tri_role != null){
		            	tri_role.close();
		            }
		        }
				list.add(user);
            }
        }
        finally{
            if (tri != null){
                tri.close();
            }
        }
        //log.debug("loadPerson end");
        return list;
	}
	
	public User findPerson(String matricule) throws SQLException{
		//log.debug("findperson");
		if(userList==null){
			try {
				loadUserPerson();
			} catch (SQLException e) {
				//log.fatal("Error to load Users: "+e);
			}
		}
		for(User u: userList){
			if(u.getMatricule().equalsIgnoreCase(matricule)){
				//log.debug("Person found in userlist "+u.getMatricule());
				return u;
			}
		}
		String query = "SELECT * from person where matricule = '"+matricule+"'"; //where id_person = (select distinct personrole.id_person from personrole)";
		List<User> list = loadPersons(query);
		if(list.isEmpty())return null;
		for(User u: list){
			userList.add(u);
		}
		return list.get(0);
	}

	public User findPerson(int id) throws SQLException {
		if(userList==null){
			try {
				loadUserPerson();
			} catch (SQLException e) {
				//log.fatal("Error to load Users: "+e);
			}
		}
		for(User u: userList){
			if(u.getId()==id)return u;
		}
		String query = "SELECT * from person where id_person ="+id; //where id_person = (select distinct personrole.id_person from personrole)";
		List<User> list = loadPersons(query);
		if(list.isEmpty())return null;
		for(User u: list){
			userList.add(u);
		}
		return list.get(0);
	}
	
	public static User findPersonInUserList(String matricule){
		if(matricule==null) return null;
		if(userList==null)userList = new ArrayList<User>();
		for(User u: userList){
			if(u.getMatricule().equalsIgnoreCase(matricule))return u;
		}
		return getUserByMatricule(matricule);
	}
	
	private static User getUserByMatricule(String matricule) {
		String query = "SELECT * from person where matricule='"+matricule+"'";
		User user = null;
		Connection db = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try{
			db = DatabaseManager.getConnection();
			statement = db.prepareStatement(query);
			rs = statement.executeQuery();
			if(rs.next()){
				int id = rs.getInt("id_person");
				String firstname = rs.getString("firstname");
				String lastname = rs.getString("lastname");
				String email = rs.getString("email");
				user = new User();
				user.setEmail(email);
				user.setFirstName(firstname);
				user.setLastName(lastname);
				user.setId(id);
				user.setMatricule(matricule);
			}
		} catch (SQLException e) {
		//	log.error(e);
		}
		finally{
			try {
				rs.close();
				statement.close();
				if(db!=null){
			    	DatabaseManager.freeConnection(db);
			    	db = null;
		    	}
			} catch (SQLException e) {
			//	log.error(e);
			}
		}
		if(userList==null)userList = new ArrayList<User>();
		if(user == null) return null;
		if(!userList.contains(user)) userList.add(user);
		return user;
	}
	
	public UserSession getMySession(){
		return this.userSession;
	}
}
