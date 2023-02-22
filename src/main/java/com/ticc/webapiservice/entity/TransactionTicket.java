package com.ticc.webapiservice.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionTicket {
    
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transaction_id")
    private Transactions transactions;
    @ManyToOne(optional = false)
    @JoinColumn(name = "tier_id")
    private Tier tier;
    private Integer purchasedQty;
    private Long tierTotalPrice;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
