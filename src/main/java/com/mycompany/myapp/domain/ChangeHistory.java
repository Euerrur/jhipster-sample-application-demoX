package com.mycompany.myapp.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 变更历史记录
 */
@Schema(description = "变更历史记录")
@Entity
@Table(name = "change_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChangeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 变更主体 id
     */
    @Schema(description = "变更主体 id", required = true)
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 描述
     */
    @Schema(description = "描述", required = true)
    @NotNull
    @Column(name = "describe", nullable = false)
    private String describe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChangeHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescribe() {
        return this.describe;
    }

    public ChangeHistory describe(String describe) {
        this.setDescribe(describe);
        return this;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChangeHistory)) {
            return false;
        }
        return id != null && id.equals(((ChangeHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChangeHistory{" +
            "id=" + getId() +
            ", describe='" + getDescribe() + "'" +
            "}";
    }
}
