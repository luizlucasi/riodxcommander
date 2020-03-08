package com.sgaraba.library.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Command.
 */
@Entity
@Table(name = "command")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Command implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "call", nullable = false)
    private String call;

    @Column(name = "in_use")
    private Boolean inUse;

    @OneToOne
    @JoinColumn(unique = true)
    private Operator operator;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "command_band",
               joinColumns = @JoinColumn(name = "command_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "band_id", referencedColumnName = "id"))
    private Set<Band> bands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCall() {
        return call;
    }

    public Command call(String call) {
        this.call = call;
        return this;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public Boolean isInUse() {
        return inUse;
    }

    public Command inUse(Boolean inUse) {
        this.inUse = inUse;
        return this;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public Operator getOperator() {
        return operator;
    }

    public Command operator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Set<Band> getBands() {
        return bands;
    }

    public Command bands(Set<Band> bands) {
        this.bands = bands;
        return this;
    }

    public Command addBand(Band band) {
        this.bands.add(band);
        band.getCommands().add(this);
        return this;
    }

    public Command removeBand(Band band) {
        this.bands.remove(band);
        band.getCommands().remove(this);
        return this;
    }

    public void setBands(Set<Band> bands) {
        this.bands = bands;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Command)) {
            return false;
        }
        return id != null && id.equals(((Command) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Command{" +
            "id=" + getId() +
            ", call='" + getCall() + "'" +
            ", inUse='" + isInUse() + "'" +
            "}";
    }
}
