//package be.ulb.plnmonitor.dao;
//
//import java.io.Serializable;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import be.ulb.plnmonitor.datacontroller.UserSession;
//import be.ulb.plnmonitor.object.Institution;
//import be.ulb.plnmonitor.object.LOCKSSBox;
//import be.ulb.plnmonitor.object.Person;
//import be.ulb.plnmonitor.rdbms.DatabaseManager;
//import be.ulb.plnmonitor.rdbms.TableRow;
//import be.ulb.plnmonitor.rdbms.TableRowIterator;
//
//public class PersonDAO  implements Serializable{
//
//	private long personId;
//	private UserSession userSession;
//	
//	public PersonDAO(UserSession userSession, long personId) {
//		this.userSession = userSession;
//		this.personId = personId;
//	}
//	
//	public Person getPerson() {
//		
//		TableRowIterator tri = null;
//		String query = "SELECT * from plnmonitor.person where id=" + personId; //where id_person = (select distinct personrole.id_person from personrole)";
//		Person curPerson = null;
//		//log.debug("loadPerson begin with query: "+query);
//        try{
//            tri = DatabaseManager.query(userSession, query);
//            while(tri.hasNext()){
//                TableRow row = tri.next();
//                long institutionId = row.getLongColumn("institution");
//                String name = row.getStringColumn("name");
//                String firstName = row.getStringColumn("first_name");
//                String emailAddress = row.getStringColumn("email_address");
//                
//                InstitutionDAO institutionDao = new InstitutionDAO(userSession, institutionId);
//                Institution institution = institutionDao.getInstitution();
//                curPerson = new Person(name, firstName, emailAddress, institution);         
//            }
//        }
//        catch (Exception e) {
//        		UserSession.getLog().error(e.getMessage());
//        }
//        finally{
//        	if (tri != null){
//        		tri.close();
//        	}
//        }
//        return curPerson;
//	}
//}
