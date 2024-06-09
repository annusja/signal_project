package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class AlertEvaluations
{ // TODO currently only gets the first alert, not all, which causes problems to combine alert check, should fix
    public static List<Alert> runALl(Patient patient)
    {
        List<PatientRecord> records10minutes = patient.getRecords(System.currentTimeMillis()-600000, System.currentTimeMillis());
        List<Alert> alertsToTrigger = new ArrayList<>();

        addAlert(checkDiastolicPressure(records10minutes), alertsToTrigger);
        addAlert(checkSystolicPressure(records10minutes), alertsToTrigger);
        addAlert(checkBloodSaturation(records10minutes), alertsToTrigger);
        addAlert(checkCombinedAlert(alertsToTrigger), alertsToTrigger);
        addAlert(checkECG(records10minutes), alertsToTrigger);
        return alertsToTrigger;
    }

    private static void addAlert(Alert alert, List<Alert> alertList)
    {
        if (alert!=null) alertList.add(alert);
    }
    public static Alert checkDiastolicPressure(List<PatientRecord> patientRecords)
    {
        double criticalHighPressure = 120;
        double criticalLowPressure = 60;
        int increaseCounter = 0;
       Double previousPressure = null;
        int decreaseCounter = 0;

        for (PatientRecord record : patientRecords)
        {
            if (record.getRecordType().equalsIgnoreCase("DiastolicPressure"))
            {
                previousPressure = record.getMeasurementValue();
                break;
            }
        }
        if (previousPressure==null) return null;

        for (PatientRecord record : patientRecords)
        {
            if (record.getRecordType().equalsIgnoreCase("DiastolicPressure"))
            {
                double pressure = record.getMeasurementValue();
                if (pressure>=criticalHighPressure) return new Alert(
                        record.getPatientId()+"", "diastolic pressure critically high", record.getTimestamp());
                if (pressure<=criticalLowPressure) return new Alert(
                        record.getPatientId()+"", "diastolic pressure critically low", record.getTimestamp());

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

                if (increaseCounter==2) return new Alert(
                        record.getPatientId()+"", "diastolic pressure increasing rapidly", record.getTimestamp());
                if (decreaseCounter==2) return new Alert(
                        record.getPatientId()+"", "diastolic pressure decreasing rapidly", record.getTimestamp());
            }
        }
        return null;
    }

    public static Alert checkSystolicPressure(List<PatientRecord> patientRecords)
    {
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
        if (previousPressure==null) return null;

        for (PatientRecord record : patientRecords)
        {
            if (record.getRecordType().equalsIgnoreCase("SystolicPressure"))
            {
                double pressure = record.getMeasurementValue();
                if (pressure>=criticalHighPressure) return new Alert(
                        record.getPatientId()+"", "systolic pressure critically high", record.getTimestamp());
                if (pressure<=criticalLowPressure) return new Alert(
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

                if (increaseCounter==2) return new Alert(
                        record.getPatientId()+"", "systolic pressure increasing rapidly", record.getTimestamp());
                if (decreaseCounter==2) return new Alert(
                        record.getPatientId()+"", "systolic pressure decreasing rapidly", record.getTimestamp());
            }
        }
        return null;
    }

    public static Alert checkBloodSaturation(List<PatientRecord> patientRecords) //needs some more implementation for the 10 minutes
    {
        double criticalSaturation = 92;
        double rapidDrop = 5;
        double peakSaturation = 0;
        double minSaturation = 0;

        for (PatientRecord record : patientRecords)
        {
            if (record.getRecordType().equalsIgnoreCase("Saturation"))
            {
                double saturation = record.getMeasurementValue();
                if (saturation<=criticalSaturation) return new Alert(
                        record.getPatientId()+"", "blood saturation critically low", record.getTimestamp());

                if (saturation>peakSaturation)
                {
                    peakSaturation=saturation;
                    minSaturation=saturation;
                }
                else if (saturation<minSaturation)
                {
                    minSaturation=saturation;
                    if (peakSaturation-minSaturation>rapidDrop) return new Alert(
                            record.getPatientId()+"", "blood saturation dropping rapidly", record.getTimestamp());
                }
            }
        }
        return null;
    }

    private static Alert checkCombinedAlert(List<Alert> alertList)
    {
        boolean lowSystolic = false;
        boolean lowSaturation = false;
        for (Alert alert : alertList)
        {
            if (alert.getCondition().equalsIgnoreCase("systolic pressure critically low")) lowSystolic=true;
            if (alert.getCondition().equalsIgnoreCase("blood saturation critically low")) lowSaturation=true;
            if (lowSaturation && lowSystolic) return new Alert(
                    alert.getPatientId(), "Hypotensive Hypoxemia Alert", alert.getTimestamp());
        }
        return null;
    }

    public static Alert checkECG(List<PatientRecord> patientRecords)
    {
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

                if (reading>avg*2) return new Alert(
                        record.getPatientId()+"", "significant ECG peak", record.getTimestamp());
                if (counter==30)
                {
                    counter--;
                    sum -= slidingWindow.removeFirst();
                }
            }
        }

        return null;
    }
}
