package ctrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;



import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellRange;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;


import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.impl.Utils;

import bo.*;


import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;



import org.zkoss.zss.ui.impl.Utils;

import util.Db4oUtil;

import com.db4o.*;

public class GererEmploye extends GenericForwardComposer {

    private Cell currentCell;
    private Spreadsheet spreadsheet;
    private Worksheet worksheet;
    private Collection<Employe> employes;
    private Collection<Employeur> employeurs; 
   
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		worksheet = spreadsheet.getSelectedSheet();
		currentCell = Utils.getCell(worksheet, 0,0);
		loadEmployeur();
		load();
		affiche();
	}
	
	public void loadEmployeur(){
		employeurs = new ArrayList<Employeur>();
		ObjectContainer db = pool.MyServletContextListener.db2;
		try {
			 ObjectSet result=db.query(Employeur.class);
			 employeurs = new ArrayList<Employeur>(((Collection<Employeur>)result));
		}
		 finally {
			 db.commit();
		}
	}
	
	private Employeur getEmployeurByName(String nomEmployeur){
		Employeur eemployeur= new Employeur();
		eemployeur.setNom("");
		for(Employeur employeur : employeurs){
			if(employeur.getNom().trim().equalsIgnoreCase(nomEmployeur.trim())){
				eemployeur = employeur;
			}
		}
		return eemployeur;
		
	}
	
	public void affiche(){
		int i = 1;
		for(Employe employe : employes){
			Utils.setCellValue(worksheet, i, 0, employe.getNom());
			Utils.setCellValue(worksheet, i, 1, employe.getPrenom());
			if(employe.getEmployeur() != null){
				Utils.setCellValue(worksheet, i, 2, employe.getEmployeur().getNom());
			}else{
			//	Utils.setCellValue(worksheet, i, 2, "");
			}
			
			try{
				Utils.setCellValue(worksheet, i, 3, ((SalariePoste)employe).getPoste());	
			}catch (Exception e) {
				System.out.println(e.toString());
				// TODO: handle exception
			}
			try{
				 AES a = ((AES)employe);
				Utils.setCellValue(worksheet, i, 3, "AESM");	
			}catch (Exception e) {
				System.out.println(e.toString());
				// TODO: handle exception
			}
			
			
			Utils.setCellValue(worksheet, i, 4, employe.getTelephoneFixe());
			Utils.setCellValue(worksheet, i, 5, employe.getTelephoneMobile());
			i++;
		}
	}
	
	public void load(){
		 employes = new ArrayList<Employe>();
		ObjectContainer db = pool.MyServletContextListener.db2;
		 try {
			 ObjectSet result=db.query(Employe.class);
			 employes = new ArrayList<Employe>(((Collection<Employe>)result));
			 for(Employe employe : employes){
			 }
		 }
		 finally {
			 db.commit();
		 }
	}
	
	public void  onClick$save(){
		ObjectContainer db = pool.MyServletContextListener.db2;
		employes = new ArrayList<Employe>();
		 try {
			 int i = 1;
			 while( Utils.getCell(worksheet, i,0)!= null){
				 Employe employe; 
				 if(((Cell)Utils.getCell(worksheet, i,2)).getStringCellValue().trim().equalsIgnoreCase("CITEDIA")){
					 if(((Cell)Utils.getCell(worksheet, i,3)).getStringCellValue().trim().equalsIgnoreCase("AESM")){
						 employe = new AES();
					 }else{
						 if(((Cell)Utils.getCell(worksheet, i,3)).getStringCellValue().trim() != null){
							 if(!((Cell)Utils.getCell(worksheet, i,3)).getStringCellValue().trim().equalsIgnoreCase("")){
								 employe = new SalariePoste() ;
							 }
							 else{
								 System.out.println("emp1");
								 employe = new Salarie();
							 }
						 } else{
							 System.out.println("emp2");
							 employe = new Salarie();
						 }
						 
							 
					 } 
					 }else{
					 employe = new Interimaire();
				 }
				 String f = currentCell.getStringCellValue();
				 employe.setNom(((Cell)Utils.getCell(worksheet, i,0)).getStringCellValue().trim());
				 employe.setPrenom(((Cell)Utils.getCell(worksheet, i,1)).getStringCellValue().trim());
				 employe.setEmployeur(getEmployeurByName(((Cell)Utils.getCell(worksheet, i,2)).getStringCellValue().trim()));
				 if(((Cell)Utils.getCell(worksheet, i,3)) != null  ){
					 if(!((Cell)Utils.getCell(worksheet, i,3)).getStringCellValue().trim().equalsIgnoreCase("AESM")){
						 ((SalariePoste)employe).setPoste(((Cell)Utils.getCell(worksheet, i,3)).getStringCellValue().trim());
					 }
					
				 }
				 //((Cell)Utils.getCell(worksheet, i,4)).getStringCellValue().trim()
				 employe.setTelephoneFixe(((Cell)Utils.getCell(worksheet, i,4)).getStringCellValue().trim());
				 employe.setTelephoneMobile(((Cell)Utils.getCell(worksheet, i,5)).getStringCellValue().trim());
				 employes.add(employe);
				i++;
			}
			ObjectSet result2=db.queryByExample(Employe.class);
			while(result2.hasNext()) {
				db.delete(result2.next());
			}
			 db.commit();
			db.store(employes);
		 }
		 finally {
			 db.commit();
		 }
	}
}
