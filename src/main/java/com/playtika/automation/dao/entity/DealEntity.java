package com.playtika.automation.dao.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity(name = "deal")
public class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   // @OneToMany
    //@JoinColumn(name = "id")
    private long buyerId;
    private Double price;
   // @OneToMany
    private long advertId;
    private String status;


}
