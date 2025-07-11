package com.demo.authentication.filehandling.task.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.authentication.filehandling.task.Entity.FormData;

public interface FormDataRepository extends JpaRepository<FormData, Integer> {

}
