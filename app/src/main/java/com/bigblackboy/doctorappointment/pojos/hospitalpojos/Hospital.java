package com.bigblackboy.doctorappointment.pojos.hospitalpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hospital implements Serializable {

    public Hospital() {

    }

    public Hospital(int lpuId, String lpuName) {
        this.idLPU = lpuId;
        this.lPUShortName = lpuName;
    }

    @SerializedName("District")
    @Expose
    private Integer district;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("IdDistrict")
    @Expose
    private Integer IdDistrict;
    @SerializedName("IsActive")
    @Expose
    private boolean IsActive;
    @SerializedName("IsEnableAppointment")
    @Expose
    private Boolean isEnableAppointment;
    @SerializedName("InfoStand")
    @Expose
    private Boolean infoStand;
    @SerializedName("IsEnableInternet")
    @Expose
    private Boolean isEnableInternet;
    @SerializedName("LPUFullName")
    @Expose
    private String LPUFullName;
    @SerializedName("LPUShortName")
    @Expose
    private String lPUShortName;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("PartOf")
    @Expose
    private Object partOf;
    @SerializedName("LPUType")
    @Expose
    private Integer lPUType;
    @SerializedName("Oid")
    @Expose
    private String oid;
    @SerializedName("WorkTime")
    @Expose
    private String workTime;
    @SerializedName("Address")
    @Expose
    private String fullAddress;
    @SerializedName("LpuType")
    @Expose
    private String lpuType;
    @SerializedName("LpuName")
    @Expose
    private String lpuName;
    @SerializedName("InternetReference")
    @Expose
    private Object internetReference;
    @SerializedName("IdLPU")
    @Expose
    private Integer idLPU;
    @SerializedName("PhoneCallCentre")
    @Expose
    private String phoneCallCentre;
    @SerializedName("ExternalHubId")
    @Expose
    private Double externalHubId;
    @SerializedName("ExternalGisId")
    @Expose
    private String externalGisId;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Comment")
    @Expose
    private Object comment;

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public Boolean getIsEnableAppointment() {
        return isEnableAppointment;
    }

    public void setIsEnableAppointment(Boolean isEnableAppointment) {
        this.isEnableAppointment = isEnableAppointment;
    }

    public Boolean getInfoStand() {
        return infoStand;
    }

    public void setInfoStand(Boolean infoStand) {
        this.infoStand = infoStand;
    }

    public Boolean getIsEnableInternet() {
        return isEnableInternet;
    }

    public void setIsEnableInternet(Boolean isEnableInternet) {
        this.isEnableInternet = isEnableInternet;
    }

    public String getLPUShortName() {
        return lPUShortName;
    }

    public void setLPUShortName(String lPUShortName) {
        this.lPUShortName = lPUShortName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getPartOf() {
        return partOf;
    }

    public void setPartOf(Object partOf) {
        this.partOf = partOf;
    }

    public Integer getLPUType() {
        return lPUType;
    }

    public void setLPUType(Integer lPUType) {
        this.lPUType = lPUType;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getLpuType() {
        return lpuType;
    }

    public void setLpuType(String lpuType) {
        this.lpuType = lpuType;
    }

    public String getLpuName() {
        return lpuName;
    }

    public void setLpuName(String lpuName) {
        this.lpuName = lpuName;
    }

    public Object getInternetReference() {
        return internetReference;
    }

    public void setInternetReference(Object internetReference) {
        this.internetReference = internetReference;
    }

    public Integer getIdLPU() {
        return idLPU;
    }

    public void setIdLPU(Integer idLPU) {
        this.idLPU = idLPU;
    }

    public String getPhoneCallCentre() {
        return phoneCallCentre;
    }

    public void setPhoneCallCentre(String phoneCallCentre) {
        this.phoneCallCentre = phoneCallCentre;
    }

    public Double getExternalHubId() {
        return externalHubId;
    }

    public void setExternalHubId(Double externalHubId) {
        this.externalHubId = externalHubId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "District: " + district + "\nIsEnableAppointment: " + isEnableAppointment + "\nInfoStand: " + infoStand +
                "\nIsEnableInternet: " + isEnableInternet + "\nLPUShortName: " + lPUShortName + "\nEmail: " + email +
                "\nPartOf: " + partOf + "\nLPUType: " + lPUType + "\nOid: " + oid + "\nWorkTime: " + workTime +
                "\nAddress: " + fullAddress + "\nLpuType: " + lpuType + "\nLpuName: " + lpuName + "\nInternetReference: " +
                internetReference + "\nIdLPU: " + idLPU + "\nPhoneCallCentre: " + phoneCallCentre + "\nExternalHubId: " +
                externalHubId + "\nDescription: " + description + "\nComment: " + comment;
    }
}
