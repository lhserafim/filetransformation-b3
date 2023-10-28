package com.suhai.transformab3.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FileService {

    public String transformFile(MultipartFile[] files) {
        String destinationFileName = null;
        List<MultipartFile> sinistroFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            log.info("File name: " + file.getOriginalFilename());
            String fileName = file.getOriginalFilename();
            if (fileName != null && fileName.toLowerCase().contains("sinistro")) {
                sinistroFiles.add(file);
                destinationFileName = createModifiedFile(file, "claim", 2);
            } // TODO adicionar tratamento para AUTOMÓVEL
            // TODO preparar para lista de arquivos
        }
        return destinationFileName;
    }

    public String createModifiedFile(MultipartFile file, String tipo, int position) {
        try {
            InputStream inputStream = file.getInputStream();

            // Crie um diretório temporário em tempo de execução
            Path tempDir = Files.createTempDirectory("tempDir");

            String destinationFileName = tempDir.toString() + File.separator + file.getOriginalFilename();
            OutputStream outputStream = new FileOutputStream(destinationFileName);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

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
                }
                if ("claim".equalsIgnoreCase(tipo)) {
                    line = parts[0] + ";" + origin + ";" + claimNumber + ";" + claimDate + ";" + line.substring(line.indexOf(";") + 1);
                } else {
                    line = parts[0] + ";" + claimNumber + ";" + line.substring(line.indexOf(";") + 1);
                }

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            bufferedReader.close();
            return destinationFileName;

            // Mova o arquivo modificado para o diretório de destino desejado
//            Files.move(Path.of(destinationFileName), Path.of("caminho_de_destino"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
