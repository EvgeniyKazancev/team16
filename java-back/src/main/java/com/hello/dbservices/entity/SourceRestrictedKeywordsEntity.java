package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "source_restricted_keywords", schema = "test", catalog = "")
public class SourceRestrictedKeywordsEntity {
    @Basic
    @Column(name = "source_id")
    private long sourceId;
    @Basic
    @Column(name = "keyword")
    private String keyword;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourceRestrictedKeywordsEntity that = (SourceRestrictedKeywordsEntity) o;
        return sourceId == that.sourceId && Objects.equals(keyword, that.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceId, keyword);
    }
}
