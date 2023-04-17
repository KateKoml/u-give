package com.ugive.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode(exclude = {
        "user"
})
@ToString(exclude = {
        "user"
})
@Entity
@Table(name = "user_balance")
public class UserBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.valueOf(0.0);

    @Column(nullable = false)
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now());

    @Column(nullable = false)
    private Timestamp changed = Timestamp.valueOf(LocalDateTime.now());

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
