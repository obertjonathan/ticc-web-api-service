package com.ticc.webapiservice.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "merchants")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Merchant {
    @Id
    private String id;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.REMOVE)
    private List<Event> events;

    private String email;
    private String username;
    private String password;
    private String name;
    private String bio;
    private String location;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
