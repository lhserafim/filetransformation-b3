package com.suhai.transformab3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;


@Slf4j
@Controller
@RequestMapping(value = "b3/file")
public class FileController {


    @PostMapping(value = "/transformation")
    public String transformFile(@RequestParam("files") MultipartFile[] files, Model model) {
        Arrays.stream(files).forEach(file -> log.info("File name: " + file.getOriginalFilename()));
        model.addAttribute("fileNames", Arrays.stream(files).map(MultipartFile::getOriginalFilename).toArray(String[]::new));
        return "file-list";
    }


}
