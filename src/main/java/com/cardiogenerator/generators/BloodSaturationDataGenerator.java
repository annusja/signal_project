package com.cardiogenerator.generators;

import java.util.Random;

import com.cardiogenerator.outputs.OutputStrategy;

/**
 * handles the creation of blood saturation information for all patients
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    /**
     * randomizer that generates pseudorandom numbers
     */
    private static final Random random = new Random();
    /**
     * integer array of previous blood saturation values for all patients
     */
    private int[] lastSaturationValues;

    /**
     * creates the generator object,
     * initializes lastSaturationValues array with baseline(95-100) saturation values for each patient between
     * @param patientCount number of patients for which to create data
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * randomly updates blood saturation for specified patient within reasonable range,
     * saves this information in lastSaturationValues array,
     * outputs the information based on specified output strategy
     * @param patientId the numerical identifier of a patient
     * @param outputStrategy the OutputStrategy object which determines how and where to return the generated data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
