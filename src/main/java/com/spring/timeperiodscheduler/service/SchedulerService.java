package com.spring.timeperiodscheduler.service;

import com.spring.timeperiodscheduler.bean.Class;
import com.spring.timeperiodscheduler.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class SchedulerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);

  private List<Subject> subjectList;
  private List<Class> classList;
  private List<Teacher> teacherList;
  private Map<TimeTableKey, Subject> defaultSubjects;
  private List<Teacher.DayPeriodKey> preBuildDayPeriodKeys;
  private Map<Integer, Set<Subject>> daySubjectsDefaultsMap;
  private Set<Subject> secondarySubjects;
  private int numDays;
  private int numPeriodsPerDay;
  private int maxClassRetryCount = 25;  //25
  private int maxFullRetryCount = 15000; // 15000
  private Map<String, List<List<TimeTableValue>>> result;
  private Map<String, List<List<String>>> teacherResult;

  public void initialize() {
    this.numDays = 6;
    this.numPeriodsPerDay = 4;

    preBuildDayPeriodKeys = new ArrayList<>();
    for (int i=1; i <= numDays; i++) {
      for (int j = 1; j <= numPeriodsPerDay; j++) {
        preBuildDayPeriodKeys.add(new Teacher.DayPeriodKey(i, j));
      }
    }

    Subject maths = new Subject("Maths");
    Subject english = new Subject("English");
    Subject evs = new Subject("EVS");
    Subject hindi = new Subject("Hindi");

    Subject library = new Subject("Library");
    Subject gk = new Subject("GK");
    Subject cca = new Subject("Co-curricular");
    Subject pt = new Subject("PT");
    Subject it = new Subject("IT");
    Subject ac = new Subject("Art & Craft");
    Subject music = new Subject("Music");

    subjectList = List.of(maths, hindi, english, evs, gk, cca, pt, it, ac, music, library);
    subjectList = new ArrayList<>(subjectList);

    Teacher t1 = new Teacher("T1", 0, 24, Set.of(maths, gk, cca, pt, it, ac, music, library));
    Teacher t2 = new Teacher("T2", 0, 24, Set.of(hindi, gk, cca, pt, it, ac, music, library));
    Teacher t3 = new Teacher("T3", 0, 24, Set.of(english, gk, cca, pt, it, ac, music, library));
    Teacher t4 = new Teacher("T4", 0, 24, Set.of(evs, gk, cca, pt, it, ac, music, library));
    Teacher t5 = new Teacher("T5", 0, 24, Set.of(maths, gk, cca, pt, it, ac, music, library));
    Teacher t6 = new Teacher("T6", 0, 24, Set.of(hindi, gk, cca, pt, it, ac, music, library));
    Teacher t7 = new Teacher("T7", 0, 24, Set.of(english, gk, cca, pt, it, ac, music, library));
    Teacher t8 = new Teacher("T8", 0, 24, Set.of(evs, gk, cca, pt, it, ac, music, library));
    Teacher t9 = new Teacher("T9", 0, 24, Set.of(maths, gk, cca, pt, it, ac, music, library));
    Teacher t10 = new Teacher("T10", 0, 24, Set.of(hindi, gk, cca, pt, it, ac, music, library));
    Teacher t11 = new Teacher("T11", 0, 24, Set.of(english, gk, cca, pt, it, ac, music, library));
    Teacher t12 = new Teacher("T12", 0, 24, Set.of(evs, gk, cca, pt, it, ac, music, library));
    teacherList = List.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
//    teacherList = List.of(t1, t2, t3, t4, t5, t6, t7, t8);
//    teacherList = List.of(t1, t2, t3, t4);
    teacherList = new ArrayList<>(teacherList);

    // Remove CCA since its a default
    Class c1 = new Class("C1", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, library, 1, it, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c1.setSubjectCountMap(new HashMap<>(c1.getSubjectCountMap()));
    c1.getSubjectCountMap().put(cca, 1);
    Class c2 = new Class("C2", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, it, 1, library, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c2.setSubjectCountMap(new HashMap<>(c2.getSubjectCountMap()));
    c2.getSubjectCountMap().put(cca, 1);
    Class c3 = new Class("C3", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, it, 1, library, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c3.setSubjectCountMap(new HashMap<>(c3.getSubjectCountMap()));
    c3.getSubjectCountMap().put(cca, 1);
    Class c4 = new Class("C4", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, it, 1, library, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c4.setSubjectCountMap(new HashMap<>(c4.getSubjectCountMap()));
    c4.getSubjectCountMap().put(cca, 1);
    Class c5 = new Class("C5", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, it, 1, library, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c5.setSubjectCountMap(new HashMap<>(c5.getSubjectCountMap()));
    c5.getSubjectCountMap().put(cca, 1);
    Class c6 = new Class("C6", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, it, 1, library, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c6.setSubjectCountMap(new HashMap<>(c6.getSubjectCountMap()));
    c6.getSubjectCountMap().put(cca, 1);
    Class c7 = new Class("C7", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, it, 1, library, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c7.setSubjectCountMap(new HashMap<>(c7.getSubjectCountMap()));
    c7.getSubjectCountMap().put(cca, 1);
    Class c8 = new Class("C8", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, it, 1, library, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c8.setSubjectCountMap(new HashMap<>(c8.getSubjectCountMap()));
    c8.getSubjectCountMap().put(cca, 1);
    Class c9 = new Class("C9", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, it, 1, library, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c9.setSubjectCountMap(new HashMap<>(c9.getSubjectCountMap()));
    c9.getSubjectCountMap().put(cca, 1);
    Class c10 = new Class("C10", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, it, 1, library, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c10.setSubjectCountMap(new HashMap<>(c10.getSubjectCountMap()));
    c10.getSubjectCountMap().put(cca, 1);
    Class c11 = new Class("C11", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, it, 1, library, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c11.setSubjectCountMap(new HashMap<>(c11.getSubjectCountMap()));
    c11.getSubjectCountMap().put(cca, 1);
    Class c12 = new Class("C12", Map.of(english, 4, hindi, 4, maths, 5, evs, 4, gk, 1, pt, 1, ac, 1, music, 1, it, 1, library, 1),
        Set.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    c12.setSubjectCountMap(new HashMap<>(c12.getSubjectCountMap()));
    c12.getSubjectCountMap().put(cca, 1);

    classList = List.of(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11);
//    classList = List.of(c1, c2, c3, c4, c5, c6, c7, c8);

    defaultSubjects = new HashMap<>();
    defaultSubjects.put(new TimeTableKey(c1.getName(), numDays, numPeriodsPerDay), cca);
    defaultSubjects.put(new TimeTableKey(c2.getName(), numDays, numPeriodsPerDay), cca);
    defaultSubjects.put(new TimeTableKey(c3.getName(), numDays, numPeriodsPerDay), cca);
    defaultSubjects.put(new TimeTableKey(c4.getName(), numDays, numPeriodsPerDay), cca);
    defaultSubjects.put(new TimeTableKey(c5.getName(), numDays, numPeriodsPerDay), cca);
    defaultSubjects.put(new TimeTableKey(c6.getName(), numDays, numPeriodsPerDay), cca);
    defaultSubjects.put(new TimeTableKey(c7.getName(), numDays, numPeriodsPerDay), cca);
    defaultSubjects.put(new TimeTableKey(c8.getName(), numDays, numPeriodsPerDay), cca);
    defaultSubjects.put(new TimeTableKey(c9.getName(), numDays, numPeriodsPerDay), cca);
    defaultSubjects.put(new TimeTableKey(c10.getName(), numDays, numPeriodsPerDay), cca);
    defaultSubjects.put(new TimeTableKey(c11.getName(), numDays, numPeriodsPerDay), cca);
    defaultSubjects.put(new TimeTableKey(c12.getName(), numDays, numPeriodsPerDay), cca);

    daySubjectsDefaultsMap = new HashMap<>();
    daySubjectsDefaultsMap.put(6, Set.of(maths, evs, english, hindi, cca, gk));

    secondarySubjects = Set.of(library, it, music, ac, pt, cca);

    //TODO Improvements
    // Rules (CCA to be clubbed with 3 studying periods)
  }

  public Map<String, Object> createTimeTable() throws Exception {
    AtomicBoolean redo = new AtomicBoolean(true);
    result = new LinkedHashMap<>();
    long starttime = System.currentTimeMillis();
    int tryCount = 0;
    int classTryCount = 0;
    int totalClassTryCount = 0;
    while (redo.get()) {
      redo.set(false);
      tryCount++;
      if (tryCount > maxFullRetryCount) {
        LOGGER.error("Couldn't schedule, timed out! ");
        break;
      }
      result = new LinkedHashMap<>();
      initialize();
      for (Class classObj : classList) {
        boolean redoClassOnly = true;
        classTryCount = 0;
        // Take original copy in case of rollback during redo
        Class originalClass = classObj.copy();
        List<Teacher> originalTeacherList = new ArrayList<>();
        // Ensure every week has atleast 1 non-important class
        Map<Integer, Set<Subject>> currDaySubjectsCountMap = new HashMap<>();
        for (Teacher teacher : teacherList)
          originalTeacherList.add(teacher.copy());
        while (redoClassOnly) {
          redoClassOnly = false;
          classTryCount++;
          totalClassTryCount++;
          if (classTryCount > maxClassRetryCount) {
            LOGGER.info("Couldn't schedule for class, timed out! Attempting to redo the full timetable.");
            redo.set(true);
            break;
          }
          Map<TimeTableKey, TimeTableValue> timeTable = new TreeMap<>((o1, o2) -> {
            if (o1.getDayNum() < o2.getDayNum()) {
              return -1;
            } else if (o1.getDayNum() > o2.getDayNum()) {
              return 1;
            } else {
              if (o1.getPeriodNum() < o2.getPeriodNum()) {
                return -1;
              } else if (o1.getPeriodNum() > o2.getPeriodNum()) {
                return 1;
              } else {
                return 0;
              }
            }
          });
          Map<Subject, Integer> sortedSubjectCountMap = sortByValue(classObj.getSubjectCountMap());
          for (Subject subject : sortedSubjectCountMap.keySet()) {

            // Find eligible teachers to teach {subject} in {class}
            Class finalClassObj = classObj;
            Collections.shuffle(teacherList);
            LOGGER.info("Looking for teachers to teach: " + subject + " in class : " + classObj.getName());
            List<Teacher> subjectTeachers = teacherList.stream().filter(teacher ->
              teacher.getSubjectList().contains(subject) && teacher.getMaxPeriods() > 0 && finalClassObj.isEligible(teacher))
                .sorted(Comparator.comparing(Teacher::getMinPeriods)).collect(Collectors.toList());
            LOGGER.info("Teachers found: " + subjectTeachers);

            // Find optimally available teacher to teach {subject} in {class}
            int subjectRequiredCount = sortedSubjectCountMap.get(subject);
            LOGGER.info("Required num of periods in a week for subject : " + subjectRequiredCount);
            Set<Integer> bookedDays = new HashSet<>();
            boolean foundTeacher = false;
            Map<Teacher.DayPeriodKey, TimeTableKey> eligibleDayPeriods = new LinkedHashMap<>();
            List<Teacher.DayPeriodKey> dayPeriodKeys;

            // Load the correct dayPeriodList based on defaults
            if (defaultSubjects.containsValue(subject)) {
              Class finalClassObj1 = classObj;
              Map.Entry<TimeTableKey, Subject> entrySet = defaultSubjects.entrySet().stream().filter(entry ->
                  entry.getValue().equals(subject) && entry.getKey().getClassName().equals(finalClassObj1.getName())).findFirst().orElse(null);
              if (entrySet != null) {
                dayPeriodKeys = new ArrayList<>(List.of(new Teacher.DayPeriodKey(entrySet.getKey().getDayNum(), entrySet.getKey().getPeriodNum())));
              } else {
                dayPeriodKeys = this.preBuildDayPeriodKeys;
              }
            } else {
              dayPeriodKeys = this.preBuildDayPeriodKeys;
            }
            for (Teacher teacher : subjectTeachers) {
              // Maintain books days to avoid same classes on same day
              bookedDays.clear();
              eligibleDayPeriods.clear();
              Collections.shuffle(dayPeriodKeys);
              dayPeriodKeys.sort((o1,o2) ->{
                if (currDaySubjectsCountMap.containsKey(o1.getDayNum()) && currDaySubjectsCountMap.containsKey(o2.getDayNum())) {
                  Integer a = currDaySubjectsCountMap.get(o1.getDayNum()).size();
                  Integer b = currDaySubjectsCountMap.get(o2.getDayNum()).size();
                  return a.compareTo(b);
                } else if (currDaySubjectsCountMap.containsKey(o1.getDayNum())) {
                  return 1;
                } else if (currDaySubjectsCountMap.containsKey(o2.getDayNum())) {
                  return -1;
                }
                return 0;
              });
              LOGGER.info("Day Subjects Set: " + currDaySubjectsCountMap);
              LOGGER.info("SORTED day periods: " + dayPeriodKeys);
              for (Teacher.DayPeriodKey dayPeriodKey : dayPeriodKeys) {
                // Check if period is already there for the day and if day has a specific subject pool
                LOGGER.info("Checking day period: " + dayPeriodKey);
                if (bookedDays.contains(dayPeriodKey.getDayNum()) ||
                    (daySubjectsDefaultsMap.containsKey(dayPeriodKey.getDayNum()) && !daySubjectsDefaultsMap.get(dayPeriodKey.getDayNum()).contains(subject)) ||
                    (currDaySubjectsCountMap.containsKey(dayPeriodKey.getDayNum()) &&
                      (
                        (secondarySubjects.contains(subject) && currDaySubjectsCountMap.get(dayPeriodKey.getDayNum()).stream().filter(subj -> secondarySubjects.contains(subj)).count() == 1
                        )
                     || (!secondarySubjects.contains(subject) && currDaySubjectsCountMap.get(dayPeriodKey.getDayNum()).size() == 3  && currDaySubjectsCountMap.get(dayPeriodKey.getDayNum()).stream().noneMatch(subj -> secondarySubjects.contains(subj))
                        )
                      )
                    )
                  )
                  continue;
                TimeTableKey key = new TimeTableKey(classObj.getName(), dayPeriodKey.getDayNum(), dayPeriodKey.getPeriodNum());
                if (!timeTable.containsKey(key) && (!defaultSubjects.containsKey(key) || defaultSubjects.containsValue(subject))
                    && teacher.isAvailable(key) && classObj.couldBeSubjectTeacher(subject, teacher)) {
                  eligibleDayPeriods.put(dayPeriodKey, key);
                  bookedDays.add(dayPeriodKey.getDayNum());
                }
              }
              // Found a valid teacher now assign to timetable
              LOGGER.info("Teacher: " + teacher.getName() + " has eligible count of " + eligibleDayPeriods.size() + " for subject " + subject);
              if (eligibleDayPeriods.size() >= subjectRequiredCount) {
                foundTeacher = true;
                int numAssigned = 0;
                for (TimeTableKey timeTableKey : eligibleDayPeriods.values()) {
                  LOGGER.info("Teacher " + teacher.getName() + " assigned to " + subject + " on " + timeTableKey);
                  timeTable.put(timeTableKey, new TimeTableValue(subject.getName(), teacher.getName()));
                  teacher.setMaxPeriods(teacher.getMaxPeriods() - 1);
                  teacher.setMinPeriods(teacher.getMinPeriods() + 1);
                  teacher.addToSchedule(timeTableKey);
                  // Update day subjects map
                  if (!currDaySubjectsCountMap.containsKey(timeTableKey.getDayNum())) {
                    currDaySubjectsCountMap.put(timeTableKey.getDayNum(), new HashSet<>());
                  }
                  currDaySubjectsCountMap.get(timeTableKey.getDayNum()).add(subject);
//                  LOGGER.info("Updated day subject count map : " + currDaySubjectsCountMap);
                  numAssigned++;
                  // Don't assign more than needed
                  if (numAssigned == subjectRequiredCount)
                    break;
                }
                // Don't iterate through other eligible teachers after finding one
                break;
              }
            } // Eligible teachers FOR loop END
            if (!foundTeacher) {
              // Attempt for class retry if this is the case
              LOGGER.info("Oops no teacher found, retrying the scheduling!");
              LOGGER.info(timeTable.toString());
              redoClassOnly = true;
              // Don't process any more subjects, need to redo the class
              break;
            }
          } // Class Subject FOR loop end
          if (redo.get()) {
            result.clear();
          } else if (!redoClassOnly) {
            result.put(classObj.getName(), format(timeTable, this.numDays, this.numPeriodsPerDay));
          } else {
            classObj = originalClass.copy();
            teacherList = new ArrayList<>();
            for (Teacher teacher : originalTeacherList)
              teacherList.add(teacher.copy());
            currDaySubjectsCountMap.clear();
          }
        } // Redo class If end
        // Don't process any more subjects, need to redo the timetable
        if (redo.get())
          break;
      } // Class list for loop end
    } // Redo time table If end
    LOGGER.warn("Classes: " + classList.size() + " | " + "Full Redo: " + tryCount + " | " + "Class Redo: " + totalClassTryCount + " | " + "Scheduling Time (ms): " + (System.currentTimeMillis() - starttime));

    // Get POJO for teacher view
    teacherResult = new LinkedHashMap<>();
    for (Teacher teacher : teacherList) {
      teacherResult.put(teacher.getName(), new ArrayList<>());
      for (int day=1; day <= this.numDays; day++) {
        teacherResult.get(teacher.getName()).add(new ArrayList<>());
        for (int period=1; period <= this.numPeriodsPerDay; period++) {
          teacherResult.get(teacher.getName()).get(day-1).add("");
        }
      }
    }

    for (String className : result.keySet()) {
      int day=0;
      List<List<TimeTableValue>> outer = result.get(className);
      for (List<TimeTableValue> inner : outer) {
        int periodnum=0;
        for (TimeTableValue key : inner) {
          teacherResult.get(key.getTeacher()).get(day).set(periodnum, key.getSubject() + " at " + className);
          periodnum++;
        }
        day++;
      }
    }

    // Send back the result
    Map<String, Object> finalResult = new HashMap<>();
    finalResult.put("timetables", result);
    finalResult.put("teachers", teacherResult);
    return finalResult;
  }

  // function to sort hashmap by values
  public static Map<Subject, Integer> sortByValue(Map<Subject, Integer> hm)
  {
    // Create a list from elements of HashMap
    List<Map.Entry<Subject, Integer> > list =
        new LinkedList<Map.Entry<Subject, Integer> >(hm.entrySet());

    // Sort the list
    Collections.sort(list, new Comparator<Map.Entry<Subject, Integer> >() {
      public int compare(Map.Entry<Subject, Integer> o2,
                         Map.Entry<Subject, Integer> o1)
      {
        return (o1.getValue()).compareTo(o2.getValue());
      }
    });

    // put data from sorted list to hashmap
    Map<Subject, Integer> temp = new LinkedHashMap<Subject, Integer>();
    for (Map.Entry<Subject, Integer> aa : list) {
      temp.put(aa.getKey(), aa.getValue());
    }
    return temp;
  }

  private static List<List<TimeTableValue>> format(Map<TimeTableKey, TimeTableValue>timeTableMap, int days, int periods) {
    List<List<TimeTableValue>> res = new ArrayList<>();
    for (int day=1; day <= days; day++) {
      res.add(new ArrayList<>());
    }
    for (TimeTableKey key : timeTableMap.keySet()) {
      res.get(key.getDayNum()-1).add(timeTableMap.get(key));
    }
    return res;
  }

  public Map<String, List<List<TimeTableValue>>> getResult() {
    return result;
  }

  public Map<String, List<List<String>>> getTeacherResult() {
    return teacherResult;
  }
}
