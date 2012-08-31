package ctrl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.zhtml.Em;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Exporter;
import org.zkoss.zss.model.Exporters;
import org.zkoss.zss.model.Importer;
import org.zkoss.zss.model.Importers;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import bo.Affectation;
import bo.Employe;
import bo.EmployeMobile;
import bo.Employeur;
import bo.Planning;
import bo.SalariePoste;


public class GererPlanningPersonnel extends GenericForwardComposer {

    private Combobox combo;
	 private Spreadsheet report;
	    private Book book;
	    private Worksheet worksheet;
	    private Spreadsheet spreadsheet;
	    private int offset; //offset to next row
	    private Range templateRange;
    private Collection<Employe> employes;
    private Collection<Affectation> affectations;
    private Date dateDepart;
    
    private Collection<EmployeMobile> employeMobiles ;
    private Collection<SalariePoste> employesPoste ;
    private String nomEmploye= "";
    private String prenomEmploye="";
    //private Collection<Employeur> employeurs; 
   
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		//reportSheet = spreadsheet.getSelectedSheet();
		//currentCell = Utils.getCell(worksheet, 0,0);
		Calendar calendier = Calendar.getInstance();
		Date maintenant = new Date();
		 calendier.setTime(maintenant);
		// date de début de la semaine
		 calendier.set(Calendar.DAY_OF_WEEK , calendier.getFirstDayOfWeek());
		Date lundi =  calendier.getTime();
		final Importer importer = Importers.getImporter("excel");
		final InputStream is2 = Sessions.getCurrent().getWebApp().getResourceAsStream("/modelesExcel/planPersonnel.xlsx"); 
	    final Book dstbook = importer.imports(is2, "F1.xlsx");
	    spreadsheet.setBook(dstbook);
		worksheet = spreadsheet.getSelectedSheet();
		dateDepart = new Date();
		initEmployeMobile();
		 initEmployePoste();
		 String[] nomPrenomEmployeMobileplusPoste = new String[employeMobiles.size()+employesPoste.size()] ;
		 int i =0;
		for(EmployeMobile em : employeMobiles){
			nomPrenomEmployeMobileplusPoste[i]= em.getNom()+ " "+ em.getPrenom();
			i++;
		}
		for(SalariePoste sal : employesPoste){
			nomPrenomEmployeMobileplusPoste[i]= sal.getNom()+ " "+ sal.getPrenom();
			i++;
		}
		 ListModelList lm = new ListModelList(nomPrenomEmployeMobileplusPoste);
	      combo.setModel(lm);
	      UserDetails  userDetails =	 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//System.out.println(userDetails.getUsername());
			if(userDetails.getUsername().contains(".")){
				int y =userDetails.getUsername().indexOf(".");
				prenomEmploye = userDetails.getUsername().substring(0, y);
				nomEmploye = userDetails.getUsername().substring( y+1);
				//userDetails.getUsername().subSequence(y);
				//System.out.println(prenomEmploye);
				//System.out.println(nomEmploye);
				/*
				String tempo =new String();
				tempo= userDetails.getUsername();
				String g[] =tempo.split(".");
				System.out.println(g.length);
				prenomEmploye = g[0];
				*/
				//nomEmploye = userDetails.getUsername().split(".")[1];
			}
			//prenomEmploye = userDetails.getUsername().split(".")[0];
			//nomEmploye = userDetails.getUsername().split(".")[1];
			Utils.setEditText(worksheet, 0, 0, nomEmploye+" "+prenomEmploye);
		
