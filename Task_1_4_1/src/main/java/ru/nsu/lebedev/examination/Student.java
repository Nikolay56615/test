package ru.nsu.lebedev.examination;

import java.util.Objects;

/**
 * Represents a student with personal details and a credit book.
 */
public class Student {
    static final int SEMESTER_BASE_NUMBER = 1;
    private final String name;
    private final String patronymic;
    private final String surname;
    private final StudentCreditBook creditBook;
    private int semesterNumber = SEMESTER_BASE_NUMBER;

    /**
     * Constructs a Student object.
     *
     * @param name       the first name of the student
     * @param patronymic the patronymic of the student
     * @param surname    the surname of the student
     */
    public Student(String name, String patronymic, String surname) {
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.creditBook = new StudentCreditBook(this);
    }

    /**
     * Returns the student's credit book.
     *
     * @return the credit book of the student
     */
    public StudentCreditBook getCreditBook() {
        return creditBook;
    }

    /**
     * Returns the current semester number.
     *
     * @return the current semester number
     */
    public int getSemesterNumber() {
        return semesterNumber;
    }

    /**
     * Advances the student to the next semester.
     *
     * @return the updated semester number
     */
    public int finishSemester() {
        return ++semesterNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(name, student.name)
                && Objects.equals(surname, student.surname)
                && Objects.equals(patronymic, student.patronymic)
                && Objects.equals(creditBook, student.creditBook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, patronymic, surname, creditBook);
    }

    @Override
    public String toString() {
        return "Student " + name + " " + patronymic + " " + surname
                + " on semester " + semesterNumber;
    }
}
