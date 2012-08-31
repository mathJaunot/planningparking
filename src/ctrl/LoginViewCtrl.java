package ctrl;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;


public class LoginViewCtrl extends GenericForwardComposer {
    
    public void onClick$confirmBtn(){
    	Executions.sendRedirect("/saccueilPl.zul");
    }
}