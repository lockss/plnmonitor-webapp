package be.ulb.plnmonitor;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.BufferedWebResponse;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.settings.IResourceSettings;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.BufferedWebResponse;
import org.apache.wicket.protocol.http.WebApplication;

import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.settings.IResourceSettings;
import org.apache.wicket.util.file.Folder;

import be.ulb.plnmonitor.datacontroller.UserSession;
import be.ulb.plnmonitor.datacontroller.Utils;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see be.ulb.plnmon.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
//	private final ContactsDatabase contactsDB = new ContactsDatabase(50);
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return GlobalNetworkStatusPage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();

		// add your configuration here

		Utils.CONFIG_PATH = this.getServletContext().getRealPath("/")+"WEB-INF/classes/plnmonitor.cfg";
		
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
	    getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
	    
	    
	    Utils.UPLOAD_DIR = ConfigurationManager.getProperty("upload.dir");
		Utils.authServer = ConfigurationManager.getProperty("authentification.server");
		
	    getMarkupSettings().setStripWicketTags(true);
	}
	
	/**
     * @return contacts database
     */
 //   public ContactsDatabase getContactsDB()
 //   {
 //       return contactsDB;
 //   }
   
	  private static final String[] botAgents = { 
			    "crawl", "googlebot", "jeeves", "msnbot", "robot", "slurp", "spider"
			    /*
			     * "appie", "architext", "jeeves", "bjaaland", "ferret", "gulliver",
			     * "harvest", "htdig", "linkwalker", "lycos_", "moget", "muscatferret",
			     * "myweb", "nomad", "scooter", "yahoo!\\sslurp\\schina", "slurp",
			     * "weblayers", "antibot", "bruinbot", "digout4u", "echo!", "ia_archiver",
			     * "jennybot", "mercator", "netcraft", "msnbot", "petersnews",
			     * "unlost_web_crawler", "voila", "webbase", "webcollage", "cfetch",
			     * "zyborg", "wisenutbot", "robot", "crawl", "spider"
			     */
			    
			    /* 'spider' catches 'baiduspider'" */
			  }; 


			  public static boolean isAgent(final String agent) {
			    if (agent != null) {
			      final String lowerAgent = agent.toLowerCase();
			      for (final String bot : botAgents) {
			        if (lowerAgent.indexOf(bot) != -1) {
			          return true;
			        }
			      }
			    }
			    return false;
			  }
			  
//			  /*
//			   * Create new session for each crawler-request and strip jsessionid parameter from URL
//			   */
//			  @Override
//			  protected WebResponse newWebResponse(final HttpServletResponse servletResponse) {
//			    return new BufferedWebResponse(servletResponse) {
//			      @Override
//			      public CharSequence encodeURL(final CharSequence url) {
//			        final String agent = ((WebRequest) RequestCycle.get().getRequest()).getHttpServletRequest().getHeader("User-Agent");
//			        return isAgent(agent) ? url : super.encodeURL(url);
//			      }
//			    };
//			  }
//			  
//				
//				@Override
				public Session newSession(Request request, Response response){
					return new UserSession(request);
			    }
//				
//				@Override
//				public Class<Login> getHomePage(){
//					return Login.class;
////					return TestPanel.class;
//				}
}
