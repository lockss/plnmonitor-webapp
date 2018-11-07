package org.safepln.plnmonitor.datacontroller;

import java.io.Serializable;

import org.safepln.plnmonitor.NewAuthenticatedWebSession;
import org.safepln.plnmonitor.dao.PLNDAO;

/**
 * The Class PLNDataController.
 * Data controller managing the PLN Data Access Object for currentSession (get and set methods)
 */
public class PLNDataController implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8466369065028943256L;
	
	/** The pln DAO. */
	private PLNDAO plnDAO;
	
	/**
	 * Instantiates a new PLN data controller.
	 *
	 * @param currentSession the current session
	 */
	public PLNDataController(NewAuthenticatedWebSession currentSession) {
		plnDAO = new PLNDAO(currentSession);
	}

	/**
	 * Gets the pln DAO.
	 *
	 * @return the pln DAO
	 */
	public PLNDAO getPlnDAO() {
		return plnDAO;
	}

	/**
	 * Sets the pln DAO.
	 *
	 * @param plnDAO the new pln DAO
	 */
	public void setPlnDAO(PLNDAO plnDAO) {
		this.plnDAO = plnDAO;
	}
	
}
