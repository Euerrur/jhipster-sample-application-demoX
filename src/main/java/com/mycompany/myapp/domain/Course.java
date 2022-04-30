package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 课程
 */
@Schema(description = "课程")
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

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
     * 简介
     */
    @Schema(description = "简介", required = true)
    @NotNull
    @Column(name = "introduce", nullable = false)
    private String introduce;

    /**
     * 上课时间
     */
    @Schema(description = "上课时间", required = true)
    @NotNull
    @Column(name = "class_time", nullable = false)
    private Instant classTime;

    /**
     * 课程关联班级
     */
    @Schema(description = "课程关联班级")
    @ManyToMany
    @JoinTable(
        name = "rel_course__classd",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "classd_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courses", "students" }, allowSetters = true)
    private Set<Classd> classds = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courses" }, allowSetters = true)
    private Set<Classroom> classrooms = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courses" }, allowSetters = true)
    private Set<Teacher> teachers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Course name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return this.introduce;
    }

    public Course introduce(String introduce) {
        this.setIntroduce(introduce);
        return this;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Instant getClassTime() {
        return this.classTime;
    }

    public Course classTime(Instant classTime) {
        this.setClassTime(classTime);
        return this;
    }

    public void setClassTime(Instant classTime) {
        this.classTime = classTime;
    }

    public Set<Classd> getClassds() {
        return this.classds;
    }

    public void setClassds(Set<Classd> classds) {
        this.classds = classds;
    }

    public Course classds(Set<Classd> classds) {
        this.setClassds(classds);
        return this;
    }

    public Course addClassd(Classd classd) {
        this.classds.add(classd);
        classd.getCourses().add(this);
        return this;
    }

    public Course removeClassd(Classd classd) {
        this.classds.remove(classd);
        classd.getCourses().remove(this);
        return this;
    }

    public Set<Classroom> getClassrooms() {
        return this.classrooms;
    }

    public void setClassrooms(Set<Classroom> classrooms) {
        if (this.classrooms != null) {
            this.classrooms.forEach(i -> i.removeCourse(this));
        }
        if (classrooms != null) {
            classrooms.forEach(i -> i.addCourse(this));
        }
        this.classrooms = classrooms;
    }

    public Course classrooms(Set<Classroom> classrooms) {
        this.setClassrooms(classrooms);
        return this;
    }

    public Course addClassroom(Classroom classroom) {
        this.classrooms.add(classroom);
        classroom.getCourses().add(this);
        return this;
    }

    public Course removeClassroom(Classroom classroom) {
        this.classrooms.remove(classroom);
        classroom.getCourses().remove(this);
        return this;
    }

    public Set<Teacher> getTeachers() {
        return this.teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        if (this.teachers != null) {
            this.teachers.forEach(i -> i.removeCourse(this));
        }
        if (teachers != null) {
            teachers.forEach(i -> i.addCourse(this));
        }
        this.teachers = teachers;
    }

    public Course teachers(Set<Teacher> teachers) {
        this.setTeachers(teachers);
        return this;
    }

    public Course addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getCourses().add(this);
        return this;
    }

    public Course removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getCourses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", introduce='" + getIntroduce() + "'" +
            ", classTime='" + getClassTime() + "'" +
            "}";
    }
}
