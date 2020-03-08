package com.sgaraba.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.sgaraba.library.domain.enumeration.BandMeter;

/**
 * A Band.
 */
@Entity
@Table(name = "band")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Band implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "band_meter")
    private BandMeter bandMeter;

    @Column(name = "in_use")
    private Boolean inUse;

    @Column(name = "command_running_band")
    private String commandRunningBand;

    @ManyToMany(mappedBy = "bands")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Command> commands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Band name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BandMeter getBandMeter() {
        return bandMeter;
    }

    public Band bandMeter(BandMeter bandMeter) {
        this.bandMeter = bandMeter;
        return this;
    }

    public void setBandMeter(BandMeter bandMeter) {
        this.bandMeter = bandMeter;
    }

    public Boolean isInUse() {
        return inUse;
    }

    public Band inUse(Boolean inUse) {
        this.inUse = inUse;
        return this;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public String getCommandRunningBand() {
        return commandRunningBand;
    }

    public Band commandRunningBand(String commandRunningBand) {
        this.commandRunningBand = commandRunningBand;
        return this;
    }

    public void setCommandRunningBand(String commandRunningBand) {
        this.commandRunningBand = commandRunningBand;
    }

    public Set<Command> getCommands() {
        return commands;
    }

    public Band commands(Set<Command> commands) {
        this.commands = commands;
        return this;
    }

    public Band addCommand(Command command) {
        this.commands.add(command);
        command.getBands().add(this);
        return this;
    }

    public Band removeCommand(Command command) {
        this.commands.remove(command);
        command.getBands().remove(this);
        return this;
    }

    public void setCommands(Set<Command> commands) {
        this.commands = commands;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Band)) {
            return false;
        }
        return id != null && id.equals(((Band) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Band{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bandMeter='" + getBandMeter() + "'" +
            ", inUse='" + isInUse() + "'" +
            ", commandRunningBand='" + getCommandRunningBand() + "'" +
            "}";
    }
}
