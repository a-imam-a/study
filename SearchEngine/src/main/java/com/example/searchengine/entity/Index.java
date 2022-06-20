package com.example.searchengine.entity;


import com.example.searchengine.entity.keys.IndexKey;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Entity
@Table(name = "`index`")
@IdClass(IndexKey.class)
public class Index {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @EmbeddedId
    @OneToOne(cascade = CascadeType.MERGE, fetch =  FetchType.LAZY)
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    private Page page;

    @EmbeddedId
    @OneToOne(cascade = CascadeType.MERGE, fetch =  FetchType.LAZY)
    @JoinColumn(name = "lemma_id", referencedColumnName = "id")
    private Lemma lemma;

    @Column(name = "`rank`", nullable = false)
    private double rank;

    public Index() {
    }
}
