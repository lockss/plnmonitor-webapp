/**
* SAFE PLN Monitor 
*
* Wicket application class 
* 
* Main Wicket application class for plnmonitor web application.
* 
* Config file plnmonitor.cfg contains basic configuration about plnmonitor database access
* 
*   <ul>
*    <li>./webpages: InfoPage class extending BasePage (public acces webpage) and other java classes extending WebPage (pages needing authorized access) </li>
*    <li>./rdbms :database manager implementation (reuse from DSpace code)</li>
*    <li>./datacontroller : data controller managing the PLN Data Access Object for current session</li>
*    <li> ./dao : data access objects making queries to database to update values of local objects </li>
*    <li> ./object : classes for AU, Institution, LOCKSSBox, LOCKSSBoxData, Person, User objects</li>
*    </ul>
* 
* @author  Anthony Leroy
* @version 0.7
* @since   2017-07-06 
*/

package org.lockss.plnmonitor;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;

import org.lockss.plnmonitor.datacontroller.Utils;

import org.lockss.plnmonitor.webpages.*;


/**
 * Application object for the plnmonitor web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see org.lockss.plnmonitor.Start#main(String[])
 */
public class WicketApplication extends AuthenticatedWebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return InfoPage.class;
	}

	
	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();

		// set configuration

		Utils.CONFIG_PATH = this.getServletContext().getRealPath("/")+"WEB-INF/classes/plnmonitor.cfg";
		
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
	    getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
	    
	    //defaut page is InfoPage (public page not requiring authentication)
	    getApplicationSettings().setPageExpiredErrorPage(InfoPage.class); 
	    		
	    getMarkupSettings().setStripWicketTags(true); //remove wicket tags
	}
	
		@Override
		public Session newSession(Request request, Response response){
					return new NewAuthenticatedWebSession(request);
			    }
		 /**
	     * @see org.apache.wicket.authentication.AuthenticatedWebApplication#getWebSessionClass()
	     */
	    @Override
	    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass()
	    {
	        return NewAuthenticatedWebSession.class;
	    }

	    /**
	     * @see org.apache.wicket.authentication.AuthenticatedWebApplication#getSignInPageClass()
	     */
	    @Override
	    protected Class<? extends WebPage> getSignInPageClass()
	    {
	        return SignInPage.class; 
	    }

	
}
