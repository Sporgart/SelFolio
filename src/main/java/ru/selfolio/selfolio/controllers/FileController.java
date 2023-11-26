package ru.selfolio.selfolio.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.selfolio.selfolio.models.AppUser;
import ru.selfolio.selfolio.models.File;
import ru.selfolio.selfolio.models.Project;
import ru.selfolio.selfolio.services.AppUserService;
import ru.selfolio.selfolio.services.FileService;
import ru.selfolio.selfolio.services.ProjectService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileController {
    private final FileService fileService;
    private final AppUserService appUserService;
    private final ProjectService projectService;
    @Value("${app.address}")
    private String serverAddress;

    public FileController(FileService fileService, AppUserService appUserService, ProjectService projectService) {
        this.fileService = fileService;
        this.appUserService = appUserService;
        this.projectService = projectService;
    }

    @GetMapping("/files{id}")
    public String index(@PathVariable("id") int projectId, Model model) {
        AppUser currentUser = appUserService.getCurrentUser();
        List<Project> currentUserProjects = currentUser.getProjects();
        boolean permissionToRedact = false;
        for (Project p :
                currentUserProjects) {
            if (p.getId() == projectId){
                permissionToRedact = true;
                model.addAttribute("permissionToRedact", permissionToRedact);
                break;
            }
        }
        Project foundProject = projectService.findProjectById(projectId);

        List<String> fileNames = foundProject.getFiles().stream().map(File::getFileName).toList();
        model.addAttribute("list", fileNames);
        model.addAttribute("projectNumber", projectId);
        model.addAttribute("projectTitle", foundProject.getTitle());
        model.addAttribute("serverAddress", serverAddress);
        return "upload";
    }

    @PostMapping("/upload{id}")
    public String singleFileUpload
            (@PathVariable("id") int projectId, @RequestParam("file") MultipartFile file, Model model) {

        if (file.isEmpty()) {
            model.addAttribute("warning",
                    "Выберите файл для загрузки");
            return "redirect:/files" + projectId;
        }

        try {
            fileService.saveFileInBD(file, projectId);
        } catch (IOException e) {
            model.addAttribute("warning",
                    "Ошибка загрузки");
            return "redirect:/files" + projectId;
        }
        model.addAttribute("message",
                "Вы успешно загрузили файл '"
                        + file.getOriginalFilename() + "'");

        List<String> fileNames = new ArrayList<>();
        fileService.findFilesByProjectId(projectId).forEach(f -> fileNames.add(f.getFileName()));
        model.addAttribute("list", fileNames);
        model.addAttribute("projectNumber", projectId);
        return "redirect:/files"+ projectId;
    }

    @GetMapping(path = "/download{id}/{name}")
    public ResponseEntity<Resource> download
            (@PathVariable("id") int projectId, @PathVariable("name") String name, Model model) throws IOException {

        File foundFile = fileService.getFile(projectId, name);
        ByteArrayResource resource = new ByteArrayResource(foundFile.getFileAsArrayOfBytes());

        return ResponseEntity.ok().headers(this.headers(name))
                .contentType(MediaType.parseMediaType
                        ("application/octet-stream")).body(resource);
    }

    @PostMapping(path = "/delete{id}")
    public String delete(@PathVariable("id") int projectId, @RequestParam("name") String name) {
        List<File> foundFiles = fileService.findFilesByProjectId(projectId);
        for (File f :
                foundFiles) {
            if (f.getFileName().equals(name)) {
                fileService.removeFile(f.getId());
                return "redirect:/files" + projectId;
            }

        }
        return "redirect:/files" + projectId;
    }

    private HttpHeaders headers(String name) {

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + name);
        header.add("Cache-Control", "no-cache, no-store,"
                + " must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;

    }

}
