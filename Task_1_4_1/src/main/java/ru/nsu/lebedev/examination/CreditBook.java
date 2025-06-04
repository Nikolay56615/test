package ru.nsu.lebedev.examination;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a student's credit book for storing grades.
 */
public abstract class CreditBook {
    private static int freeId = 0;
    private final int id;
    private final Student student;
    private final Set<Grade> grades = new HashSet<>();

    /**
     * Constructs a CreditBook object.
     *
     * @param student the student owning this credit book
     */
    protected CreditBook(Student student) {
        this.id = getFreeId();
        this.student = student;
    }

    /**
     * Returns the student associated with this credit book.
     *
     * @return the student
     */
    protected Student getStudent() {
        return student;
    }

    /**
     * Returns freeId.
     *
     * @return freeId
     */
    private static int getFreeId() {
        return freeId++;
    }

    /**
     * Adds a grade for a specific academic discipline.
     *
     * @param value      the grade value
     * @param discipline the academic discipline
     * @return the added grade or null if the grade already exists
     */
    public Grade addGrade(int value, AcademicDiscipline discipline) {
        Grade grade = new Grade(value, student.getSemesterNumber(), discipline);
        if (grades.add(grade)) {
            return grade;
        }
        return null;
    }

    /**
     * Returns the set of all grades.
     *
     * @return the grades
     */
    Set<Grade> getGrades() {
        return grades;
    }

    /**
     * Calculates the average grade.
     *
     * @return the average grade, or 0 if no grades exist
     */
    public double averageGrade() {
        if (grades.isEmpty()) {
            return 0;
        }
        return ((double) grades.stream().mapToInt(Grade::getValue).sum()) / grades.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreditBook that = (CreditBook) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Credit book " + id
                + "; student: " + student.toString();
    }
}