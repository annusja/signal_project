package com.cardiogenerator.outputs;

/**
 * handles how and where to show simulated data
 */
public interface OutputStrategy {
    /**
     *
     * @param patientId the numerical identifier of a patient
     * @param timestamp long number of when the data generation happened
     * @param label string which specifies what kind of data is shown
     * @param data produced information about the specified patient
     */
    void output(int patientId, long timestamp, String label, String data);
}
