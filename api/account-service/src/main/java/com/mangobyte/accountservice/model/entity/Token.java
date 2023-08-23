package com.mangobyte.accountservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.Duration;
import java.util.Date;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_tok_acc", columnNames = "account_id"),
        @UniqueConstraint(name = "UK_tok_token", columnNames = "token"),
        @UniqueConstraint(name = "UK_tok_refresh_token", columnNames = "refreshToken")
})
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "FK_tok_acc_from"))
    private Account account;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String refreshToken;

    @ColumnDefault("(NOW())")
    @Column(nullable = false)
    private Date createdAt;

    @ColumnDefault("(DATE_ADD(NOW(), INTERVAL 2 HOUR))")
    @Column(nullable = false)
    private Date expiredAt;

    @PrePersist
    void preInsert() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
        if (this.expiredAt == null) {
            this.expiredAt = Date.from(this.createdAt.toInstant().plus(Duration.ofHours(1)));
        }
    }
}
