package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy{
    @Override
    public void checkAlert(Patient patient) {
        AlertFactory factory = new BloodOxygenAlertFactory();
        List<PatientRecord> patientRecords = patient.getRecords(System.currentTimeMillis()-600000, System.currentTimeMillis());
        double criticalSaturation = 92;
        double rapidDrop = 5;
        double peakSaturation = 0;
        double minSaturation = 0;

        for (PatientRecord record : patientRecords)
        {
            if (record.getRecordType().equalsIgnoreCase("Saturation"))
            {
                double saturation = record.getMeasurementValue();
                if (saturation<=criticalSaturation) factory.createAlert(
                        record.getPatientId()+"", "blood saturation critically low", record.getTimestamp());

                if (saturation>peakSaturation)
                {
                    peakSaturation=saturation;
                    minSaturation=saturation;
                }
                else if (saturation<minSaturation)
                {
                    minSaturation=saturation;
                    if (peakSaturation-minSaturation>rapidDrop) factory.createAlert(
                            record.getPatientId()+"", "blood saturation dropping rapidly", record.getTimestamp());
                }
            }
        }
    }
}
