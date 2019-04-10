package me.devld.tour.template.thymeleaf;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Image;
import org.thymeleaf.context.ITemplateContext;

public class MarkdownHtmlTagVisitor extends AbstractVisitor {

    private final ITemplateContext context;
    private final String imageBasePath;

    public MarkdownHtmlTagVisitor(ITemplateContext context, String imageBasePath) {
        this.context = context;
        this.imageBasePath = imageBasePath;
    }

    @Override
    public void visit(Image image) {
        image.setDestination(context.buildLink(imageBasePath + image.getDestination(), null));
    }
}
