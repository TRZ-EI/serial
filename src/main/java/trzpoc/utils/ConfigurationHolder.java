package trzpoc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 29/07/17
 * Time: 15.33
 */
public class ConfigurationHolder {

    public static final String DEBUG="DEBUG";

// SERIAL PORT CONNECTION ---
    public static final String PORT="PORT";
    public static final String BAUD_RATE="BAUD_RATE";

// FONT CONFIGURATION ---
    public static final String SMALL_FONT="SMALL_FONT";
    public static final String SMALL_FONT_WEIGHT="SMALL_FONT_WEIGHT";
    public static final String SMALL_SIZE="SMALL_SIZE";
    public static final String BIG_FONT="BIG_FONT";
    public static final String BIG_FONT_WEIGHT="BIG_FONT_WEIGHT";
    public static final String BIG_SIZE="BIG_SIZE";
    public static final String CRC="CRC";
// END OF LINE VALUE ---
    public static final String END_OF_LINE="END_OF_LINE";





    private static String configurationUri;

    private Properties properties;
    private static ConfigurationHolder singleInstance;

    private ConfigurationHolder(String uri){
        configurationUri = uri;
        this.createProperties(uri);
    }
    private void createProperties(String resourceFile) {
        try {
            if (resourceFile != null && resourceFile.length() > 0) {
                String path = new File(resourceFile).getAbsolutePath();


                InputStream s = new FileInputStream(path);
                this.properties = new Properties();
                this.properties.load(s);
                s.close();
            }
        }catch (Exception ex){
            // TODO: log file
            System.out.println(ex);
        }

    }


    public static ConfigurationHolder createSingleInstanceByConfigUri(String uri){
        if (singleInstance == null) {
            singleInstance = new ConfigurationHolder(uri);
        }
        return singleInstance;
    }

    public static ConfigurationHolder getInstance(){
        if (singleInstance == null){
            singleInstance = ConfigurationHolder.createSingleInstanceByConfigUri(configurationUri);
        }
        return singleInstance;
    }

    public Properties getProperties() {
        return this.properties;
    }
}
