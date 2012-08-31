package ctrl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

//import org.datanucleus.store.types.sco.simple.ArrayList;
import org.zkoss.bind.annotation.*;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.calendar.impl.SimpleCalendarEvent;
import org.zkoss.calendar.impl.SimpleCalendarModel;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import bo.*;

 
public class gererInterimaire  {
 
	ArrayList<Declaration> declarations ;
	ArrayList<DeclarationPlus> declarationsPlus;
	ArrayList<Interimaire> interimaireAffectes;
	ArrayList<Interimaire> interimaireNonAffectes;
	
	Declaration selectedDeclaration ;
	
	DeclarationPlus declarationPlus ;
	DeclarationPlus selectedDeclarationPlus ;
	
	Interimaire selectedInterimaireEnAffect;
	Interimaire selectedInterimairePlanning;
	
	public Interimaire getSelectedInterimairePlanning() {
		System.out.println("getSelectedInterimairePlanning");
		return selectedInterimairePlanning;
	}

	public void setSelectedInterimairePlanning(
			Interimaire selectedInterimairePlanning) {
		System.out.println("setSelectedInterimairePlanning");
		this.selectedInterimairePlanning = selectedInterimairePlanning;
	}

	public Interimaire getSelectedInterimaireEnAffect() {
		System.out.println("getSelectedInterimaireEnAffect");
		return selectedInterimaireEnAffect;
	}

	public void setSelectedInterimaireEnAffect(	
		Interimaire selectedInterimaireEnAffect) {
		System.out.print("setSelectedInterimaireEnAffect");
		System.out.println(selectedInterimaireEnAffect.getNom());
		this.selectedInterimaireEnAffect = selectedInterimaireEnAffect;
	}

	public DeclarationPlus getSelectedDeclarationPlus() {
		System.out.println("getSelectedDeclarationPlus");
		return selectedDeclarationPlus;
	}

	@NotifyChange({"interimaireAffectes"})
	public void setSelectedDeclarationPlus(DeclarationPlus selectedDeclarationPlus) {
		System.out.print("setselecteddecplus");
		if(selectedDeclarationPlus != null){
			this.selectedDeclarationPlus = selectedDeclarationPlus;
		}else{
			System.out.print(" qq");
		}
		System.out.print(" qa");
		interimaireAffectes =  new ArrayList<Interimaire>();
		System.out.print(" qv");
		interimaireNonAffectes = new ArrayList<Interimaire>();
		System.out.println(" fffffffff");
	}

	public Declaration getselectedDeclaration() {
		System.out.println("iciefsd");
		selectedDeclarationPlus = null;
		return selectedDeclaration;
	}
	
	@NotifyChange({"declarationsPlus","interimaireAffectes"})
	public void setSelectedDeclaration(Declaration  selectedDeclaration) {
		System.out.println("setSelectedDeclaration");
		if(selectedDeclaration != null){
			System.out.println("setSelectedDeclarationNotNull");
			this.selectedDeclaration= selectedDeclaration;
		}
	}
	
//	public ListModel<Declaration> getDeclarations(){
//		System.out.println("getDeclaration");
	//	DeclarationService declarationService = DeclarationService.getInstance();
	//	declarations  = new ListModelList<Declaration>(declarationService.list());
//		return declarations ;
 //   }
    /*
	public void  setDeclarations(ListModelList<Declaration> declarations) {
		System.out.println("setDeclaration");
		this.declarations  = declarations;
	}
	
	public ListModelList<DeclarationPlus> getDeclarationsPlus() {
		System.out.print("getDeclarationPlus");
		selectedDeclarationPlus = null;
		if(selectedDeclaration != null){
			System.out.print("notnull");
			declarationsPlus.clear();
			DeclarationService declarationService = DeclarationService.getInstance();
			declarationsPlus = new ListModelList<DeclarationPlus>(declarationService.listDeclarationplus(selectedDeclaration));
		}
		if(selectedDeclaration == null){
			System.out.print("null");
			declarationsPlus =  new ListModelList<DeclarationPlus>();
			declarationsPlus.clear();
		}
		if(declarationsPlus == null){
			declarationsPlus = new ListModelList<DeclarationPlus>();
		}
		System.out.println("ndddl");
		return declarationsPlus;
	}

	public void setDeclarationsPlus(ListModelList<DeclarationPlus> declarationsPlus) {
		System.out.println("setSelectedDeclarationplus");
		this.declarationsPlus = declarationsPlus;
	}

	public DeclarationPlus getDeclarationPlus() {
		System.out.println("getDeclarationPlus");
		return declarationPlus;
	}

	public void setDeclarationPlus(DeclarationPlus declarationPlus) {
		this.declarationPlus = declarationPlus;
	}
	*/
	
