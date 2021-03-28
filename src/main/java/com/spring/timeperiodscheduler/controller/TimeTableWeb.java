package com.spring.timeperiodscheduler.controller;

import com.spring.timeperiodscheduler.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/timetable")
public class TimeTableWeb {

  private final SchedulerService schedulerService;

  @Autowired
  public TimeTableWeb(SchedulerService schedulerService) {
    this.schedulerService = schedulerService;
  }

  @GetMapping
  public String getTimeTable(Model model) throws Exception {
    this.schedulerService.createTimeTable();
    model.addAttribute("timetables", this.schedulerService.getResult());
    model.addAttribute("teachers", this.schedulerService.getTeacherResult());
    return "timetable";
  }
}
