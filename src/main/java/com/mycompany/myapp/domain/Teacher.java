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
 * 老师
 */
@Schema(description = "老师")
@Entity
@Table(name = "teacher")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Teacher implements Serializable {

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

    /**
     * 年龄
     */
    @Schema(description = "年龄", required = true)
    @NotNull
    @Column(name = "age", nullable = false)
    private Integer age;

    /**
     * 擅长专业
     */
    @Schema(description = "擅长专业", required = true)
    @NotNull
    @Column(name = "professional", nullable = false)
    private String professional;

    /**
     * 老师关联课程
     */
    @Schema(description = "老师关联课程")
    @ManyToMany
    @JoinTable(
        name = "rel_teacher__course",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classds", "classrooms", "teachers" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Teacher id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Teacher name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public Teacher age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getProfessional() {
        return this.professional;
    }

    public Teacher professional(String professional) {
        this.setProfessional(professional);
        return this;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Teacher courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Teacher addCourse(Course course) {
        this.courses.add(course);
        course.getTeachers().add(this);
        return this;
    }

    public Teacher removeCourse(Course course) {
        this.courses.remove(course);
        course.getTeachers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Teacher)) {
            return false;
        }
        return id != null && id.equals(((Teacher) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Teacher{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", professional='" + getProfessional() + "'" +
            "}";
    }
}
