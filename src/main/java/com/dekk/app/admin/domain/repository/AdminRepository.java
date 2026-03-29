package com.dekk.app.admin.domain.repository;

import com.dekk.app.admin.domain.model.Admin;
import java.util.Optional;

public interface AdminRepository {
    Admin save(Admin admin);

    Optional<Admin> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Admin> findById(Long id);
}
