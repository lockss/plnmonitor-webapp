package be.ulb.plnmonitor.object;

import java.io.Serializable;

public class Person implements Serializable {
	private String name;
	private String firstName;
	private String emailAddress;
	private String phone;
	private long institutionId;
	private boolean techAdmin;
	private boolean orgAdmin;

	public Person(String name, String firstName, String phone, String emailAddress, long institutionId,
			boolean techAdmin, boolean orgAdmin) {
		super();
		this.name = name;
		this.firstName = firstName;
		this.phone = phone;
		this.emailAddress = emailAddress;
		this.institutionId = institutionId;
		this.techAdmin = techAdmin;
		this.orgAdmin = orgAdmin;

	}

	public long getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(long institutionId) {
		this.institutionId = institutionId;
	}

	public boolean isTechAdmin() {
		return techAdmin;
	}

	public void setTechAdmin(boolean techAdmin) {
		this.techAdmin = techAdmin;
	}

	public boolean isOrgAdmin() {
		return orgAdmin;
	}

	public void setOrgAdmin(boolean orgAdmin) {
		this.orgAdmin = orgAdmin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Person(Person person) {
		super();
		this.name = person.getName();
		this.firstName = person.getFirstName();
		this.phone = person.getPhone();
		this.emailAddress = person.getEmailAddress();
		this.institutionId = person.getInstitutionId();
		this.techAdmin = person.isTechAdmin();
		this.orgAdmin = person.isOrgAdmin();
	}
}
