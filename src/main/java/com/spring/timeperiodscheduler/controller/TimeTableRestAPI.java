package com.spring.timeperiodscheduler.controller;

import com.spring.timeperiodscheduler.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/timetable")
public class TimeTableRestAPI {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableRestAPI.class);

  @Autowired
  private SchedulerService schedulerService;

  @GetMapping
  public Map<String, Object> getTimeTable() throws Exception {
    LOGGER.info("Calling scheduler service");
    return schedulerService.createTimeTable();
  }

}
