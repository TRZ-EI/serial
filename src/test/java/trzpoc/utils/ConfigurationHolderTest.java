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
    private ConfigurationHolder sut;

    @BeforeClass
    private void setup(){
        this.realPathConfigurationUri = this.getClass().getClassLoader().getResource(configurationUri).getFile();
    }
    @Test
    public void testCreateSingleInstanceByConfigUri() throws Exception {
        this.sut = ConfigurationHolder.createSingleInstanceByConfigUri(this.realPathConfigurationUri);
        assertNotNull(this.sut);
    }
    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(ConfigurationHolder.getInstance());
    }
    @Test
    public void testGetProperties() throws Exception {
        this.sut = ConfigurationHolder.createSingleInstanceByConfigUri(this.realPathConfigurationUri);
        assertNotNull(this.sut.getProperties().getProperty(ConfigurationHolder.BAUD_RATE));
        assertNotNull(this.sut.getProperties().getProperty(ConfigurationHolder.PORT));
        assertNotNull(this.sut.getProperties().getProperty(ConfigurationHolder.BIG_FONT));
        assertNotNull(this.sut.getProperties().getProperty(ConfigurationHolder.BIG_FONT_WEIGHT));
        assertNotNull(this.sut.getProperties().getProperty(ConfigurationHolder.BIG_SIZE));
        assertNotNull(this.sut.getProperties().getProperty(ConfigurationHolder.SMALL_FONT));
        assertNotNull(this.sut.getProperties().getProperty(ConfigurationHolder.SMALL_FONT_WEIGHT));
        assertNotNull(this.sut.getProperties().getProperty(ConfigurationHolder.SMALL_SIZE));
        assertNotNull(this.sut.getProperties().getProperty(ConfigurationHolder.DEBUG));
    }
}