package ru.selfolio.selfolio.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.selfolio.selfolio.models.AppUser;
import ru.selfolio.selfolio.models.Project;
import ru.selfolio.selfolio.repositories.ProjectRepository;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AppUserService appUserService;
    public ProjectService(ProjectRepository projectRepository, AppUserService appUserService) {
        this.projectRepository = projectRepository;
        this.appUserService = appUserService;
    }

    public Project findProjectById(int id){
        return projectRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveProject(Project project){
        projectRepository.save(project);
    }
    @Transactional
    public void saveProject(Project project, AppUser appUser){
        project.setAppUser(appUserService.getCurrentUser());
        projectRepository.save(project);
    }

    @Transactional
    public void updateProject(Project project, int id){
        Project foundProject = findProjectById(id);
        foundProject.setTitle(project.getTitle());
        foundProject.setDescription(project.getDescription());
        foundProject.setText_project(project.getText_project());
        saveProject(foundProject);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public void removeProject(int id){
        projectRepository.deleteById(id);
    }
}
