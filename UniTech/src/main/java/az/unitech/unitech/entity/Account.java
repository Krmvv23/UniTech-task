package az.unitech.unitech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private long number;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private int cvv;
    @Column(nullable = false)
    private double balance;
    @Column(nullable = false)
    private boolean active;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

}
