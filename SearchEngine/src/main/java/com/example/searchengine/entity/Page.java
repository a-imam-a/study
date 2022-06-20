package com.example.searchengine.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;

@Setter
@Entity
@Table(name = "page")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @NaturalId
    @Column(columnDefinition = "text", nullable = false, unique = true)
    private String path;

    private int code;

    @Getter
    @Column(columnDefinition = "mediumtext", nullable = false)
    private String content;

    @ManyToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    @JoinTable(name = "index",
            joinColumns = @JoinColumn(name = "page_id"),
            inverseJoinColumns = @JoinColumn(name = "lemma_id"))
    private List<Lemma> lemmaList;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    public Page() {
    }
}
