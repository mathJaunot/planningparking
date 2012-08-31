package ctrl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import bo.*;

import no.geosoft.cc.util.Day;


import org.zkoss.poi.hssf.usermodel.HSSFClientAnchor;
import org.zkoss.poi.hssf.usermodel.HSSFPatriarch;
import org.zkoss.poi.hssf.usermodel.HSSFSheet;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.ClientAnchor;
import org.zkoss.poi.ss.usermodel.Color;
import org.zkoss.poi.ss.usermodel.Comment;
import org.zkoss.poi.ss.usermodel.CreationHelper;
import org.zkoss.poi.ss.usermodel.Drawing;
import org.zkoss.poi.ss.usermodel.RichTextString;
import org.zkoss.poi.ss.usermodel.Sheet;


import org.zkoss.poi.ss.util.CellRangeAddressList;
import org.zkoss.poi.xssf.usermodel.XSSFDataValidation;
import org.zkoss.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.zkoss.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.zkoss.poi.xssf.usermodel.XSSFDrawing;
import org.zkoss.poi.xssf.usermodel.XSSFSheet;

import org.zkoss.zss.model.Worksheet;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Exporter;
import org.zkoss.zss.model.Exporters;
import org.zkoss.zss.model.Importer;
import org.zkoss.zss.model.Importers;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.impl.BookHelper;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellMouseEvent;
import org.zkoss.zss.ui.event.StopEditingEvent;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.SimpleListModel;

import org.zkoss.zul.Window;

import util.CasePoste;
import util.GrandeCase;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import bo.EmployeMobile;
import bo.Interimaire;
import bo.SalariePoste;
import bo.Stationnement;;

public class GererPlanningParking extends GenericForwardComposer {

		Planning planning;
		private org.zkoss.zul.Popup pp;
		private Window modalDialog;
		private Window modalDialogErreur;
        private Cell currentCell;
        private Spreadsheet spreadsheet;  
        private Worksheet worksheet;
        private int numeroCycleFDeLaSemaineActuelle=1;
        private int nombreCycleFTotal=0;
        private int cycleAffiche = 0;
        private Date dateDepartCycleAffiche ;
        private Date dateFinCycleAffiche; 
        private int numeroSemaineDepartCycleAffiche ;
        private static Book book = null;
        private Iframe report;
        private Collection<EmployeMobile> employeMobiles ;
        private Collection<Stationnement> parkings ;
        private Collection<SalariePoste> employesPoste ;
        
        private EmployeMobile employeMobile = null;
        private Date dateDepartCycleFDeLaSemaineActuelle;
        private Date dateF1Xls;
        private int nombreDaysEntreDateF1XlsEtToday;
        private int nombreDeJourParCycle= 14;
        private int hauteurEnteteEnCellule = 2;
        private int largeurEnteteEnCellule = 1;
        private String[] motifs = new String[]{"CP", "CPF", "RTT","AM","FORMATION","AUTRE","NON REMPLACE"};
        private GrandeCase [] [] grandCaseArray ; 
        private Combobox combo;
        private Combobox comboMotif;
        private CasePoste casePosteSelected;
        private Label erreurMsg ;
        private Collection<Employe> employes; 
        Label  planningversion;
    
