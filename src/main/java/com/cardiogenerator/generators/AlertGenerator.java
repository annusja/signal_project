// reindented entire file from 4 spaces to 2 spaces
// renamed package to remove underscore
package com.cardiogenerator.generators;

// deleted blank line, reordered imports in ASCII sort order
import com.cardiogenerator.outputs.OutputStrategy;
import java.util.Random;

/**
 * handles the creation of alert information for patients
 */
public class AlertGenerator implements PatientDataGenerator {

  //renamed to all caps as it's a constant
  /**
   * randomizer that generates pseudorandom numbers
   */
  public static final Random RANDOM_GENERATOR = new Random();
  // renamed to lowerCamelCase here and in all usages
  /**
   * boolean array of alert states for all patients, where false = resolved, true = pressed
   */
  private boolean[] alertStates; // false = resolved, true = pressed

  /**
   * creates the AlertGenerator object and
   * initializes the alertStates boolean array to size one bigger than number of patients
   * @param patientCount number of patients for which to create alert data
   */
  public AlertGenerator(int patientCount) {
    alertStates = new boolean[patientCount + 1];
  }

  /**
   * randomly resolves or triggers the alert state for a specified patient,
   * saves this information in alertStates array,
   * outputs the information based on specified output strategy
   * @param patientId the numerical identifier of a patient
   * @param outputStrategy the OutputStrategy object which determines how and where to return the generated data
   */
  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      if (alertStates[patientId]) {
        if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
          alertStates[patientId] = false;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
        }
      } else {
        // renamed to lowerCamelCase here and in all usages
        double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
        double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
        boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

        if (alertTriggered) {
          alertStates[patientId] = true;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
        }
      }
    } catch (Exception e) {
      System.err.println("An error occurred while generating alert data for patient " + patientId);
      e.printStackTrace();
    }
  }
}