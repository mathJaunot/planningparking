package bo;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Declaration {

	
	//@Element(types=DeclarationPlus.class, dependent="true", mappedBy="declaration")

	Set<DeclarationPlus> declarationPlus ;//= new HashSet();
	public Set<DeclarationPlus> getDeclarationPlus() {
		return declarationPlus;
	}

	private int ID;
	private java.util.Date dateConfirmation;
	private boolean isConfirmer = false;
	private java.util.Date dateCreation = new Date();
	
	Collection<Facture> facture;

	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public java.util.Date getDateConfirmation() {
		return this.dateConfirmation;
	}

	public void setDateConfirmation(java.util.Date dateConfirmation) {
		this.dateConfirmation = dateConfirmation;
	}

	public java.util.Date getDateCreation() {
		return this.dateCreation;
	}

	public void setDateCreation(java.util.Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Collection<Facture> getFacture() {
		return this.facture;
	}

	public void setFacture(Collection<Facture> facture) {
		this.facture = facture;
	}

	public Declaration() {
		
	}

	/**
	 * 
	 * @param declarationPlus
	 */
	public void setDeclarationPlus(Set<DeclarationPlus> declarationPlus) {
		this.declarationPlus = declarationPlus;
	}

	public boolean isConfirmer() {
		return isConfirmer;
	}

	/**
	 * 
	 * @param isConfirmer
	 */
	public void setConfirmer(boolean isConfirmer) {
		this.isConfirmer = isConfirmer;
	}

	public boolean getConfirmer() {
		return this.isConfirmer ;
	}

}