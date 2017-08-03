package trzpoc.utils;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 29/07/17
 * Time: 15.58
 */
public class ConfigurationHolderTest {
    private String configurationUri = "application.properties";
    private String realPathConfigurationUri;

    @BeforeClass
    private void setup(){
        this.realPathConfigurationUri = this.getClass().getClassLoader().getResource(configurationUri).getFile();
        ConfigurationHolder.createSingleInstanceByConfigUri(this.realPathConfigurationUri);
    }
    @Test
    public void testCreateSingleInstanceByConfigUri() throws Exception {
        assertNotNull(ConfigurationHolder.getInstance());
    }
    @Test
    public void testGetProperties() throws Exception {
        assertNotNull(ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.BAUD_RATE));
        assertNotNull(ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.PORT));
        assertNotNull(ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.BIG_FONT));
        assertNotNull(ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.BIG_FONT_WEIGHT));
        assertNotNull(ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.BIG_SIZE));
        assertNotNull(ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.SMALL_FONT));
        assertNotNull(ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.SMALL_FONT_WEIGHT));
        assertNotNull(ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.SMALL_SIZE));
        assertNotNull(ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.DEBUG));
    }
}