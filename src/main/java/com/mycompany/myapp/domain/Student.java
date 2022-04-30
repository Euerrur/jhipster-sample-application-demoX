package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.GradeLevelType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 学生
 */
@Schema(description = "学生")
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student implements Serializable {

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
     * 学号
     */
    @Schema(description = "学号", required = true)
    @NotNull
    @Column(name = "number", nullable = false)
    private Integer number;

    /**
     * 年级
     */
    @Schema(description = "年级", required = true)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false)
    private GradeLevelType grade;

    /**
     * 学生关联班级
     */
    @Schema(description = "学生关联班级")
    @ManyToMany
    @JoinTable(
        name = "rel_student__classd",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "classd_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courses", "students" }, allowSetters = true)
    private Set<Classd> classds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Student id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Student name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public Student age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getNumber() {
        return this.number;
    }

    public Student number(Integer number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public GradeLevelType getGrade() {
        return this.grade;
    }

    public Student grade(GradeLevelType grade) {
        this.setGrade(grade);
        return this;
    }

    public void setGrade(GradeLevelType grade) {
        this.grade = grade;
    }

    public Set<Classd> getClassds() {
        return this.classds;
    }

    public void setClassds(Set<Classd> classds) {
        this.classds = classds;
    }

    public Student classds(Set<Classd> classds) {
        this.setClassds(classds);
        return this;
    }

    public Student addClassd(Classd classd) {
        this.classds.add(classd);
        classd.getStudents().add(this);
        return this;
    }

    public Student removeClassd(Classd classd) {
        this.classds.remove(classd);
        classd.getStudents().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", number=" + getNumber() +
            ", grade='" + getGrade() + "'" +
            "}";
    }
}
