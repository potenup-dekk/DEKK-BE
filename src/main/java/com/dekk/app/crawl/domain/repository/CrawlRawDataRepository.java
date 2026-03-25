package com.dekk.app.crawl.domain.repository;

import com.dekk.app.crawl.domain.model.CrawlRawData;
import com.dekk.app.crawl.domain.model.enums.RawDataStatus;
import java.util.List;
import java.util.Optional;

public interface CrawlRawDataRepository {

    CrawlRawData save(CrawlRawData rawData);

    Optional<CrawlRawData> findById(Long id);

    List<CrawlRawData> findByStatusWithLimit(RawDataStatus status, int limit);

    long countByBatchIdAndStatusNot(Long batchId, RawDataStatus status);
}