	/*
	@NotifyChange({"declarations","selectedDeclaration"})
	@Command
	public void ajouter() {
		DeclarationService declarationService = DeclarationService.getInstance();
		declarationService.save(new Declaration());  
		selectedDeclaration = null;
	}
	
	@NotifyChange("declarationsPlus")
	@Command
	public void ajouterDecPlus() {
		DeclarationService declarationService = DeclarationService.getInstance();
		DeclarationPlus de = new DeclarationPlus(selectedDeclaration);
		selectedDeclaration.getDeclarationPlus().add(de);
		declarationService.save(selectedDeclaration);
	}
	
	public ListModel<Interimaire> getInterimaireNonAffectes(){
		System.out.println("getInterimaireNonAffectes");
		DeclarationService declarationService = DeclarationService.getInstance();
		interimaireNonAffectes = new ListModelList<Interimaire>(declarationService.listInterimaire());
		if(interimaireNonAffectes==null){
			interimaireNonAffectes = new ListModelList<Interimaire>();
		}
		return interimaireNonAffectes ;
    }
	
	public ListModel<Interimaire> aaaagetInterimaireNonAffectes(){
		System.out.print("getInterimaireNonAffectes");
		DeclarationService declarationService = DeclarationService.getInstance();
		interimaireNonAffectes = new ListModelList<Interimaire>();
		if(selectedDeclarationPlus != null){
			ListModelList<Interimaire> interimaireAll  = new ListModelList<Interimaire>(declarationService.listInterimaire());
			ListModelList<Interimaire> interimaires2 = new ListModelList<Interimaire>();
			for(Interimaire inteAll : interimaireAll){
				System.out.print("eee");
				System.out.println(inteAll.getNom());
				for(Interimaire inteAff : interimaireAffectes){
					System.out.print("dd");
					System.out.print(inteAff.getNom());
					if(inteAll.getID() == inteAff.getID() ){
						System.out.print("remo");
						interimaires2.add(inteAll);
					}
				}
			}
			interimaireAll.removeAll(interimaires2);
			interimaireNonAffectes = interimaireAll;
		}
		if(interimaireNonAffectes==null){
			interimaireNonAffectes = new ListModelList<Interimaire>();
		}
		System.out.println("");
		return interimaireNonAffectes ;
    }
	
	public ListModel<Interimaire> getInterimaireAffectes(){
		System.out.print("getInterimaireAffectes");
		DeclarationService declarationService = DeclarationService.getInstance();
		interimaireAffectes = new ListModelList<Interimaire>();
		if(selectedDeclarationPlus != null){
			System.out.print(" lalllaaa");
			interimaireAffectes  = new ListModelList<Interimaire>(declarationService.listInterimaireAffectes(selectedDeclarationPlus));
			System.out.print(" iiiciii");
		}
		System.out.print(" hzzzzz");
		if(interimaireAffectes == null){
			System.out.print(" here1");
			interimaireAffectes = new ListModelList<Interimaire>();
			System.out.print(" here2");
		}
		System.out.println(" hyyyy");
		return interimaireAffectes ;
    }
	
	@NotifyChange({"interimaireAffectes"})
	@Command
	public void deplaceAgent() {
		DeclarationService declarationService = DeclarationService.getInstance();
		System.out.println("issscddssssdi");
			if(selectedDeclarationPlus != null){
			declarationService.addInterimaireAffectes(selectedDeclarationPlus, selectedInterimaireEnAffect);
		}
	}
	*/
}