		loadData();
	}
	
	 public void onClick$prec(){
		 
		Calendar calendaru = GregorianCalendar.getInstance();
 		calendaru.setTime(dateDepart);
 		calendaru.add(Calendar.DATE, -56);
		dateDepart = calendaru.getTime();
		initEmployeMobile();
		initEmployePoste();
		
		
		
		loadData();
	 }
	
	 public void onChange$combo(){
		nomEmploye= combo.getText().split(" ")[0];
		prenomEmploye= combo.getText().split(" ")[1];
		
		Utils.setEditText(worksheet, 0, 0, nomEmploye+" "+prenomEmploye);
			loadData();
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
	 
	 
	 
	 
	 
	public void loadData(){

		ObjectContainer db = pool.MyServletContextListener.db2;
		 
		
		  ArrayList<Planning> plannings;
		
	 
	//	Planning planningModel = new Planning();
		try{
			Query query=db.query();
			 
			query.constrain(Planning.class);
			query.descend("dateDebut").orderAscending();
			query.descend("version").orderAscending();
			ObjectSet result3=query.execute();
			// ObjectSet result3 = db.queryByExample(planningModel);
			 plannings = new ArrayList<Planning>(((Collection<Planning>)result3));
		 }finally {
			 db.commit();
		 }
		affectations = new ArrayList<Affectation>();
		for(Planning pla : plannings){
			if(pla.getDateFin().after(dateDepart)){
				for(Affectation affectation :pla.getAffectation()){
					//if( affectation.getEmploye().getNom().trim().equalsIgnoreCase("BOUDIER")){
					if(affectation.getEmployeRemplacent() != null){
						if(  affectation.getEmployeRemplacent().getNom().trim().equalsIgnoreCase(nomEmploye) && affectation.getEmployeRemplacent().getPrenom().trim().equalsIgnoreCase(prenomEmploye) || (affectation.getEmploye().getNom().trim().equalsIgnoreCase(nomEmploye) && affectation.getEmploye().getPrenom().trim().equalsIgnoreCase(prenomEmploye))){
							//System.out.println( affectation.getDateDebut());
							//System.out.println( affectation.getDateDebut());
							affectations.add(affectation);
						}
					}else{
						if( affectation.getEmploye().getNom().trim().equalsIgnoreCase(nomEmploye) && affectation.getEmploye().getPrenom().trim().equalsIgnoreCase(prenomEmploye) ){
							//System.out.println( affectation.getDateDebut());
							//System.out.println( affectation.getDateDebut());
							affectations.add(affectation);
						}
					}
					
					
				}
			}
		}
		
		Calendar calendarSemaineActuelle = GregorianCalendar.getInstance();  
    	calendarSemaineActuelle.setTime(dateDepart);
    	 calendarSemaineActuelle.get(Calendar.WEEK_OF_YEAR);
		
    	 
    	 Calendar calendar2 = GregorianCalendar.getInstance();
    	 Calendar calendar3 = GregorianCalendar.getInstance();
    	 Date dt = new Date();
    	 //System.out.println(dt);
    	 calendar3.setTime(dt);
    	 calendar2.clear();
    	 
    	 calendar2.setFirstDayOfWeek(calendar2.MONDAY);
    	 calendar2.set(Calendar.YEAR, 2012);
    	 calendar2.set(Calendar.WEEK_OF_YEAR,Calendar.WEEK_OF_YEAR);
      	 SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd/MM/yyyy");
      	SimpleDateFormat dateformathhmm = new SimpleDateFormat("HH:mm");
   	     
   	   
   	     
		for(int j=0;j<=7;j++){
		
			//System.out.println(getNumeroSemaineDepart()+j);
			 calendar2.set(Calendar.WEEK_OF_YEAR, getNumeroSemaineDepart()+j);
			 Utils.setEditText(worksheet, j+3, 0, Integer.toString(getNumeroSemaineDepart()));
			for(int k=0;k<=6;k++){
				calendar2.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY+k);
				//System.out.print(calendar2.getTime()+ "  ");
				//System.out.println((k+1+(j*7)));
			    String stringDateDebut;
			    stringDateDebut = new StringBuilder( dateformatMMDDYYYY.format(calendar2.getTime())).toString();
			    Utils.setEditText(worksheet, (k+1+(j*7))+3, 2, stringDateDebut);
				//calendarSemaineActuelle.get(Calendar.)
				Calendar currDtCal = GregorianCalendar.getInstance();
				
		   		currDtCal.setTime(calendar2.getTime());
		   		currDtCal.add(Calendar.DAY_OF_MONTH, +1);
				    currDtCal.set(Calendar.HOUR_OF_DAY, 00);
				    currDtCal.set(Calendar.MINUTE, 00);
				    currDtCal.set(Calendar.SECOND, 00);
				    currDtCal.set(Calendar.MILLISECOND, 00);
				
				    
				    Utils.setEditText(worksheet, (k+1+(j*7))+3, 3,"");
		    		 Utils.setEditText(worksheet, (k+1+(j*7))+3, 4, "");
		    		 Utils.setEditText(worksheet, (k+1+(j*7))+3, 5,"");
		    		 Utils.setEditText(worksheet, (k+1+(j*7))+3, 6,"");
				     for(Affectation af : affectations){
				    	 
				    	 if(af.getDateFin().after(calendar2.getTime()) && af.getDateDebut().before(calendar2.getTime()) && af.getDateFin().before(currDtCal.getTime())){
				   // 		 System.out.print(calendar2.getTime());
				   // 		 System.out.print(af.getDateFin());
				   // 		 System.out.println(af.getLieu().getNom());
				    	/*	 
				    		 if( af.getEmployeRemplacent().getNom().trim().equalsIgnoreCase(nomEmploye) && af.getEmployeRemplacent().getPrenom().trim().equalsIgnoreCase(prenomEmploye) ){
				    			 Utils.setEditText(worksheet, (k+1+(j*7))+3, 5, af.getMotif());
				    		
				    		 if(){
				    			 
				    		 }
				    		 */
				    		 if(af.getMotif() != null && af.getEmployeRemplacent() != null &&  af.getEmploye().getNom().trim().equalsIgnoreCase(nomEmploye) && af.getEmploye().getPrenom().trim().equalsIgnoreCase(prenomEmploye) ){
				    			 Utils.setEditText(worksheet, (k+1+(j*7))+3, 5, af.getMotif());
				    		 }
				    		 /*
				    		 if(af.getMotif() != null && af.getEmployeRemplacent() != null &&  af.getEmployeRemplacent().getNom().trim().equalsIgnoreCase(nomEmploye) && af.getEmployeRemplacent().getPrenom().trim().equalsIgnoreCase(prenomEmploye)){
				    			 
				    		 }
				    		 
	 */
				    		 stringDateDebut = new StringBuilder( dateformathhmm.format(calendar2.getTime())).toString();
				    		 Utils.setEditText(worksheet, (k+1+(j*7))+3, 3, af.getLieu().getNom());
				    		 Utils.setEditText(worksheet, (k+1+(j*7))+3, 4, new StringBuilder( dateformathhmm.format(calendar2.getTime())).toString()+" à " + new StringBuilder( dateformathhmm.format(af.getDateFin())).toString());
				    		 
				    		 
				    		 DateTime startTime, endTime;
				    		 startTime = new DateTime(calendar2.getTime());
				    		 endTime = new DateTime( af.getDateFin());
				    		 Period p = new Period(startTime, endTime);
				    		 long hours = p.getHours();
				    		 long minutes = p.getMinutes();
				    		 
				    		 Utils.setEditText(worksheet, (k+1+(j*7))+3, 6, hours+","+minutes*100/60);
				    		 
				    		 
				    	 }
				    	 
				    	 if(af.getDateDebut().after(calendar2.getTime()) && af.getDateDebut().before((currDtCal.getTime()))){
				   // 		 System.out.print(af.getDateDebut());
				    		 Date fin ;
				    		 if(af.getDateFin().before((currDtCal.getTime()))){
				    			 fin = af.getDateFin();
					    //		 System.out.print(af.getDateFin());
					    	 }else{
					    		 fin = currDtCal.getTime();
					    //		 System.out.print(currDtCal.getTime());
					    	 }
				    	//	System.out.println(af.getLieu().getNom());
				    		 
				    		 if(af.getMotif() != null && af.getEmployeRemplacent() != null &&  af.getEmploye().getNom().trim().equalsIgnoreCase(nomEmploye) && af.getEmploye().getPrenom().trim().equalsIgnoreCase(prenomEmploye) ){
				    			 Utils.setEditText(worksheet, (k+1+(j*7))+3, 5, af.getMotif());
				    		 }
				    		 
				    		 Utils.setEditText(worksheet, (k+1+(j*7))+3, 3, af.getLieu().getNom());
				    		 Utils.setEditText(worksheet, (k+1+(j*7))+3, 4, new StringBuilder( dateformathhmm.format(af.getDateDebut())).toString()+" à " + new StringBuilder( dateformathhmm.format(fin)).toString());
				    		 Utils.setEditText(worksheet, (k+1+(j*7))+3, 5, af.getMotif());
				    		 DateTime startTime, endTime;
				    		 startTime = new DateTime(af.getDateDebut());
				    		 endTime = new DateTime( fin);
				    		 Period p = new Period(startTime, endTime);
				    		 long hours = p.getHours();
				    		 long minutes = p.getMinutes();
				    		 
				    		 Utils.setEditText(worksheet, (k+1+(j*7))+3, 6, hours+","+minutes*100/60);
				    	 }
				    	 
				    	 
				    		
				     }
			}
		}

		
	}
	
    public void onClick$print(){
 	   Book wb = spreadsheet.getBook();
 	    Exporter c = Exporters.getExporter("excel");
 	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
 	    c.export(wb, baos);
 	    Filedownload.save(baos.toByteArray(), "application/file",
 	            wb.getBookName());
    }

    public int getNumeroSemaineDepart(){
    	Calendar calendarSemaineActuelle = GregorianCalendar.getInstance();  
    	calendarSemaineActuelle.setTime(dateDepart);
    	return calendarSemaineActuelle.get(Calendar.WEEK_OF_YEAR);
    }
	
}
	