
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

public class GererEmployeurs extends GenericForwardComposer {

    private Cell currentCell;
    private Spreadsheet spreadsheet;
    private Worksheet worksheet;
    
    private Collection<Employeur> employeurs; 
   
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		worksheet = spreadsheet.getSelectedSheet();
		//currentCell = Utils.getCell(worksheet, 0,0);
		loadEmployeur();
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
	
	public void affiche(){
		int i = 1;
		for(Employeur employeur : employeurs){
			Utils.setCellValue(worksheet, i, 0, employeur.getNom());
			Utils.setCellValue(worksheet, i, 1, employeur.getSiret());
			i++;
		}
	}
	
	public void  onClick$save(){
		ObjectContainer db = pool.MyServletContextListener.db2;
		employeurs = new ArrayList<Employeur>();
		 try {
			 int i = 1;
			 while(Utils.getCell(worksheet, i,0) != null){
				 Employeur employeur = new Employeur(); 
				 employeur.setNom(((Cell)Utils.getCell(worksheet, i,0)).getStringCellValue().trim());
				 if(((Cell)Utils.getCell(worksheet, i,1)) != null){
					 employeur.setSiret(((Cell)Utils.getCell(worksheet, i,1)).getStringCellValue().trim());
				 }else{
					 employeur.setSiret("");
				 }
				 System.out.println(employeur.getNom());
				 employeurs.add(employeur);
				i++;
			}
			ObjectSet result2=db.queryByExample(Employeur.class);
			while(result2.hasNext()) {
				db.delete(result2.next());
			}
			db.commit();
			db.store(employeurs);
		 }
		 finally {
			 db.commit();
		 }
	}
}
