package com.example.searchengine.entity;

import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;
import java.util.List;

@Setter
@Entity
@Table(name = "lemma")
@SQLInsert(sql = "insert into lemma(frequency, lemma, site_id) VALUES(?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE frequency = frequency + 1")
public class Lemma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "varchar", nullable = false)
    private String lemma;

    @Column(nullable = false)
    private int frequency;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "index",
            joinColumns = @JoinColumn(name = "lemma_id"),
            inverseJoinColumns = @JoinColumn(name = "page_id"))
    private List<Page> pageList;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    public Lemma() {
    }
}
