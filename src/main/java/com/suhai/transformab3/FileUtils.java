package com.suhai.transformab3;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class FileUtils {
    private static final String SOURCE_PATH = "C:\\Users\\luiz.serafim\\Workspace\\Java\\B3\\B3\\source";
    private static final String DESTINATION_PATH = "C:\\Users\\luiz.serafim\\Workspace\\Java\\B3\\B3\\destination";

    private static final String CLAIM_SOURCE_PATH = "C:\\Users\\luiz.serafim\\Workspace\\Java\\B3\\B3\\sinistro\\source";
    private static final String CLAIM_DESTINATION_PATH = "C:\\Users\\luiz.serafim\\Workspace\\Java\\B3\\B3\\sinistro\\destination";



    public void temp() throws IOException {
        System.out.println("Starting the process...");

        FileUtils fileUtils = new FileUtils();
        Stream<Path> files = Files.walk(Paths.get(SOURCE_PATH));

        files.filter(Files::isRegularFile).forEach(file -> fileUtils
                .createModifiedFile(file.getParent().toString(), file.getFileName().toString(), DESTINATION_PATH,2, null));

        System.out.println("File copied successfully.");


        System.out.println("Starting the process claim...");

        files = Files.walk(Paths.get(CLAIM_SOURCE_PATH));

        files.filter(Files::isRegularFile).forEach(file -> fileUtils
                .createModifiedFile(file.getParent().toString(), file.getFileName().toString(), CLAIM_DESTINATION_PATH,4, "claim"));

        System.out.println("File copied successfully.");
    }

    public void createModifiedFile(String path, String filename, String destination, int position, String tipo) {
        try {
            FileReader fileReader = new FileReader(path + "\\" + filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            FileWriter fileWriter = new FileWriter(destination + "\\" + filename);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String line;
            String claimNumber = "";
            String claimDate = "";
            String origin = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (Objects.equals(parts[0], "1")) {
                    claimNumber = parts[position];
                    if ("claim".equalsIgnoreCase(tipo)) {
                        origin = parts[1];
                        claimDate = parts[3];
                    }
                } else if ("claim".equalsIgnoreCase(tipo)) {
                    line = parts[0] + ";" + origin + ";" + claimNumber + ";" + claimDate + ";" + line.substring(line.indexOf(";") + 1);
                } else {
                    line = parts[0] + ";" + claimNumber + ";" + line.substring(line.indexOf(";") + 1);
                }

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            bufferedReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
