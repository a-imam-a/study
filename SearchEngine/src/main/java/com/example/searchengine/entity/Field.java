package com.example.searchengine.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "field")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Column(columnDefinition = "varchar", nullable = false)
    private String name;

    @Getter
    @Column(columnDefinition = "varchar", nullable = false)
    private String selector;

    @Getter
    @Column(columnDefinition = "float", nullable = false)
    private Double weight;

    public Field() {
    }
}
