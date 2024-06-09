package com.alerts;

public class BloodPressureAlertFactory extends AlertFactory
{

    /**
     * @param patientId
     * @param condition
     * @param timestamp
     * @return
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}
