package com.spring.timeperiodscheduler.bean;

public class TimeTableValue {

  private String subject;
  private String teacher;

  public TimeTableValue(String subject, String teacher) {
    this.subject = subject;
    this.teacher = teacher;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getTeacher() {
    return teacher;
  }

  public void setTeacher(String teacher) {
    this.teacher = teacher;
  }

  @Override
  public String toString() {
    return "[" + subject + ", " + teacher + ']';
  }
}
