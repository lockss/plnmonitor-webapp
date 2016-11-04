package be.ulb.plnmonitor.datacontroller;

import java.io.Serializable;
import java.sql.SQLException;

import be.ulb.plnmonitor.dao.PLNDAO;

public class PLNDataController implements Serializable {
	private PLNDAO plnDAO;
	
	public PLNDataController(UserSession currentSession) {
		plnDAO = new PLNDAO(currentSession);
	}
	
//	// set PLN properties from UI to database (IP address, url, address...)
//	public void setLOCKSSBoxProperties(LOCKSSBox lockssBox){
//		
//	}
	
	public PLNDAO getPlnDAO() {
		return plnDAO;
	}

	public void setPlnDAO(PLNDAO plnDAO) {
		this.plnDAO = plnDAO;
	}

	// load current PLN status in the database
//	public void loadPLNStatus(){
//		try {
//			plnDAO.loadPLN();
//			//UserSession.getLog().info(plnDAO.toString());
//			//UserSession.getLog().info(plnDAO.getPlnList().get(0).toString());
//			//UserSession.getLog().info(plnDAO.getPlnList().get(0).getPlnBoxes().get(0).toString());
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}
