package com.dekk.app.crawl.infrastructure;

import com.dekk.app.crawl.domain.model.CrawlBatch;
import com.dekk.app.crawl.domain.repository.CrawlBatchRepository;
import com.dekk.app.crawl.infrastructure.jpa.CrawlBatchJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrawlBatchRepositoryImpl implements CrawlBatchRepository {
    private final CrawlBatchJpaRepository crawlBatchJpaRepository;

    @Override
    public CrawlBatch save(CrawlBatch batch) {
        return crawlBatchJpaRepository.save(batch);
    }

    @Override
    public Optional<CrawlBatch> findById(Long id) {
        return crawlBatchJpaRepository.findById(id);
    }
}
