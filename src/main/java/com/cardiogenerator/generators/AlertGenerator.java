// reindented entire file from 4 spaces to 2 spaces
// renamed package to remove underscore
package com.cardiogenerator.generators;

// deleted blank line, reordered imports in ASCII sort order
import com.cardiogenerator.outputs.OutputStrategy;
import java.util.Random;

public class AlertGenerator implements PatientDataGenerator {

  public static final Random randomGenerator = new Random();
  // renamed to lowerCamelCase here and in all usages
  private boolean[] alertStates; // false = resolved, true = pressed

  public AlertGenerator(int patientCount) {
    alertStates = new boolean[patientCount + 1];
  }

  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      if (alertStates[patientId]) {
        if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
          alertStates[patientId] = false;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
        }
      } else {
        // renamed to lowerCamelCase here and in all usages
        double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
        double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
        boolean alertTriggered = randomGenerator.nextDouble() < p;

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