package com.mangobyte.accountservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mangobyte.accountservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_acc_username", columnNames = "username"),
        @UniqueConstraint(name = "UK_acc_email", columnNames = "email")
})
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 30)
    @Column(nullable = false)
    private String username;

    @Size(min = 3, max = 125)
    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @ColumnDefault("(NOW())")
    @Column(nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @ColumnDefault("(NOW())")
    @Column(nullable = false)
    private Date updatedAt;

    @Column
    private String profileImgUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private Boolean isFriend;

    public Account(Account account) {
        BeanUtils.copyProperties(account, this);
    }

    public Account(Account account, Boolean isFriend) {
        this(account);
        this.isFriend = isFriend;
    }

}
