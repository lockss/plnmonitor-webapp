package org.lockss.plnmonitor.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.lockss.plnmonitor.NewAuthenticatedWebSession;
import org.lockss.plnmonitor.object.AU;
import org.lockss.plnmonitor.object.Institution;
import org.lockss.plnmonitor.object.LOCKSSBox;
import org.lockss.plnmonitor.object.LOCKSSBoxData;
import org.lockss.plnmonitor.object.PLN;
import org.lockss.plnmonitor.object.Peer;
import org.lockss.plnmonitor.object.Person;
import org.lockss.plnmonitor.rdbms.DatabaseManager;
import org.lockss.plnmonitor.rdbms.TableRow;
import org.lockss.plnmonitor.rdbms.TableRowIterator;

// TODO: Auto-generated Javadoc
/**
 * The Class PLNDAO.
 * 
 * Main Data Access Object updating values of all other LOCKSS objects by making queries to the database
 * 
 */
public class PLNDAO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1817288939011123360L;
	
	/** The user session. */
	private NewAuthenticatedWebSession userSession;
	
	/** The pln list. */
	private List<PLN> plnList;

	/**
	 * Instantiates a new plndao.
	 *
	 * @param userSession the user session
	 */
	public PLNDAO(NewAuthenticatedWebSession userSession) {
		this.userSession = userSession;
	}

	/**
	 * Gets the user session.
	 *
	 * @return the user session
	 */
	public NewAuthenticatedWebSession getUserSession() {
		return userSession;
	}

	/**
	 * Sets the new authenticated web session.
	 *
	 * @param userSession the new new authenticated web session
	 */
	public void setNewAuthenticatedWebSession(NewAuthenticatedWebSession userSession) {
		this.userSession = userSession;
	}


	/**
	 * Gets the pln list.
	 *
	 * @return the pln list
	 */
	public List<PLN> getPlnList() {
		return plnList;
	}

	/**
	 * Sets the pln list.
	 *
	 * @param plnList the new pln list
	 */
	public void setPlnList(List<PLN> plnList) {
		this.plnList = plnList;
	}

	/**
	 * Gets a list of all PLNs for which info is available in the database.
	 *
	 * @return a list of all available PLNs 
	 * @throws SQLException the SQL exception
	 */
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

	/**
	 * Gets status info from all boxes in the PLN plnID.
	 *
	 * @param plnID the pln ID
	 * @return a list of LOCKSSBox objects with updated status
	 * @throws SQLException the SQL exception
	 */
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
					NewAuthenticatedWebSession.getLog().error(e.getMessage());
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return list;
	}

	/**
	 * Gets status info about all AUs in box boxId in the PLN plnID.
	 *
	 * @param plnID the pln ID
	 * @param boxId the box id
	 * @return a list of AU objects with updated status
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return auList;
	}

	/**
	 * Gets status info about one specific AU identified by its name AUName in a box boxID in pln plnID.
	 *
	 * @param plnID the pln ID
	 * @param boxID the box ID
	 * @param AUName the AU name
	 * @return an object AU with updated status info
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return auCurrent;
	}

	/**
	 * Gets status info about an AU identified by its name AUName across all boxes in the PLN plnID.
	 *
	 * @param plnID the pln ID
	 * @param AUName the AU name
	 * @return a list of AU objects corresponding to the name AUName with updated status info
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return auList;
	}

	/**
	 * Gets all existing AU Names in the pln plnID
	 *
	 * @param plnID the pln ID
	 * @return a list of Strings with all AU Names
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return auList;
	}

	/**
	 * Gets the number of AUs existing in the PLN plnID.
	 *
	 * @param plnID the pln ID
	 * @return an Integer, the number of AUs in the PLN plnID
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return auList.size();
	}

	/**
	 * Gets the sum of sizes of all the AUs preserved in the PLN plnID.
	 *
	 * @param plnID the pln ID
	 * @return a long integer corresponding to the sum of all AU sizes
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
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

	/**
	 * Gets the poll agreement value for a specific AU identified by its name in a box boxId
	 *
	 * @param boxId the box id
	 * @param Name the name of the AU
	 * @return a double value corresponding to the agreement
	 * @throws SQLException the SQL exception
	 */
	public double AUAgreementInBox(long boxId, String Name) throws SQLException{
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());

		} finally {
			if (tri != null) {
				tri.close();
			}
		}
