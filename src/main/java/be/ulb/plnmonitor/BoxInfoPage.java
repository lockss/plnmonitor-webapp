package be.ulb.plnmonitor;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.behavior.AttributeAppender;

import be.ulb.plnmonitor.datacontroller.PLNDataController;
import be.ulb.plnmonitor.object.AU;
import be.ulb.plnmonitor.object.LOCKSSBox;
import be.ulb.plnmonitor.object.Peer;
import be.ulb.plnmonitor.object.Person;

public class BoxInfoPage extends BasePage {

	 public BoxInfoPage(final PageParameters parameters, final PLNDataController plnDataController, final String boxName) {
	        super(parameters);      
	        
	        
	        //TODO: replace with get BoxInfo(name)
	        LOCKSSBox box=null;
			try {
				box = plnDataController.getPlnDAO().getBoxInfo(boxName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        add(new Label("boxname",boxName));

			if (box!=null){
	        RepeatingView boxrepeating = new RepeatingView("boxrepeating");
	        add(boxrepeating);
            
	        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
	        
	        AbstractItem item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "IP address"));
	        item.add(new Label("boxvalue", box.getIpAddress()));
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Country"));
	        item.add(new Label("boxvalue", box.getCountry()));
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);	        
	        item.add(new Label("boxitem", "UI Port"));
	        item.add(new Label("boxvalue", box.getUiport()));
	       
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item); 
	        item.add(new Label("boxitem", "Org Manager"));
	        
	        Person orgAdmin = null;
            Person techAdmin = null;
            try {
            	orgAdmin = plnDataController.getPlnDAO().getPerson(box.getOrgAdmin());
            	techAdmin = plnDataController.getPlnDAO().getPerson(box.getTechAdmin());
            }
            catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            }
            
           final long orgAdminId = box.getOrgAdmin();
        	
	       Link<String> orgManlink = new Link<String>("boxvalue") {
                /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
                public void onClick() {
					setResponsePage(new PersonDisplayPage(parameters, plnDataController, orgAdminId ));
                }
            };
            orgManlink.setBody(Model.of(orgAdmin.getFirstName() + " " +orgAdmin.getName()));
            orgManlink.add(new AttributeAppender("style", "cursor: pointer;"));
        	item.add(orgManlink);
        	
            item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item); 
	        item.add(new Label("boxitem", "Tech Manager"));
	        
        	final long techAdminId = box.getTechAdmin();
        	
 	       Link<String> techManlink = new Link<String>("boxvalue") {
                 /**
 				 * 
 				 */
 				private static final long serialVersionUID = 1L;

 				@Override
                 public void onClick() {
 					setResponsePage(new PersonDisplayPage(parameters, plnDataController, techAdminId ));
                 }
             };
             techManlink.setBody(Model.of(techAdmin.getFirstName() + " " +techAdmin.getName()));
             techManlink.add(new AttributeAppender("style", "cursor: pointer;"));
         	item.add(techManlink);
         	
