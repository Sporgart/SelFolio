package ru.selfolio.selfolio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.selfolio.selfolio.models.File;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer> {

    List<File> findAllByProjectId(int id);
}
