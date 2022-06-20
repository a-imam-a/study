package com.example.searchengine.entity;

import com.example.searchengine.entity.enums.IndexingStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Entity
@Table(name = "site")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum")
    private IndexingStatus status;

    @Column(name = "status_time", nullable = false)
    private Date statusTime;

    @Column(columnDefinition = "text", name = "last_error")
    private String lastError;

    @Getter
    @Column(columnDefinition = "varchar", nullable = false)
    private String url;

    @Column(columnDefinition = "varchar", nullable = false)
    private String name;

   @OneToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY, mappedBy = "site")
    private List<Page> pageList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "site")
    private List<Lemma> lemmaList;

    public Site() {
    }
}
