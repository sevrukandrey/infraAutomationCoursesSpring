package com.playtika.automation.dao.entity;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "owner")
public class OwnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @OneToMany(mappedBy = "owner")
    Set<CarEntity> carEntitySet;
}
