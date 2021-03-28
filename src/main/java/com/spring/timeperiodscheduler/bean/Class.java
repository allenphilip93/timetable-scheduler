package com.spring.timeperiodscheduler.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Class {

  private String name;
  private Map<Subject, Integer> subjectCountMap;
  private Set<Teacher> eligibleTeachers;
  private Map<Subject, String> subjectTeacherMap;

  public Class(String name, Map<Subject, Integer> subjectCountMap, Set<Teacher> eligibleTeachers) {
    this.name = name;
    this.subjectCountMap = subjectCountMap;
    this.eligibleTeachers = eligibleTeachers;
    this.subjectTeacherMap = new HashMap<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<Subject, Integer> getSubjectCountMap() {
    return subjectCountMap;
  }

  public void setSubjectCountMap(Map<Subject, Integer> subjectCountMap) {
    this.subjectCountMap = subjectCountMap;
  }

  public Set<Teacher> getEligibleTeachers() {
    return eligibleTeachers;
  }

  public void setEligibleTeachers(Set<Teacher> eligibleTeachers) {
    this.eligibleTeachers = eligibleTeachers;
  }

  public boolean isEligible(Teacher teacher) {
    return eligibleTeachers.contains(teacher);
  }

  public boolean couldBeSubjectTeacher(Subject subject, Teacher teacher) {
    if (subjectTeacherMap.containsKey(subject)) {
      return subjectTeacherMap.get(subject).equals(teacher.getName());
    }
    return true;
  }

  @Override
  public String toString() {
    return "Class{" +
        "name='" + name + '\'' +
        ", subjectCountMap=" + subjectCountMap +
        '}';
  }

  public Class copy() {
    Class clone = new Class(this.name, this.subjectCountMap, this.eligibleTeachers);
    clone.subjectTeacherMap = this.subjectTeacherMap.entrySet()
                              .stream()
                              //perform customization
                              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return clone;
  }
}
