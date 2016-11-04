package be.ulb.plnmonitor.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import be.ulb.plnmonitor.datacontroller.UserSession;
import be.ulb.plnmonitor.object.Institution;
import be.ulb.plnmonitor.object.LOCKSSBox;
import be.ulb.plnmonitor.object.Peer;
import be.ulb.plnmonitor.object.Person;
import be.ulb.plnmonitor.object.User;
import be.ulb.plnmonitor.rdbms.DatabaseManager;
import be.ulb.plnmonitor.rdbms.TableRow;
import be.ulb.plnmonitor.rdbms.TableRowIterator;

public class PeerDAO  implements Serializable {
	
	private long boxId;
	private UserSession userSession;
	
	public PeerDAO(UserSession userSession) {
		this.userSession = userSession;
	}
	
	public List<Peer> loadPeerBoxes(LOCKSSBox curLockssBox) throws SQLException{
		List<Peer> peerList = new ArrayList<Peer>();
		TableRowIterator tri = null;
		//String query = "SELECT * from plnmonitor.lockss_box, plnmonitor.lockss_box_data_current, plnmonitor.lockss_box_info where pln='" + String.valueOf(this.plnId)+"'"; //where id_person = (select distinct personrole.id_person from personrole)";
		String query = "SELECT * FROM plnmonitor.peer WHERE box="+curLockssBox.getBoxId(); 
		//log.debug("loadPerson begin with query: "+query);
        try{
            tri = DatabaseManager.query(userSession, query);
            while(tri.hasNext()){
                TableRow row = tri.next();
                long peerId = row.getLongColumn("id");
                long lastPoll = row.getLongColumn("last_poll");
                long pollsCalled= row.getLongColumn("polls_called"); 
                long lastInvitation = row.getLongColumn("last_invitation");
                long lastVote = row.getLongColumn("last_vote");
                String peerLockssId = row.getStringColumn("peer_lockss_id");
                long lastMessage = row.getLongColumn("last_message");
                long invitationCount = row.getLongColumn("invitation_count");
                long messageCount = row.getLongColumn("message_count");
                String messageType = row.getStringColumn("message_type");
    			long pollsRejected = row.getLongColumn("polls_rejected");
    			long votesCast = row.getLongColumn("votes_cast");
                
                Peer curPeer = new Peer(peerId, lastPoll, pollsCalled, lastInvitation, lastVote, curLockssBox,
                		peerLockssId, lastMessage, invitationCount, messageCount, messageType, pollsRejected, votesCast);

                peerList.add(curPeer);
            }
        }
        catch (Exception e) {
        	UserSession.getLog().error(e.getMessage());
        }
        finally{
        	if (tri != null){
        		tri.close();
        	}
        }
        return peerList;
	}
}
