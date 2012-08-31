package bo;

public  class SalariePoste extends Employe implements ISalarie  {
	private String Poste;
	public String getPoste() {
		return Poste;
	}

	public void setPoste(String poste) {
		Poste = poste;
	}

}
