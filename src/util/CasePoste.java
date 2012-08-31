package util;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import bo.Employe;
import bo.EmployeMobile;
import bo.Planning.fonction;
import bo.SalariePoste;


public class CasePoste {
	
	private Collection<SalariePoste>  employesPoste;
	private Collection<EmployeMobile> employeMobiles;
	
	public CasePoste(){
	
	}
	
	
	public void initEmployeMobile(){
		 ObjectContainer db = pool.MyServletContextListener.db2;
		 try{
			 ObjectSet result5=db.query(EmployeMobile.class);
			 employeMobiles = result5;
		 }finally {
			 db.commit();
		 }
	}
	

	
	public void initEmployePoste(){
		 ObjectContainer db = pool.MyServletContextListener.db2;
		 
		try{
			 ObjectSet result=db.query(SalariePoste.class);
			 employesPoste= result;
		 }finally {
			 db.commit();
		 }
	}
	
	private Cell cellHoraire ;
	private Cell cellNomTitulaire;
	public Cell getCellNomTitulaire() {
		return cellNomTitulaire;
	}

	public void setCellNomTitulaire(Cell cellNomTitulaire) {
		this.cellNomTitulaire = cellNomTitulaire;
	}

	private Cell cellNomRemplacant ;
	private Cell[] arrayCell = {cellHoraire,cellNomTitulaire,cellNomRemplacant};
	private String nomTitulaire;
	private String prenomTitulaire;
	
	
	
	public void setNomTitulaire(String nomTitulaire) {
		this.nomTitulaire = nomTitulaire;
	}

	private String motif="";
	private String nomRemplacant;
	private String prenomRemplacant;
	private int row=0;
	private int col=0;
	private Date dateDebut;
	private Date dateFin; 
	private String[] s2 = new String[2];
	private String[] s3 = new String[2];

	public CasePoste(int r, int c){
		row = r;
		col = c;
		initEmployeMobile();
		initEmployePoste();
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public void loadCell(Worksheet worksheet,Date date){
		cellHoraire = Utils.getCell(worksheet,row+0,col);
		cellNomTitulaire = Utils.getCell(worksheet,row+1,col);
		cellNomRemplacant = Utils.getCell(worksheet,row+2,col);
		
		if(cellHoraire.getStringCellValue() != null && !cellHoraire.getStringCellValue().trim().equalsIgnoreCase("") ){
			int j = 0;
			Employe emplo =null;
			if(!cellNomTitulaire.getStringCellValue().trim().contains(".") ){
				if(!cellNomTitulaire.getStringCellValue().trim().equalsIgnoreCase("AESM") ){
				for(EmployeMobile em :employeMobiles){
					if(em.getNom().trim().equalsIgnoreCase(cellNomTitulaire.getStringCellValue().trim())){
						emplo = em;
						j++;
					}
				}
				for(SalariePoste em : employesPoste){
					if(em.getNom().trim().equalsIgnoreCase(cellNomTitulaire.getStringCellValue().trim())){
						emplo = em;
						j++;
					}
				}
				if(j==1){
					cellNomTitulaire.setCellValue(emplo.getNom()+"."+emplo.getPrenom());
					Utils.setEditText(worksheet, row+1, col, emplo.getNom()+"."+emplo.getPrenom());
					nomTitulaire = emplo.getNom();
					prenomTitulaire = emplo.getPrenom();
					
				}else{
					//System.out.println("ici----"+cellNomTitulaire.getStringCellValue());
					nomTitulaire ="";
					prenomTitulaire ="";
				}
			}else{
				nomTitulaire ="AESM";
				nomRemplacant = "";
			}
				
			}else{
				int t =cellNomTitulaire.getStringCellValue().indexOf(".");
				nomTitulaire = cellNomTitulaire.getStringCellValue().substring(0,t );
				prenomTitulaire = cellNomTitulaire.getStringCellValue().substring(t+1 );
				
				if(!cellNomRemplacant.getStringCellValue().equalsIgnoreCase("") ){
					if(cellNomRemplacant.getStringCellValue().indexOf(".") != -1){
						int t2=cellNomRemplacant.getStringCellValue().indexOf(".");
						nomRemplacant = cellNomRemplacant.getStringCellValue().substring(0,t2 );
						prenomRemplacant = cellNomRemplacant.getStringCellValue().substring(t2+1 );
						
					}
				}else{
					nomRemplacant= "";
					prenomRemplacant="";
				
				}
				//nomTitulaire = cellNomTitulaire.getStringCellValue().split(".")[0];
				//prenomTitulaire = cellNomTitulaire.getStringCellValue().split(".")[1];
			}
		if(cellHoraire.getStringCellValue() != null && !cellHoraire.getStringCellValue().trim().equalsIgnoreCase("") ){
		//	System.out.println(cellHoraire.getStringCellValue());
			setDate(date, cellHoraire.getStringCellValue());
		}
		}
	}

	public String getNomTitulaire() {
		return nomTitulaire;
	}

	public String getNomRemplacant() {
		return nomRemplacant;
	}

	public void setNomRemplacant(String nomRemplacant) {
		this.nomRemplacant = nomRemplacant;
	}

	public void setDate(Date date, String sHeureDebutEtFin){
		dateDebut = date;
		dateFin = date;
		String[] s = sHeureDebutEtFin.split("/");
		s2 = s[0].split("h");
		s3 = s[1].split("h");
		
		
		
		Calendar currDtCal = GregorianCalendar.getInstance();
    		currDtCal.setTime(date);
	    // 	Zero out the hour, minute, second, and millisecond
	    	currDtCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s2[0]));
	    	currDtCal.set(Calendar.MINUTE, Integer.parseInt(s2[1]));
	    	currDtCal.set(Calendar.SECOND, 0);
	    	currDtCal.set(Calendar.MILLISECOND, 0);
	    	dateDebut =currDtCal.getTime();
		
