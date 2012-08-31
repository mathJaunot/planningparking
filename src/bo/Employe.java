package bo;

public abstract class Employe {

	private String nom;
	private Adresse adresse;
	private String prenom;
	private Employeur employeur;
	private String telephoneFixe;
	private String telephoneMobile;
	private TempsTravailEffectue tempsTravailEffectue;


	private IComportementPoste comportementPoste;
	public IComportementPoste getComportementPoste() {
		return comportementPoste;
	}

	public void setComportementPoste(IComportementPoste comportementPoste) {
		this.comportementPoste = comportementPoste;
	}

	private IComportementCalculRemuneration comportementCalculRemuneration;
	private Qualification qualification;

	public Employeur getEmployeur() {
		return employeur;
	}

	public void setEmployeur(Employeur employeur) {
		this.employeur = employeur;
	}

	public String getTelephoneFixe() {
		return telephoneFixe;
	}

	public void setTelephoneFixe(String telephoneFixe) {
		this.telephoneFixe = telephoneFixe;
	}

	public String getTelephoneMobile() {
		return telephoneMobile;
	}

	public void setTelephoneMobile(String telephoneMobile) {
		this.telephoneMobile = telephoneMobile;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

}
