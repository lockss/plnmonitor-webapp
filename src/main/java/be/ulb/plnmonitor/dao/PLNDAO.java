package be.ulb.plnmonitor.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import be.ulb.plnmonitor.datacontroller.UserSession;
import be.ulb.plnmonitor.object.AU;
import be.ulb.plnmonitor.object.Institution;
import be.ulb.plnmonitor.object.LOCKSSBox;
import be.ulb.plnmonitor.object.LOCKSSBoxData;
import be.ulb.plnmonitor.object.PLN;
import be.ulb.plnmonitor.object.Peer;
import be.ulb.plnmonitor.object.Person;
import be.ulb.plnmonitor.object.User;
import be.ulb.plnmonitor.rdbms.DatabaseManager;
import be.ulb.plnmonitor.rdbms.TableRow;
import be.ulb.plnmonitor.rdbms.TableRowIterator;

public class PLNDAO implements Serializable {

	private UserSession userSession;
	private List<PLN> plnList;

	public PLNDAO(UserSession userSession) {
		this.userSession = userSession;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	// public void loadPLN() throws SQLException{
	// String query = "SELECT * from plnmonitor.pln";
	//
	// plnList = new ArrayList<PLN>();
	// TableRowIterator tri = null;
	// //log.debug("loadPerson begin with query: "+query);
	// try{
	// tri = DatabaseManager.query(userSession, query);
	// while(tri.hasNext()){
	// TableRow row = tri.next();
	// long plnId = row.getIntColumn("id");
	// String name = row.getStringColumn("name");
	// String configUrl = row.getStringColumn("config_url");
	//
	// LOCKSSBoxDAO lockssBoxDAO= new LOCKSSBoxDAO(userSession, plnId);
	// List<LOCKSSBox> lockssBoxes = lockssBoxDAO.loadLOCKSSBoxes();
	// plnList.add(new PLN(name, plnId, configUrl, lockssBoxes));
	// }
	// }
	// finally{
	// if (tri != null){
	// tri.close();
	// }
	// }
	// }

	public List<PLN> getPlnList() {
		return plnList;
	}

	public void setPlnList(List<PLN> plnList) {
		this.plnList = plnList;
	}

	public List<PLN> getPLNs() throws SQLException {
		String query = "SELECT * from plnmonitor.pln";

		List<PLN> plnList = new ArrayList<PLN>();
		TableRowIterator tri = null;
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				long plnId = row.getIntColumn("id");
				String name = row.getStringColumn("name");
				String configUrl = row.getStringColumn("config_url");

				plnList.add(new PLN(name, plnId, configUrl, null));
			}
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return (plnList);
	}