//		if (poll_agreement == 0) {
//			return "noinfo";
//		}
//		if (poll_agreement < 0.5) {
//			return "danger";
//		}
//		if (poll_agreement < 0.9) {
//			return "warning";
//		}
//		else {
//			return "success";
//		}
		return poll_agreement;

	}

	/**
	 * Gets all status info from a specific box identified by its name (name of the institution) .
	 *
	 * @param nameID the box name ID (name of the institution)
	 * @return a LOCKSSbox object with updated status info
	 * @throws SQLException the SQL exception
	 */
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
					NewAuthenticatedWebSession.getLog().error(e.getMessage());
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return boxInfo;
	}

	/**
	 * Gets a list of all boxes names in the PLN.
	 *
	 * @return a list of strings with all boxes names
	 * @throws SQLException the SQL exception
	 */
	public List<String> getBoxesNames() throws SQLException {
		List<String> boxesName = new ArrayList<String>();
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return boxesName;
	}

	/**
	 * Gets the name of a box for a given box Id.
	 *
	 * @param boxId the box id
	 * @return a string with the box name
	 * @throws SQLException the SQL exception
	 */
	public String getBoxName(long boxId) throws SQLException {
		String boxName = null;
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return boxName;
	}

	
	/**
	 * Gets the total number of boxes in the PLN.
	 *
	 * @return an integer, the number of boxes in the PLN
	 * @throws SQLException the SQL exception
	 */
	public int getNbBoxes() throws SQLException {
		List<String> boxesIP = new ArrayList<String>();
		
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return boxesIP.size();
	}

	/**
	 * Gets a person info based on her id.
	 *
	 * @param personId the person id
	 * @return a person object with updated info
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return person;
	}

	/**
	 * Gets a list of all institutions participating in the PLN.
	 *
	 * @return a list of Institution objects with updated info
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return institutions;
	}

	/**
	 * Gets a list of persons from a given institution identified by institutionId.
	 *
	 * @param institutionId the institution id
	 * @return a list of Person object from the given institution
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return personsFromInstitution;
	}

	/**
	 * Gets the time of the last completed poll in the network.
	 *
	 * @return the last completed poll
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return lastCompletedPoll;
	}

	/**
	 * Gets the min available capacity of boxes in the network.
	 *
	 * @return the min available capacity 
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return (minCapacity *1024); // return number of 1k-blocks -> x 1024 bytes
	}

	/**
	 * Gets the time since the last update of the status info in the database.
	 *
	 * @return the last update (Unix Epoch time in ms)
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return lastUpdate;
	}

	/**
	 * Gets the total size of the AUs for each institution.
	 *
	 * @return a list of total AU size per institution
	 * @throws SQLException the SQL exception
	 */
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
			NewAuthenticatedWebSession.getLog().error(e.getMessage());

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

	/**
	 * Gets the names of the AU publishers.
	 *
	 * @return a list of string with the names of the publishers
	 * @throws SQLException the SQL exception
	 */
	public List<String> getAUPublisherNames() throws SQLException  {
		List<String> auPublisherNames = new ArrayList<String>();
		// TODO Auto-generated method stub
		TableRowIterator tri = null;
		String query = "SELECT tdb_publisher FROM plnmonitor.au_current"; 
		// log.debug("loadPerson begin with query: "+query);
		try {
			tri = DatabaseManager.query(userSession, query);
			while (tri.hasNext()) {
				TableRow row = tri.next();
				String publisherName = row.getStringColumn("tdb_publisher");
				if (!auPublisherNames.contains(publisherName) ){
					if (publisherName != null && !publisherName.isEmpty()) {
						auPublisherNames.add(publisherName);
					}
				}
			}
		} catch (SQLException e) {
			NewAuthenticatedWebSession.getLog().error(e.getMessage());
			userSession.closeDBConnection();
			throw new SQLException("Error while getting LOCKSS box information from database: " + e);
		} finally {
			if (tri != null) {
				tri.close();
			}
		}
		return auPublisherNames;
	}

	/**
	 * Gets all Aus for a given Publisher publisherName in the network plnID
	 *
	 * @param plnID the pln ID
	 * @param publisherName the name of the publisher
	 * @return a list of AUs published by publisherName
	 * @throws SQLException the SQL exception
	 */
	public List<AU> getAUsFromPublisher(int plnID, String publisherName) throws SQLException {
		
			List<AU> auList = new ArrayList<AU>();
			TableRowIterator tri = null;

			// TODO: query based on plnID
			String query = "SELECT * FROM plnmonitor.au_current WHERE plnmonitor.au_current.tdb_publisher='" + publisherName + "'";

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
				NewAuthenticatedWebSession.getLog().error(e.getMessage());

			} finally {
				if (tri != null) {
					tri.close();
				}
			}
			return auList;
		}

}
