package com.dekk.category.domain.repository;

import com.dekk.category.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAllByDepth(int depth);
    List<Category> findAllByParentId(Long parentId);
    void delete(Category category);
    void deleteAllByParentId(Long parentId);
}
