package com.FortuneDay.fortune.entity;
    
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sajus")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SajuInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
}