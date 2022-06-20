package searchengine.entity;

import lombok.Data;
import searchengine.entity.enums.IndexingStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
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

    @Column(columnDefinition = "varchar", nullable = false)
    private String url;

    @Column(columnDefinition = "varchar", nullable = false)
    private String name;

   @OneToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY, mappedBy = "site")
    private List<Page> pageList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "site")
    private List<Lemma> lemmaList;

}
