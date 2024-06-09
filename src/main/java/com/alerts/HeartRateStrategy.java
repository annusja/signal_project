package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class HeartRateStrategy implements AlertStrategy{
    @Override
    public void checkAlert(Patient patient) {
        AlertFactory factory = new ECGAlertFactory();
        List<PatientRecord> patientRecords = patient.getRecords(System.currentTimeMillis()-600000, System.currentTimeMillis());
        int windowSize = 30;
        int counter = 0;
        double sum=0;
        double avg = 0;
        List<Double> slidingWindow = new ArrayList<>();
        for (PatientRecord record : patientRecords)
        {
            if (record.getRecordType().equalsIgnoreCase("ECG"))
            {
                double reading = record.getMeasurementValue();
                sum+=reading;
                slidingWindow.add(reading);
                counter++;
                avg=sum/counter;

                if (reading>avg*2) factory.createAlert(
                        record.getPatientId()+"", "significant ECG peak", record.getTimestamp());
                if (counter==windowSize)
                {
                    counter--;
                    sum -= slidingWindow.removeFirst();
                }
            }
        }
    }
}
