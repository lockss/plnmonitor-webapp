package org.safepln.plnmonitor.object;

import java.io.Serializable;
import java.util.List;

public class PLN implements Serializable {
	private String name;
	private long pln_id;
	private List<LOCKSSBox> plnBoxes;
	private String configUrl;
	
	public String getConfigUrl() {
		return configUrl;
	}

	public void setConfigUrl(String configUrl) {
		this.configUrl = configUrl;
	}

	public PLN(String name, long pln_id, String configUrl, List<LOCKSSBox> plnBoxes) {
		super();
		this.name = name;
		this.pln_id = pln_id;
		this.plnBoxes = plnBoxes;
		this.configUrl = configUrl;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getPln_id() {
		return pln_id;
	}
	public void setPln_id(long pln_id) {
		this.pln_id = pln_id;
	}
	public List<LOCKSSBox> getPlnBoxes() {
		return plnBoxes;
	}
	public void setPlnBoxes(List<LOCKSSBox> plnBoxes) {
		this.plnBoxes = plnBoxes;
	}
}
