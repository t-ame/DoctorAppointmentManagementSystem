package com.java.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.java.dto.Specialty;

@RepositoryRestResource(path = "specialties", exported = true)
public interface SpecialtyRepository extends JpaRepository<Specialty, String> {

}
