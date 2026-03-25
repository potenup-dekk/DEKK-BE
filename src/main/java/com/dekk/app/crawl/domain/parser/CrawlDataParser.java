package com.dekk.app.crawl.domain.parser;

import com.dekk.app.card.application.dto.command.CardCreateCommand;
import com.dekk.app.card.domain.model.enums.Platform;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface CrawlDataParser {

    Platform supportedPlatform();

    List<CardCreateCommand> parse(String rawData) throws JsonProcessingException;
}
