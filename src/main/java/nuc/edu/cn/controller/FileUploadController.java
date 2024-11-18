package nuc.edu.cn.controller;

import nuc.edu.cn.model.FileInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileUploadController {

    private static final String UPLOAD_DIR = "src/main/webapp/uploads/";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload-single")
    public String uploadSingle(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            return "result";
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);

            model.addAttribute("message", "File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to upload file: " + file.getOriginalFilename());
        }

        return "result";
    }

    @PostMapping("/upload-multiple")
    public String uploadMultiple(@RequestParam("files") MultipartFile[] files, Model model) {
        List<String> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                    Files.write(path, bytes);
                    uploadedFiles.add(file.getOriginalFilename());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        model.addAttribute("message", "Files uploaded successfully: " + String.join(", ", uploadedFiles));
        return "result";
    }

    @ModelAttribute("fileInfo")
    public FileInfo getFileInfo() {
        return new FileInfo();
    }

    @PostMapping("/upload-with-info")
    public String uploadWithInfo(@ModelAttribute("fileInfo") FileInfo fileInfo, @RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            return "result";
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);

            model.addAttribute("message", "File uploaded successfully: " + file.getOriginalFilename());
            model.addAttribute("fileName", fileInfo.getFileName());
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to upload file: " + file.getOriginalFilename());
        }

        return "result";
    }
}
