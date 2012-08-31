package bo;

import java.util.Date;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class DeclarationPlus {

	private int ID;
	private java.util.Date dateCreation;
	private java.util.Date dateModification;
	DeclarationPlus prec;
	DeclarationPlus suiv;
	private Collection<Message> messages;
	
	//@Element(types=Interimaire.class, dependent="true", mappedBy="declarationPluses")
	//@Join

	private Set<Interimaire> interimaire ;//= new HashSet();
	
	private Declaration declaration;

	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public java.util.Date getDateCreation() {
		return this.dateCreation;
	}

	public void setDateCreation(java.util.Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public java.util.Date getDateModification() {
		return this.dateModification;
	}

	public void setDateModification(java.util.Date dateModification) {
		this.dateModification = dateModification;
	}

	public DeclarationPlus getPrec() {
		return this.prec;
	}

	public void setPrec(DeclarationPlus prec) {
		this.prec = prec;
	}

	public DeclarationPlus getSuiv() {
		return this.suiv;
	}

	public void setSuiv(DeclarationPlus suiv) {
		this.suiv = suiv;
	}

	public Collection<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(Collection<Message> messages) {
		this.messages = messages;
	}

	public Set<Interimaire> getInterimaire() {
		return this.interimaire;
	}

	public void setInterimaire(Set<Interimaire> interimaire) {
		this.interimaire = interimaire;
	}

	public Declaration getDeclaration() {
		return this.declaration;
	}

	public void setDeclaration(Declaration declaration) {
		this.declaration = declaration;
	}

	/**
	 * 
	 * @param declaration
	 */
	public DeclarationPlus(Declaration declaration) {
		dateCreation = new Date();
		int idMax =0;
		if(declaration != null){
			if( declaration.getDeclarationPlus() != null){
				for(DeclarationPlus decPlus : declaration.getDeclarationPlus()){
					if(idMax < decPlus.getID()) {
						idMax = decPlus.getID();
					}
				}
				idMax = idMax+1;
			}
		this.declaration = declaration; 
		//declaration.declarationsPlus.add(this);
		
		}
		this.ID = idMax;
	}

}