package com.dekk.app.crawl.infrastructure.jpa;

import com.dekk.app.crawl.domain.model.CrawlBatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawlBatchJpaRepository extends JpaRepository<CrawlBatch, Long> {}
