package com.example.project_service.repository;

import com.example.project_service.entity.Project;
import com.example.project_service.Enum.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserId(Long userId);
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByUserIdAndStatus(Long userId, ProjectStatus status);
}
