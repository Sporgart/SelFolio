package ru.selfolio.selfolio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.selfolio.selfolio.models.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
