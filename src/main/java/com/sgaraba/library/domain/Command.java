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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Command implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "in_use")
    private Boolean inUse;

    @OneToOne
    @JoinColumn(unique = true)
    private Radio radio;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "command_band",
               joinColumns = @JoinColumn(name = "command_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "band_id", referencedColumnName = "id"))
    private Set<Band> bands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Command nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Radio getRadio() {
        return radio;
    }

    public Command radio(Radio radio) {
        this.radio = radio;
        return this;
    }

    public void setRadio(Radio radio) {
        this.radio = radio;
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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

    // prettier-ignore
    @Override
    public String toString() {
        return "Command{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", inUse='" + isInUse() + "'" +
            "}";
    }
}
