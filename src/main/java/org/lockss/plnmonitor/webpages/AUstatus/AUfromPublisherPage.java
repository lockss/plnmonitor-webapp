package org.lockss.plnmonitor.webpages.AUstatus;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.lockss.plnmonitor.datacontroller.PLNDataController;
import org.lockss.plnmonitor.object.AU;
import org.lockss.plnmonitor.webpages.BasePage;

public class AUfromPublisherPage extends BasePage {

	public AUfromPublisherPage(final PageParameters parameters) {
		super(parameters);
	}

	public AUfromPublisherPage(final PageParameters parameters, final PLNDataController plnDataController, String publisherName) {
		super(parameters);
		 
		List<AU> ausFromPublisher = null;
	      // TODO: adapt PLN identifier in query
	      
	      try {
	    	  ausFromPublisher = plnDataController.getPlnDAO().getAUsFromPublisher(0, publisherName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	      add(new Label("publisherName",publisherName));
	      
	      RepeatingView auRepeating = new RepeatingView("AUrepeating");
	      add(auRepeating);
	        
	    	        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    			format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
	     
	      Iterator<AU> ausFromPublisherIterator = ausFromPublisher.iterator();
	      while (ausFromPublisherIterator.hasNext()){
	    	  		AU currentAU = ausFromPublisherIterator.next();
	    	        AbstractItem item = new AbstractItem(auRepeating.newChildId());
	    	        auRepeating.add(item);
	    	        
	    	        final String currentAUName = currentAU.getName();
	    	        Link<String> link = new Link<String>("AUitem") {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							setResponsePage(new AUAcrossBoxesDisplayPage(parameters, plnDataController, currentAUName));
						}
					};
					link.setBody(Model.of(currentAUName));
					link.add(new AttributeAppender("style", "cursor: pointer;"));
					item.add(link);
					
					item.add(new Label("AUvalue",  FileUtils.byteCountToDisplaySize(currentAU.getContentSize())));
					item.add(new Label("AUstatus", currentAU.getLastPollResult()));
	    	        
	     }
	      
	}

}
