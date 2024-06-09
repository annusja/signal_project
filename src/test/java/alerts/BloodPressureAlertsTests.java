package alerts;

import com.alerts.DiastolicBloodPressureStrategy;
import com.alerts.SystolicBloodPressureStrategy;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BloodPressureAlertsTests
{   // TODO fix the fact that all alerts disappear into nothingness, not logged anywhere, so can't test them
    DiastolicBloodPressureStrategy diaStrat = new DiastolicBloodPressureStrategy();
    SystolicBloodPressureStrategy sysStrat = new SystolicBloodPressureStrategy();
    DataStorage storage = DataStorage.getInstanceOfDataStorage();

    @Test
    public void testCriticalAlerts()
    {
        storage.addPatientData(1, 121, "DiastolicPressure", System.currentTimeMillis());
        storage.addPatientData(1, 59, "DiastolicPressure", System.currentTimeMillis());
        storage.addPatientData(1, 181, "SystolicPressure", System.currentTimeMillis());
        storage.addPatientData(1, 89, "SystolicPressure", System.currentTimeMillis());

        diaStrat.checkAlert(storage.getAllPatients().getFirst());
        // assertEquals("DiastolicPressure", );
        sysStrat.checkAlert(storage.getAllPatients().getFirst());
    }

}
