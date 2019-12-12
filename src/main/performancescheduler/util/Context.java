package performancescheduler.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import performancescheduler.data.Auditorium;

public class Context {
    public static final String CONFIG_FILE = "perfsched.conf";
    public static final String PERFSCHED_DIR = ".perfsched";
    
    private static Properties presentContext;
    static {
        String filename = String.format("%s%s%s%s%s", System.getProperty("user.home"), 
                System.getProperty("file.separator"), PERFSCHED_DIR, System.getProperty("file.separator"), CONFIG_FILE);
        System.out.println("[DEBUG] finding properties file: " + filename);
        presentContext = new Properties();
        try (FileInputStream fis = new FileInputStream(filename)) {
            presentContext.load(fis);
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
}
