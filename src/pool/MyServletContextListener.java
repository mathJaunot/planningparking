package pool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;



	public class MyServletContextListener implements ServletContextListener {
	    public void contextInitialized(ServletContextEvent sce) {
	    	System.out.println("contextinit");
	    	  //String DB4OFILENAME = System.getProperty("user.home")  + "/test.db4o.yap";
	    	String DB4OFILENAME = "c:/test.db4o.yap";
	           db2 = Db4oEmbedded.openFile(Db4oEmbedded
	                  .newConfiguration(), DB4OFILENAME);
	        // initialize and startup whatever you need here
	    }
	    public static ObjectContainer db2;
	    public void contextDestroyed(ServletContextEvent sce) {
	    	System.out.println("contextdestr");
	    	db2.close();
	        // shutdown and destroy those things here
	    }
	}

