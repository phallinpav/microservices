package com.mangobyte.accountservice.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_fri_acc_from", columnNames = "account_from"),
        @UniqueConstraint(name = "UK_fri_acc_to", columnNames = "account_to"),
        @UniqueConstraint(name = "UK_fri_remove_by", columnNames = "removed_by")
})
public class Friend {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_from", nullable = false, foreignKey = @ForeignKey(name = "FK_fri_acc_from"))
    private Account accountFrom;

    @ManyToOne
    @JoinColumn(name = "account_to", nullable = false, foreignKey = @ForeignKey(name = "FK_fri_acc_to"))
    private Account accountTo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendStatus status;

    @CreationTimestamp
    @ColumnDefault("(NOW())")
    @Column(nullable = false)
    private Date requestedAt;

    private Date respondedAt;

    private Date removedAt;

    @ManyToOne
    @JoinColumn(name = "removed_by", foreignKey = @ForeignKey(name = "FK_fri_removed_by"))
    private Account removedBy;
}
