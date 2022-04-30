package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Course} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CourseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CourseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter introduce;

    private InstantFilter classTime;

    private LongFilter classdId;

    private LongFilter classroomId;

    private LongFilter teacherId;

    private Boolean distinct;

    public CourseCriteria() {}

    public CourseCriteria(CourseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.introduce = other.introduce == null ? null : other.introduce.copy();
        this.classTime = other.classTime == null ? null : other.classTime.copy();
        this.classdId = other.classdId == null ? null : other.classdId.copy();
        this.classroomId = other.classroomId == null ? null : other.classroomId.copy();
        this.teacherId = other.teacherId == null ? null : other.teacherId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CourseCriteria copy() {
        return new CourseCriteria(this);
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

    public StringFilter getIntroduce() {
        return introduce;
    }

    public StringFilter introduce() {
        if (introduce == null) {
            introduce = new StringFilter();
        }
        return introduce;
    }

    public void setIntroduce(StringFilter introduce) {
        this.introduce = introduce;
    }

    public InstantFilter getClassTime() {
        return classTime;
    }

    public InstantFilter classTime() {
        if (classTime == null) {
            classTime = new InstantFilter();
        }
        return classTime;
    }

    public void setClassTime(InstantFilter classTime) {
        this.classTime = classTime;
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

    public LongFilter getClassroomId() {
        return classroomId;
    }

    public LongFilter classroomId() {
        if (classroomId == null) {
            classroomId = new LongFilter();
        }
        return classroomId;
    }

    public void setClassroomId(LongFilter classroomId) {
        this.classroomId = classroomId;
    }

    public LongFilter getTeacherId() {
        return teacherId;
    }

    public LongFilter teacherId() {
        if (teacherId == null) {
            teacherId = new LongFilter();
        }
        return teacherId;
    }

    public void setTeacherId(LongFilter teacherId) {
        this.teacherId = teacherId;
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
        final CourseCriteria that = (CourseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(introduce, that.introduce) &&
            Objects.equals(classTime, that.classTime) &&
            Objects.equals(classdId, that.classdId) &&
            Objects.equals(classroomId, that.classroomId) &&
            Objects.equals(teacherId, that.teacherId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, introduce, classTime, classdId, classroomId, teacherId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (introduce != null ? "introduce=" + introduce + ", " : "") +
            (classTime != null ? "classTime=" + classTime + ", " : "") +
            (classdId != null ? "classdId=" + classdId + ", " : "") +
            (classroomId != null ? "classroomId=" + classroomId + ", " : "") +
            (teacherId != null ? "teacherId=" + teacherId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
