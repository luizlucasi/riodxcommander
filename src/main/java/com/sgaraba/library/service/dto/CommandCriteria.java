package com.sgaraba.library.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.sgaraba.library.domain.Command} entity. This class is used
 * in {@link com.sgaraba.library.web.rest.CommandResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /commands?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommandCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter call;

    private BooleanFilter inUse;

    private LongFilter operatorId;

    private LongFilter bandId;

    public CommandCriteria() {
    }

    public CommandCriteria(CommandCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.call = other.call == null ? null : other.call.copy();
        this.inUse = other.inUse == null ? null : other.inUse.copy();
        this.operatorId = other.operatorId == null ? null : other.operatorId.copy();
        this.bandId = other.bandId == null ? null : other.bandId.copy();
    }

    @Override
    public CommandCriteria copy() {
        return new CommandCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCall() {
        return call;
    }

    public void setCall(StringFilter call) {
        this.call = call;
    }

    public BooleanFilter getInUse() {
        return inUse;
    }

    public void setInUse(BooleanFilter inUse) {
        this.inUse = inUse;
    }

    public LongFilter getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(LongFilter operatorId) {
        this.operatorId = operatorId;
    }

    public LongFilter getBandId() {
        return bandId;
    }

    public void setBandId(LongFilter bandId) {
        this.bandId = bandId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommandCriteria that = (CommandCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(call, that.call) &&
            Objects.equals(inUse, that.inUse) &&
            Objects.equals(operatorId, that.operatorId) &&
            Objects.equals(bandId, that.bandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        call,
        inUse,
        operatorId,
        bandId
        );
    }

    @Override
    public String toString() {
        return "CommandCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (call != null ? "call=" + call + ", " : "") +
                (inUse != null ? "inUse=" + inUse + ", " : "") +
                (operatorId != null ? "operatorId=" + operatorId + ", " : "") +
                (bandId != null ? "bandId=" + bandId + ", " : "") +
            "}";
    }

}
