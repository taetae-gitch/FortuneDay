package com.FortuneDay.fortune.entity;
    
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @Column(name = "year_ganji", nullable = false)
    private String yearGanji;

    @Column(name = "month_ganji", nullable = false)
    private String monthGanji;

    @Column(name = "day_ganji", nullable = false)
    private String dayGanji;

    @Column(name = "five_elements", nullable = false)
    private String fiveElements;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "saju", cascade = CascadeType.ALL)
    @Builder.Default
    private List<DailyFortune> dailyFortunes = new ArrayList<>();
}