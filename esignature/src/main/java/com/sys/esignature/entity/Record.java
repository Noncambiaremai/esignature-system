package com.sys.esignature.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "doc_id")
    private Integer docId;

    @Column(name = "sig_id")
    private Integer sigId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "sig_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sigTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public Integer getSigId() {
        return sigId;
    }

    public void setSigId(Integer sigId) {
        this.sigId = sigId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getSigTime() {
        return sigTime;
    }

    public void setSigTime(Date sigTime) {
        this.sigTime = sigTime;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
