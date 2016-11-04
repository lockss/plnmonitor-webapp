//package be.ulb.plnmonitor.dao;
//
//import java.io.Serializable;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.eclipse.jetty.util.log.Log;
//
//import be.ulb.plnmonitor.datacontroller.UserSession;
//import be.ulb.plnmonitor.object.AU;
//import be.ulb.plnmonitor.object.LOCKSSBox;
//import be.ulb.plnmonitor.object.LOCKSSBoxData;
//import be.ulb.plnmonitor.object.Peer;
//import be.ulb.plnmonitor.object.Person;
//import be.ulb.plnmonitor.object.User;
//import be.ulb.plnmonitor.rdbms.DatabaseManager;
//import be.ulb.plnmonitor.rdbms.TableRow;
//import be.ulb.plnmonitor.rdbms.TableRowIterator;
//
//public class LOCKSSBoxDAO  implements Serializable {
//
//	private UserSession userSession;
//	private long plnId;
//
//	public LOCKSSBoxDAO(UserSession session, long plnId){
//		this.userSession=session;
//		this.plnId = plnId;
//	}
//
//
//
//	public List<LOCKSSBox> loadLOCKSSBoxes() throws SQLException{
//		List<LOCKSSBox> list = new ArrayList<LOCKSSBox>();
//		TableRowIterator tri = null;
//		//String query = "SELECT * from plnmonitor.lockss_box, plnmonitor.lockss_box_data_current, plnmonitor.lockss_box_info where pln='" + String.valueOf(this.plnId)+"'"; //where id_person = (select distinct personrole.id_person from personrole)";
//		String query = "SELECT * FROM plnmonitor.lockss_box AS box " +
//				"INNER JOIN plnmonitor.lockss_box_info AS info " + 
//				"ON box.id=info.box ";
//		//log.debug("loadPerson begin with query: "+query);
//		try{
//			tri = DatabaseManager.query(userSession, query);
//			while(tri.hasNext()){
//				TableRow row = tri.next();
//				long plnId = row.getIntColumn("pln");
//				long boxId = row.getIntColumn("id");
//				String ipAddress = row.getStringColumn("ipaddress");
//				String uiPort = row.getStringColumn("uiport");
//				String hostname = ipAddress;
//				String username = row.getStringColumn("username");
//				String password = row.getStringColumn("password");
//				String urlDSS = row.getStringColumn("urldss");
//				double latitude = row.getDoubleColumn("latitude");
//				double longitude = row.getDoubleColumn("longitude");
//				String country =  row.getStringColumn("country");
//				String physicalAddress = row.getStringColumn("physical_address");
//				long orgManID = row.getIntColumn("organizational_manager");
//				long techManID = row.getIntColumn("technical_manager");
//				String name = row.getStringColumn("name");
//
//				PersonDAO orgManDao = new PersonDAO(userSession, orgManID);
//				PersonDAO techManDao = new PersonDAO(userSession, techManID);
//
//				Person orgMan = orgManDao.getPerson();
//				Person techMan= techManDao.getPerson(); 
//				PeerDAO peerDao = new PeerDAO(userSession);
//				AUDAO auDao = new AUDAO(userSession);
//
//
//				LOCKSSBox curLOCKSSBox = new LOCKSSBox(plnId, hostname, username, password, urlDSS, ipAddress, uiPort,
//						boxId, latitude, longitude, country, physicalAddress, orgMan, techMan, name);
//
//
//				List<Peer> peers  = peerDao.loadPeerBoxes(curLOCKSSBox);
//				curLOCKSSBox.setPeers(peers);
//
//				List<AU> AUs  = auDao.loadAUs(curLOCKSSBox);
//				curLOCKSSBox.setAUs(AUs);
//
//
//				// load  data for current LOCKSS box
//
//				List<LOCKSSBoxData> lockssBoxData = new ArrayList<LOCKSSBoxData>();
//
//				String dataQuery = "SELECT * FROM plnmonitor.lockss_box_data_current WHERE box= " + curLOCKSSBox.getBoxId();
//				TableRowIterator tridata = null;
//				try{
//					tridata = DatabaseManager.query(userSession, dataQuery);
//					while(tridata.hasNext()){
//						TableRow datarow = tridata.next();
//
//						long used = (long) datarow.getLongColumn("used");
//						long free = (long) datarow.getLongColumn("free");
//						long size = (long) datarow.getLongColumn("size");
//						double percentage = datarow.getDoubleColumn("percentage");
//						long activeAUs = datarow.getIntColumn("active_aus");
//						long inactiveAUs = datarow.getIntColumn("inactive_aus");
//						long deletedAUs = datarow.getIntColumn("deleted_aus");
//						long orphanedAUs = datarow.getIntColumn("orphaned_aus");
//						String repositorySpace = datarow.getStringColumn("repository_space_lockss_id");
//						Date updatedAt = datarow.getDateColumn("updated_at");
//
//						lockssBoxData.add(new LOCKSSBoxData(used, free, size, activeAUs,  deletedAUs,  inactiveAUs,
//								orphanedAUs,  percentage,  repositorySpace,  updatedAt));
//					}
//				}
//				catch(SQLException e){
//					UserSession.getLog().error(e.getMessage());
//					userSession.closeDBConnection();
//					throw new SQLException("Error while getting LOCKSS box data information from database: "+e);
//				}
//				finally{
//					if (tridata != null){
//						tridata.close();
//					}
//				}
//				curLOCKSSBox.setLockssBoxData(lockssBoxData);
//
//				list.add(curLOCKSSBox);
//
//			}
//		}
//		catch(SQLException e){
//			UserSession.getLog().error(e.getMessage());
//			userSession.closeDBConnection();
//			throw new SQLException("Error while getting LOCKSS box information from database: "+e);
//		}
//		finally{
//			if (tri != null){
//				tri.close();
//			}
//		}
//		return list;
//	}
//
//
//}	
//
//
