package performancescheduler.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import performancescheduler.data.Auditorium;

public class Context {
    public static final String CONFIG_FILE = "perfsched.conf";
    public static final String PERFSCHED_DIR = ".perfsched";
    
    private static Properties presentContext = new Properties();
    static {
        try (InputStream is = Context.class.getResourceAsStream("/perfsched_default.conf")) {
            presentContext.load(is);
            System.out.println("[DEBUG] found. properties loaded to context.");
        } catch (IOException ex) {
            System.exit(1);
        }
    }
    
    public static String getProperty(String propertyName) {
        return presentContext.getProperty(propertyName);
    }
    
    public static Auditorium getAuditorium(int number) {
        return Auditorium.getInstance(number, null, false, 1);
    }
    
    public static void setProperty(String propName, String value) {
        presentContext.setProperty(propName, value);
    }
    
    static void dumpProperties() {
        presentContext.forEach((p, v) -> System.out.format("%s = %s%n", p, v));
    }
}
