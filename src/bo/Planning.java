package bo;

import java.util.Collection;
import java.util.Date;

import org.zkoss.zss.model.Worksheet;

public class Planning {
	private Date dateCreation;
	
	public Date getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public Collection<Affectation> getAffectation() {
		return Affectation;
	}
	public void setAffectation(Collection<Affectation> affectation) {
		Affectation = affectation;
	}
	public Collection<Date> getJoursFeries() {
		return joursFeries;
	}
	public void setJoursFeries(Collection<Date> joursFeries) {
		this.joursFeries = joursFeries;
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
	public int getVersionCycle() {
		return versionCycle;
	}
	public void setVersionCycle(int versionCycle) {
		this.versionCycle = versionCycle;
	}

	
	private int version;
	//private Collection<Employe> Employe;
	private Collection<Affectation> Affectation;
	private Collection<Date> joursFeries;
	private Date dateDebut;
	private Date dateFin;
	private int versionCycle;
	public enum fonction {Previsionnel,Declare};
}
