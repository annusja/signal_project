package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class SystolicBloodPressureStrategy implements AlertStrategy
{

    @Override
    public void checkAlert(Patient patient) {
        AlertFactory factory = new BloodPressureAlertFactory();
        List<PatientRecord> patientRecords = patient.getRecords(System.currentTimeMillis()-600000, System.currentTimeMillis());
        double criticalHighPressure = 180;
        double criticalLowPressure = 90;
        int increaseCounter = 0;
        Double previousPressure = null;
        int decreaseCounter = 0;

        for (PatientRecord record : patientRecords)
        {
            if (record.getRecordType().equalsIgnoreCase("SystolicPressure"))
            {
                previousPressure = record.getMeasurementValue();
                break;
            }
        }
        if (previousPressure==null) return;

        for (PatientRecord record : patientRecords)
        {
            if (record.getRecordType().equalsIgnoreCase("SystolicPressure"))
            {
                double pressure = record.getMeasurementValue();
                if (pressure>=criticalHighPressure) factory.createAlert(
                        record.getPatientId()+"", "systolic pressure critically high", record.getTimestamp());
                if (pressure<=criticalLowPressure) factory.createAlert(
                        record.getPatientId()+"", "systolic pressure critically low", record.getTimestamp());

                if (previousPressure-pressure>=10) // decrease
                {
                    decreaseCounter++;
                    increaseCounter=0;
                }
                else if (previousPressure-pressure<=10) //increase
                {
                    decreaseCounter=0;
                    increaseCounter++;
                }
                else //plateau
                {
                    increaseCounter=0;
                    decreaseCounter=0;
                }
                previousPressure=pressure;

                if (increaseCounter==2) factory.createAlert(
                        record.getPatientId()+"", "systolic pressure increasing rapidly", record.getTimestamp());
                if (decreaseCounter==2) factory.createAlert(
                        record.getPatientId()+"", "systolic pressure decreasing rapidly", record.getTimestamp());
            }
        }
    }
}
