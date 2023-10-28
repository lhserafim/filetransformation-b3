package com.suhai.transformab3.controller;

import com.suhai.transformab3.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;


@Slf4j
@Controller
@RequestMapping(value = "b3/file")
public class FileController {

    // TODO capturar e transformar os arquivos, com base no seu tipo
    // TODO melhorar a aparência da página
    // TODO colocar autenticação
    // TODO subir na AWS
    // TODO colocar tratamento para exceções
    // TODO refatorar o código

    private static final String UPLOAD_DIR = "/Users/lhserafim/Downloads/B3/source/21403_SEG_230815_SP_MOVIMENTO-DIARIO-AUTOMOVEL-TXT.txt";


    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/transformation")
    public String transformFile(@RequestParam("files") MultipartFile[] files, Model model) {
        Arrays.stream(files).forEach(file -> log.info("File name: " + file.getOriginalFilename()));
        model.addAttribute("fileNames", Arrays.stream(files).map(MultipartFile::getOriginalFilename).toArray(String[]::new));
        String path = fileService.transformFile(files);
        model.addAttribute("path", path);
        return "file-list";
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("path") String path) {
        try {
            File file = new File(path );
            FileInputStream inputStream = new FileInputStream(file);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=xxxx.txt" ); // TODO usar o filename original

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(inputStream));
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
