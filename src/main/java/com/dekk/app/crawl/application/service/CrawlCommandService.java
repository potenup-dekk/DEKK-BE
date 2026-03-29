package com.dekk.app.crawl.application.service;

import com.dekk.app.crawl.application.command.CrawlBatchCreateCommand;
import com.dekk.app.crawl.application.command.CrawlRawDataCreateCommand;
import com.dekk.app.crawl.application.result.CrawlBatchResult;
import com.dekk.app.crawl.application.result.CrawlRawDataResult;
import com.dekk.app.crawl.domain.exception.CrawlBusinessException;
import com.dekk.app.crawl.domain.exception.CrawlErrorCode;
import com.dekk.app.crawl.domain.model.CrawlBatch;
import com.dekk.app.crawl.domain.model.CrawlRawData;
import com.dekk.app.crawl.domain.model.enums.CrawlBatchStatus;
import com.dekk.app.crawl.domain.repository.CrawlBatchRepository;
import com.dekk.app.crawl.domain.repository.CrawlRawDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlCommandService {

    private final CrawlBatchRepository batchRepository;
    private final CrawlRawDataRepository rawDataRepository;

    public CrawlBatchResult createBatch(CrawlBatchCreateCommand command) {
        CrawlBatch batch = CrawlBatch.create(command.platform());
        return CrawlBatchResult.from(batchRepository.save(batch));
    }

    public CrawlRawDataResult addRawData(CrawlRawDataCreateCommand command) {
        CrawlBatch batch = batchRepository
                .findById(command.batchId())
                .orElseThrow(() -> new CrawlBusinessException(CrawlErrorCode.BATCH_NOT_FOUND));

        if (batch.getStatus() != CrawlBatchStatus.COLLECTING) {
            throw new CrawlBusinessException(CrawlErrorCode.BATCH_NOT_COLLECTING);
        }

        CrawlRawData rawData = CrawlRawData.create(batch, batch.getPlatform(), command.rawData());
        return CrawlRawDataResult.from(rawDataRepository.save(rawData));
    }

    public void completeBatch(Long batchId) {
        CrawlBatch batch = batchRepository
                .findById(batchId)
                .orElseThrow(() -> new CrawlBusinessException(CrawlErrorCode.BATCH_NOT_FOUND));

        batch.markAsCollected();
        batchRepository.save(batch);
    }
}
