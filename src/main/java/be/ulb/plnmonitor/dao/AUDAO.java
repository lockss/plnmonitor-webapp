//package be.ulb.plnmonitor.dao;
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
//
//import be.ulb.plnmonitor.datacontroller.UserSession;
//import be.ulb.plnmonitor.object.AU;
//import be.ulb.plnmonitor.object.LOCKSSBox;
//import be.ulb.plnmonitor.object.Peer;
//import be.ulb.plnmonitor.object.User;
//import be.ulb.plnmonitor.rdbms.DatabaseManager;
//import be.ulb.plnmonitor.rdbms.TableRow;
//import be.ulb.plnmonitor.rdbms.TableRowIterator;
//
//public class AUDAO  implements Serializable{
//private UserSession userSession;
//	
//	public AUDAO(UserSession userSession) {
//		this.userSession = userSession;
//	}
//
//
//	public List<AU> loadAUs(LOCKSSBox curLockssBox) throws SQLException{
//		List<AU> auList = new ArrayList<AU>();
//		TableRowIterator tri = null;
//
//		String query = "SELECT * FROM plnmonitor.au_current WHERE box="+curLockssBox.getBoxId(); 
//				
//		try{
//            tri = DatabaseManager.query(userSession, query);
//            while(tri.hasNext()){
//                TableRow row = tri.next();
//                
//    			long auId = row.getLongColumn("id"); // AU id from database
//    			String name = row.getStringColumn("name");
//    			String pluginName = row.getStringColumn("plugin_name");
//    			String tdbYear = row.getStringColumn("tdb_year");
//    			String accessType = row.getStringColumn("access_type");
//    			long contentSize = row.getLongColumn("content_size");
//    			double recentPollAgreement = row.getDoubleColumn("recent_poll_agreement");
//    			long creationTime = row.getLongColumn("creation_time");
//    			long version = row.getLongColumn("version");
//    			Date createdAt = row.getDateColumn("created_at");
//    			Date updatedAt = row.getDateColumn("updated_at");
//    			
//    			String au_LOCKSS_Id = row.getStringColumn("au_lockss_id");
//    			String tdbPublisher = row.getStringColumn("tdb_publisher");
//    			String volume = row.getStringColumn("volume");
//    			long diskUsage = row.getLongColumn("disk_usage");
//    			long lastCompletedCrawl = row.getLongColumn("last_completed_crawl");
//    			long lastCompletedPoll = row.getLongColumn("last_completed_poll");;
//    			long lastCrawl = row.getLongColumn("last_crawl");;
//    			long lastPoll = row.getLongColumn("last_poll");
//    			
//    			String crawlPool = row.getStringColumn("crawl_pool");
//    			String crawlProxy = row.getStringColumn("crawl_proxy");
//    			String crawlWindow = row.getStringColumn("crawl_window");
//    			
//    			String lastCrawlResult = row.getStringColumn("last_crawl_result");
//    			String lastPollResult = row.getStringColumn("last_poll_result");
//    			String publishingPlatform = row.getStringColumn("publishing_platform");
//    			String repositoryPath = row.getStringColumn("repository_path");
//    			
//    			String subscriptionStatus = row.getStringColumn("subscription_status");
//    			String substanceState = row.getStringColumn("substance_state");
//    			
//    			boolean availableFromPublisher = row.getBooleanColumn("available_from_publisher");
//    			
//                AU curAU = new AU(auId, name, pluginName, tdbYear, accessType, contentSize,
//            			 recentPollAgreement,  creationTime,  version,  createdAt,  updatedAt,
//            			 au_LOCKSS_Id,  tdbPublisher,  volume,  diskUsage,  lastCompletedCrawl,
//            			 lastCompletedPoll,  lastCrawl,  lastPoll,  crawlPool,  crawlProxy,
//            			 crawlWindow,  lastCrawlResult,  lastPollResult,  publishingPlatform,
//            			 repositoryPath,  subscriptionStatus,  substanceState, availableFromPublisher,
//            			 curLockssBox);
//                auList.add(curAU);
//            }
//        }
//        catch (Exception e) {
//        	UserSession.getLog().error(e.getMessage());
//            
//        }
//        finally{
//        	if (tri != null){
//        		tri.close();
//        	}
//        }
//        return auList;
//	}
//
//}
//
