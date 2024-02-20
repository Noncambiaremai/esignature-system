package com.sys.esignature.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "signature")
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sig_id")
    private Integer sigId;

    @Column(name = "sig_name")
    private String sigName;

    @Column(name = "sig_path")
    private String sigPath;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSigId() {
        return sigId;
    }

    public void setSigId(Integer sigId) {
        this.sigId = sigId;
    }

    public String getSigName() {
        return sigName;
    }

    public void setSigName(String sigName) {
        this.sigName = sigName;
    }

    public String getSigPath() {
        return sigPath;
    }

    public void setSigPath(String sigPath) {
        this.sigPath = sigPath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
