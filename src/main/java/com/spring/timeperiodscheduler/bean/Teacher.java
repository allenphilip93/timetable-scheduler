package com.spring.timeperiodscheduler.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Teacher {

  private String name;
  private int minPeriods;
  private int maxPeriods;
  private Set<Subject> subjectList;
  private Set<DayPeriodKey> dayPeriodSet;
  private Set<ClassDayKey> classDaySet;

  public Teacher(String name, int minPeriods, int maxPeriods, Set<Subject> subjectList) {
    this.name = name;
    this.minPeriods = minPeriods;
    this.maxPeriods = maxPeriods;
    this.subjectList = subjectList;
    dayPeriodSet = new HashSet<>();
    classDaySet = new HashSet<>();
  }

  public boolean isAvailable(TimeTableKey timeTableKey) {
    DayPeriodKey dayPeriodKey = new DayPeriodKey(timeTableKey.getDayNum(), timeTableKey.getPeriodNum());
    ClassDayKey classDayKey = new ClassDayKey(timeTableKey.getClassName(), timeTableKey.getDayNum());
    return !dayPeriodSet.contains(dayPeriodKey) && !classDaySet.contains(classDayKey);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getMinPeriods() {
    return minPeriods;
  }

  public void setMinPeriods(int minPeriods) {
    this.minPeriods = minPeriods;
  }

  public int getMaxPeriods() {
    return maxPeriods;
  }

  public void setMaxPeriods(int maxPeriods) {
    this.maxPeriods = maxPeriods;
  }

  public Set<Subject> getSubjectList() {
    return subjectList;
  }

  public void setSubjectList(Set<Subject> subjectList) {
    this.subjectList = subjectList;
  }

  public void addToSchedule(TimeTableKey timeTableKey) {
    DayPeriodKey dayPeriodKey = new DayPeriodKey(timeTableKey.getDayNum(), timeTableKey.getPeriodNum());
    ClassDayKey classDayKey = new ClassDayKey(timeTableKey.getClassName(), timeTableKey.getDayNum());
    this.dayPeriodSet.add(dayPeriodKey);
    this.classDaySet.add(classDayKey);
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Teacher teacher = (Teacher) o;
    return name.equals(teacher.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  public Teacher copy() {
    Teacher clone = new Teacher(this.name, this.minPeriods, this.maxPeriods, this.subjectList);
    clone.dayPeriodSet = this.dayPeriodSet.stream().collect(Collectors.toSet());
    clone.classDaySet = this.classDaySet.stream().collect(Collectors.toSet());
    return clone;
  }

  // Ensure teacher don't each same period for different section on same day
  public static class DayPeriodKey {
    private int dayNum;
    private int periodNum;

    public DayPeriodKey(int dayNum, int periodNum) {
      this.dayNum = dayNum;
      this.periodNum = periodNum;
    }

    public int getDayNum() {
      return dayNum;
    }

    public void setDayNum(int dayNum) {
      this.dayNum = dayNum;
    }

    public int getPeriodNum() {
      return periodNum;
    }

    public void setPeriodNum(int periodNum) {
      this.periodNum = periodNum;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      DayPeriodKey that = (DayPeriodKey) o;
      return dayNum == that.dayNum && periodNum == that.periodNum;
    }

    @Override
    public int hashCode() {
      return Objects.hash(dayNum, periodNum);
    }

    @Override
    public String toString() {
      return "{" +
          "dayNum=" + dayNum +
          ", periodNum=" + periodNum +
          '}';
    }
  }

  // Ensure teacher don't each same class twice in a day
  private static class ClassDayKey {
    private String className;
    private int dayNum;

    public ClassDayKey(String className, int dayNum) {
      this.className = className;
      this.dayNum = dayNum;
    }

    public String getClassName() {
      return className;
    }

    public void setClassName(String className) {
      this.className = className;
    }

    public int getDayNum() {
      return dayNum;
    }

    public void setDayNum(int dayNum) {
      this.dayNum = dayNum;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ClassDayKey that = (ClassDayKey) o;
      return dayNum == that.dayNum && className.equals(that.className);
    }

    @Override
    public int hashCode() {
      return Objects.hash(className, dayNum);
    }
  }
}
