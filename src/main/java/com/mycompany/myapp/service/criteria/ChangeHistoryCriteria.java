package com.mycompany.myapp.service.criteria;

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
 * Criteria class for the {@link com.mycompany.myapp.domain.ChangeHistory} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ChangeHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /change-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ChangeHistoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter describe;

    private Boolean distinct;

    public ChangeHistoryCriteria() {}

    public ChangeHistoryCriteria(ChangeHistoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.describe = other.describe == null ? null : other.describe.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ChangeHistoryCriteria copy() {
        return new ChangeHistoryCriteria(this);
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

    public StringFilter getDescribe() {
        return describe;
    }

    public StringFilter describe() {
        if (describe == null) {
            describe = new StringFilter();
        }
        return describe;
    }

    public void setDescribe(StringFilter describe) {
        this.describe = describe;
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
        final ChangeHistoryCriteria that = (ChangeHistoryCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(describe, that.describe) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, describe, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChangeHistoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (describe != null ? "describe=" + describe + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
