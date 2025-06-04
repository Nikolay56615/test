package ru.nsu.lebedev.examination;

import java.util.Objects;

/**
 * Grade class.
 * Represents a grade assigned to a student for a specific subject in a particular semester.
 */
public final class Grade {
    public static final int MIN_VALUE = 2; // Minimum allowable grade value
    public static final int MAX_VALUE = 5; // Maximum allowable grade value
    private final int value;
    private final int semester;
    private final AcademicDiscipline discipline;
    private final DisciplineType disciplineType;

    /**
     * Constructs a Grade object with the specified value, semester, and subject.
     *
     * @param value    the grade value (clamped between MIN_VALUE and MAX_VALUE)
     * @param semester the semester number
     * @param subject  the subject associated with the grade
     */
    public Grade(int value, int semester,
                 AcademicDiscipline subject) {
        this.value = Math.max(MIN_VALUE, Math.min(value, MAX_VALUE));
        this.semester = semester;
        this.discipline = subject;
        this.disciplineType = subject.getType();
    }

    /**
     * Gets the type of the discipline associated with this grade.
     *
     * @return the discipline type, which determines the assessment format.
     */
    public DisciplineType getDisciplineType() {
        return disciplineType;
    }

    /**
     * Determines if this grade was awarded later than another grade for the same subject.
     *
     * @param otherGrade another Grade object
     * @return true if this grade was awarded in a later semester for the same subject
     */
    public boolean isAwardedLaterThan(Grade otherGrade) {
        return this.discipline.equals(otherGrade.getAcademicDiscipline())
                && this.semester > otherGrade.getSemester();
    }

    /**
     * Provides a string representation of the grade.
     *
     * @return a string describing the subject, semester, and grade value
     */
    @Override
    public String toString() {
        return discipline.getDisplayName() + " in semester " + semester + ": " + value;
    }

    /**
     * Checks if two grades are equivalent based on their semester and subject.
     * Grade values are ignored because two grades for
     * the same semester and subject cannot differ.
     *
     * @param o the reference object with which to compare
     * @return true if the objects have the same semester and subject
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Grade grade = (Grade) o;
        return semester == grade.semester && discipline.equals(grade.discipline);
    }

    /**
     * Computes a hash code based on the semester and subject.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(semester, discipline);
    }

    /**
     * Retrieves the grade value.
     *
     * @return the numeric grade value
     */
    public int getValue() {
        return value;
    }

    /**
     * Retrieves the semester in which the grade was awarded.
     *
     * @return the semester number
     */
    public int getSemester() {
        return semester;
    }

    /**
     * Retrieves the AcademicDiscipline associated with the grade.
     *
     * @return the AcademicDiscipline object
     */
    public AcademicDiscipline getAcademicDiscipline() {
        return discipline;
    }
}

