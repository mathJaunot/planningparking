package ctrl;



import java.text.*;
import java.util.*;
import java.util.Calendar;

import org.zkoss.calendar.*;
import org.zkoss.calendar.api.CalendarEvent;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.calendar.impl.*;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;
import org.zkoss.zul.Timer;



import bo.*;

public class CalendarComposer extends GenericForwardComposer {
	
	private static final long serialVersionUID = 201011240904L;

	private SimpleCalendarModel cm;
	private Calendars calendars;
 
	
	private Popup updateMsg;
	private	Label popupLabel;
	private Timer timer;

	
	private Window createEvent;
	private Datebox createEvent$ppbegin;
	private Datebox createEvent$ppend;
	private Listbox createEvent$ppbt;
	private Listbox createEvent$ppet;
	private Checkbox createEvent$ppallDay;
	private Checkbox createEvent$pplocked;
	private Combobox createEvent$ppcolor;
	private Textbox createEvent$ppcnt;
	
	private Window editEvent;
	private Datebox editEvent$ppbegin;
	private Datebox editEvent$ppend;
	private Listbox editEvent$ppbt;
	private Listbox editEvent$ppet;
	private Checkbox editEvent$ppallDay;
	private Checkbox editEvent$pplocked;
	private Combobox editEvent$ppcolor;
	private Textbox editEvent$ppcnt;
	
	private Listbox oko;
	private Listbox ok;
	
	
	Interimaire interimaire ;
	Declaration declaration ;
	DeclarationPlus declarationPlus ;
	Collection<Intervention> interventions = new ArrayList<Intervention>();
	

	public void onSelect$ok(SelectEvent event) {
		//System.out.println("onselect");
		cm.clear();
		Listitem item = (Listitem) new ArrayList(event.getSelectedItems()).get(0);
		 declaration= (Declaration)item.getValue();
		 
	}
	
	public void onSelect$oko(SelectEvent event) {
		//System.out.println("onselect");
		cm.clear();
		 Listitem item = (Listitem) new ArrayList(event.getSelectedItems()).get(0);
		 declarationPlus= (DeclarationPlus)item.getValue();
	}
	
