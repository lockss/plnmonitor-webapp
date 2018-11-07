package org.safepln.plnmonitor.object;

import java.io.Serializable;
import java.util.List;

public class Institution implements Serializable {
	private String address;
	private String name;
	private String shortName;
	private long institutionId;
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public long getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(long institutionId) {
		this.institutionId = institutionId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Institution(long institutionId, String name, String address, String shortName) {
		super();
		this.institutionId=institutionId;
		this.shortName=shortName;
		this.address = address;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