	    	currDtCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s3[0]));
	    	currDtCal.set(Calendar.MINUTE, Integer.parseInt(s3[1].trim()));
	    	currDtCal.set(Calendar.SECOND, 0);
	    	currDtCal.set(Calendar.MILLISECOND, 0);
	    	dateFin =currDtCal.getTime();
	//	System.out.println("datedebut : "+dateDebut + "dateFin :"+dateFin);
		
		
		if(dateDebut.after(dateFin)){
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(dateFin);
			calendar.add(Calendar.DAY_OF_MONTH, +1);
			dateFin = calendar.getTime();
		}
	}

	public Date getDateDebut(){
		return dateDebut;
	}
	
	public Date getDateFin(){
		return dateFin;
	}
	
	public boolean isCasePosteEmploye(int r, int c, /*String[] employesMobileNames*/ Collection<EmployeMobile> employesMobile,Collection<SalariePoste> employesPoste, Window modalDialog){
		boolean isTrue =false;
	
		Combobox comboMotif = (Combobox)modalDialog.getFellow("comboMotif");
		Label labelMotif = (Label)modalDialog.getFellow("motif");
		Label label = (Label)modalDialog.getFellow("label");
		Combobox combo = (Combobox)modalDialog.getFellow("combo");
//		System.out.println(cellNomRemplacant.getRowIndex()+"row :"+r+"/"+cellNomRemplacant.getColumnIndex()+"-col"+c);
		if(cellNomRemplacant.getRowIndex() == r && cellNomRemplacant.getColumnIndex() == c){
//			System.out.println("remplacent");
			isTrue=true;
			modalDialog.setTitle("Remplacant");
			comboMotif.setVisible(true);
			comboMotif.setValue(motif);
			labelMotif.setVisible(true);
			label.setValue("Remplacant :");
			combo.setValue(cellNomRemplacant.getStringCellValue());
		}
		boolean isEmployeMobile = false;
		
		
		
    	 
    	 String[] nomPrenomEmployPoste = new String[employesPoste.size()] ;
    	 int y = 0;
    	 for(SalariePoste emp : employesPoste){
    		 nomPrenomEmployPoste[y] = emp.getNom()+" "+emp.getPrenom();
    		 if(emp.getNom().trim().equalsIgnoreCase(cellNomTitulaire.getStringCellValue().trim())){
				 isEmployeMobile =false;
    		 }
    		 y++;
    	 }
    	 
    	 String[] nomPrenomEmployeMobile = new String[employesMobile.size()] ;
    	 y = 0;
    	 for(EmployeMobile emp : employesMobile){
    	//	 System.out.println(emp.getNom()+"---");
    		 nomPrenomEmployeMobile[y] = emp.getNom()+" "+emp.getPrenom();
    		 if(emp.getNom().trim().equalsIgnoreCase(cellNomTitulaire.getStringCellValue().trim())){
				 isEmployeMobile =true;
    		 }
    		 y++;
    	 }
    	 
		//return nomEmployeMobile;
		/*
		for(String employeMobileName : employesMobileNames){
			if(employeMobileName.trim().equalsIgnoreCase(cellNomTitulaire.getStringCellValue().trim())){
				 isEmployeMobile =true;
			}
			if(employeMobileName.trim().equalsIgnoreCase(cellNomTitulaire.getStringCellValue().trim())){
				 isEmployeMobile =true;
			}
		}
		*/
    	 
   // 	 System.out.println("ttttttttt");
    	 //cellNomTitulaire.getRowIndex() == r && cellNomTitulaire.getColumnIndex() == c  &&
		if( (cellNomTitulaire.getStringCellValue().equalsIgnoreCase("AESM") || isEmployeMobile || cellNomTitulaire.getStringCellValue().equalsIgnoreCase(""))){
			isTrue=true;
			modalDialog.setTitle("Titulaire Mobile");
			ListModelList lm = new ListModelList(nomPrenomEmployeMobile);
   	        combo.setModel(lm);
		//	combo.setValue(cellNomTitulaire.getStringCellValue());
			
			labelMotif.setVisible(false);
			comboMotif.setVisible(false);
			label.setValue("Titulaire Mobile :");
	//		System.out.println("uuu");
		}
		//cellNomTitulaire.getRowIndex() == r && cellNomTitulaire.getColumnIndex() == c  &&
		if(  !isEmployeMobile){
			isTrue=true;
			modalDialog.setTitle("Titulaire Poste");
			ListModelList lm = new ListModelList(nomPrenomEmployPoste);
   	        combo.setModel(lm);
			//combo.setValue(cellNomTitulaire.getStringCellValue());
			labelMotif.setVisible(false);
			comboMotif.setVisible(false);
			label.setValue("Titulaire :");
//			System.out.println("vvvv");
		}
		return isTrue;
	}

	
	
	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public Cell[] getArrayCell() {
		return arrayCell;
	}

	public String getPrenomTitulaire() {
		return prenomTitulaire;
	}

	public void setPrenomTitulaire(String prenomTitulaire) {
		this.prenomTitulaire = prenomTitulaire;
	}

	public String getPrenomRemplacant() {
		return prenomRemplacant;
	}

	public void setPrenomRemplacant(String prenomRemplacant) {
		this.prenomRemplacant = prenomRemplacant;
	}

	
}
