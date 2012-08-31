package util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.zkoss.zss.model.Worksheet;

public class GrandeCase {
	public CasePoste caseMatin = new CasePoste(0,0);
	public CasePoste caseApresMidi = new CasePoste(0,1);
	public CasePoste caseMatin2 = new CasePoste(3,0);
	public CasePoste caseApresMidi2 = new CasePoste(3,1);
	private CasePoste[] arrayCase = {caseMatin,caseApresMidi,caseMatin2,caseApresMidi2};
	private Date dateDuJour;
	
	public Date getDate() {
		return dateDuJour;
	}

	public void setDate(Date date) {
		this.dateDuJour = date;
	}

	//public CaseRenfort caseRenfort = new CaseRenfort();
	public static int hauteur = 6; 
	public GrandeCase(int nbjour, int nbparking, int hauteurEnteteEnCellule, int largeurEnteteEnCellule,Worksheet worksheet, Date date){
		Calendar currDtCal = GregorianCalendar.getInstance();
		currDtCal.setTime(date);
	    // Zero out the hour, minute, second, and millisecond
	    currDtCal.set(Calendar.HOUR_OF_DAY, 0);
	    currDtCal.set(Calendar.MINUTE, 0);
	    currDtCal.set(Calendar.SECOND, 0);
	    currDtCal.set(Calendar.MILLISECOND, 0);
	    date = currDtCal.getTime();
		dateDuJour = date;
		int j =0;
		for(CasePoste casePoste : arrayCase){
			int rowtot = casePoste.getRow()+hauteurEnteteEnCellule+hauteur*nbjour;
			int coltot = largeurEnteteEnCellule+casePoste.getCol()+2*nbparking;
			casePoste.setRow(rowtot);
			casePoste.setCol(coltot);
		//	System.out.println("Cposte:"+j+"-row:"+rowtot+"-col:"+coltot);
			
			casePoste.loadCell( worksheet, date);
			j++;
		}
	}
	
	public CasePoste[] getArrayCase(){
		return arrayCase; 
	}
	
	
}
