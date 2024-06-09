package com.data_management;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class FileDataReader implements DataReader
{
    File filepath;
    public FileDataReader(String file)
    {
        filepath=new File(file);
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException
    {
        if (!filepath.exists()) throw new IOException("file not found");
        Scanner scanner = new Scanner(filepath);
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(", ");
            int patientID = lineScanner.nextInt();
            long timeStamp = lineScanner.nextLong();
            String recordType = lineScanner.next();
            String measurementString = lineScanner.next();
            double measurementValue = Double.parseDouble(measurementString);
            dataStorage.addPatientData(patientID,measurementValue,recordType, timeStamp);
        }
    }
}