//	        item = new AbstractItem(boxrepeating.newChildId());
//	        boxrepeating.add(item);
//	        item.add(new Label("boxitem", "Tech Manager"));
//	        item.add(new Label("boxvalue", techAdmin.getFirstName() + " " + techAdmin.getName()));

	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Username"));
	        item.add(new Label("boxvalue", box.getUserName()));
	     
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Password"));
	        item.add(new Label("boxvalue", box.getPassword()));
	        
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Active AUs"));
	        if (box.getLockssBoxData()!= null && (box.getLockssBoxData().size()!=0)) {
	        	item.add(new Label("boxvalue", box.getLockssBoxData().get(0).getActiveAUs()));
	        }
	        else {
	         	item.add(new Label("boxvalue","")); 
	        }
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Hostname"));
	        item.add(new Label("boxvalue", box.getHostname()));
	        

	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Admin Email"));
	        item.add(new Label("boxvalue", box.getAdminEmail()));
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Disks"));
	        item.add(new Label("boxvalue", box.getDisks()));
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Groups"));
	        item.add(new Label("boxvalue", box.getGroups()));
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Java Version"));
	        item.add(new Label("boxvalue", box.getJavaVersion()));
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Daemon Version"));
	        item.add(new Label("boxvalue", box.getDaemon_full_version()));
	        
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Platform"));
	        item.add(new Label("boxvalue", box.getPlatform()));
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Uptime (at last status update)"));
	        
	        long uptime=box.getUptime();
	        String uptimeStr = String.format("%03d days %02d:%02d", TimeUnit.MILLISECONDS.toDays(uptime), TimeUnit.MILLISECONDS.toHours(uptime),
	                TimeUnit.MILLISECONDS.toMinutes(uptime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(uptime)),
	                TimeUnit.MILLISECONDS.toSeconds(uptime));
	        item.add(new Label("boxvalue", uptimeStr));
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Physical Address"));
	        item.add(new Label("boxvalue", box.getPhysicalAddress()));
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "URL DSS"));
	        item.add(new Label("boxvalue", box.getUrlDSS()));
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Free space"));
	        if (box.getLockssBoxData()!= null && (box.getLockssBoxData().size()!=0)) {
	        	item.add(new Label("boxvalue", FileUtils.byteCountToDisplaySize(1024*box.getLockssBoxData().get(0).getFree())));
	  	    }
	        else {
	         	item.add(new Label("boxvalue","")); 
	        }
	       
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Percentage space"));
	        if (box.getLockssBoxData()!= null && (box.getLockssBoxData().size()!=0)) {
	        	 item.add(new Label("boxvalue", box.getLockssBoxData().get(0).getPercentage()));
	  	    }
	        else {
	         	item.add(new Label("boxvalue","")); 
	        }
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Used space"));
	        if (box.getLockssBoxData()!= null && (box.getLockssBoxData().size()!=0)) {
	        	item.add(new Label("boxvalue", FileUtils.byteCountToDisplaySize(1024*box.getLockssBoxData().get(0).getUsed())));
		    }
	        else {
	         	item.add(new Label("boxvalue","")); 
	        }
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Size"));
	        if (box.getLockssBoxData()!= null && (box.getLockssBoxData().size()!=0)) {
	        	item.add(new Label("boxvalue",  FileUtils.byteCountToDisplaySize(1024*box.getLockssBoxData().get(0).getSize())));
		    }
	        else {
	         	item.add(new Label("boxvalue","")); 
	        }
	        
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Longitude"));
	        item.add(new Label("boxvalue", box.getLongitude()));
	        
	        item = new AbstractItem(boxrepeating.newChildId());
	        boxrepeating.add(item);
	        item.add(new Label("boxitem", "Latitude"));
	        item.add(new Label("boxvalue", box.getLatitude()));
	         
	        

	        RepeatingView peersRepeating = new RepeatingView("peersrepeating");
	        add(peersRepeating);
	        
	        AbstractItem peersItem = null;
	        
	        Iterator<Peer> peersIterator = box.getPeers().iterator();
	        while (peersIterator.hasNext()) {
	        	Peer peer = peersIterator.next();
	        	
	        	peersItem = new AbstractItem(peersRepeating.newChildId());
	        	peersRepeating.add(peersItem);
	        	
				
				peersItem.add(new Label("peerslockssid", peer.getPeerLockssId()));
				peersItem.add(new Label("peerspollscalled", peer.getPollsCalled()));
				peersItem.add(new Label("peerspollsrejected", peer.getPollsRejected()));
				peersItem.add(new Label("peerslastvote",  format.format(peer.getLastVote())));
				peersItem.add(new Label("peerslastinvitation",  format.format(peer.getLastInvitation())));
				peersItem.add(new Label("peerslastpoll", format.format(peer.getLastPoll())));
				peersItem.add(new Label("peerslastmsg",  format.format(peer.getLastMessage())));
				peersItem.add(new Label("peersmsgtype", peer.getMessageType()));
	        	
	        }
	     
	        RepeatingView AUrepeating = new RepeatingView("AUrepeating");
	        add(AUrepeating);
	        
	        AbstractItem AUitem = null;
	        
	        //TODO: generalize for all PLN id
	        Iterator<AU> auIterator = null;
			try {
				auIterator = plnDataController.getPlnDAO().getAllAUsinBox(0, box.getBoxId()).iterator();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        while (auIterator.hasNext()) {
	        	final AU au = auIterator.next();
	        	String ID = AUrepeating.newChildId();
	        	
	        	
	        	AUitem = new AbstractItem(ID);
	        	
	        	Link<String> link = new Link<String>("AUName") {
	                /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
	                public void onClick() {
	                	setResponsePage(new AUinBoxDisplayPage(parameters, plnDataController, boxName, au.getName()));
	                }
	            };
	        	link.setBody(Model.of(au.getName()));
	        	link.add(new AttributeAppender("style", "cursor: pointer;"));
	        	AUitem.add(link);
	        	AUitem.add(new Label("AUContentSize", FileUtils.byteCountToDisplaySize(au.getContentSize())));	
	        	AUitem.add(new Label("AULastPollResult", au.getLastPollResult()));
	        	AUitem.add(new Label("AUDiskUsage", FileUtils.byteCountToDisplaySize(au.getDiskUsage())));
	        	AUitem.add(new Label("AURecentPoll", au.getRecentPollAgreement()));
	        	if (au.getUpdatedAt() != null ) {
	        		AUitem.add(new Label("AUUpdatedAt", format.format(au.getUpdatedAt())));
	        	}
	        	else {
	         		AUitem.add(new Label("AUUpdatedAt", "never"));
	        	}
	        	AUrepeating.add(AUitem);
	        	
	        }
			}
			else {
				RepeatingView boxrepeating = new RepeatingView("boxrepeating");
		        add(boxrepeating);
		        AbstractItem item = new AbstractItem(boxrepeating.newChildId());
		        boxrepeating.add(item);
		        item.add(new Label("boxitem", "Box not found"));
		        item.add(new Label("boxvalue", "Box not found"));
		        
		        RepeatingView peersRepeating = new RepeatingView("peersrepeating");
		        add(peersRepeating);
		        
		        AbstractItem peersItem = new AbstractItem(peersRepeating.newChildId());
		        peersRepeating.add(peersItem);
	        	peersItem.add(new Label("peersitem", "Box not found"));
	        	peersItem.add(new Label("peersvalue","Box not found"));
	        	
		        RepeatingView AUrepeating = new RepeatingView("AUrepeating");
		        add(AUrepeating);
		        
		        AbstractItem AUitem = new AbstractItem(AUrepeating.newChildId());
	        	AUrepeating.add(AUitem);
	        	AUitem.add(new Label("AUitem", "Box not found"));
	        	AUitem.add(new Label("AUvalue", "Box not found"));
			}
}   
	 
	    
}
