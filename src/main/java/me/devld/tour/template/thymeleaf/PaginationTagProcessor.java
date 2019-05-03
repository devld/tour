package me.devld.tour.template.thymeleaf;

import me.devld.tour.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.support.RequestContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Map;

public class PaginationTagProcessor extends AbstractStandardExpressionAttributeTagProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaginationTagProcessor.class);

    private static final String PARAM_PAGE = "page";
    private static final int PAGE_START = 1;

    PaginationTagProcessor(String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, "pagination", 1000, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
                             AttributeName attributeName,
                             String attributeValue, Object expressionResult,
                             IElementTagStructureHandler structureHandler) {
        if (!(expressionResult instanceof Page)) {
            LOGGER.warn("invalid pageable");
            return;
        }
        Page page = (Page) expressionResult;
        StringBuilder res = new StringBuilder();

        int currentPage = page.getPageable().getPageNumber();
        int totalPage = page.getTotalPages();

        Map<String, String> queries = TextUtils.parseQueryString(getQueryString(context));

        // 首页
        appendPageLink(res, "&laquo;", "first-page", PAGE_START, currentPage == 0, queries);
        // 上一页
        appendPageLink(res, "&lt;", "pre-page", currentPage - 1 + PAGE_START, currentPage <= 0, queries);

        // 跳页 form
        res
                .append("<form class=\"jump-page\" action=\"\">")
                .append("<input type=\"text\" pattern=\"\\d+\" required name=\"").append(PARAM_PAGE).append("\" value=\"").append(currentPage + PAGE_START).append("\">")
                .append("</form>");

        // 下一页
        appendPageLink(res, "&gt;", "next-page", currentPage + 1 + PAGE_START, currentPage >= totalPage - 1, queries);
        //尾页
        appendPageLink(res, "&raquo;", "last-page", totalPage - 1 + PAGE_START, currentPage == totalPage - 1, queries);

        structureHandler.setBody(res.toString(), false);
    }

    private void appendPageLink(StringBuilder sb, String text, String clazz, int page, boolean disabled, Map<String, String> queries) {
        if (clazz == null) {
            clazz = "";
        }
        if (disabled) {
            appendHtmlTag(sb, "span", text, "class", clazz + " disabled");
        } else {
            queries.put(PARAM_PAGE, String.valueOf(page));
            appendHtmlTag(sb, "a", text, "class", clazz, "href", "?" + TextUtils.makeQueryString(queries));
        }
    }

    private void appendHtmlTag(StringBuilder sb, String tagName, String text, String... attrsValues) {
        sb.append("<").append(tagName);
        for (int i = 0; i < attrsValues.length; i += 2) {
            sb.append(' ').append(attrsValues[i]).append('=');
            if (i + 1 < attrsValues.length) {
                sb.append('"').append(attrsValues[i + 1]).append('"');
            }
        }
        sb.append('>').append(text).append("</").append(tagName).append('>');
    }

    private String getQueryString(ITemplateContext context) {
        Object requestContextObj = context.getVariable("springRequestContext");
        if (!(requestContextObj instanceof RequestContext)) {
            throw new IllegalArgumentException();
        }
        RequestContext requestContext = (RequestContext) requestContextObj;
        String query = requestContext.getQueryString();
        return query == null ? "" : query;
    }
}
