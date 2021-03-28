package com.spring.timeperiodscheduler.bean;

import java.util.Objects;

public class TimeTableKey {

  private String className;
  private int dayNum;
  private int periodNum;

  public TimeTableKey(String className, int dayNum, int periodNum) {
    this.className = className;
    this.dayNum = dayNum;
    this.periodNum = periodNum;
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
    TimeTableKey that = (TimeTableKey) o;
    return dayNum == that.dayNum && periodNum == that.periodNum && className.equals(that.className);
  }

  @Override
  public int hashCode() {
    return Objects.hash(className, dayNum, periodNum);
  }

  @Override
  public String toString() {
    return "[" +
        "className='" + className + '\'' +
        ", dayNum=" + dayNum +
        ", periodNum=" + periodNum +
        ']';
  }
}
