package cz.cvut.fit.baklaal1.server.suite;

import cz.cvut.fit.baklaal1.entity.Assessment;
import cz.cvut.fit.baklaal1.entity.Student;
import cz.cvut.fit.baklaal1.entity.Teacher;
import cz.cvut.fit.baklaal1.entity.Work;
import cz.cvut.fit.baklaal1.model.data.helper.Grades;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public abstract class BasicTestSuite {

    protected <L extends Collection<Integer>> L fillIntegerCollectionUpTo(L collection, int limit) {
        for (int i = 0; i < limit; i++) {
            collection.add(i);
        }
        return collection;
    }

    protected <E, V> E setField(E entity, String fieldName, V fieldValue) {
        ReflectionTestUtils.setField(entity, fieldName, fieldValue);
        return entity;
    }

    protected <E, V, C extends Collection<E>> C setFieldForAll(C collection, String fieldName, V fieldValue) {
        for(E entity : collection) {
            setField(entity, fieldName, fieldValue);
        }
        return collection;
    }

    protected float generateAverageGrade(int i) {
        return Grades.A + (i * 0.1f) % Grades.E;
    }

    protected Student generateStudent(int i) {
        String username = "studentUsername" + "_" + this.hashCode() + "_" + i;
        String name = "studentName" + i;
        Timestamp birthdate = new Timestamp(i*10000000);
        float averageGrade = generateAverageGrade(i);
        Student student = new Student(username, name, birthdate, averageGrade);
        ReflectionTestUtils.setField(student, "id", i);
        return student;
    }

    protected Work generateWork(int i) {
        String title = "title" + i;
        String text = "text" + i;
        Set<Student> authors = new TreeSet<>();
        Assessment assessment = null;
        Work work = new Work(title, text, authors, assessment);
        ReflectionTestUtils.setField(work, "id", i);
        return work;
    }

    protected Teacher generateTeacher(int i) {
        String username = "teacherUsername" + "_" + this.hashCode() + "_" + i;
        String name = "teacherName" + i;
        Timestamp birthdate = new Timestamp(i*10000000);
        double wage = i * 10000;
        Teacher teacher = new Teacher(username, name, birthdate, wage);
        ReflectionTestUtils.setField(teacher, "id", i);
        return teacher;
    }

    protected int generateGrade(int i) {
        return Grades.A + i % Grades.F;
    }

    protected Assessment generateAssessment(int i) {
        int grade = generateGrade(i);
        Work work = new Work("title"+i, "text"+i);
        ReflectionTestUtils.setField(work, "id", i);
        Teacher teacher = new Teacher("username"+i, "name"+i, new Timestamp(1000000000*i), 10000d*i);
        ReflectionTestUtils.setField(teacher, "id", i);
        Assessment assessment = new Assessment(grade, work, teacher);
        ReflectionTestUtils.setField(assessment, "id", i);
        return assessment;
    }
}
