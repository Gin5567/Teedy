package com.sismics.docs.core.model.jpa;

import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "T_USER_REQUEST")
public class UserRequest {

    @Id
    @Column(name = "USR_ID_C")
    private String id;

    @Column(name = "USR_EMAIL_C")
    private String email;

    @Column(name = "USR_NAME_C")
    private String name;

    @Column(name = "USR_CREATEDATE_D")
    private Date createDate;

    @Column(name = "USR_STATUS_C")
    private String status; // pending / accepted / rejected

    // --- Getters and Setters ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // --- Debug toString ---
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("email", email)
                .add("name", name)
                .add("createDate", createDate)
                .add("status", status)
                .toString();
    }
}
