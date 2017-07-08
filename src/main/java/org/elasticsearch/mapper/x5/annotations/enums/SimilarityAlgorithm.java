package org.elasticsearch.mapper.x5.annotations.enums;

import java.lang.String;

public enum SimilarityAlgorithm {
    Default {
        @Override
        public String code() {
            return "BM25";
        }
    },
    BM25 {
        @Override
        public String code() {
            return "BM25";
        }
    };

    public abstract java.lang.String code();
}