	//@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		initTimeDropdown(page);
		// prepare model data
		initCalendarModel(page);
		return super.doBeforeCompose(page, parent, compInfo);
	}
	
	//@Override
	public void doAfterCompose(Component comp) throws Exception {
		System.out.println("doAfterCompose");
		super.doAfterCompose(comp);
		syncModel();

	
	}

	private void initTimeDropdown(Page page) {
		List dateTime = new LinkedList();
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		for (int i = 0; i < 288; i++) {
			dateTime.add(sdf.format(calendar.getTime()));
			calendar.add(Calendar.MINUTE, 5);
		}
		page.setAttribute("dateTime", dateTime);
	}
	
	private void initCalendarModel(Page page) {
		
		Calendar cal = Calendar.getInstance();
		cm = new SimpleCalendarModel();
		page.setAttribute("cm", cm);
	}

	private void syncModel() {
	/*
		List list = cm.get(calendars.getBeginDate(), calendars.getEndDate(), null);
		double red = 0, blue = 0, green = 0, purple = 0, khaki = 0;
		int size = list.size();
		for (Iterator it = list.iterator(); it.hasNext();) {
			String color = ((CalendarEvent)it.next()).getContentColor();
			if ("#D96666".equals(color))
				red += 1;
			else if ("#668CD9".equals(color))
				blue += 1;
			else if ("#4CB052".equals(color))
				green += 1;
			else if ("#B373B3".equals(color))
				purple += 1;
			else
				khaki += 1;
		}
		*/
	}
	

	public void onEventCreate$calendars(ForwardEvent event) {
		System.out.println("onEventCreate$calendars");
		CalendarsEvent evt = (CalendarsEvent) event.getOrigin();
		int left = evt.getX();
		int top = evt.getY();
		int timeslots = calendars.getTimeslots();
		int timeslotTime = 60 / timeslots;
		if (top + 245 > evt.getDesktopHeight())
			top = evt.getDesktopHeight() - 245;
		if (left + 410 > evt.getDesktopWidth())
			left = evt.getDesktopWidth() - 410;
		createEvent.setLeft(left + "px");
		createEvent.setTop(top + "px");
		SimpleDateFormat create_sdf = new SimpleDateFormat("HH:mm");
		create_sdf.setTimeZone(calendars.getDefaultTimeZone());
		String[] times = create_sdf.format(evt.getBeginDate()).split(":");
		int hours = Integer.parseInt(times[0]) * timeslots;
		int mins = Integer.parseInt(times[1]);
		int bdTimeSum = hours + mins;
		hours += mins / timeslotTime;
		
		createEvent$ppbt.setSelectedIndex(hours * 12 / timeslots);
		times = create_sdf.format(evt.getEndDate()).split(":");
		hours = Integer.parseInt(times[0]) * timeslots;
		mins = Integer.parseInt(times[1]);
		int edTimeSum = hours + mins;
		hours += mins / timeslotTime;
		((Listbox)createEvent.getFellow("ppet")).setSelectedIndex(hours * 12 / timeslots);
		boolean isAllday = (bdTimeSum + edTimeSum) == 0;
		
		createEvent$ppbegin.setTimeZone(calendars.getDefaultTimeZone());
		createEvent$ppbegin.setValue(evt.getBeginDate());
		createEvent$ppend.setTimeZone(calendars.getDefaultTimeZone());
		createEvent$ppend.setValue(evt.getEndDate());
		createEvent$ppallDay.setChecked(isAllday);
		createEvent$pplocked.setChecked(false);
		createEvent$ppbt.setVisible(!isAllday);
		createEvent$ppet.setVisible(!isAllday);

		createEvent.setVisible(true);
		createEvent.setAttribute("calevent", evt);
		evt.stopClearGhost();
	}
	
	public void onClose$createEvent(ForwardEvent event) {
		System.out.println("onClose$createEvent");
		event.getOrigin().stopPropagation();
		((CalendarsEvent)createEvent.getAttribute("calevent")).clearGhost();
		createEvent.setVisible(false);
	}
	
	public void onClick$okBtn$createEvent(ForwardEvent event) {
		System.out.println("onClick$okBtn$createEvent");
		SimpleCalendarEvent ce = new SimpleCalendarEvent();
		Calendar cal = Calendar.getInstance(calendars.getDefaultTimeZone());
		Date beginDate = createEvent$ppbegin.getValue();
		Date endDate = createEvent$ppend.getValue();
		
		int bmin = 0;
		int emin = 0;
		if (!createEvent$ppallDay.isChecked()) {
			String[] times = createEvent$ppbt.getSelectedItem().getLabel().split(":");
			
			cal.setTime(beginDate);
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(times[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			beginDate = cal.getTime();
			bmin = cal.get(Calendar.MINUTE);
			times = createEvent$ppet.getSelectedItem().getLabel().split(":");
			cal.setTime(endDate);
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(times[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			endDate = cal.getTime();
			emin = cal.get(Calendar.MINUTE);
		}
		

		if (!beginDate.before(endDate)) {
			createEvent.setVisible(false);
			alert("The end date cannot be earlier than or equal to begin date!");
			((CalendarsEvent)createEvent.getAttribute("calevent")).clearGhost();
			return;
		}
		if (bmin == 5 || bmin == 25 || bmin == 35 || bmin == 55) {
			createEvent.setVisible(false);
			alert("The begin minute:" + bmin + ", is not supported");
			((CalendarsEvent)createEvent.getAttribute("calevent")).clearGhost();
			return;
		}
		if (emin == 5 || emin == 25 || emin == 35 || emin == 55) {
			createEvent.setVisible(false);
			alert("The end minute:" + emin + ", doesn't support");
			((CalendarsEvent)createEvent.getAttribute("calevent")).clearGhost();
			return;
		}
		
		String[] colors = ((String)createEvent$ppcolor.getSelectedItem().getValue()).split(",");
		ce.setHeaderColor(colors[0]);
		ce.setContentColor(colors[1]);
		ce.setBeginDate(beginDate);
		ce.setEndDate(endDate);
		ce.setContent(createEvent$ppcnt.getValue());
		ce.setLocked(createEvent$pplocked.isChecked());
		cm.add(ce);
		
		createEvent$ppcnt.setRawValue("");
		createEvent$ppbt.setSelectedIndex(0);
		createEvent$ppet.setSelectedIndex(0);
		createEvent.setVisible(false);
		
		Intervention intervention = new Intervention();
		intervention.setDateDebut(beginDate);
		intervention.setDateFin(endDate);
		intervention.setParking(null);
		Motif motif = new Motif();
		motif.setNom(createEvent$ppcnt.getValue());
		intervention.setMotif(motif);
		intervention.setDeclarationPlus(declarationPlus);
		intervention.setInterimaire(interimaire);
		interventions.add( intervention);
		//DeclarationService declarationService ;//beginDate;//= DeclarationService.getInstance();
		if(interimaire != null){
		//	Collection<Intervention> interventions = interimaire.getIntervention();
			interventions.add(intervention);
	
			
		//	interimaire.setIntervention(interventions);
			
			
			//if(interimaire.getIntervention() != null){
				
			//}
		}
	//	declarationService.save(interimaire);
		
		
		syncModel();
	}
	
	public void onClick$cancelBtn$createEvent(ForwardEvent event) {
		createEvent$ppcnt.setRawValue("");
		createEvent$ppbt.setSelectedIndex(0);
		createEvent$ppet.setSelectedIndex(0);
		createEvent.setVisible(false);
		((CalendarsEvent)createEvent.getAttribute("calevent")).clearGhost();
	}
	
	public void onEventEdit$calendars(ForwardEvent event) {
		CalendarsEvent evt = (CalendarsEvent) event.getOrigin();
		
		int left = evt.getX();
		int top = evt.getY();
		if (top + 245 > evt.getDesktopHeight())
			top = evt.getDesktopHeight() - 245;
		if (left + 410 > evt.getDesktopWidth())
			left = evt.getDesktopWidth() - 410;
		
		TimeZone tz = calendars.getDefaultTimeZone();
		
		editEvent.setLeft(left + "px");
		editEvent.setTop(top + "px");
		CalendarEvent ce = evt.getCalendarEvent();
		SimpleDateFormat edit_sdf = new SimpleDateFormat("HH:mm");
		edit_sdf.setTimeZone(tz);
		Calendar calendar = Calendar.getInstance(org.zkoss.util.Locales
				.getCurrent());
		String[] times = edit_sdf.format(ce.getBeginDate()).split(":");
		int hours = Integer.parseInt(times[0]);
		int mins = Integer.parseInt(times[1]);
		int bdTimeSum = hours + mins;
		editEvent$ppbt.setSelectedIndex(hours*12 + mins/5);
		times = edit_sdf.format(ce.getEndDate()).split(":");
		hours = Integer.parseInt(times[0]);
		mins = Integer.parseInt(times[1]);
		int edTimeSum = hours + mins;
		editEvent$ppet.setSelectedIndex(hours*12 + mins/5);
		boolean isAllday = (bdTimeSum + edTimeSum) == 0;
		editEvent$ppbegin.setTimeZone(tz);
		editEvent$ppbegin.setValue(ce.getBeginDate());
		editEvent$ppend.setTimeZone(tz);
		editEvent$ppend.setValue(ce.getEndDate());
		editEvent$ppallDay.setChecked(isAllday);
		editEvent$pplocked.setChecked(ce.isLocked());
		editEvent$ppbt.setVisible(!isAllday);
		editEvent$ppet.setVisible(!isAllday);
		editEvent$ppcnt.setValue(ce.getContent());
		String colors = ce.getHeaderColor() + "," + ce.getContentColor();
		int index = 0;
		if ("#3467CE,#668CD9".equals(colors))
			index = 1;
		else if ("#0D7813,#4CB052".equals(colors))
			index = 2;
		else if ("#88880E,#BFBF4D".equals(colors))
			index = 3;
		else if ("#7A367A,#B373B3".equals(colors))
			index = 4;
	
		switch (index) {
		case 0:
			editEvent$ppcolor.setStyle("color:#D96666;font-weight: bold;");
			break;
		case 1:
			editEvent$ppcolor.setStyle("color:#668CD9;font-weight: bold;");
			break;
		case 2:
			editEvent$ppcolor.setStyle("color:#4CB052;font-weight: bold;");
			break;
		case 3:
			editEvent$ppcolor.setStyle("color:#BFBF4D;font-weight: bold;");
			break;
		case 4:
			editEvent$ppcolor.setStyle("color:#B373B3;font-weight: bold;");
			break;
		}
		editEvent$ppcolor.setSelectedIndex(index);
		editEvent.setVisible(true);
	
		// store for the edit marco component.
		editEvent.setAttribute("ce", ce);
	}
	
	public void onClose$editEvent(ForwardEvent event) {
		event.getOrigin().stopPropagation();
		editEvent.setVisible(false);
	}
	
	public void onClick$okBtn$editEvent(ForwardEvent event) {
		SimpleCalendarEvent ce = (SimpleCalendarEvent) editEvent.getAttribute("ce");
		Calendar cal = Calendar.getInstance(calendars.getDefaultTimeZone());
		Date beginDate = editEvent$ppbegin.getValue();
		Date endDate = editEvent$ppend.getValue();
		
		int bmin = 0;
		int emin = 0;
		if (!editEvent$ppallDay.isChecked()) {
			String[] times = editEvent$ppbt.getSelectedItem().getLabel().split(":");
			cal.setTime(beginDate);
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(times[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			beginDate = cal.getTime();
			times = editEvent$ppet.getSelectedItem().getLabel().split(":");
			cal.setTime(endDate);
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(times[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			endDate = cal.getTime();
		} else {
			cal.setTime(beginDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			beginDate = cal.getTime();
			bmin = cal.get(Calendar.MINUTE);
			cal.setTime(endDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			endDate = cal.getTime();
			emin = cal.get(Calendar.MINUTE);
		}			
		if (!beginDate.before(endDate)) {
			editEvent.setVisible(false);
			alert("The end date cannot be earlier than or equal to begin date!");
			((org.zkoss.calendar.event.CalendarsEvent)editEvent.getAttribute("calevent")).clearGhost();
			return;
		}
		if (bmin == 5 || bmin == 25 || bmin == 35 || bmin ==55) {
			editEvent.setVisible(false);
			alert("The begin minute:" + bmin + ", is not supported");
			((org.zkoss.calendar.event.CalendarsEvent)editEvent.getAttribute("calevent")).clearGhost();
			return;
		}
		if (emin == 5 || emin == 25 || emin == 35 || emin ==55) {
			editEvent.setVisible(false);
			alert("The end minute:" + emin + ", doesn't support");
			((org.zkoss.calendar.event.CalendarsEvent)editEvent.getAttribute("calevent")).clearGhost();
			return;
		}
		String[] colors = ((String)editEvent$ppcolor.getSelectedItem().getValue()).split(",");
		ce.setHeaderColor(colors[0]);
		ce.setContentColor(colors[1]);
		ce.setBeginDate(beginDate);
		ce.setEndDate(endDate);
		ce.setContent(editEvent$ppcnt.getValue());
		ce.setLocked(editEvent$pplocked.isChecked());
		cm.update(ce);
		editEvent.setVisible(false);
		syncModel();
	}

	public void onClick$deleteBtn$editEvent(ForwardEvent event) throws Exception {
		Messagebox.show("Are you sure to delete the event!", "Question",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new EventListener() {
					public void onEvent(Event evt) throws Exception {
						if (((Integer) evt.getData()).intValue() != Messagebox.OK)
							return;
						cm.remove((SimpleCalendarEvent) editEvent.getAttribute("ce"));
						syncModel();
					}
				});
		editEvent.setVisible(false);
	}
	
	public void onSelect$right(SelectEvent event) {
		System.out.println("onSelect$right");
		 Listitem item = (Listitem) new ArrayList(event.getSelectedItems()).get(0);
		 interimaire= (Interimaire)item.getValue();
		 /*
		 cm = new SimpleCalendarModel();
		 if(interimaire != null ){
			  System.out.println("cfsd");
			  if(interimaire.getIntervention() != null){
				  System.out.println("rrrrr");
				  interventions = interimaire.getIntervention();
					for(Intervention intervention : interventions){
						System.out.println("ioiioioi");
						
						SimpleCalendarEvent sce = new SimpleCalendarEvent();
						sce.setBeginDate(intervention.getDateDebut());
						sce.setEndDate(intervention.getDateFin());
					 
						sce.setHeaderColor("#88880E");
						sce.setContentColor( "#BFBF4D");
						// ce.setTitle() if any, otherwise, the time stamp is assumed.
						sce.setContent("gfhfghgfhgf");
						sce.setLocked(false);
						cm.add(sce);
						
					}
			  }
		  }
		page.setAttribute("cm", cm);
		*/
		 
		 
		 SimpleDateFormat dataSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		// DeclarationPlus decplus = (DeclarationPlus)page.getAttribute("cd");
		// System.out.println(decplus.getID());
		 Calendar cal = Calendar.getInstance();
		 cm.clear();
		 if(interimaire != null ){
			System.out.print(" interimaire !=null"); 
			
			
			/*
			
			  if(interimaire.getIntervention() != null){
				  System.out.print(" interimaire.getIntervention() !=null");
				  interventions = interimaire.getIntervention();
					for(Intervention interventionii : interventions){
						System.out.print(" Intervention interventionii : interventions");
						if(interventionii.getDeclarationPlus() != null ){
							System.out.print(" interventionii.getDeclarationPlus()");	
							if((interventionii.getDeclarationPlus().getID() == declarationPlus.getID())){
								System.out.print(" interventionii.getDeclarationPlus().getID()");
								if( interventionii.getDeclarationPlus().getDeclaration().getID() ==  declaration.getID()){
									System.out.print(" interventionii.getDeclarationPlus().getDeclaration().getID() : "+interventionii.getDeclarationPlus().getDeclaration().getID());
									System.out.print(" declaration.getID()"+declaration.getID());
									System.out.print(" declarationPlus.getDeclaration().getID()");
									//if( interventionii.getDeclarationPlus().getDeclaration() != null){
									SimpleCalendarEvent sce = new SimpleCalendarEvent();
									sce.setBeginDate(interventionii.getDateDebut());
									sce.setEndDate(interventionii.getDateFin());
									sce.setHeaderColor("#88880E");
									sce.setContentColor( "#D96666");
									// ce.setTitle() if any, otherwise, the time stamp is assumed.
									//sce.setContent(intervention.getMotif().getNom());
									sce.setContent("Motif");
									sce.setLocked(false);
									cm.add(sce);
									
								}
							}
						}
					}
			  }
			  
			  
			  */
		 }
	}
	
	public void onEventUpdate$calendars(ForwardEvent event) {
		System.out.println("onEventUpdate");
		CalendarsEvent evt = (CalendarsEvent) event.getOrigin();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/d");
		sdf1.setTimeZone(calendars.getDefaultTimeZone());
		StringBuffer sb = new StringBuffer("Mise à jour... de ");
		sb.append(sdf1.format(evt.getCalendarEvent().getBeginDate()));
		sb.append(" de ");
		sb.append(sdf1.format(evt.getBeginDate()));
		popupLabel.setValue(sb.toString());
		int left = evt.getX();
		int top = evt.getY();
		if (top + 100 > evt.getDesktopHeight())
			top = evt.getDesktopHeight() - 100;
		if (left + 330 > evt.getDesktopWidth())
			left = evt.getDesktopWidth() - 330;
		updateMsg.open(left, top);
		timer.start();
		org.zkoss.calendar.Calendars cal = (org.zkoss.calendar.Calendars) evt
				.getTarget();
		SimpleCalendarModel m = (SimpleCalendarModel) cal.getModel();
		SimpleCalendarEvent sce = (SimpleCalendarEvent) evt.getCalendarEvent();
		sce.setBeginDate(evt.getBeginDate());
		sce.setEndDate(evt.getEndDate());
		m.update(sce);
	}
	
	public void onMoveDate(ForwardEvent event) {
		if ("arrow-left".equals(event.getData()))
			calendars.previousPage();
		else calendars.nextPage();
		syncModel();
	}
	
	public void onUpdateView(ForwardEvent event) {
		System.out.println("onUpdateView");
		String text = String.valueOf(event.getData());
		int days = "Day".equals(text) ? 1: "5 Days".equals(text) ? 5: "Week".equals(text) ? 7: 0;
		
		if (days > 0) {
			calendars.setMold("default");
			calendars.setDays(days);
		} else calendars.setMold("month");
	}
	
}