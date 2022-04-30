package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 班级
 */
@Schema(description = "班级")
@Entity
@Table(name = "classd")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Classd implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * 名称
     */
    @Schema(description = "名称", required = true)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "classds")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classds", "classrooms", "teachers" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "classds")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classds" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Classd id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Classd name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        if (this.courses != null) {
            this.courses.forEach(i -> i.removeClassd(this));
        }
        if (courses != null) {
            courses.forEach(i -> i.addClassd(this));
        }
        this.courses = courses;
    }

    public Classd courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Classd addCourse(Course course) {
        this.courses.add(course);
        course.getClassds().add(this);
        return this;
    }

    public Classd removeCourse(Course course) {
        this.courses.remove(course);
        course.getClassds().remove(this);
        return this;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.removeClassd(this));
        }
        if (students != null) {
            students.forEach(i -> i.addClassd(this));
        }
        this.students = students;
    }

    public Classd students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public Classd addStudent(Student student) {
        this.students.add(student);
        student.getClassds().add(this);
        return this;
    }

    public Classd removeStudent(Student student) {
        this.students.remove(student);
        student.getClassds().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classd)) {
            return false;
        }
        return id != null && id.equals(((Classd) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classd{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
