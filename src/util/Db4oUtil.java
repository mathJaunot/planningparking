package util;

import java.io.File;

import com.db4o.*;

public class Db4oUtil {
	  // EDIT THESE SETTINGS
    private static final String YAPFILENAME = "test.db4o.yap";
    private static final int PORT = 0;
    /*
     If you want the server to be networked, change the port number above and uncomment the USER, PASSWORD lines below/
     Then in getObjectServer, uncomment the objectServer.grantAccess line
     */
    //private static final String USER = "username";
    //private static final String PASSWORD = "password";
    
    private static ObjectServer objectServer;

    private static final ThreadLocal dbThreadLocal = new ThreadLocal();

    public static ObjectContainer getObjectContainer() {
        ObjectContainer oc = (ObjectContainer) dbThreadLocal.get();
        if (oc == null || oc.ext().isClosed()) {
            oc = getObjectServer().openClient();
            dbThreadLocal.set(oc);
        }
        return oc;
    }

    public static void closeObjectContainer() {
        ObjectContainer oc = (ObjectContainer) dbThreadLocal.get();
        dbThreadLocal.set(null);
        if (oc != null) oc.close();
    }

    public synchronized static ObjectServer getObjectServer() {
        if (objectServer == null) {
            objectServer = getObjectServerForFilename(YAPFILENAME, PORT);
            // and give access
            //objectServer.grantAccess(USER, PASSWORD);
        }
        return objectServer;
    }


    public static void shutdown() {
        if (objectServer != null) {
            objectServer.close();
        }
    }

    public static ObjectServer getObjectServerForFilename(String yapfilename, int port) {
        File parentDir = getDbDirectory();
        File dbfile = new File(parentDir, yapfilename);

        // for replication //////////////////////////
   //     Db4o.configure().generateUUIDs(Integer.MAX_VALUE);
     //   Db4o.configure().generateVersionNumbers(Integer.MAX_VALUE);

        // other options
        Db4o.configure().exceptionsOnNotStorable(true);
        Db4o.configure().objectClass("java.math.BigDecimal").translate(new com.db4o.config.TSerializable());

        // now open server
        ObjectServer objectServer = Db4o.openServer(dbfile.getPath(), port);

        return objectServer;
    }

    private static File getDbDirectory() {
        // will store data files in {user.home}/db4o/data directory
        String dbfile = System.getProperty("user.home") + "/db4o/data";
        File f = new File(dbfile);
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }
}