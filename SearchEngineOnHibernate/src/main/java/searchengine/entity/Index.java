package searchengine.entity;

import lombok.Data;
import searchengine.entity.keys.IndexKey;

import javax.persistence.*;

@Data
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
}
