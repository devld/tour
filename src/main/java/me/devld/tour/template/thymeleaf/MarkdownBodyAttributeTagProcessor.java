package me.devld.tour.template.thymeleaf;

import me.devld.tour.config.AppConfig;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Arrays;
import java.util.List;

public class MarkdownBodyAttributeTagProcessor extends AbstractStandardExpressionAttributeTagProcessor {

    private static final String ATTR_NAME = "text";
    private static final int PRECEDENCE = 10000;

    private final Parser parser;
    private final HtmlRenderer htmlRenderer;

    private final AppConfig.FileConfig fileConfig;

    MarkdownBodyAttributeTagProcessor(String dialectPrefix, AppConfig.FileConfig fileConfig) {
        super(TemplateMode.HTML, dialectPrefix, ATTR_NAME, PRECEDENCE, true);
        this.fileConfig = fileConfig;

        List<Extension> extensions = Arrays.asList(
                HeadingAnchorExtension.create(),
                TablesExtension.create()
        );

        this.parser = Parser.builder()
                .extensions(extensions)
                .build();
        htmlRenderer = HtmlRenderer.builder()
                .extensions(extensions)
                .percentEncodeUrls(true)
                .build();
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
                             AttributeName attributeName, String attributeValue,
                             Object expressionResult, IElementTagStructureHandler structureHandler) {
        String htmlText = "";
        if (expressionResult instanceof String) {
            Node rootNode = parser.parse((String) expressionResult);
            rootNode.accept(new MarkdownHtmlTagVisitor(context, fileConfig.getImageBasePath()));
            htmlText = htmlRenderer.render(rootNode);
        }
        structureHandler.setBody(htmlText, false);
    }
}
