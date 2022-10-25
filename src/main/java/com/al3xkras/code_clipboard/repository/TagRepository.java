package com.al3xkras.code_clipboard.repository;

import com.al3xkras.code_clipboard.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findAllByValueIn(Collection<String> values);
}
