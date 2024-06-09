package alerts;

import com.alerts.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FactoryTests
{
    @Test
    public void testBloodPressureAlertFactory(){
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("1", "worrying blood pressure", System.currentTimeMillis());

        assertTrue(alert instanceof BloodPressureAlert);
        assertEquals("1", alert.getPatientId());
        assertEquals("worrying blood pressure", alert.getCondition());
    }

    @Test
    public void testBloodOxygenAlertFactory(){
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("1", "critical blood oxygen", System.currentTimeMillis());

        assertTrue(alert instanceof BloodOxygenAlert);
        assertEquals("1", alert.getPatientId());
        assertEquals("critical blood oxygen", alert.getCondition());
    }

    @Test
    public void testECGAlertFactory(){
        AlertFactory alertFactory = new ECGAlertFactory();
        Alert alert = alertFactory.createAlert("1", "significant ECG peak", System.currentTimeMillis());

        assertTrue(alert instanceof ECGAlert);
        assertEquals("1", alert.getPatientId());
        assertEquals("significant ECG peak", alert.getCondition());
    }
}

