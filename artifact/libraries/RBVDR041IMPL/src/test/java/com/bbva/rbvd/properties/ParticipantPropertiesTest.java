package com.bbva.rbvd.properties;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.properties.Properties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParticipantPropertiesTest {
    private final Properties properties = new Properties();

    private final ParticipantProperties participantProperties = new ParticipantProperties();
    private ApplicationConfigurationService applicationConfigurationService;

    @Before
    public void setUp() throws IOException {
        applicationConfigurationService = mock(ApplicationConfigurationService.class);
        properties.setApplicationConfigurationService(applicationConfigurationService);
        participantProperties.setApplicationConfigurationService(applicationConfigurationService);
        when(applicationConfigurationService.getDefaultProperty("3","3")).thenReturn("responseRole");
        when(applicationConfigurationService.getDefaultProperty("","3")).thenReturn("responseRoleDefault");
    }

    @Test
    public void executePropertiesTest() {
        String result = properties.getProperty("3","3");
        Assert.assertNotNull(result);
        Assert.assertEquals("responseRole",result);
    }

    @Test
    public void executeparticipantPropertiesTest() {
        String result = participantProperties.getProperty("3","3");
        Assert.assertNotNull(result);
        Assert.assertEquals("responseRole",result);
    }

    @Test
    public void executeparticipantPropertiesEmptyCase() {
        String result = participantProperties.getProperty(null,"3");
        Assert.assertNotNull(result);
        Assert.assertEquals("responseRoleDefault",result);
    }

    @Test
    public void executeparticipantPropertiesNull() {
        String result = participantProperties.obtainRoleCodeByEnum(null);
        Assert.assertNull(result);
    }

}
