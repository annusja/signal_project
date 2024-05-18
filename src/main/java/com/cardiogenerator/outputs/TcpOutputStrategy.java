package com.cardiogenerator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * Streams the simulated data to TCP clients connected to the specified port
 */
public class TcpOutputStrategy implements OutputStrategy {

    /**
     * connects to tcp server
     */
    private ServerSocket serverSocket;
    /**
     * connects the client
     */
    private Socket clientSocket;
    /**
     * for putting out the information
     */
    private PrintWriter out;

    /**
     * starts a TCP server on the specified port, connects client
     * @param port through where to stream the information
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends the specified information to TCP clients connected to the specified port and prints it
     * @param patientId the numerical identifier of a patient
     * @param timestamp long number of when the data generation happened
     * @param label string which specifies what kind of data is shown
     * @param data produced information about the specified patient
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
