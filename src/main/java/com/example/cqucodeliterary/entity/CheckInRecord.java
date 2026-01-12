package com.example.cqucodeliterary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "check_in_records")
@Data
@NoArgsConstructor
public class CheckInRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private LocalDate checkInDate;

    public CheckInRecord(String username, LocalDate checkInDate) {
        this.username = username;
        this.checkInDate = checkInDate;
    }
}