package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.GradeLevelType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Student} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.StudentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /students?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class StudentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GradeLevelType
     */
    public static class GradeLevelTypeFilter extends Filter<GradeLevelType> {

        public GradeLevelTypeFilter() {}

        public GradeLevelTypeFilter(GradeLevelTypeFilter filter) {
            super(filter);
        }

        @Override
        public GradeLevelTypeFilter copy() {
            return new GradeLevelTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter age;

    private IntegerFilter number;

    private GradeLevelTypeFilter grade;

    private LongFilter classdId;

    private Boolean distinct;

    public StudentCriteria() {}

    public StudentCriteria(StudentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.age = other.age == null ? null : other.age.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.grade = other.grade == null ? null : other.grade.copy();
        this.classdId = other.classdId == null ? null : other.classdId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StudentCriteria copy() {
        return new StudentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getAge() {
        return age;
    }

    public IntegerFilter age() {
        if (age == null) {
            age = new IntegerFilter();
        }
        return age;
    }

    public void setAge(IntegerFilter age) {
        this.age = age;
    }

    public IntegerFilter getNumber() {
        return number;
    }

    public IntegerFilter number() {
        if (number == null) {
            number = new IntegerFilter();
        }
        return number;
    }

    public void setNumber(IntegerFilter number) {
        this.number = number;
    }

    public GradeLevelTypeFilter getGrade() {
        return grade;
    }

    public GradeLevelTypeFilter grade() {
        if (grade == null) {
            grade = new GradeLevelTypeFilter();
        }
        return grade;
    }

    public void setGrade(GradeLevelTypeFilter grade) {
        this.grade = grade;
    }

    public LongFilter getClassdId() {
        return classdId;
    }

    public LongFilter classdId() {
        if (classdId == null) {
            classdId = new LongFilter();
        }
        return classdId;
    }

    public void setClassdId(LongFilter classdId) {
        this.classdId = classdId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentCriteria that = (StudentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(age, that.age) &&
            Objects.equals(number, that.number) &&
            Objects.equals(grade, that.grade) &&
            Objects.equals(classdId, that.classdId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, number, grade, classdId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (age != null ? "age=" + age + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (grade != null ? "grade=" + grade + ", " : "") +
            (classdId != null ? "classdId=" + classdId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
