// reindented entire file from 4 spaces to 2 spaces
// renamed package to remove underscore
package com.cardiogenerator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

// renamed class to be in UpperCamelCase
/**
 * Saves the simulated data to files within the specified directory.
 */
public class FileOutputStrategy implements OutputStrategy {

  // renamed to lowerCamelCase here and in all usages
  /**
   * String of where to put all the created files
   */
  private String baseDirectory;
  // renamed to lowerCamelCase here and in all usages (couldn't decide if it's a true constant or not)
  /**
   * hashmap for holding the paths to files
   */
  public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

  // renamed to match new class name, deleted linebreak for readability

  /**
   * creates the FileOutputStrategy object, initializes baseDirectory String
   * @param baseDirectory String of where to put all the created files
   */
  public FileOutputStrategy(String baseDirectory) {
    this.baseDirectory = baseDirectory;
  }

  /**
   * creates a directory and writes the specified data to the file
   * @param patientId the numerical identifier of a patient
   * @param timestamp long number of when the data generation happened
   * @param label string which specifies what kind of data is shown
   * @param data produced information about the specified patient
   */
  @Override
  public void output(int patientId, long timestamp, String label, String data) {
    try {
      // Create the directory
      Files.createDirectories(Paths.get(baseDirectory));
    } catch (IOException e) {
      System.err.println("Error creating base directory: " + e.getMessage());
      return;
    }
    // Set the filePath variable
    // line-wrapping to not exceed column limit, renamed variable to lowerCamelCase here and in all usages
    String filePath = fileMap.computeIfAbsent(
        label, k -> Paths.get(baseDirectory, label + ".txt").toString());

    // Write the data to the file
    try (PrintWriter out = new PrintWriter(
        Files.newBufferedWriter(Paths.get(filePath), // line-wrapping to not exceed column limit
        StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
      out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
    } catch (Exception e) {
      System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
    }
  }
}