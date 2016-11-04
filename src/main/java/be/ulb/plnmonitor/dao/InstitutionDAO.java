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
//public class InstitutionDAO  implements Serializable {
//	private UserSession userSession;
//	private long institutionId;
//	
//	public InstitutionDAO(UserSession userSession, long institutionId) {
//		this.userSession=userSession;
//		this.institutionId = institutionId;
//	}
//	
//	public Institution getInstitution() {
//		
//		TableRowIterator tri = null;
//		String query = "SELECT * from plnmonitor.\"Institutions\" where institution_ID=" + institutionId; //where id_person = (select distinct personrole.id_person from personrole)";
//		Institution curInstitution = null;
//		//log.debug("loadPerson begin with query: "+query);
//        try{
//            tri = DatabaseManager.query(userSession, query);
//            while(tri.hasNext()){
//                TableRow row = tri.next();
//                long institutionId = row.getIntColumn("institution_ID");
//                String name = row.getStringColumn("name");
//                String address = row.getStringColumn("address");
//                curInstitution = new Institution(name, address);         
//            }
//        }
//        catch (Exception e){
//        	
//        }
//        finally{
//        	if (tri != null){
//        		tri.close();
//        	}
//        }
//        //log.debug("loadPerson end");
//        return curInstitution;
//	}
//}
