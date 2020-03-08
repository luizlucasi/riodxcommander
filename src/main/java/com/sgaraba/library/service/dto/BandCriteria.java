package com.sgaraba.library.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.sgaraba.library.domain.enumeration.BandMeter;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.sgaraba.library.domain.Band} entity. This class is used
 * in {@link com.sgaraba.library.web.rest.BandResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bands?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BandCriteria implements Serializable, Criteria {
    /**
     * Class for filtering BandMeter
     */
    public static class BandMeterFilter extends Filter<BandMeter> {

        public BandMeterFilter() {
        }

        public BandMeterFilter(BandMeterFilter filter) {
            super(filter);
        }

        @Override
        public BandMeterFilter copy() {
            return new BandMeterFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BandMeterFilter bandMeter;

    private BooleanFilter inUse;

    private StringFilter commandRunningBand;

    private LongFilter commandId;

    public BandCriteria() {
    }

    public BandCriteria(BandCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.bandMeter = other.bandMeter == null ? null : other.bandMeter.copy();
        this.inUse = other.inUse == null ? null : other.inUse.copy();
        this.commandRunningBand = other.commandRunningBand == null ? null : other.commandRunningBand.copy();
        this.commandId = other.commandId == null ? null : other.commandId.copy();
    }

    @Override
    public BandCriteria copy() {
        return new BandCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BandMeterFilter getBandMeter() {
        return bandMeter;
    }

    public void setBandMeter(BandMeterFilter bandMeter) {
        this.bandMeter = bandMeter;
    }

    public BooleanFilter getInUse() {
        return inUse;
    }

    public void setInUse(BooleanFilter inUse) {
        this.inUse = inUse;
    }

    public StringFilter getCommandRunningBand() {
        return commandRunningBand;
    }

    public void setCommandRunningBand(StringFilter commandRunningBand) {
        this.commandRunningBand = commandRunningBand;
    }

    public LongFilter getCommandId() {
        return commandId;
    }

    public void setCommandId(LongFilter commandId) {
        this.commandId = commandId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BandCriteria that = (BandCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(bandMeter, that.bandMeter) &&
            Objects.equals(inUse, that.inUse) &&
            Objects.equals(commandRunningBand, that.commandRunningBand) &&
            Objects.equals(commandId, that.commandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        bandMeter,
        inUse,
        commandRunningBand,
        commandId
        );
    }

    @Override
    public String toString() {
        return "BandCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (bandMeter != null ? "bandMeter=" + bandMeter + ", " : "") +
                (inUse != null ? "inUse=" + inUse + ", " : "") +
                (commandRunningBand != null ? "commandRunningBand=" + commandRunningBand + ", " : "") +
                (commandId != null ? "commandId=" + commandId + ", " : "") +
            "}";
    }

}
