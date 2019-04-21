package me.devld.tour.template.thymeleaf;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Collections;
import java.util.Set;

public class PaginationDialect extends AbstractProcessorDialect {

    private static final String NAME = "Pagination Dialect";
    private static final String PREFIX = "p";

    public PaginationDialect() {
        super("Pagination Dialect", PREFIX, 1000);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return Collections.singleton(new PaginationTagProcessor(PREFIX));
    }
}
