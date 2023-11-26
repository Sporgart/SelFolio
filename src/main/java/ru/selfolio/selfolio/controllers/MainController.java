package ru.selfolio.selfolio.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.selfolio.selfolio.models.AppUser;
import ru.selfolio.selfolio.models.Project;
import ru.selfolio.selfolio.security.AppUserDetails;
import ru.selfolio.selfolio.services.AppUserService;
import ru.selfolio.selfolio.services.ProjectService;

import java.util.List;

@Controller
public class MainController {
    private final AppUserService appUserService;
    private final ProjectService projectService;

    public MainController(AppUserService appUserService, ProjectService projectService) {
        this.appUserService = appUserService;
        this.projectService = projectService;
    }

    @GetMapping("/")
    public String getMainPage(Model model, @RequestParam(name = "search", required = false) String searchContext) {
        List<Project> allProjects = projectService.findAll();
        if (searchContext != null){
            allProjects = allProjects.stream()
                    .filter(project -> project.getTitle().toLowerCase()
                            .contains(searchContext.toLowerCase())
                            || project.getDescription().toLowerCase()
                            .contains(searchContext.toLowerCase())).toList();
        }

        model.addAttribute("projects", allProjects);
        return "index";
    }

    @GetMapping("/account")
    public String getAccount(Model model) {
        model.addAttribute("projects", appUserService.getCurrentUser().getProjects());
        return "account";
    }

    @GetMapping("/project{id}")
    public String getAccount(@PathVariable(value = "id", required = false) int id, Model model) {

        AppUser appUser = appUserService.getCurrentUser();
        List<Project> projectList = appUser.getProjects();
        for (Project p :
                projectList) {
            if (p.getId() == id) {
                model.addAttribute("project", p);
                model.addAttribute("contacts", null);
                return "project";
            }
        }
        Project foundProject = projectService.findProjectById(id);
        AppUser author = foundProject.getAppUser();
        model.addAttribute("contacts", author.getEmail());
        model.addAttribute("project", foundProject);
        return "project";
    }




    @GetMapping("/project{id}/edit")
    public String editProject(@PathVariable("id") int id, Model model) {
        AppUser appUser = appUserService.getCurrentUser();
        List<Project> projectList = appUser.getProjects();
        for (Project project :
                projectList) {
            if (id == project.getId()) {
                model.addAttribute("project", project);
                return "edit";
            }
        }
        return "error";
    }

    @PatchMapping("/project{id}/edit")
    public String updateProject(@ModelAttribute("project") Project project,
                                @PathVariable("id") int id) {
        projectService.updateProject(project, id);
        return "redirect:/account";
    }


    @GetMapping("/project/new")
    public String addNewProject(@ModelAttribute("project") Project project) {
        return "new";
    }

    @PostMapping("/project/new")
    public String saveNewProject(@ModelAttribute("project") Project project) {
        AppUser appUser = appUserService.getCurrentUser();
        projectService.saveProject(project, appUser);
        return "redirect:/account";
    }
    @GetMapping("/project{id}/delete")
    public String deleteProject(@PathVariable("id") int id){
        projectService.removeProject(id);
        return "redirect:/account";
    }
}
