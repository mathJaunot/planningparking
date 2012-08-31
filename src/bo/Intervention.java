package bo;

import java.util.Date;



public class Intervention {

	private int ID;
	private java.util.Date dateDebut;
	private java.util.Date dateFin;
	Interimaire interimaire;
	Motif motif;
	Poste poste;
	Contrat contrat;
	Lieu parking;
	DeclarationPlus declarationPlus;

	public Interimaire getInterimaire() {
		return interimaire;
	}

	public void setInterimaire(Interimaire interimaire) {
		this.interimaire = interimaire;
	}

	public DeclarationPlus getDeclarationPlus() {
		return declarationPlus;
	}

	public void setDeclarationPlus(DeclarationPlus declarationPlus) {
		this.declarationPlus = declarationPlus;
	}

	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public java.util.Date getDateDebut() {
		return this.dateDebut;
	}

	public void setDateDebut(java.util.Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public java.util.Date getDateFin() {
		return this.dateFin;
	}

	public void setDateFin(java.util.Date dateFin) {
		this.dateFin = dateFin;
	}

	public Motif getMotif() {
		return this.motif;
	}

	public void setMotif(Motif motif) {
		this.motif = motif;
	}

	public Poste getPoste() {
		return this.poste;
	}

	public void setPoste(Poste poste) {
		this.poste = poste;
	}

	public Contrat getContrat() {
		return this.contrat;
	}

	public void setContrat(Contrat contrat) {
		this.contrat = contrat;
	}

	public Lieu getParking() {
		return this.parking;
	}

	public void setParking(Lieu parking) {
		this.parking = parking;
	}

}