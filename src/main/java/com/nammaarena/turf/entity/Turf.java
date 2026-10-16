package com.nammaarena.turf.entity;

import com.nammaarena.turf.enums.TurfStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "turfs", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "ownerId"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Turf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String location;
    
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    
    @Column(nullable = false)
    private String startTime;
    
    @Column(nullable = false)
    private String endTime;
    
    @Column(nullable = false)
    private Long ownerId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TurfStatus status = TurfStatus.PENDING;
    
    @OneToMany(mappedBy = "turf", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sport> sports;
}