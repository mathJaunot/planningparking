package bo;

import java.util.Date;

public class Affectation {
	private Employe employe;
	private Employe employeRemplacent;
	private Lieu lieu;
	private Date dateDebut;
	private Date dateFin;
	private String motif;
	private boolean isAbsent = false;
	
	public boolean isAbsent() {
		return isAbsent;
	}
	public void setAbsent(boolean isAbsent) {
		this.isAbsent = isAbsent;
	}
	public Employe getEmploye() {
		return employe;
	}
	public void setEmploye(Employe employe) {
		this.employe = employe;
	}
	public Lieu getLieu() {
		return lieu;
	}
	public void setLieu(Lieu lieu) {
		this.lieu = lieu;
	}
	public Date getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}
	public Date getDateFin() {
		return dateFin;
	}
	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}
	public String getMotif() {
		return motif;
	}
	public void setMotif(String motif) {
		this.motif = motif;
	}
	public Employe getEmployeRemplacent() {
		return employeRemplacent;
	}
	public void setEmployeRemplacent(Employe employeRemplacent) {
		this.employeRemplacent = employeRemplacent;
	}
}
