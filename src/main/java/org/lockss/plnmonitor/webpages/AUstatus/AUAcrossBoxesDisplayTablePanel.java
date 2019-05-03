package org.lockss.plnmonitor.webpages.AUstatus;

import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Panel;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.lockss.plnmonitor.datacontroller.PLNDataController;
import org.lockss.plnmonitor.object.AU;

import org.apache.wicket.markup.html.basic.Label;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;

import org.apache.wicket.markup.repeater.RepeatingView;

/////////////////////////////////////////////////////////////////////
// Webpage displaying AU info for one specific AU in one specific box
/////////////////////////////////////////////////////////////////////

class AUAcrossBoxesDisplayTablePanel extends Panel {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	
	public AUAcrossBoxesDisplayTablePanel(final String id, final PageParameters parameters, final PLNDataController plnDataController, final AU au) {
		super(id);
			String boxName = new String();
			try {
				boxName = plnDataController.getPlnDAO().getBoxName(au.getLockssBoxId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			add(new Label("tablename", boxName));
			
		        
		      add(new Label("auName",au.getName()));
		      add(new Label("boxName",boxName));

		    	        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		    			format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
		    	        
		    	        add(new Label("auFullName", au.getName()));
		    			add(new Label("auContentSize",FileUtils.byteCountToDisplaySize(au.getContentSize())));
		    			add(new Label("auDiskUsage", FileUtils.byteCountToDisplaySize(au.getDiskUsage())));
		    			add(new Label("auCreationTime", format.format(au.getCreationTime())));
		    		    add(new Label("auPublisher", au.getTdbPublisher()));
		                add(new Label("auYear", au.getTdbYear()));
		            
		    			add(new Label("auLastPollResult", au.getLastPollResult()));
		    	        add(new Label("auLastCompletedPoll", format.format(au.getLastCompletedPoll())));
		    	        add(new Label("auRecentPollAgreement",  String.format("%.1f", 100*au.getRecentPollAgreement())+"%"));
		    			
		   			 	add(new Label("auLastCrawl", format.format(au.getLastCrawl())));
		            	add(new Label("auLastCrawlResult", au.getLastCrawlResult()));
		            	add(new Label("auLastCompletedCrawl", format.format(au.getLastCompletedCrawl())));
		            	add(new Label("auCrawlPool", au.getCrawlPool()));
		            	   
		    			add(new Label("auCrawlProxy", au.getCrawlProxy()));
		            	add(new Label("auCrawlWindow", au.getCrawlWindow()));
		            	
		            	
		            	add(new Label("auPluginName", au.getPluginName()));
		            	add(new Label("auLOCKSSID", au.getAu_LOCKSS_Id()));
		            	add(new Label("auRepositoryPath", au.getRepositoryPath()));
		            

		            	String createdAt = "No data";
		    	        if (au.getCreatedAt() != null) {
		    	        	 createdAt = format.format(au.getCreatedAt());
		    	        }
		    	        
		    	        add(new Label("auInfoCreated",createdAt));
		    	        	
		    	        String updatedAt = "No data";
		    	        if (au.getUpdatedAt() != null) {
		    	        	updatedAt = format.format(au.getUpdatedAt());
		    	        }
		    	        
		    	        add(new Label("auInfoUpdated", updatedAt)); 
		    	        
		    	        String gauge_color = "";
		    	        if (au.getRecentPollAgreement() == 0) {
		    	        	gauge_color = "'#a9a9a9'";
		    	        }
		    	        else if (au.getRecentPollAgreement() < 0.7) {
		    	        	gauge_color = "'#bc1a3a'"; 	
		    	        }
		    	        else if  (au.getRecentPollAgreement() < 0.95) {
		    	        	gauge_color = "'#e15f28'"; 	
		    	        }
		    	        else if (au.getRecentPollAgreement() >= 0.95){
		    	        	gauge_color = "'#1abc4b'"; 	
		    	        }
		    	        add(new Label("gauge_settings","var gauge_value = " + String.format("%.1f", 100*au.getRecentPollAgreement()) + "; \n"+
		    	        "var gauge_color = " +  gauge_color + ";\n"
		    	        		).setEscapeModelStrings(false));
		    	        				



		}
	}


