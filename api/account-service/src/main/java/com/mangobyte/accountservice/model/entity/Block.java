package com.mangobyte.accountservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_block_from", nullable = false, foreignKey = @ForeignKey(name = "FK_acc_block_from"))
    private Account accountFrom;

    @ManyToOne
    @JoinColumn(name = "account_block_to", nullable = false, foreignKey = @ForeignKey(name = "FK_acc_block_to"))
    private Account accountTo;

    @Column(nullable = false)
    private Boolean isBlocked;

    @CreationTimestamp
    @ColumnDefault("(NOW())")
    @Column(nullable = false)
    private Date blockedAt;

    private Date unblockedAt;

}
