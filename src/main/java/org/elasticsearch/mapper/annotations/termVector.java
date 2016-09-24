package org.elasticsearch.mapper.annotations;

public enum TermVector {
    no,
    yes,
    with_positions,
    with_offsets,
    with_positions_offsets,
}
