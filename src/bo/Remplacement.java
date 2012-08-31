package bo;

public class Remplacement {
	private ISalarie salarie;
	private Employe employeMobile;
	private MotifRemplacement motifRemplacement;
	private TempsTravailEffectue TempsTravailEffectue;
	public ISalarie getSalarie() {
		return salarie;
	}
	public void setSalarie(ISalarie salarie) {
		this.salarie = salarie;
	}
	public Employe getEmployeMobile() {
		return employeMobile;
	}
	public void setEmployeMobile(Employe employeMobile) {
		this.employeMobile = employeMobile;
	}
	public String getMotifRemplacement() {
		String motif = "Nom Remplacé";
		if(motifRemplacement != null){
			 motif = motifRemplacement.toString(); 
		}
		return motif;
	}
	public void setMotifRemplacement(MotifRemplacement motifRemplacement) {
		this.motifRemplacement = motifRemplacement;
	}
	public TempsTravailEffectue getTempsTravailEffectue() {
		return TempsTravailEffectue;
	}
	public void setTempsTravailEffectue(TempsTravailEffectue tempsTravailEffectue) {
		TempsTravailEffectue = tempsTravailEffectue;
	} 

}
