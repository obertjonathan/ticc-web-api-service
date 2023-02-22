package com.ticc.webapiservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @OneToMany(mappedBy = "transactions")
    private List<TransactionTicket> transactionTickets;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_id")
    private PaymentChannel paymentChannel;
    @Column(name = "total_qty")
    private Integer totalQty;
    @Column(name = "total_price")
    private Long totalPrice;
    @Column(name = "payment_status")
    private String paymentStatus;
    @Column(name = "purchased_date")
    private LocalDateTime purchasedDate;
    @Column(name = "payment_link")
    private String paymentLink;
    @Column(name = "request_id")
    private String requestId;
    @Column(name = "invoice_id")
    private String invoiceId;
    @Column(name = "fcm_token")
    private String fcmToken;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
