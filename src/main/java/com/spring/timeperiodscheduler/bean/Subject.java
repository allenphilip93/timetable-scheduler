package com.spring.timeperiodscheduler.bean;

import org.springframework.stereotype.Component;

import java.util.Objects;

public class Subject {
  private String name;

  public Subject(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Subject subject = (Subject) o;
    return name.equals(subject.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return "Subject{" +
        "name='" + name + '\'' +
        '}';
  }
}
