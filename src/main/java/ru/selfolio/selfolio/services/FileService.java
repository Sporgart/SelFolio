package ru.selfolio.selfolio.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.selfolio.selfolio.models.File;
import ru.selfolio.selfolio.models.Project;
import ru.selfolio.selfolio.repositories.FileRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final ProjectService projectService;

    public FileService(FileRepository fileRepository, ProjectService projectService) {
        this.fileRepository = fileRepository;
        this.projectService = projectService;
    }

    public void saveFileInBD(MultipartFile uploadedFile, int projectId) throws IOException {
        Project refProject = projectService.findProjectById(projectId);
        File file = new File();
        file.setFileName(uploadedFile.getOriginalFilename());
        file.setFileAsArrayOfBytes(uploadedFile.getBytes());
        file.setProject(refProject);
        fileRepository.save(file);
    }


    public List<File> findFilesByProjectId(int id) {
        return fileRepository.findAllByProjectId(id);
    }

    public File getFile(int projectId, String fileName){
        Project project = projectService.findProjectById(projectId);
       return project.getFiles().stream()
               .filter(file -> file.getFileName().equals(fileName)).findFirst().orElse(null);
    }

    public void removeFile(int fileId){
        fileRepository.deleteById(fileId);
    }

}
