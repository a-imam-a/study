package searchengine.entity.keys;

import lombok.Data;
import searchengine.entity.Lemma;
import searchengine.entity.Page;

import java.io.Serializable;

@Data
public class IndexKey implements Serializable {
    private Page page;
    private Lemma lemma;
}
