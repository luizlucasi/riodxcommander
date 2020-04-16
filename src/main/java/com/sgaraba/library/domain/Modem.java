package com.sgaraba.library.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Modem.
 */
@Entity
@Table(name = "modem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Modem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "crm_estacao_id")
    private Long crmEstacaoId;

    @Column(name = "vsat_id")
    private Integer vsatId;

    @Column(name = "vsat_uid")
    private Integer vsatUid;

    @Column(name = "vsat_group")
    private String vsatGroup;

    @Column(name = "hub")
    private Integer hub;

    @Column(name = "name")
    private String name;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "status")
    private Integer status;

    @Column(name = "status_desc")
    private String statusDesc;

    @Column(name = "object_oid")
    private String objectOID;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "lan_ip_address")
    private String lanIpAddress;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCrmEstacaoId() {
        return crmEstacaoId;
    }

    public Modem crmEstacaoId(Long crmEstacaoId) {
        this.crmEstacaoId = crmEstacaoId;
        return this;
    }

    public void setCrmEstacaoId(Long crmEstacaoId) {
        this.crmEstacaoId = crmEstacaoId;
    }

    public Integer getVsatId() {
        return vsatId;
    }

    public Modem vsatId(Integer vsatId) {
        this.vsatId = vsatId;
        return this;
    }

    public void setVsatId(Integer vsatId) {
        this.vsatId = vsatId;
    }

    public Integer getVsatUid() {
        return vsatUid;
    }

    public Modem vsatUid(Integer vsatUid) {
        this.vsatUid = vsatUid;
        return this;
    }

    public void setVsatUid(Integer vsatUid) {
        this.vsatUid = vsatUid;
    }

    public String getVsatGroup() {
        return vsatGroup;
    }

    public Modem vsatGroup(String vsatGroup) {
        this.vsatGroup = vsatGroup;
        return this;
    }

    public void setVsatGroup(String vsatGroup) {
        this.vsatGroup = vsatGroup;
    }

    public Integer getHub() {
        return hub;
    }

    public Modem hub(Integer hub) {
        this.hub = hub;
        return this;
    }

    public void setHub(Integer hub) {
        this.hub = hub;
    }

    public String getName() {
        return name;
    }

    public Modem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Modem ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public Modem status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public Modem statusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
        return this;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getObjectOID() {
        return objectOID;
    }

    public Modem objectOID(String objectOID) {
        this.objectOID = objectOID;
        return this;
    }

    public void setObjectOID(String objectOID) {
        this.objectOID = objectOID;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Modem latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Modem longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLanIpAddress() {
        return lanIpAddress;
    }

    public Modem lanIpAddress(String lanIpAddress) {
        this.lanIpAddress = lanIpAddress;
        return this;
    }

    public void setLanIpAddress(String lanIpAddress) {
        this.lanIpAddress = lanIpAddress;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Modem)) {
            return false;
        }
        return id != null && id.equals(((Modem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Modem{" +
            "id=" + getId() +
            ", crmEstacaoId=" + getCrmEstacaoId() +
            ", vsatId=" + getVsatId() +
            ", vsatUid=" + getVsatUid() +
            ", vsatGroup='" + getVsatGroup() + "'" +
            ", hub=" + getHub() +
            ", name='" + getName() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", status=" + getStatus() +
            ", statusDesc='" + getStatusDesc() + "'" +
            ", objectOID='" + getObjectOID() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", lanIpAddress='" + getLanIpAddress() + "'" +
            "}";
    }
}
