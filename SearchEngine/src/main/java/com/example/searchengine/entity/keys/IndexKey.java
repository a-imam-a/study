package com.example.searchengine.entity.keys;



import com.example.searchengine.entity.Lemma;
import com.example.searchengine.entity.Page;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class IndexKey implements Serializable {

    @Getter
    @Setter
    private Page page;

    @Getter
    @Setter
    private Lemma lemma;
}