	public List<LOCKSSBox> getAllBoxesInfo(int plnID) throws SQLException {
		List<LOCKSSBox> list = new ArrayList<LOCKSSBox>();
		TableRowIterator tri = null;
		// String query = "SELECT * from plnmonitor.lockss_box,
		// plnmonitor.lockss_box_data_current, plnmonitor.lockss_box_info where
		// pln='" + String.valueOf(this.plnId)+"'"; //where id_person = (select
		// distinct personrole.id_person from personrole)";
		// TODO: check plnID
		String query = "SELECT *, box.id as boxId FROM plnmonitor.lockss_box AS box " + "INNER JOIN plnmonitor.lockss_box_info AS info "
				+ "ON box.id=info.box"; // WHERE pln=" + plnID;
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				long plnId = row.getIntColumn("pln");
				long boxId = row.getIntColumn("boxId");
				String ipAddress = row.getStringColumn("ipaddress");
				String uiPort = row.getStringColumn("uiport");
				String hostname = ipAddress;
				String userName = row.getStringColumn("username");
				String password = row.getStringColumn("password");
				String urlDSS = row.getStringColumn("urldss");
				
				String javaVersion = row.getStringColumn("java_version");
				String daemonFullVersion = row.getStringColumn("daemon_full_version");
				String platform = row.getStringColumn("platform");
				long uptime = row.getLongColumn("uptime");
				String groups = row.getStringColumn("groups");
				String disks = row.getStringColumn("disks");
				String adminEmail = row.getStringColumn("admin_email");
				
				double latitude = row.getDoubleColumn("latitude");
				double longitude = row.getDoubleColumn("longitude");
				String country = row.getStringColumn("country");
				String physicalAddress = row.getStringColumn("physical_address");
				long orgManID = row.getIntColumn("organizational_manager");
				long techManID = row.getIntColumn("technical_manager");
				String name = row.getStringColumn("name");

				//PeerDAO peerDao = new PeerDAO(userSession);
				// AUDAO auDao = new AUDAO(userSession);

				LOCKSSBox curLOCKSSBox = new LOCKSSBox(plnId, hostname, userName, password, urlDSS, ipAddress,
						 uiPort, javaVersion, daemonFullVersion, platform, uptime, groups,
						 disks, boxId, latitude, longitude, country, physicalAddress,
						 name, adminEmail, techManID, orgManID); 
				// List<Peer> peers = peerDao.loadPeerBoxes(curLOCKSSBox);
				// curLOCKSSBox.setPeers(peers);

				// List<AU> AUs = auDao.loadAUs(curLOCKSSBox);
				// curLOCKSSBox.setAUs(AUs);

				// load data for current LOCKSS box

				List<LOCKSSBoxData> lockssBoxData = new ArrayList<LOCKSSBoxData>();

				String dataQuery = "SELECT * FROM plnmonitor.lockss_box_data_current WHERE box= "
						+ curLOCKSSBox.getBoxId();
				TableRowIterator tridata = null;
				try {
					tridata = DatabaseManager.query(userSession, dataQuery);
					while (tridata.hasNext()) {
						TableRow datarow = tridata.next();

						long used = (long) datarow.getLongColumn("used");
						long free = (long) datarow.getLongColumn("free");
						long size = (long) datarow.getLongColumn("size");
						double percentage = datarow.getDoubleColumn("percentage");
						long activeAUs = datarow.getIntColumn("active_aus");
						long inactiveAUs = datarow.getIntColumn("inactive_aus");
						long deletedAUs = datarow.getIntColumn("deleted_aus");
						long orphanedAUs = datarow.getIntColumn("orphaned_aus");
						String repositorySpace = datarow.getStringColumn("repository_space_lockss_id");
						Date updatedAt = datarow.getDateColumn("updated_at");

						lockssBoxData.add(new LOCKSSBoxData(used, free, size, activeAUs, deletedAUs, inactiveAUs,
								orphanedAUs, percentage, repositorySpace, updatedAt));
					}
				} catch (SQLException e) {
					UserSession.getLog().error(e.getMessage());
					userSession.closeDBConnection();
					throw new SQLException("Error while getting LOCKSS box data information from database: " + e);
				} finally {
					if (tridata != null) {
						tridata.close();
					}
				}
				curLOCKSSBox.setLockssBoxData(lockssBoxData);

				list.add(curLOCKSSBox);

			}
		} catch (SQLException e) {
			UserSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return list;
	}

	public List<AU> getAllAUsinBox(int plnID, long boxId) throws SQLException {
		List<AU> auList = new ArrayList<AU>();
		TableRowIterator tri = null;

		// TODO: select AUs based on PLN ID
		String query = "SELECT * FROM plnmonitor.au_current WHERE box=" + boxId;

		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();

				long auId = row.getLongColumn("id"); // AU id from database
				String name = row.getStringColumn("name");
				String pluginName = row.getStringColumn("plugin_name");
				String tdbYear = row.getStringColumn("tdb_year");
				String accessType = row.getStringColumn("access_type");
				long contentSize = row.getLongColumn("content_size");
				double recentPollAgreement = row.getDoubleColumn("recent_poll_agreement");
				long creationTime = row.getLongColumn("creation_time");
				long version = row.getLongColumn("version");
				Date createdAt = row.getDateColumn("created_at");
				Date updatedAt = row.getDateColumn("updated_at");

				String au_LOCKSS_Id = row.getStringColumn("au_lockss_id");
				String tdbPublisher = row.getStringColumn("tdb_publisher");
				String volume = row.getStringColumn("volume");
				long diskUsage = row.getLongColumn("disk_usage");
				long lastCompletedCrawl = row.getLongColumn("last_completed_crawl");
				long lastCompletedPoll = row.getLongColumn("last_completed_poll");
				;
				long lastCrawl = row.getLongColumn("last_crawl");
				long lastPoll = row.getLongColumn("last_poll");

				String crawlPool = row.getStringColumn("crawl_pool");
				String crawlProxy = row.getStringColumn("crawl_proxy");
				String crawlWindow = row.getStringColumn("crawl_window");

				String lastCrawlResult = row.getStringColumn("last_crawl_result");
				String lastPollResult = row.getStringColumn("last_poll_result");
				String publishingPlatform = row.getStringColumn("publishing_platform");
				String repositoryPath = row.getStringColumn("repository_path");

				String subscriptionStatus = row.getStringColumn("subscription_status");
				String substanceState = row.getStringColumn("substance_state");

				boolean availableFromPublisher = row.getBooleanColumn("available_from_publisher");

				AU curAU = new AU(auId, name, pluginName, tdbYear, accessType, contentSize, recentPollAgreement,
						creationTime, version, createdAt, updatedAt, au_LOCKSS_Id, tdbPublisher, volume, diskUsage,
						lastCompletedCrawl, lastCompletedPoll, lastCrawl, lastPoll, crawlPool, crawlProxy, crawlWindow,
						lastCrawlResult, lastPollResult, publishingPlatform, repositoryPath, subscriptionStatus,
						substanceState, availableFromPublisher, boxId);
				auList.add(curAU);
			}
		} catch (Exception e) {
			UserSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return auList;
	}

	public AU getAUinBox(int plnID, String boxID, String AUName) throws SQLException {
		AU auCurrent = new AU();
		TableRowIterator tri = null;

		// TODO: select AUs based on PLN ID
		String query = "SELECT * FROM plnmonitor.au_current WHERE box IN (SELECT box FROM plnmonitor.lockss_box_info WHERE name='"
				+ boxID + "') AND name='" + AUName + "'";
		// System.out.println(query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();

				long auId = row.getLongColumn("id"); // AU id from database
				String name = row.getStringColumn("name");
				String pluginName = row.getStringColumn("plugin_name");
				String tdbYear = row.getStringColumn("tdb_year");
				String accessType = row.getStringColumn("access_type");
				long contentSize = row.getLongColumn("content_size");
				double recentPollAgreement = row.getDoubleColumn("recent_poll_agreement");
				long creationTime = row.getLongColumn("creation_time");
				long version = row.getLongColumn("version");
				Date createdAt = row.getDateColumn("created_at");
				Date updatedAt = row.getDateColumn("updated_at");

				String au_LOCKSS_Id = row.getStringColumn("au_lockss_id");
				String tdbPublisher = row.getStringColumn("tdb_publisher");
				String volume = row.getStringColumn("volume");
				long diskUsage = row.getLongColumn("disk_usage");
				long lastCompletedCrawl = row.getLongColumn("last_completed_crawl");
				long lastCompletedPoll = row.getLongColumn("last_completed_poll");
				;
				long lastCrawl = row.getLongColumn("last_crawl");
				long lastPoll = row.getLongColumn("last_poll");

				long boxId = row.getLongColumn("box");

				String crawlPool = row.getStringColumn("crawl_pool");
				String crawlProxy = row.getStringColumn("crawl_proxy");
				String crawlWindow = row.getStringColumn("crawl_window");

				String lastCrawlResult = row.getStringColumn("last_crawl_result");
				String lastPollResult = row.getStringColumn("last_poll_result");
				String publishingPlatform = row.getStringColumn("publishing_platform");
				String repositoryPath = row.getStringColumn("repository_path");

				String subscriptionStatus = row.getStringColumn("subscription_status");
				String substanceState = row.getStringColumn("substance_state");

				boolean availableFromPublisher = row.getBooleanColumn("available_from_publisher");

				auCurrent = new AU(auId, name, pluginName, tdbYear, accessType, contentSize, recentPollAgreement,
						creationTime, version, createdAt, updatedAt, au_LOCKSS_Id, tdbPublisher, volume, diskUsage,
						lastCompletedCrawl, lastCompletedPoll, lastCrawl, lastPoll, crawlPool, crawlProxy, crawlWindow,
						lastCrawlResult, lastPollResult, publishingPlatform, repositoryPath, subscriptionStatus,
						substanceState, availableFromPublisher, boxId);
			}
		} catch (Exception e) {
			UserSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return auCurrent;
	}

	public List<AU> getAUsAcrossBoxes(int plnID, String AUName) throws SQLException {
		List<AU> auList = new ArrayList<AU>();
		TableRowIterator tri = null;

		// TODO: query based on plnID
		String query = "SELECT * FROM plnmonitor.au_current WHERE plnmonitor.au_current.name='" + AUName + "'";

		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();

				long auId = row.getLongColumn("id"); // AU id from database
				String name = row.getStringColumn("name");
				String pluginName = row.getStringColumn("plugin_name");
				String tdbYear = row.getStringColumn("tdb_year");
				String accessType = row.getStringColumn("access_type");
				long contentSize = row.getLongColumn("content_size");
				double recentPollAgreement = row.getDoubleColumn("recent_poll_agreement");
				long creationTime = row.getLongColumn("creation_time");
				long version = row.getLongColumn("version");
				Date createdAt = row.getDateColumn("created_at");
				Date updatedAt = row.getDateColumn("updated_at");

				String au_LOCKSS_Id = row.getStringColumn("au_lockss_id");
				String tdbPublisher = row.getStringColumn("tdb_publisher");
				String volume = row.getStringColumn("volume");
				long diskUsage = row.getLongColumn("disk_usage");
				long lastCompletedCrawl = row.getLongColumn("last_completed_crawl");
				long lastCompletedPoll = row.getLongColumn("last_completed_poll");
				;
				long lastCrawl = row.getLongColumn("last_crawl");
				;
				long lastPoll = row.getLongColumn("last_poll");

				long boxId = row.getLongColumn("box");

				String crawlPool = row.getStringColumn("crawl_pool");
				String crawlProxy = row.getStringColumn("crawl_proxy");
				String crawlWindow = row.getStringColumn("crawl_window");

				String lastCrawlResult = row.getStringColumn("last_crawl_result");
				String lastPollResult = row.getStringColumn("last_poll_result");
				String publishingPlatform = row.getStringColumn("publishing_platform");
				String repositoryPath = row.getStringColumn("repository_path");

				String subscriptionStatus = row.getStringColumn("subscription_status");
				String substanceState = row.getStringColumn("substance_state");

				boolean availableFromPublisher = row.getBooleanColumn("available_from_publisher");

				AU curAU = new AU(auId, name, pluginName, tdbYear, accessType, contentSize, recentPollAgreement,
						creationTime, version, createdAt, updatedAt, au_LOCKSS_Id, tdbPublisher, volume, diskUsage,
						lastCompletedCrawl, lastCompletedPoll, lastCrawl, lastPoll, crawlPool, crawlProxy, crawlWindow,
						lastCrawlResult, lastPollResult, publishingPlatform, repositoryPath, subscriptionStatus,
						substanceState, availableFromPublisher, boxId);
				auList.add(curAU);
			}
		} catch (Exception e) {
			UserSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return auList;
	}

	public List<String> getAllAUsInPLN(int plnID) throws SQLException {
		List<String> auList = new ArrayList<String>();
		TableRowIterator tri = null;

		// TODO: query based on plnID
		String query = "SELECT name FROM plnmonitor.au_current"; // WHERE
																	// pln="+plnID;

		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				String name = row.getStringColumn("name");
				if (!auList.contains(name)) {
					auList.add(name);
				}
			}
		} catch (Exception e) {
			UserSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return auList;
	}

	public int getNbOfAUsInPLN(int plnID) throws SQLException {
		List<String> auList = new ArrayList<String>();
		TableRowIterator tri = null;

		// TODO: query based on plnID
		String query = "SELECT name FROM plnmonitor.au_current"; // WHERE
																	// pln="+plnID;

		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				String name = row.getStringColumn("name");
				if (!auList.contains(name)) {
					auList.add(name);
				}
			}
		} catch (Exception e) {
			UserSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return auList.size();
	}

	public long getTotalSize(int plnID) throws SQLException {
		long totalSize = 0;
		HashMap<String, Long> sizeList = new HashMap<String, Long>();
		TableRowIterator tri = null;

		// TODO: query based on plnID
		String query = "SELECT name, content_size FROM plnmonitor.au_current"; // WHERE
		// pln="+plnID;

		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				String name = row.getStringColumn("name");
				long size = row.getLongColumn("content_size");

				if (!sizeList.containsKey(name) || sizeList.get(name) < size) {
					sizeList.put(name, new Long(size));
				}

			}
		} catch (Exception e) {
			UserSession.getLog().error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		for (Long size : sizeList.values()) {
			totalSize += size.longValue();
		}

		return totalSize;
	}

	public String isAUInBox(long boxId, String Name) throws SQLException{
		TableRowIterator tri = null;
		Double poll_agreement = 0.0;
		// TODO: query based on plnID
		String query = "SELECT recent_poll_agreement FROM plnmonitor.au_current WHERE box=" + boxId + " and name LIKE '"
				+ Name.replaceAll("'", "''") + "'"; // WHERE pln="+plnID; // twice single quote to escape single quotes in name resulting in sql error
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				poll_agreement = row.getDoubleColumn("recent_poll_agreement");
			}
		} catch (Exception e) {
			UserSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		if (poll_agreement == 0) {
			return "noinfo";
		}
		if (poll_agreement < 0.5) {
			return "danger";
		}
		if (poll_agreement < 0.9) {
			return "warning";
		}
		else {
			return "success";
		}

	}

	public LOCKSSBox getBoxInfo(String nameID) throws SQLException {
		LOCKSSBox boxInfo = null;
		TableRowIterator tri = null;
		// String query = "SELECT * from plnmonitor.lockss_box,
		// plnmonitor.lockss_box_data_current, plnmonitor.lockss_box_info where
		// pln='" + String.valueOf(this.plnId)+"'"; //where id_person = (select
		// distinct personrole.id_person from personrole)";
		// TODO: check plnID
		String query = "SELECT *, box.id as boxId FROM plnmonitor.lockss_box AS box " + "INNER JOIN plnmonitor.lockss_box_info AS info "
				+ "ON box.id=info.box WHERE name='" + nameID + "'"; // WHERE
																	// pln=" +
																	// plnID;
		// System.out.println(query);
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {


				TableRow row = tri.next();
				long plnId = row.getIntColumn("pln");
				long boxId = row.getIntColumn("boxId");
				String ipAddress = row.getStringColumn("ipaddress");
				String uiPort = row.getStringColumn("uiport");
				String hostname = ipAddress;
				String userName = row.getStringColumn("username");
				String password = row.getStringColumn("password");
				String urlDSS = row.getStringColumn("urldss");
				
				String javaVersion = row.getStringColumn("java_version");
				String daemonFullVersion = row.getStringColumn("daemon_full_version");
				String platform = row.getStringColumn("platform");
				long uptime = row.getLongColumn("uptime");
				String groups = row.getStringColumn("groups");
				String disks = row.getStringColumn("disks");
				String adminEmail = row.getStringColumn("admin_email");
				
				double latitude = row.getDoubleColumn("latitude");
				double longitude = row.getDoubleColumn("longitude");
				String country = row.getStringColumn("country");
				String physicalAddress = row.getStringColumn("physical_address");
				long orgManID = row.getIntColumn("organizational_manager");
				long techManID = row.getIntColumn("technical_manager");
				String name = row.getStringColumn("name");

				
				// PersonDAO orgManDao = new PersonDAO(userSession, orgManID);
				// PersonDAO techManDao = new PersonDAO(userSession, techManID);

				// Person orgMan = orgManDao.getPerson();
				// Person techMan= techManDao.getPerson();
				PeerDAO peerDao = new PeerDAO(userSession);
				// AUDAO auDao = new AUDAO(userSession);

				
				boxInfo = new LOCKSSBox(plnId, hostname, userName, password, urlDSS, ipAddress,
						 uiPort, javaVersion, daemonFullVersion, platform, uptime, groups,
						 disks, boxId, latitude, longitude, country, physicalAddress,
						 name, adminEmail, techManID, orgManID); 
				
				List<Peer> peers = peerDao.loadPeerBoxes(boxInfo);
				boxInfo.setPeers(peers);

				// List<AU> AUs = auDao.loadAUs(boxInfo);
				// boxInfo.setAUs(AUs);

				// load data for current LOCKSS box

				List<LOCKSSBoxData> lockssBoxData = new ArrayList<LOCKSSBoxData>();

				String dataQuery = "SELECT * FROM plnmonitor.lockss_box_data_current WHERE box= " + boxInfo.getBoxId();
				TableRowIterator tridata = null;
				try {
					tridata = DatabaseManager.query(userSession, dataQuery);
					while (tridata.hasNext()) {
						TableRow datarow = tridata.next();

						long used = (long) datarow.getLongColumn("used");
						long free = (long) datarow.getLongColumn("free");
						long size = (long) datarow.getLongColumn("size");
						double percentage = datarow.getDoubleColumn("percentage");
						long activeAUs = datarow.getIntColumn("active_aus");
						long inactiveAUs = datarow.getIntColumn("inactive_aus");
						long deletedAUs = datarow.getIntColumn("deleted_aus");
						long orphanedAUs = datarow.getIntColumn("orphaned_aus");
						String repositorySpace = datarow.getStringColumn("repository_space_lockss_id");
						Date updatedAt = datarow.getDateColumn("updated_at");
						if (updatedAt == null) {
							updatedAt = new Date();
						}
						lockssBoxData.add(new LOCKSSBoxData(used, free, size, activeAUs, deletedAUs, inactiveAUs,
								orphanedAUs, percentage, repositorySpace, updatedAt));
					}
				} catch (SQLException e) {
					UserSession.getLog().error(e.getMessage());
					userSession.closeDBConnection();
					throw new SQLException("Error while getting LOCKSS box data information from database: " + e);
				} finally {
					if (tridata != null) {
						tridata.close();
					}
				}
				boxInfo.setLockssBoxData(lockssBoxData);

			}
		} catch (SQLException e) {
			UserSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return boxInfo;
	}

	public List<String> getBoxesNames() throws SQLException {
		List<String> boxesName = new ArrayList<String>();
		// TODO Auto-generated method stub
		TableRowIterator tri = null;
		String query = "SELECT name FROM plnmonitor.lockss_box AS box "
				+ "INNER JOIN plnmonitor.lockss_box_info AS info " + "ON box.id=info.box"; // WHERE
																							// pln="
																							// +
																							// plnID;
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				String boxName = row.getStringColumn("name");
				boxesName.add(boxName);
			}
		} catch (SQLException e) {
			UserSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return boxesName;
	}

	public String getBoxName(long boxId) throws SQLException {
		String boxName = null;
		// TODO Auto-generated method stub
		TableRowIterator tri = null;
		String query = "SELECT name  FROM plnmonitor.lockss_box AS box "
				+ "INNER JOIN plnmonitor.lockss_box_info AS info " + "ON box.id=info.box WHERE box.id=" +boxId; // WHERE
																							// pln="
																							// +
																							// plnID;
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				boxName = row.getStringColumn("name");
			}
		} catch (SQLException e) {
			UserSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return boxName;
	}

	
	public int getNbBoxes() throws SQLException {
		List<String> boxesIP = new ArrayList<String>();
		// TODO Auto-generated method stub
		TableRowIterator tri = null;
		String query = "SELECT ipaddress FROM plnmonitor.lockss_box";
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				String ipaddress = row.getStringColumn("ipaddress");
				boxesIP.add(ipaddress);
			}
		} catch (SQLException e) {
			UserSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return boxesIP.size();
	}

	public Person getPerson(long personId) throws SQLException {
		Person person = null;
		// TODO Auto-generated method stub
		TableRowIterator tri = null;

		// TODO: check plnID
		String query = "SELECT *, " + " CASE WHEN EXISTS (SELECT id FROM plnmonitor.lockss_box_info "
				+ " WHERE plnmonitor.lockss_box_info.technical_manager =  plnmonitor.person.id)"
				+ " THEN TRUE ELSE FALSE END AS techMan,"
				+ " CASE WHEN EXISTS (SELECT id FROM plnmonitor.lockss_box_info "
				+ " WHERE plnmonitor.lockss_box_info.organizational_manager =  plnmonitor.person.id)"
				+ " THEN TRUE ELSE FALSE END AS orgMan " + " FROM plnmonitor.person WHERE id =" + personId;

		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				String name = row.getStringColumn("name");
				String firstName = row.getStringColumn("first_name");
				String emailAddress = row.getStringColumn("email_address");
				String phone = row.getStringColumn("phone");
				long institutionId = row.getLongColumn("institution");
				boolean techAdmin = row.getBooleanColumn("techMan");
				boolean orgAdmin = row.getBooleanColumn("orgMan");
				person = new Person(name, firstName, phone, emailAddress, institutionId, techAdmin, orgAdmin);
			}
		} catch (SQLException e) {
			UserSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return person;
	}

	public List<Institution> getAllInstitutions() throws SQLException {
		List<Institution> institutions = new ArrayList<Institution>();
		// TODO Auto-generated method stub
		TableRowIterator tri = null;
		// String query = "SELECT * from plnmonitor.lockss_box,
		// plnmonitor.lockss_box_data_current, plnmonitor.lockss_box_info where
		// pln='" + String.valueOf(this.plnId)+"'"; //where id_person = (select
		// distinct personrole.id_person from personrole)";
		// TODO: check plnID
		String query = "SELECT * FROM plnmonitor.institution"; // WHERE
																// pln="
																// +
																// plnID;
		// System.out.println(query);
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				String name = row.getStringColumn("name");
				String address = row.getStringColumn("address");
				String shortName = row.getStringColumn("shortname");
				long institutionId = row.getLongColumn("id");
				institutions.add(new Institution(institutionId, name, address, shortName));
			}
		} catch (SQLException e) {
			UserSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return institutions;
	}

	public List<Person> getPersonsFromInstitution(long institutionId) throws SQLException {
		List<Person> personsFromInstitution = new ArrayList<Person>();
		// TODO Auto-generated method stub
		TableRowIterator tri = null;
		// String query = "SELECT * from plnmonitor.lockss_box,
		// plnmonitor.lockss_box_data_current, plnmonitor.lockss_box_info where
		// pln='" + String.valueOf(this.plnId)+"'"; //where id_person = (select
		// distinct personrole.id_person from personrole)";
		// TODO: check plnID
		// String query = "SELECT * FROM plnmonitor.person WHERE institution=" +
		// institutionId; // WHERE
		// pln="
		// +
		// plnID;
		String query = "SELECT *, " + " CASE WHEN EXISTS (SELECT id FROM plnmonitor.lockss_box_info "
				+ " WHERE plnmonitor.lockss_box_info.technical_manager =  plnmonitor.person.id)"
				+ " THEN TRUE ELSE FALSE END AS techMan,"
				+ " CASE WHEN EXISTS (SELECT id FROM plnmonitor.lockss_box_info "
				+ " WHERE plnmonitor.lockss_box_info.organizational_manager =  plnmonitor.person.id)"
				+ " THEN TRUE ELSE FALSE END AS orgMan " + " FROM plnmonitor.person WHERE institution ="
				+ institutionId;
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				String name = row.getStringColumn("name");
				String firstName = row.getStringColumn("first_name");
				String emailAddress = row.getStringColumn("email_address");
				String phone = row.getStringColumn("phone");
				boolean techAdmin = row.getBooleanColumn("techMan");
				boolean orgAdmin = row.getBooleanColumn("orgMan");

				personsFromInstitution
						.add(new Person(name, firstName, phone, emailAddress, institutionId, techAdmin, orgAdmin));
			}
		} catch (SQLException e) {
			UserSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return personsFromInstitution;
	}

	public long getLastCompletedPoll() throws SQLException {
		long lastCompletedPoll = 0;
		// TODO Auto-generated method stub
		TableRowIterator tri = null;
		String query = "select last_completed_poll from plnmonitor.au_current";
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				long curPoll = row.getLongColumn("last_completed_poll");
				if (curPoll > lastCompletedPoll) {
					lastCompletedPoll = curPoll;
				}
			}
		} catch (SQLException e) {
			UserSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return lastCompletedPoll;
	}

	public long getminAvailableCapacity() throws SQLException {
		long minCapacity = Long.MAX_VALUE;
		// TODO Auto-generated method stub
		TableRowIterator tri = null;
		String query = "select free from plnmonitor.lockss_box_data_current";
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				long curCapa = row.getLongColumn("free");
				if (curCapa < minCapacity) {
					minCapacity = curCapa;
				}
			}
		} catch (SQLException e) {
			UserSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return (minCapacity *1024); // return number of 1k-blocks -> x 1024 bytes
	}

	public long getLastUpdate() throws SQLException {
		long lastUpdate = 0;
		// TODO Auto-generated method stub
		TableRowIterator tri = null;
		String query = "select updated_at from plnmonitor.lockss_box_data_current";
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				Date currentDate = row.getDateColumn("updated_at");
				long curUpdate = 0;
				if (currentDate != null) {
					curUpdate = currentDate.getTime();
				}

				if (curUpdate > lastUpdate) {
					lastUpdate = curUpdate;
				}
			}
		} catch (SQLException e) {
			UserSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return lastUpdate;
	}

	public HashMap<String, Long> getAUSizePerInstitution() throws SQLException {
		HashMap<String, Long> AUSizePerAU = new HashMap<String, Long>();
		HashMap<String, Long> AUSizePerInstitution = new HashMap<String, Long>();
		HashMap<String, String> AUCorrespondingInstitution = new HashMap<String, String>();
		TableRowIterator tri = null;

		// TODO: query based on plnID
		String query = "SELECT tdb_publisher, name, disk_usage FROM plnmonitor.au_current"; // WHERE
		// pln="+plnID;

		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				String publisher = row.getStringColumn("tdb_publisher");
				String name = row.getStringColumn("name");
				Long diskUsage = row.getLongColumn("disk_usage");
				if (!AUSizePerAU.containsKey(name) || (AUSizePerAU.get(name) < diskUsage)) {
					AUSizePerAU.put(name, diskUsage);
					AUCorrespondingInstitution.put(name, publisher);
				}
			}
		} catch (Exception e) {
			UserSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		
		
		
		for (String au : AUSizePerAU.keySet()) {
			String curInstitution = AUCorrespondingInstitution.get(au);
			if (AUSizePerInstitution.containsKey(curInstitution)) {
				Long newSize = AUSizePerInstitution.get(curInstitution) + AUSizePerAU.get(au);
				AUSizePerInstitution.put(curInstitution, newSize);
			} else {
				AUSizePerInstitution.put(curInstitution, AUSizePerAU.get(au));
				}

		}

		
		return AUSizePerInstitution;
	}

}
