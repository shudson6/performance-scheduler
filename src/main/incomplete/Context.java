package main.incomplete;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Context {
    public static final String CONFIG_FILE = "perfsched.conf";
    public static final String PERFSCHED_DIR = ".perfsched";
    
    private static Properties presentContext;
    static {
        String filename = String.format("%s%s%s%s%s", System.getProperty("user.home"), 
                System.getProperty("file.separator"), PERFSCHED_DIR, System.getProperty("file.separator"), CONFIG_FILE);
        System.out.println("[DEBUG] finding properties file: " + filename);
        presentContext = new Properties();
        try {
            presentContext.load(Context.class.getResourceAsStream("perfsched_default.conf"));
            System.out.println("[DEBUG] loaded properties. Db user: " + presentContext.getProperty("DATABASE_USER"));
        } catch (IOException ex) {
            System.exit(1);
        }
    }
    
    public static Properties getPresentContext() {
        return presentContext;
    }
}
