package com.java.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.java.dto.Calendar;

@Repository
@Transactional
public interface CalendarRepository extends JpaRepository<Calendar, String> {

}
