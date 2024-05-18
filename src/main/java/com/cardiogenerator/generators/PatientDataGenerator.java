package com.cardiogenerator.generators;

import com.cardiogenerator.outputs.OutputStrategy;

/**
 * Handles the creation of information about patients
 */
public interface PatientDataGenerator {
    /**
     * generates information about the specified patient and returns it as specified by outputStrategy
     * @param patientId the numerical identifier of a patient
     * @param outputStrategy the OutputStrategy object which determines how and where to return the generated data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
