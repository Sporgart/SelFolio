package ru.selfolio.selfolio.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_file")
@Setter
@Getter
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private int id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file")
    private byte[] fileAsArrayOfBytes;

    @ManyToOne
    @JoinColumn(name = "project_ref_id", referencedColumnName = "project_id")
    private Project project;

}
