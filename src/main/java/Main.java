import com.data_management.DataStorage;
import com.cardiogenerator.HealthDataSimulator;
import com.data_management.FileDataReader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        try
        {
            if (args.length > 0 && args[0].equals("DataStorage")) {
                DataStorage.main(new String[]{});
            } else {
                HealthDataSimulator.main(new String[]{});
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

