package com.dekk.crawl.application.service;

import com.dekk.card.application.command.CardCreateCommand;
import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.repository.CardRepository;
import com.dekk.crawl.domain.model.CrawlRawData;
import com.dekk.crawl.infrastructure.parser.CrawlDataParserFactory;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrawlRawDataProcessor {

    private static final Logger log = LoggerFactory.getLogger("CRAWL");

    private final CrawlDataParserFactory parsers;
    private final CardRepository cardRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(CrawlRawData rawData) {
        rawData.markAsProcessing();

        try {
            List<CardCreateCommand> commands = parseRawData(rawData);
            saveNewCards(commands);
            rawData.markAsCompleted();
        } catch (Exception e) {
            log.error("raw data 처리 실패: rawDataId={}", rawData.getId(), e);
            rawData.fail();
        }
    }

    private List<CardCreateCommand> parseRawData(CrawlRawData rawData) throws JsonProcessingException {
        return parsers
                .getParser(rawData.getPlatform())
                .parse(rawData.getRawData());
    }

    private void saveNewCards(List<CardCreateCommand> commands) {
        commands.forEach(card -> {
            if (!cardRepository.existsByPlatformAndOriginId(card.platform(), card.originId())) {
                cardRepository.save(Card.create(card));
            }
        });
    }
}
