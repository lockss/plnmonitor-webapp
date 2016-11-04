package be.ulb.plnmonitor.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable, Comparable<User>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String email;
	private String matricule;
	private int id;
	private String titre = "M.";
	
	private List<String> roles = new ArrayList<String>();
	
	public User(){		
	}
	
//	public User(Node xml){
//		if (xml != null) {
//			setMatricule(getElementData(xml, "matricule"));
//			email = getElementData(xml, "email");
//			lastName = getElementData(xml, "nom");
//			firstName = getElementData(xml, "prenom");
//		}
//	}
	
	public User(String matricule) throws Exception{
		if(matricule==null){
			throw new Exception("Matricule is null");
		}
		setMatricule(matricule);
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMatricule() {
		String n = null;
		if (matricule != null) {
			String m = matricule.trim();
			if (!m.equals("")) {
				int i = m.lastIndexOf(":");
				n = m.substring(i + 1);
			}
		}
		return n;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public boolean isValid() {
		boolean valid = true;
		if (matricule == null || email == null || email.equals("")) {
			valid = false;
		}
		return valid;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getRoles() {
		return roles;
	}
	
	public void addRole(String role){
		if(!roles.contains(role)){
			roles.add(role);
		}
	}

	public int compareTo(User u) {
		if(u!=null && u.getId()==this.getId()) return 0;
		else return 1;
	}

	public void setTitre(String titre) {
		this.titre = titre;		
	}
		
	public String getTitre(){
		return this.titre;
	}
}
