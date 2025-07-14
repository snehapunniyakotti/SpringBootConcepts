package com.demo.authentication.filehandling.task.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.authentication.filehandling.task.Entity.FileData;

public interface FileDataRepository extends JpaRepository<FileData, Integer>{

}
