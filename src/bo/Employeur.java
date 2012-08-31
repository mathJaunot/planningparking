package bo;

public class Employeur {
	
	private String Nom;
	private String Siret;
	public String getNom() {
		return Nom;
	}
	public void setNom(String nom) {
		Nom = nom;
	}
	public String getSiret() {
		return Siret;
	}
	public void setSiret(String siret) {
		Siret = siret;
	}
	public String getSecteurActivite() {
		return secteurActivite;
	}
	public void setSecteurActivite(String secteurActivite) {
		this.secteurActivite = secteurActivite;
	}
	private String secteurActivite;

}
