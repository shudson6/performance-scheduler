package performancescheduler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Scanner;

public final class ChecksumVerifier {    
    private static ChecksumVerifier instance = null;
    
    public static ChecksumVerifier getInstance() {
        if (instance == null) {
            instance = new ChecksumVerifier();
        }
        return instance;
    }
    
    private MessageDigest md = null;
    private Properties props = new Properties();
    
    public void displayFile(File file) {
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNext()) {
                System.out.println(scan.nextLine());
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("Crap.");
        }
    }
        
    public boolean verifyFile(File file) {
        boolean result = false;
        if (initMd()) {
            String cs = calculateChecksum(file);
            result = getChecksum(file.getName()).equals(cs);
            if (!result) {
                System.out.format("Calculated checksum %s for file %s does not match value in resources/checksum%n",
                        cs, file.getName());
                displayFile(file);
            }
        }
        return result;
    }
    
    private String calculateChecksum(File file) {
        // open the file, read it in chunks, digest it til we're done
        try (FileInputStream in = new FileInputStream(file)){
            byte[] b = new byte[1024];
            int c = 0;
            while ((c = in.read(b)) != -1) {
                md.update(b);
            }
            return checksumString(md.digest());
        } catch (FileNotFoundException e) {
            System.err.println("Failed to find resource or file " + file.getAbsolutePath());
        } catch (IOException e1) {
            System.err.println("Oops.");
        }
        return null;
    }
    
    private String checksumString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toString(Byte.toUnsignedInt(b), 16));
        }
        return sb.toString();
    }

    private String getChecksum(String name) {
        return props.getProperty(name, "");
    }
    
    private ChecksumVerifier() {
        initProperties();
        initMd();
    }
    
    private void initProperties() {
        try {
            props.load(ChecksumVerifier.class.getResourceAsStream("/checksum"));
        } catch (IOException e) {
            System.err.println("ChecksumVerifier failed to load /checksum");
        }
    }
    
    private boolean initMd() {
        try {
            if (md == null) {
                md = MessageDigest.getInstance("MD5");
            }
            return true;
        } catch (NoSuchAlgorithmException ex) {
            return false;
        }
    }
}
