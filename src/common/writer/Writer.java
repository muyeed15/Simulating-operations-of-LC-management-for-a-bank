package common.writer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Muyeed
 */
public class Writer {
    private String location;
    private String fileName;
    private String data;

    public Writer(String location, String fileName, String data) {
        this.location = location;
        this.fileName = fileName;
        this.data = data;
    }

    public String getLocation() {
        return location;
    }

    public String getFileName() {
        return fileName;
    }

    public String getData() {
        return data;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Writer{" + "location=" + location + ", fileName=" + fileName + ", data=" + data + '}';
    }
    
    public void writeFile() {
        try {
            File directory = new File(location);
            
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            File myFile = new File(location + "/" + fileName);
            try (FileWriter myWriter = new FileWriter(myFile)) {
                myWriter.write(data);
            }
            System.out.println("Data Stored");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void overWriteFile() {
        try {
            File directory = new File(location);

            if (!directory.exists()) {
                System.out.println("File does not exist!");
            } else {
                File myFile = new File(location + "/" + fileName);
                try (FileWriter myWriter = new FileWriter(myFile, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(myWriter)) {
                    bufferedWriter.write(data);
                    bufferedWriter.newLine();
                }
                System.out.println("Data Stored");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteLine(String lineToDelete) {
        try {
            File directory = new File(location);
            if (!directory.exists()) {
                System.out.println("Directory does not exist!");
                return;
            }

            File file = new File(location + "/" + fileName);
            if (!file.exists()) {
                System.out.println("File does not exist!");
                return;
            }

            List<String> linesToKeep = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.equals(lineToDelete)) {
                        linesToKeep.add(line);
                    }
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : linesToKeep) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            System.out.println("Line deleted successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
