package me.devld.tour.template.thymeleaf;

import me.devld.tour.config.AppConfig;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Collections;
import java.util.Set;

public class MarkdownDialect extends AbstractProcessorDialect {

    private static final String PREFIX = "md";
    private static final int PRECEDENCE = 1000;

    private final AppConfig.FileConfig fileConfig;

    public MarkdownDialect(AppConfig.FileConfig fileConfig) {
        super("Markdown Render Dialect", PREFIX, PRECEDENCE);
        this.fileConfig = fileConfig;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return Collections.singleton(new MarkdownBodyAttributeTagProcessor(dialectPrefix, fileConfig));
    }
}