		public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
    		return super.doBeforeCompose(page, parent, compInfo);
    	}
    	
    	public void doAfterCompose(Component comp) throws Exception {
    		super.doAfterCompose(comp);
    		
    		LoadF1XlsAndInit();
   	        loadSpreadSheet();
   	        grandCaseArray = new GrandeCase [nombreDeJourParCycle] [parkings.size()];
   	        loadGrandeCaseArray();
   	      //  ListModel l = new SimpleListModel(getNomEmployeMobiles());
   	       
   	        combo = (Combobox)modalDialog.getFellow("combo");
   	     
   	        comboMotif = (Combobox)modalDialog.getFellow("comboMotif");
   	        comboMotif.setModel(new ListModelList(Arrays.asList(motifs)));
   	        combo.addEventListener("onChange", new EventListener() {
             public void onEvent(Event event) throws Exception {
             	onChangeCombo(event);
             }});
   	        comboMotif.addEventListener("onChange", new EventListener() {
             public void onEvent(Event event) throws Exception {
             	onChangeComboMotif(event);
             }});
   	     
   	    // XSSFPatriarch
   	        /*
   		 XSSFDrawing
   	 	final Map<Sheet, HSSFPatriarch> drawingPatriarches = new HashMap<Sheet, HSSFPatriarch>();
   	 	CreationHelper createHelper = spreadsheet.getSelectedSheet().getWorkbook().getCreationHelper();
   	 XSSFDrawing
   	 XSSFSheet sheet1 = (XSSFSheet) worksheet;
	    HSSFPatriarch drawingPatriarch = drawingPatriarches.get(sheet1);
	    if (drawingPatriarch == null) {
	        drawingPatriarch = ( XSSFDrawing) worksheet.createDrawingPatriarch();
	        drawingPatriarches.put(worksheet, drawingPatriarch);
	    }
*/
   	        
   	  /*      
   	     CreationHelper createHelper =  spreadsheet.getSelectedSheet().getWorkbook().getCreationHelper();
   	  Drawing drawingPatriarch =   worksheet.createDrawingPatriarch();
   	  Drawing
   	  */
	        //drawingPatriarches.put(worksheet, drawingPatriarch);
 		//HSSFPatriarch patr = worksheet.createDrawingPatriarch();
 		//drawingPatriarch.createChart(anchor) patr = sheet.createDrawingPatriarch();
 	  
   	        /*
   	        Comment comment1 = drawingPatriarch.createComment(new HSSFClientAnchor(100, 100, 100, 100, (short)1, 1, (short) 6, 5)); 
 		
 		
 		comment1.setString(createHelper.createRichTextString("ffffffffffffff"));
 		
 		currentCell = Utils.getCell(worksheet, 5,0);
 	      currentCell.setCellComment(comment1);
			*/
   	 
    	}
    	
    	public void afficheversionPlanning(){
    		SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd/MM/yyyy");
	   	     StringBuilder dateDepartCycleAfficheMMDDYYYY = new StringBuilder( dateformatMMDDYYYY.format(dateDepartCycleAffiche ) );
	   	     StringBuilder dateFinCycleAfficheMMDDYYYY = new StringBuilder( dateformatMMDDYYYY.format(dateFinCycleAffiche) );
	   	     
	   	     planningversion.setValue("Du "+dateDepartCycleAfficheMMDDYYYY+" au "+dateFinCycleAfficheMMDDYYYY+ " : Pas d'enregistrement");
	   	     if(planning != null){
	   	  planningversion.setValue("Du "+dateDepartCycleAfficheMMDDYYYY+" au "+dateFinCycleAfficheMMDDYYYY+ " : Version : "+((Integer)planning.getVersion()).toString());
	   	     }
    	}
    	
    	public void getPlanning(){
    	
    		ObjectContainer db = pool.MyServletContextListener.db2;
    		
	   	     Planning planningModel = new Planning();
	   	  
	   	     
	   	
	   	     
	   	     
	   	     
	  
	   	     planningModel.setDateDebut(dateDepartCycleAffiche);
	   	     planningModel.setDateFin(dateFinCycleAffiche);
	   	     /*
	   	     if(planning != null){
	   	    	planningModel.setVersion(planning.getVersion());	
	   	    	System.out.println("version planing: "+planning.getVersion());
	   	     }
	   	     */
	   	    
	   	     planning = null;
	   	     ArrayList<Planning> plannings;
	   	    
	   	     
	   	     
			 try{
				 ObjectSet result3 = db.queryByExample(planningModel);
				 plannings = new ArrayList<Planning>(((Collection<Planning>)result3));
			 }finally {
				 db.commit();
			 }
			 
			 if(planning != null){
		   	    	//planningModel.setVersion(planning.getVersion());	
		   	   // 	System.out.println("version planing: "+planning.getVersion());
		   	   }
			 
		//	 System.out.println("Taille: "+plannings.size());
			 if(plannings!= null && plannings.size() !=0){
				 for(int ii= 0; ii < plannings.size(); ii++){
					 planning = new Planning();
					 if(planning.getVersion() <= plannings.get(ii).getVersion()){
						 planning = plannings.get(ii);
					 }
				 }
				 if(planning!=null){
					 if(planning.getAffectation() != null){
						 for(Affectation affectation : planning.getAffectation()){
							// System.out.println(affectation.getEmploye().getNom());
							 //System.out.println(affectation.getEmploye().getNom());
						 }
					 }
				 
		//		 System.out.println(planning.getDateDebut().getTime());
		//		 System.out.println(planning.getDateFin().getTime());
				 }
				
			 }
			 
    	}
    	
    	public void onChangeCombo(Event event){
    		Utils.setEditText(worksheet, currentCell.getRowIndex(), currentCell.getColumnIndex(), ((Combobox)event.getTarget()).getValue());
    		changeColorCase(((Combobox)event.getTarget()).getValue());
    	}
    	
    	public void onChangeComboMotif(Event event){
    		changeColorCase(((Combobox)event.getTarget()).getValue());
    		
    		
    	   // currentCell = Utils.getCell(worksheet, 5,0);
      	     CreationHelper factory = spreadsheet.getSelectedSheet().getWorkbook().getCreationHelper();
      	     Drawing drawing = worksheet.createDrawingPatriarch();
      	     ClientAnchor anchor = factory.createClientAnchor();
      	     anchor.setCol1(  currentCell.getColumnIndex());
      	     anchor.setCol2(  currentCell.getColumnIndex()+1);
      	     anchor.setRow1(  currentCell.getRowIndex());
      	     anchor.setRow2(currentCell.getRowIndex()+3);
      	        
      	  Comment comment = drawing.createCellComment(anchor);
         RichTextString str = factory.createRichTextString(((Combobox)event.getTarget()).getValue());
         comment.setString(str);
         comment.setAuthor("Apache POI");

         // Assign the comment to the cell
         currentCell.setCellComment(comment); 
         //Comment c =currentCell.getCellComment();
    //     System.out.println(c.getString().toString()); 
    		
    	//casePosteSelected.setMotif();
    	}
    	
    	public void LoadF1XlsAndInit(){
    		initDateF1Xls();
    		initNombreDeCycleFTotal();
    		cycleAffiche = getNumeroCycleFDeLaSemaineActuelle();
    		initDatesEtNumeroSemaine();
    		initParking();
    		initEmployeMobile();
    		initEmployePoste();
    		initEmploye();
    	}
    	
    	public void initDatesEtNumeroSemaine(){
    		Calendar calendarSemaineActuelle = GregorianCalendar.getInstance();  
			calendarSemaineActuelle.setTime(dateDepartCycleFDeLaSemaineActuelle);
			int numeroSemaineActuelleDeDepartCycleF = calendarSemaineActuelle.get(Calendar.WEEK_OF_YEAR);
			dateDepartCycleAffiche = dateDepartCycleFDeLaSemaineActuelle;
			Calendar currDtCal = GregorianCalendar.getInstance();
			currDtCal.setTime(dateDepartCycleAffiche);
		    // Zero out the hour, minute, second, and millisecond
		    currDtCal.set(Calendar.HOUR_OF_DAY, 0);
		    currDtCal.set(Calendar.MINUTE, 0);
		    currDtCal.set(Calendar.SECOND, 0);
		    currDtCal.set(Calendar.MILLISECOND, 0);
		    dateDepartCycleAffiche = currDtCal.getTime();
			Calendar calendar = GregorianCalendar.getInstance();
      		calendar.setTime(dateDepartCycleAffiche);
      		calendar.add(Calendar.DATE, +(nombreDeJourParCycle-1));
      		dateFinCycleAffiche = calendar.getTime();
      		currDtCal = GregorianCalendar.getInstance();
      		currDtCal.setTime(dateFinCycleAffiche);
		    currDtCal.set(Calendar.HOUR_OF_DAY, 23);
		    currDtCal.set(Calendar.MINUTE, 59);
		    currDtCal.set(Calendar.SECOND, 59);
		    currDtCal.set(Calendar.MILLISECOND, 00);
		    dateFinCycleAffiche = currDtCal.getTime();
      		numeroSemaineDepartCycleAffiche = numeroSemaineActuelleDeDepartCycleF;
    	}
    	
    	public void setDatesOnXls(){
        	currentCell = Utils.getCell(worksheet, 5,0);
    		currentCell.setCellValue(dateDepartCycleAffiche);
    		currentCell = Utils.getCell(worksheet, 2,0);
    		currentCell.setCellValue(numeroSemaineDepartCycleAffiche);
    		currentCell = Utils.getCell(worksheet, 44,0);
    		currentCell.setCellValue(numeroSemaineDepartCycleAffiche+1);
    	}
	
    	public void afficheferie(){
    		for(int i =0; i<nombreDeJourParCycle; i++){
				currentCell = Utils.getCell(worksheet, hauteurEnteteEnCellule+(i*GrandeCase.hauteur)+4,0);
				currentCell.setCellValue("Ouvrable");
				setListConstraint(hauteurEnteteEnCellule+(i*GrandeCase.hauteur)+4,0,new String[]{ "Ferie", "Ouvrable"});
			}
    	}
        
    	public void initParking(){
    		int i=1;
    		parkings = new ArrayList<Stationnement>();
    		while (i<50){
    			currentCell = Utils.getCell(worksheet, 0,i);
    			if(currentCell != null){
	    			if(!currentCell.getStringCellValue().isEmpty() ){
	    				Stationnement p = new Stationnement();
	    				p.setNom(currentCell.getStringCellValue().split(" ")[0]);
	    				parkings.add(p);
	    			}
    			}
    			i++;
    		}
    	}
    	
    	public void initDateF1Xls(){
    		System.out.println("initdateF1");
    		final Importer importer = Importers.getImporter("excel");
    		final InputStream is2 = Sessions.getCurrent().getWebApp().getResourceAsStream("/modelesExcel/v0/F1.xlsx"); 
    	    final Book dstbook = importer.imports(is2, "F1.xlsx");
    	    spreadsheet.setBook(dstbook);
    		worksheet = spreadsheet.getSelectedSheet();
    		currentCell = Utils.getCell(worksheet, 5,0);
    		
    		
    		
    		
    		dateF1Xls =  currentCell.getDateCellValue();
    	}
    	
    	public int getNumeroCycleFDeLaSemaineActuelle(){
    		Day day1 = new Day (dateF1Xls);
    		Day day2 = new Day (new Date());
    		nombreDaysEntreDateF1XlsEtToday = day1.daysBetween(day2);
    		
    		initNumeroCycleFSemaineActuelle();
    		cycleAffiche = numeroCycleFDeLaSemaineActuelle;
    		initDateDepartCycleFDeLaSemaineActuelle();
    		return numeroCycleFDeLaSemaineActuelle;
    	}
    	
    	public void initDateDepartCycleFDeLaSemaineActuelle(){
	    	int a =0;
	    	//System.out.println(nombreDaysEntreDateF1XlsEtToday);
			if(nombreDaysEntreDateF1XlsEtToday>nombreDeJourParCycle){
				a = nombreDaysEntreDateF1XlsEtToday%nombreDeJourParCycle;
	    	}
		//	System.out.println("a1:"+a+"/");
			if(a>(nombreDeJourParCycle/2)){
				a=a-(nombreDeJourParCycle/2);
			}
		
			Calendar ca = GregorianCalendar.getInstance();
			ca.setTime(new Date());
			ca.add(Calendar.DATE, -a+1);
			dateDepartCycleFDeLaSemaineActuelle = ca.getTime();
			//System.out.println("a2:"+a+"/"+dateDepartCycleFDeLaSemaineActuelle);
    	}
    	
    	public void initNumeroCycleFSemaineActuelle(){
    		double F = (nombreDaysEntreDateF1XlsEtToday/15);
    		 System.out.println(F);
    		if(nombreDaysEntreDateF1XlsEtToday%15 != 0){
    			 F=F+1;
    			 double ft = F%nombreCycleFTotal;
    			 if(ft == 0.0){
    				 ft=1.0;
    			 }
    			 numeroCycleFDeLaSemaineActuelle  = (int) ft;
    			 System.out.println(numeroCycleFDeLaSemaineActuelle);
    		}else{
    			numeroCycleFDeLaSemaineActuelle  = (int)F;
    			System.out.println(numeroCycleFDeLaSemaineActuelle+"ppp");
    		}
    	}
        
    	 public int initNombreDeCycleFTotal(){
           	while(true){
           		File fichier = new File(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/")+"/modelesExcel/v0/F"+(nombreCycleFTotal+1)+".xlsx"); 
           		if( !fichier.exists() ) {
           			break;
           		}
           		nombreCycleFTotal++;
           	}
           	return nombreCycleFTotal;
         }  

         public void setListConstraint(int row, int col, String[] liste){
         	worksheet = spreadsheet.getSelectedSheet();    	
         	XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet)worksheet);
         	XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)dvHelper.createExplicitListConstraint(liste);
       	  	CellRangeAddressList addressList = new CellRangeAddressList(row, row, col, col);
       	  	XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList);
       	  	validation.setShowErrorBox(true);
       	  	worksheet.addValidationData(validation);
         }
        public String getfilePath(){
        	
        	 
	   	     return "c:/planningsExcel/"+getfileName();
        }
        
        public String getfileName(){
        	
       	 SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("ddMMyyyy");
	   	     StringBuilder dateeMMDDYYYY = new StringBuilder( dateformatMMDDYYYY.format(planning.getDateDebut() ) );
	   	     return "planning-"+dateeMMDDYYYY+"-"+planning.getVersion()+".xlsx";
       }
         
         public void  onClick$save(){
        	 boolean estOk=true;
        	 loadGrandeCaseArray();
        	 if(planning == null){
        		 planning = new Planning();
        		 planning.setVersionCycle(1);
        	 }
    		 planning.setDateDebut(dateDepartCycleAffiche);
    		 planning.setDateFin(dateFinCycleAffiche);
    		 planning.setDateCreation( GregorianCalendar.getInstance().getTime());
        	 planning.setVersion( planning.getVersion()+1);
        	// planning.setWorksheet( worksheet);
        	 
        	 
        	 Exporter exporter = Exporters.getExporter("excel");
             FileOutputStream outputStream = null;
             try {
            	//Executions.getCurrent().getDesktop().getWebApp().getRealPath("/")+
            	 File fichier = new File(getfilePath());
                 outputStream = new FileOutputStream(fichier);
                 exporter.export(spreadsheet.getBook(), outputStream);
   
        	 
        	 String erreurMessage ="Planning Non Sauvegardé. Salarié(s) absent(s) des effectifs : ";
        	 Collection<Affectation> affectations = new ArrayList<Affectation>();
        	 for(int jj=0; jj < nombreDeJourParCycle; jj++){
         		int tt=0;
        		 	for(Stationnement parking : parkings){
 	       			GrandeCase grandeCase2 = grandCaseArray[jj][tt];
 	       			int cp=0;
	 	       			for( CasePoste caseposte  : grandeCase2.getArrayCase()){
	 	       				cp++;
	 	       				//System.out.println("col:"+tt+"-Parking"+parking.getNom()+"-casePoste:"+cp+"date debut"+caseposte.getDateDebut());
	 	       				Employe employe = null;
		       				Employe employeRemp = null;
		       				if(caseposte.getDateDebut() != null && !caseposte.getNomTitulaire().equalsIgnoreCase("")  ){
		       					//System.out.println(caseposte.getNomTitulaire());
			       				 for(Employe emp :employes){
			       					if(emp.getNom().trim().equalsIgnoreCase(caseposte.getNomTitulaire().trim()) && emp.getPrenom().trim().equalsIgnoreCase(caseposte.getPrenomTitulaire().trim())){
			       						
			       					//	System.out.println( emp.getPrenom().trim()+ "/j/"+caseposte.getPrenomTitulaire().trim());
			       						employe =emp; 
			       					}
			       					if(emp.getNom().trim().equalsIgnoreCase(caseposte.getNomRemplacant().trim()) && emp.getPrenom().trim().equalsIgnoreCase(caseposte.getPrenomRemplacant().trim()) ){
			       						System.out.println( emp.getPrenom().trim()+ "///"+ caseposte.getPrenomRemplacant().trim());
			       						employeRemp =emp; 
			       					}
			       				 } 
			       				if(!caseposte.getNomTitulaire().trim().equalsIgnoreCase("AES")){
				       				if(employe == null && employeRemp == null ){
				       					estOk=false;
				       					erreurMessage =  erreurMessage+"/"+caseposte.getNomTitulaire().trim();
				       				}else{
					       				Affectation affectation;
				 	       				if(employe != null){
					       				 affectation = new Affectation();
				 	       				 affectation.setDateDebut(caseposte.getDateDebut());
				 	       				 affectation.setDateFin(caseposte.getDateFin());
				 	       				 affectation.setMotif(caseposte.getMotif());
				 	       				 affectation.setAbsent(!caseposte.getMotif().trim().equalsIgnoreCase(""));
				 	       				 affectation.setEmploye(employe);
				 	       				 affectation.setEmployeRemplacent(employeRemp);
				 	       				 affectation.setLieu(parking);
				 	       				 affectations.add(affectation);
				 	       				//System.out.println("setaff:"+affectation.getEmploye().getNom()+"-"+affectation.getDateDebut()+affectation.getLieu().getNom()); 
				 	       				}
				 	       				
				       				}
			       				}
		       				}
	 	       			}
 	       			tt++;
        		 	}
        	 }
        	 if(estOk){
	        	 planning.setAffectation(affectations);
	        	 ObjectContainer db = pool.MyServletContextListener.db2;
				 try{
					 db.store(planning);
				 }finally {
					 db.commit();
				 }
        	 }else{
        		 erreurMsg = (Label)modalDialogErreur.getFellow("erreurMsg");
        		 erreurMsg.setValue(erreurMessage);
        		 modalDialogErreur.setVisible(true);
        	 }
             } catch (FileNotFoundException e) {
            	 System.out.println(e);
                 
             } finally {
                 if (outputStream != null) {
                     try {
                         outputStream.close();
                     } catch (IOException e) {
                    	 System.out.println(e);
                     }
                 }
             }
             afficheversionPlanning();
        //	 getPlanning();
        //	 update();
         }
         
        public void onClick$suiv(){
        	if(cycleAffiche < nombreCycleFTotal){
        		cycleAffiche = cycleAffiche+1;
        	}else{
        		cycleAffiche=1;
        	}
    		Calendar calendar = GregorianCalendar.getInstance();
    		calendar.setTime(dateDepartCycleAffiche);
    		calendar.add(Calendar.DATE, nombreDeJourParCycle);
    		dateDepartCycleAffiche = calendar.getTime();
    		calendar = GregorianCalendar.getInstance();
      		calendar.setTime(dateFinCycleAffiche);
      		calendar.add(Calendar.DATE, +(nombreDeJourParCycle-1));
      		dateFinCycleAffiche = calendar.getTime();
    		numeroSemaineDepartCycleAffiche = numeroSemaineDepartCycleAffiche+2;
        	loadSpreadSheet();
        }
        
        public void onClick$prec(){
        	if(cycleAffiche!=1){
        		cycleAffiche = cycleAffiche-1;
            }else{
            	cycleAffiche=nombreCycleFTotal ;
            }
        	Calendar calendar = GregorianCalendar.getInstance();
    		calendar.setTime(dateDepartCycleAffiche);
    		calendar.add(Calendar.DATE, -nombreDeJourParCycle);
    		dateDepartCycleAffiche = calendar.getTime();
    		calendar = GregorianCalendar.getInstance();
      		calendar.setTime(dateFinCycleAffiche);
      		calendar.add(Calendar.DATE, -(nombreDeJourParCycle-1));
      		dateFinCycleAffiche = calendar.getTime();
    		numeroSemaineDepartCycleAffiche = numeroSemaineDepartCycleAffiche-2;
    		loadSpreadSheet();
        }
        
        public void loadSpreadSheet(){	
        	getPlanning();
        	
        	spreadsheet.removeEditorFocus("spreadsheet");
        	
        	final Importer importer = Importers.getImporter("excel");
        	final InputStream is2;
        	final Book book;
        	if(planning == null){
        	
        		 is2 = Sessions.getCurrent().getWebApp().getResourceAsStream("/modelesExcel/v0/F"+cycleAffiche+".xlsx"); 
        		 book = importer.imports(is2, "F"+cycleAffiche+".xlsx");
        		 
        		// spreadsheet = new Spreadsheet();
                 spreadsheet.setBook(book);
        		 
        	}else{
        	//	is2 = Sessions.getCurrent().getWebApp().getResourceAsStream("/"+getfilePath());
        		
        		File f =new File(getfilePath());
        		try {
					is2 = new FileInputStream(f);
					book = importer.imports(is2, getfileName());
					spreadsheet.setBook(book);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        	}
            
        	
        	
        	
    		worksheet = spreadsheet.getSelectedSheet();
    		
    		afficheversionPlanning();
    		
    		setDatesOnXls();
    		spreadsheet.applyProperties();
    		
    		grandCaseArray = new GrandeCase [nombreDeJourParCycle] [parkings.size()];
    		loadGrandeCaseArray();
    	//	getPlanning();
    		afficheferie();
    		
    		/**/
    //		initConstraint();
    		modalDialog.setVisible(false);
    		
        }
        /*
        public void update(){
        	 boolean estOk=true;
             
        	 if(planning != null){
   
      //  	 Collection<Affectation> affectations = new ArrayList<Affectation>();
        	 for(int jj=0; jj < nombreDeJourParCycle; jj++){
         		int tt=0;
        		 	for(Stationnement parking : parkings){
 	       			GrandeCase grandeCase2 = grandCaseArray[jj][tt];
 	       			Calendar currDtCal = GregorianCalendar.getInstance();
 	       		currDtCal.setTime(grandeCase2.getDate());
 	   	    // 	Zero out the hour, minute, second, and millisecond
 	   	    	currDtCal.set(Calendar.HOUR_OF_DAY, 0);
 	   	    	currDtCal.set(Calendar.MINUTE, 0);
 	   	    	currDtCal.set(Calendar.SECOND, 0);
 	   	    	currDtCal.set(Calendar.MILLISECOND, 0);
 	   	    	grandeCase2.setDate(currDtCal.getTime());
 	       			//grandeCase2.setDate(date);
 	       			int i=0;
 	      // 		System.out.println(grandeCase2.getDate()+"//"+parking.getNom());
 	       			//boolean isFini=false;
 	       			//while(grandeCase2.getArrayCase().length > i || isFini){
 	       				for(Affectation aff :planning.getAffectation()){
 	       				
 	       				Calendar calendar = GregorianCalendar.getInstance();
 	       				calendar.setTime(grandeCase2.getDate());
 	         			calendar.add(Calendar.DATE, +1);
 	         			
 	         		//	System.out.println(aff.getEmploye().getNom()+"ddd" + aff.getDateDebut() +"///"+ grandeCase2.getDate()+"voo "+calendar.getTime()+"//"+parking.getNom()+"-"+aff.getLieu().getNom());
 	       					if(aff.getDateDebut().after(grandeCase2.getDate()) && aff.getDateDebut().before(calendar.getTime()) && parking == aff.getLieu()){
 	       				
 	       		//		System.out.println(aff.getEmploye().getNom()+"ddd" + aff.getDateDebut() +"///"+ grandeCase2.getDate()+"voo "+calendar.getTime()+"//"+parking.getNom()+"-"+aff.getLieu().getNom());
 	       			//		((CasePoste)grandeCase2.getArrayCase()[i]).setNomTitulaire(aff.getEmploye().getNom());
 	       			//		((CasePoste)grandeCase2.getArrayCase()[i]).setNomRemplacant(aff.getEmploye().getNom());
 	       				//int u = ((CasePoste)grandeCase2.getArrayCase()[i]).getRow();
 	       				//int v = ((CasePoste)grandeCase2.getArrayCase()[i]).getCol();
 	       			int u =	((Cell)((CasePoste)grandeCase2.getArrayCase()[i]).getCellNomTitulaire()).getRowIndex();
 	       			int v =	((Cell)((CasePoste)grandeCase2.getArrayCase()[i]).getCellNomTitulaire()).getColumnIndex();
 	       			//	int u =	((Cell)((CasePoste)grandeCase2.getArrayCase()[i]).getArrayCell()[1]).getRowIndex();
 	       			//	int v =((Cell)((CasePoste)grandeCase2.getArrayCase()[i]).getArrayCell()[1]).getColumnIndex();
 	       			//currentCell = Utils.getCell(worksheet, j+2,k+nbPark);
 	       				
 	       			Utils.setEditText(worksheet, u+1, v, aff.getEmploye().getNom());
 	       						i++;
 	       					}
 	       				}
 	       			//}
 	       			
 	       			
 	       			
 	       			tt++;
        		 	}

        	 }
        	 }
        }
        */
		public void initEmployeMobile(){
			 ObjectContainer db = pool.MyServletContextListener.db2;
			 try{
				 ObjectSet result5=db.query(EmployeMobile.class);
				 employeMobiles = result5;
			 }finally {
				 db.commit();
			 }
		}
		
		public void initEmploye(){
			 ObjectContainer db = pool.MyServletContextListener.db2;
			 try{
				 ObjectSet result=db.query(Employe.class);
				 employes= result;
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
      /*  
		public String[] getNomEmployeMobiles(){
			 String[] nomEmployeMobile = new String[employeMobiles.size()] ;
        	 int y = 0;
        	 for(EmployeMobile emp : employeMobiles){
        		 nomEmployeMobile[y] = emp.getNom(); 
        		 y++;
        	 }
			return nomEmployeMobile;
		}
		*/
		/*
		public String[] getNomEmployePosts(){
			 String[] nomEmployeMobile = new String[employeMobiles.size()] ;
       	 int y = 0;
       	 for(EmployeMobile emp : employeMobiles){
       		 nomEmployeMobile[y] = emp.getNom(); 
       		 y++;
       	 }
			return nomEmployeMobile;
		}*/
		/*
        public void initConstraint(){
        	 int nbPark = 0;
        	 worksheet = spreadsheet.getSelectedSheet();
        	 for(Stationnement parking : parkings){
				 int j=hauteurEnteteEnCellule-1;
				 int k=largeurEnteteEnCellule;
				 for(int jj=0; jj < nombreDeJourParCycle; jj++){
					 currentCell = Utils.getCell(worksheet, j+2,k+nbPark);
					 if(currentCell != null ){
						 if( currentCell.getStringCellValue().trim().equalsIgnoreCase("AES")){
							 setListConstraint(j+2,k+nbPark,getNomEmployeMobiles());
						 }
					 }
					 setListConstraint(j+3,k+nbPark,getNomEmployeMobiles());
					 currentCell = Utils.getCell(worksheet, j+2,k+nbPark+1);
					 if(currentCell != null ){
						 if( currentCell.getStringCellValue().trim().equalsIgnoreCase("AES")){
							 setListConstraint(j+2,k+nbPark+1,getNomEmployeMobiles());
						 }
					}
					 setListConstraint(j+5,k+nbPark,getNomEmployeMobiles());
					 setListConstraint(j+6,k+nbPark,getNomEmployeMobiles());
					 setListConstraint(j+5,k+nbPark+1,getNomEmployeMobiles());
					 setListConstraint(j+6,k+nbPark+1,getNomEmployeMobiles());
					 
					 j=j+GrandeCase.hauteur;
				 }
				 nbPark=nbPark+2;
        	 }
        }
            */
        public void loadGrandeCaseArray(){
        	for(int nbjour=0; nbjour < nombreDeJourParCycle; nbjour++){
        		int nbparking=0;
        		Calendar calendar = GregorianCalendar.getInstance();
    			calendar.setTime(dateDepartCycleAffiche);
    			calendar.add(Calendar.DATE, nbjour);
       		 	for(Stationnement parking : parkings){
	       			GrandeCase grandeCase = grandCaseArray[nbjour][nbparking];
	       			grandeCase = new GrandeCase(nbjour,nbparking,hauteurEnteteEnCellule,largeurEnteteEnCellule, worksheet, calendar.getTime());
	       			grandCaseArray[nbjour][nbparking] = grandeCase;
	       			nbparking++;
       		 	}
        	}
        }
     
        public void changeCellColor(Range range, String color){
        	Range currentRange = range;
    		org.zkoss.poi.ss.usermodel.CellStyle cellStyle  = currentCell.getCellStyle();
    		org.zkoss.poi.ss.usermodel.CellStyle newStyle =  book.createCellStyle();
   		    newStyle.cloneStyleFrom( cellStyle);
   		    newStyle.setFillPattern(newStyle.SOLID_FOREGROUND);
    		Color newColor =  BookHelper.HTMLToColor(book, color);
    		BookHelper.setFillForegroundColor((org.zkoss.poi.ss.usermodel.CellStyle) newStyle, newColor);
    		currentRange.setStyle((org.zkoss.poi.ss.usermodel.CellStyle)newStyle);
        }
        
        public void onStopEditing$spreadsheet(StopEditingEvent event) {
 			String gg= ((String)event.getEditingValue());
 			changeColorCase( gg);
 			currentCell = Utils.getCell(worksheet,event.getRow(),event.getColumn());
        }
        
        public void changeColorCase(String gg){
        	 worksheet = spreadsheet.getSelectedSheet();
        	 book = spreadsheet.getBook();
 			
        	 
        	 
        	if(gg.equalsIgnoreCase("NON REMPLACE")){
        		changeCellColor(Ranges.range(worksheet, currentCell.getRowIndex(), currentCell.getColumnIndex(),
        				currentCell.getRowIndex(),	currentCell.getColumnIndex()) ,"#FFFF00");
        	}
        	if(gg.equalsIgnoreCase("Ferie")){
        		changeCellColor( Ranges.range(worksheet, currentCell.getRowIndex()-4, 1,
        				currentCell.getRowIndex()-4,	800 ), "#FFCCCC");
        		changeCellColor( Ranges.range(worksheet, currentCell.getRowIndex()-1, 1,
        				currentCell.getRowIndex()-1,	800 ), "#FFCCCC");
            		changeCellColor(Ranges.range(worksheet, currentCell.getRowIndex(), currentCell.getColumnIndex(),
            				currentCell.getRowIndex(),	currentCell.getColumnIndex()) ,"#FFCCCC");
        	}
        	if(gg.equalsIgnoreCase("Ouvrable")){
        		changeCellColor(Ranges.range(worksheet, currentCell.getRowIndex()-4, 1,
        				currentCell.getRowIndex()-4,	800 ), "#FFFFFF");
        		changeCellColor(Ranges.range(worksheet, currentCell.getRowIndex()-1, 1,
        				currentCell.getRowIndex()-1,	800 ), "#FFFFFF");
        		changeCellColor(Ranges.range(worksheet, currentCell.getRowIndex(), currentCell.getColumnIndex(),
        				currentCell.getRowIndex(),	currentCell.getColumnIndex()) ,"#FFFFFF");
        	}
       
        	for (EmployeMobile employeMobil : employeMobiles){
        		if(gg.equalsIgnoreCase(employeMobil.getNom().trim())){
        			String color = "#FFA500";
        			try{
        				Interimaire interimaire = (Interimaire)employeMobil; 
        				color = "#ADD8E6";
        			}catch(Exception e){
        				System.out.println(e.toString());
        			}
        			changeCellColor(Ranges.range(worksheet,  currentCell.getRowIndex(), currentCell.getColumnIndex(),
       					 currentCell.getRowIndex(),  currentCell.getColumnIndex()),color);
        		}
        	}
        	if(gg.equalsIgnoreCase("AES")){
        		changeCellColor(Ranges.range(worksheet, currentCell.getRowIndex(), currentCell.getColumnIndex(),
        				currentCell.getRowIndex(),	currentCell.getColumnIndex()) , "#92D050");
        	}
        	
        }
        
        public boolean isEmployeMobile(String nom){
        	boolean isEmployeMobile = false;
        	 String[] nomPrenomEmployeMobile = new String[employeMobiles.size()] ;
        	 int y = 0;
        	 for(EmployeMobile emp : employeMobiles){
        	//	 System.out.println(emp.getNom()+"---");
        		 nomPrenomEmployeMobile[y] = emp.getNom()+"."+emp.getPrenom();
        		 if(emp.getNom().trim().equalsIgnoreCase(nom.trim())){
    				 isEmployeMobile =true;
        		 }
        		 y++;
        	 }
        	return isEmployeMobile;
        }
        
        public void onCellClick$spreadsheet(CellMouseEvent event) {
        	int r= ((Spreadsheet)event.getTarget()).getCellFocus().getRow();
        	int c =((Spreadsheet)event.getTarget()).getCellFocus().getColumn();
        	currentCell = Utils.getCell(worksheet,r ,c);
        	boolean isClose =true;
        /*
        	for(int jj=0; jj < nombreDeJourParCycle; jj++){
        		int tt=0;
        		for(Stationnement parking : parkings){
		        	for( CasePoste casePosteD :  grandCaseArray[jj][tt].getArrayCase()){
		        		
		        		casePosteSelected = casePosteD;
		        		
		        	}
        		}
        	}
        	*/
        	int e=c;
        	while(e%2 !=0){
        		e++;
        	}
        	e= (e/2)-1;
        	int z = r-hauteurEnteteEnCellule;
        	while(z%6 !=0){
        		z++;
        	}
        	z=(z/6)-1;
        	
        	CasePoste[] casePostes =  grandCaseArray[z][e].getArrayCase();
        	
        	CasePoste seleCasePoste;
        	r = (r-hauteurEnteteEnCellule)%6;
        	c = (c-largeurEnteteEnCellule);
        	
     
        	if(  r == 2 ){
        		if((c%2)==0){
        			casePosteSelected = grandCaseArray[z][e].getArrayCase()[0];
        		}else{
        			casePosteSelected = grandCaseArray[z][e].getArrayCase()[1];
        		}
        	}else if(r==5){
        		if((c%2)==0){
        			casePosteSelected = grandCaseArray[z][e].getArrayCase()[2];
        		}else{
        			casePosteSelected = grandCaseArray[z][e].getArrayCase()[3];
        		}
        	}
        //	casePosteSelected.loadCell(worksheet, casePosteSelected.getDateDebut() );
        //	System.out.println(r);
        	
        	if(r == 1 || r==4 || r == 2 || r ==5){
        		
        	}
        	
        	Combobox comboMotif = (Combobox)modalDialog.getFellow("comboMotif");
    		Label labelMotif = (Label)modalDialog.getFellow("motif");
    		Label label = (Label)modalDialog.getFellow("label");
    		Combobox combo = (Combobox)modalDialog.getFellow("combo");
        	if(r == 2 || r ==5){
        //		System.out.println("remp");
        		
        		modalDialog.setTitle("Remplacant");
    			comboMotif.setVisible(true);
    			
    			labelMotif.setVisible(true);
    			label.setValue("Remplacant :");
    			
    			 String[] nomPrenomEmployeMobile = new String[employeMobiles.size()] ;
    	    	 int yy = 0;
    	    	 for(EmployeMobile emp : employeMobiles){
    	    		 nomPrenomEmployeMobile[yy] = emp.getNom()+"."+emp.getPrenom();
    	    		 yy++;
    	    	 }
    	    	 ListModelList lm = new ListModelList(nomPrenomEmployeMobile);
     	        combo.setModel(lm);
    			comboMotif.setValue(casePosteSelected.getMotif());
    			//combo.setValue(cellNomRemplacant.getStringCellValue());
        	}
        	if(r == 1 || r==4){
        	//	System.out.println("titulaire");
        		
        		 String[] nomPrenomEmployPoste = new String[employesPoste.size()] ;
            	 int uy = 0;
            	 for(SalariePoste emp : employesPoste){
            		 nomPrenomEmployPoste[uy] = emp.getNom()+"."+emp.getPrenom();
            		 uy++;
            	 }
            	 modalDialog.setTitle("Titulaire Poste");
            	 ListModelList lm = new ListModelList(nomPrenomEmployPoste);
        	        combo.setModel(lm);
     			labelMotif.setVisible(false);
     			comboMotif.setVisible(false);
     			label.setValue("Titulaire :");
        		
        		if( (currentCell.getStringCellValue().equalsIgnoreCase("AES") || isEmployeMobile(currentCell.getStringCellValue()) || currentCell.getStringCellValue().equalsIgnoreCase(""))){
        			 String[] nomPrenomEmployeMobile = new String[employeMobiles.size()] ;
        	    	 int yy = 0;
        	    	 for(EmployeMobile emp : employeMobiles){
        	    		 nomPrenomEmployeMobile[yy] = emp.getNom()+"."+emp.getPrenom();
        	    		 yy++;
        	    	 }
        			modalDialog.setTitle("Titulaire Mobile");
        			lm = new ListModelList(nomPrenomEmployeMobile);
           	        combo.setModel(lm);
        		//	combo.setValue(cellNomTitulaire.getStringCellValue());
      
        			label.setValue("Titulaire Mobile :");
        		}
        	}
        	if(r == 1 || r==4 || r == 2 || r ==5){
        		
            	
    			String g = event.getPageX()+"px";
            	String h = event.getPageY()+"px";
            	modalDialog.setLeft(g);
            	modalDialog.setTop(h);
            	modalDialog.setVisible(true);
            	isClose=false;
            }
        	
        	
        	
		        		/*if(casePosteSelected.isCasePosteEmploye(r,c, employeMobiles, employesPoste, modalDialog)){
		
        	
		        	//		casePosteSelected = casePosteD;
        	
		        			String g = event.getPageX()+"px";
		                	String h = event.getPageY()+"px";
		                	modalDialog.setLeft(g);
		                	modalDialog.setTop(h);
		                	modalDialog.setVisible(true);
		                	isClose=false;
		        		//	break;
		        		}
		        		*/
		        //	}
		  //      	tt++;
       		// 	}
       	//	}
        	if(isClose){
        		modalDialog.setVisible(!isClose);
        	}
        }
        
        public void onClick$print(){
        	   Book wb = spreadsheet.getBook();
        	    Exporter c = Exporters.getExporter("excel");
        	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	    c.export(wb, baos);
        	    Filedownload.save(baos.toByteArray(), "application/file",
        	            wb.getBookName());
        	/*
        	String outputFilePath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + ".pdf";
        	Exporter c = new PdfExporter();
            ((PdfExporter)c).enableHeadings(true);
            try{
	            OutputStream os = new java.io.FileOutputStream(outputFilePath);
	            PrintSetup ps  = (book.getSheet(worksheet.getSheetName())).getPrintSetup();
	            ps.setLandscape(true);
	            ps.setFitWidth((short)1);
	            ps.setFitHeight((short)1);
	            ps.setFooterMargin(0.0);
	            ps.setScale((short)10);
	            c.export(book, os);
	            Files.close(os);
	            final InputStream mediais = new FileInputStream(new File(outputFilePath));
	            final AMedia amedia = new AMedia("generatedReport.pdf", "pdf", "application/pdf", mediais);
	            report.setContent(amedia);
            }catch(Exception e){
            	System.out.print(e.toString());
            }
            */
        }
}