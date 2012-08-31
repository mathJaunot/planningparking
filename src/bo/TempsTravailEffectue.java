package bo;

import java.util.Date;

public class TempsTravailEffectue {
	
	private Date Debut;
	private Date Fin;
	private Lieu lieu;
	public Date getDebut() {
		return Debut;
	}
	public void setDebut(Date debut) {
		Debut = debut;
	}
	public Date getFin() {
		return Fin;
	}
	public void setFin(Date fin) {
		Fin = fin;
	}
	public Lieu getLieu() {
		return lieu;
	}
	public void setLieu(Lieu lieu) {
		this.lieu = lieu;
	}

}
