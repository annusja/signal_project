package com.cardiogenerator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cardiogenerator.generators.AlertGenerator;

import com.cardiogenerator.generators.BloodPressureDataGenerator;
import com.cardiogenerator.generators.BloodSaturationDataGenerator;
import com.cardiogenerator.generators.BloodLevelsDataGenerator;
import com.cardiogenerator.generators.ECGDataGenerator;
import com.cardiogenerator.outputs.ConsoleOutputStrategy;
import com.cardiogenerator.outputs.FileOutputStrategy;
import com.cardiogenerator.outputs.OutputStrategy;
import com.cardiogenerator.outputs.TcpOutputStrategy;
import com.cardiogenerator.outputs.WebSocketOutputStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * handles the creation of information about ECG, blood saturation, blood pressure, blood levels and alerts for multiple patients
 */
public class HealthDataSimulator {

    private static HealthDataSimulator instance;
    /**
     * number of patients used in the simulation
     */
    private static int patientCount = 50; // Default number of patients
    /**
     * scheduling object that executes data generation at specified frequency
     */
    private static ScheduledExecutorService scheduler;
    /**
     * OutputStrategy object that dictates how and where generated data is returned
     */
    private static OutputStrategy outputStrategy = new ConsoleOutputStrategy(); // Default output strategy
    /**
     * randomizer that generates pseudorandom numbers
     */
    private static final Random RANDOM = new Random();

    /**
     * executes the simulation of all health data for patients
     * @param args all arguments
     * @throws IOException thrown when parseArguments doesn't work out
     */
    public static void main(String[] args) throws IOException {

        parseArguments(args);

        scheduler = Executors.newScheduledThreadPool(patientCount * 4);

        List<Integer> patientIds = initializePatientIds(patientCount);
        Collections.shuffle(patientIds); // Randomize the order of patient IDs

        scheduleTasksForPatients(patientIds);
    }

    private HealthDataSimulator(int patientCount, ScheduledExecutorService scheduler, OutputStrategy outputStrategy) {
        this.patientCount = patientCount;
        this.scheduler = scheduler;
        this.outputStrategy = outputStrategy;

    }

    public static HealthDataSimulator getInstanceOfHealthDataSimulator(int patientCount, ScheduledExecutorService scheduler, OutputStrategy outputStrategy) {
        if(instance==null) instance = new HealthDataSimulator(patientCount,scheduler,outputStrategy);
        return instance;
    }

    /**
     * parses the arguments and prints out helpful messages when problems arise
     * @param args inputs from command line
     * @throws IOException generic something went wrong and parseArguments wasn't written to help with it
     */
    private static void parseArguments(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                    printHelp();
                    System.exit(0);
                    break;
                case "--patient-count":
                    if (i + 1 < args.length) {
                        try {
                            patientCount = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            System.err
                                    .println("Error: Invalid number of patients. Using default value: " + patientCount);
                        }
                    }
                    break;
                case "--output":
                    if (i + 1 < args.length) {
                        String outputArg = args[++i];
                        if (outputArg.equals("console")) {
                            outputStrategy = new ConsoleOutputStrategy();
                        } else if (outputArg.startsWith("file:")) {
                            String baseDirectory = outputArg.substring(5);
                            Path outputPath = Paths.get(baseDirectory);
                            if (!Files.exists(outputPath)) {
                                Files.createDirectories(outputPath);
                            }
                            outputStrategy = new FileOutputStrategy(baseDirectory);
                        } else if (outputArg.startsWith("websocket:")) {
                            try {
                                int port = Integer.parseInt(outputArg.substring(10));
                                // Initialize your WebSocket output strategy here
                                outputStrategy = new WebSocketOutputStrategy(port);
                                System.out.println("WebSocket output will be on port: " + port);
                            } catch (NumberFormatException e) {
                                System.err.println(
                                        "Invalid port for WebSocket output. Please specify a valid port number.");
                            }
                        } else if (outputArg.startsWith("tcp:")) {
                            try {
                                int port = Integer.parseInt(outputArg.substring(4));
                                // Initialize your TCP socket output strategy here
                                outputStrategy = new TcpOutputStrategy(port);
                                System.out.println("TCP socket output will be on port: " + port);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid port for TCP output. Please specify a valid port number.");
                            }
                        } else {
                            System.err.println("Unknown output type. Using default (console).");
                        }
                    }
                    break;
                default:
                    System.err.println("Unknown option '" + args[i] + "'");
                    printHelp();
                    System.exit(1);
            }
        }
    }

    /**
     * prints more helpful messaging (used in parseArguments only)
     */
    private static void printHelp() {
        System.out.println("Usage: java HealthDataSimulator [options]");
        System.out.println("Options:");
        System.out.println("  -h                       Show help and exit.");
        System.out.println(
                "  --patient-count <count>  Specify the number of patients to simulate data for (default: 50).");
        System.out.println("  --output <type>          Define the output method. Options are:");
        System.out.println("                             'console' for console output,");
        System.out.println("                             'file:<directory>' for file output,");
        System.out.println("                             'websocket:<port>' for WebSocket output,");
        System.out.println("                             'tcp:<port>' for TCP socket output.");
        System.out.println("Example:");
        System.out.println("  java HealthDataSimulator --patient-count 100 --output websocket:8080");
        System.out.println(
                "  This command simulates data for 100 patients and sends the output to WebSocket clients connected to port 8080.");
    }

    /**
     * creates a list of patient identification numbers from one to whichever many patients are specified
     * @param patientCount number of patients that need an ID number
     * @return an Integer list of patient ids, starting from 1
     */
    private static List<Integer> initializePatientIds(int patientCount) {
        List<Integer> patientIds = new ArrayList<>();
        for (int i = 1; i <= patientCount; i++) {
            patientIds.add(i);
        }
        return patientIds;
    }

    /**
     * creates data generator objects for all health data and creates the data at hardcoded intervals
     * @param patientIds integer list of all patient identification numbers
     */
    private static void scheduleTasksForPatients(List<Integer> patientIds) {
        ECGDataGenerator ecgDataGenerator = new ECGDataGenerator(patientCount);
        BloodSaturationDataGenerator bloodSaturationDataGenerator = new BloodSaturationDataGenerator(patientCount);
        BloodPressureDataGenerator bloodPressureDataGenerator = new BloodPressureDataGenerator(patientCount);
        BloodLevelsDataGenerator bloodLevelsDataGenerator = new BloodLevelsDataGenerator(patientCount);
        AlertGenerator alertGenerator = new AlertGenerator(patientCount);

        for (int patientId : patientIds) {
            scheduleTask(() -> ecgDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.SECONDS);
            scheduleTask(() -> bloodSaturationDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.SECONDS);
            scheduleTask(() -> bloodPressureDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.MINUTES);
            scheduleTask(() -> bloodLevelsDataGenerator.generate(patientId, outputStrategy), 2, TimeUnit.MINUTES);
            scheduleTask(() -> alertGenerator.generate(patientId, outputStrategy), 20, TimeUnit.SECONDS);
        }
    }

    /**
     * makes a task happen at specified intervals
     * @param task the Runnable to be done
     * @param period long number for length of period between doing tasks
     * @param timeUnit TimeUnit object for giving the period units
     */
    private static void scheduleTask(Runnable task, long period, TimeUnit timeUnit) {
        scheduler.scheduleAtFixedRate(task, RANDOM.nextInt(5), period, timeUnit);
    }
}
