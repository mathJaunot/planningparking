package util;

import java.util.Calendar;
import java.util.Date;

public abstract class Case {
	
	public int row=0;
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	public  int col=0;
	
	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	private Date dateDebut;
	private Date dateFin; 
	private String[] s2 = new String[2];
	private String[] s3 = new String[2];

	public void setDate(Date date, String sHeureDebutEtFin){
		 
		dateDebut = date;
		dateFin = date;
		String[] s = sHeureDebutEtFin.split("/");
		
		s2 = s[0].split("h");
		s3 = s[1].split("h");
		dateDebut.setHours(Integer.parseInt(s2[0]));
		dateDebut.setMinutes(Integer.parseInt(s2[1]));
		dateDebut.setSeconds(0);
		
		dateFin.setHours(Integer.parseInt(s3[0]));
		dateFin.setMinutes(Integer.parseInt(s3[1]));
		dateFin.setSeconds(0);

		if(dateDebut.after(dateFin)){
			Calendar calendar = Calendar.getInstance();
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
	
}
