package me.devld.tour.util;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlStreamEventReceiver;
import org.owasp.html.HtmlStreamEventReceiverWrapper;
import org.owasp.html.PolicyFactory;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {

    private static final Pattern COLOR_NAME = Pattern.compile(
            "(?:aqua|black|blue|fuchsia|gray|grey|green|lime|maroon|navy|olive|purple"
                    + "|red|silver|teal|white|yellow)");

    // HTML/CSS Spec allows 3 or 6 digit hex to specify color
    private static final Pattern COLOR_CODE = Pattern.compile(
            "(?:#(?:[0-9a-fA-F]{3}(?:[0-9a-fA-F]{3})?))");

    private static final Pattern NUMBER_OR_PERCENT = Pattern.compile(
            "[0-9]+%?");
    private static final Pattern PARAGRAPH = Pattern.compile(
            "(?:[\\p{L}\\p{N},'\\.\\s\\-_\\(\\)]|&[0-9]{2};)*");
    private static final Pattern HTML_ID = Pattern.compile(
            "[a-zA-Z0-9\\:\\-_\\.]+");
    // force non-empty with a '+' at the end instead of '*'
    private static final Pattern HTML_TITLE = Pattern.compile(
            "[\\p{L}\\p{N}\\s\\-_',:\\[\\]!\\./\\\\\\(\\)&]*");
    private static final Pattern HTML_CLASS = Pattern.compile(
            "[a-zA-Z0-9\\s,\\-_]+");

    private static final Pattern ONSITE_URL = Pattern.compile(
            "(?:[\\p{L}\\p{N}\\\\\\.\\#@\\$%\\+&;\\-_~,\\?=/!]+|\\#(\\w)+)");
    private static final Pattern OFFSITE_URL = Pattern.compile(
            "\\s*(?:(?:ht|f)tps?://|mailto:)[\\p{L}\\p{N}]"
                    + "[\\p{L}\\p{N}\\p{Zs}\\.\\#@\\$%\\+&;:\\-_~,\\?=/!\\(\\)]*+\\s*");

    private static final Pattern NUMBER = Pattern.compile(
            "[+-]?(?:(?:[0-9]+(?:\\.[0-9]*)?)|\\.[0-9]+)");

    private static final Pattern NAME = Pattern.compile("[a-zA-Z0-9\\-_\\$]+");

    private static final Pattern ALIGN = Pattern.compile(
            "(?i)center|left|right|justify|char");

    private static final Pattern VALIGN = Pattern.compile(
            "(?i)baseline|bottom|middle|top");

    private static final Predicate<String> COLOR_NAME_OR_COLOR_CODE
            = matchesEither(COLOR_NAME, COLOR_CODE);

    private static final Predicate<String> ONSITE_OR_OFFSITE_URL
            = matchesEither(ONSITE_URL, OFFSITE_URL);

    private static final Pattern HISTORY_BACK = Pattern.compile(
            "(?:javascript:)?\\Qhistory.go(-1)\\E");

    private static final Pattern ONE_CHAR = Pattern.compile(
            ".?", Pattern.DOTALL);
    private static final PolicyFactory POLICY = getBaseBuilder().toFactory();

    private static Predicate<String> matchesEither(
            final Pattern a, final Pattern b) {
        return s -> a.matcher(s).matches() || b.matcher(s).matches();
    }

    private static final Pattern HTML_IMG_PATTERN = Pattern.compile("<img[^<]*/?>", Pattern.CASE_INSENSITIVE);
    private static final Pattern HTML_ATTR_PATTERN = Pattern.compile("([A-z0-9\\-_]+)=(?:[\"'](.*?)[\"'])?");
    private static final Pattern MARKDOWN_IMG_PATTERN = Pattern.compile("(?:!\\[(.*?)]\\((.*?)\\))");

    public static String sanitizer(String raw) {
        return POLICY.sanitize(raw);
    }

    private static HtmlPolicyBuilder getBaseBuilder() {
        return new HtmlPolicyBuilder()
                .allowAttributes("id").matching(HTML_ID).globally()
                .allowAttributes("class").matching(HTML_CLASS).globally()
                .allowAttributes("lang").matching(Pattern.compile("[a-zA-Z]{2,20}"))
                .globally()
                .allowAttributes("title").matching(HTML_TITLE).globally()
                .allowStyling()
                .allowAttributes("align").matching(ALIGN).onElements("p")
                .allowAttributes("color").matching(COLOR_NAME_OR_COLOR_CODE::test)
                .onElements("font")
                .allowAttributes("face")
                .matching(Pattern.compile("[\\w;, \\-]+"))
                .onElements("font")
                .allowAttributes("size").matching(NUMBER).onElements("font")
                .allowAttributes("href").matching(ONSITE_OR_OFFSITE_URL::test)
                .onElements("a")
                .allowStandardUrlProtocols()
                .allowAttributes("name").matching(NAME).onElements("a")
                .allowAttributes("src").matching(ONSITE_OR_OFFSITE_URL::test)
                .onElements("img")
                .allowAttributes("name").matching(NAME)
                .onElements("img")
                .allowAttributes("alt").matching(PARAGRAPH)
                .onElements("img")
                .allowAttributes("border", "hspace", "vspace").matching(NUMBER)
                .onElements("img")
                .allowAttributes("border", "cellpadding", "cellspacing")
                .matching(NUMBER).onElements("table")
                .allowAttributes("bgcolor").matching(COLOR_NAME_OR_COLOR_CODE::test)
                .onElements("table")
                .allowAttributes("background").matching(ONSITE_URL)
                .onElements("table")
                .allowAttributes("align").matching(ALIGN)
                .onElements("table")
                .allowAttributes("noresize").matching(Pattern.compile("(?i)noresize"))
                .onElements("table")
                .allowAttributes("background").matching(ONSITE_URL)
                .onElements("td", "th", "tr")
                .allowAttributes("bgcolor").matching(COLOR_NAME_OR_COLOR_CODE::test)
                .onElements("td", "th")
                .allowAttributes("abbr").matching(PARAGRAPH)
                .onElements("td", "th")
                .allowAttributes("axis", "headers").matching(NAME)
                .onElements("td", "th")
                .allowAttributes("scope")
                .matching(Pattern.compile("(?i)(?:row|col)(?:group)?"))
                .onElements("td", "th")
                .allowAttributes("nowrap")
                .onElements("td", "th")
                .allowAttributes("height", "width").matching(NUMBER_OR_PERCENT)
                .onElements("table", "td", "th", "tr", "img")
                .allowAttributes("align").matching(ALIGN)
                .onElements("thead", "tbody", "tfoot", "img",
                        "td", "th", "tr", "colgroup", "col")
                .allowAttributes("valign").matching(VALIGN)
                .onElements("thead", "tbody", "tfoot",
                        "td", "th", "tr", "colgroup", "col")
                .allowAttributes("charoff").matching(NUMBER_OR_PERCENT)
                .onElements("td", "th", "tr", "colgroup", "col",
                        "thead", "tbody", "tfoot")
                .allowAttributes("char").matching(ONE_CHAR)
                .onElements("td", "th", "tr", "colgroup", "col",
                        "thead", "tbody", "tfoot")
                .allowAttributes("colspan", "rowspan").matching(NUMBER)
                .onElements("td", "th")
                .allowAttributes("span", "width").matching(NUMBER_OR_PERCENT)
                .onElements("colgroup", "col")
                .allowElements(
                        "a", "label", "noscript", "h1", "h2", "h3", "h4", "h5", "h6",
                        "p", "i", "b", "u", "strong", "em", "small", "big", "pre", "code",
                        "cite", "samp", "sub", "sup", "strike", "center", "blockquote",
                        "hr", "br", "col", "font", "map", "span", "div", "img",
                        "ul", "ol", "li", "dd", "dt", "dl", "tbody", "thead", "tfoot",
                        "table", "td", "th", "tr", "colgroup", "fieldset", "legend");
    }

    public static SanitizerBuilder buildSanitizer(HtmlTagProcessor processor) {
        HtmlPolicyBuilder builder = getBaseBuilder().withPostprocessor(sink -> new HtmlStreamEventReceiverWrapper(sink) {
            @Override
            public void openTag(String elementName, List<String> attrs) {
                DefaultHtmlTagModifier modifier = new DefaultHtmlTagModifier(this, elementName, attrs);
                processor.process(elementName, modifier);
                super.openTag(modifier.name(), modifier.attrs());
            }
        });
        return new SanitizerBuilder() {
            @Override
            public SanitizerBuilder tag(String name) {
                builder.allowElements(name);
                return this;
            }

            @Override
            public SanitizerBuilder attr(String key, Pattern pattern) {
                HtmlPolicyBuilder.AttributeBuilder attributeBuilder = builder.allowAttributes(key);
                if (pattern != null) {
                    attributeBuilder.matching(pattern);
                }
                attributeBuilder.globally();
                return this;
            }

            @Override
            public SanitizerBuilder tagAndAttr(String name, String key, Pattern pattern) {
                HtmlPolicyBuilder.AttributeBuilder attributeBuilder = builder.allowAttributes(key);
                if (pattern != null) {
                    attributeBuilder.matching(pattern);
                }
                attributeBuilder.onElements(name);
                return this;
            }

            @Override
            public HtmlSanitizer get() {
                return builder.toFactory()::sanitize;
            }
        };
    }

    public static Map<String, String> extractAttr(String input) {
        Matcher m = HTML_ATTR_PATTERN.matcher(input);
        Map<String, String> result = new HashMap<>();
        while (m.find()) {
            String key = m.group(1);
            if (!StringUtils.isEmpty(key)) {
                result.put(key.toLowerCase(), m.group(2));
            }
        }
        return result;
    }

    private static ImageProp makeImageProp(String img) {
        Map<String, String> attrs = extractAttr(img);
        if (attrs.containsKey("src")) {
            return new ImageProp(attrs.get("src"), attrs.get("alt"));
        }
        return null;
    }

    public static List<ImageProp> extractImagesFromHtml(String text) {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        }
        Matcher m = HTML_IMG_PATTERN.matcher(text);
        List<ImageProp> result = new LinkedList<>();
        while (m.find()) {
            ImageProp imageProp = makeImageProp(m.group());
            if (imageProp != null) {
                result.add(imageProp);
            }
        }
        return result;
    }

    public static List<ImageProp> extractImagesFromMarkdown(String text) {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        }
        Matcher m = MARKDOWN_IMG_PATTERN.matcher(text);
        List<ImageProp> result = new LinkedList<>();
        while (m.find()) {
            String src = m.group(2);
            if (!StringUtils.isEmpty(src)) {
                result.add(new ImageProp(src, m.group(1)));
            }
        }
        result.addAll(extractImagesFromHtml(text));
        return result;
    }

    public static String htmlToText(String html) {
        if (StringUtils.isEmpty(html)) {
            return "";
        }
        return html.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "");
    }

    public interface HtmlSanitizer {
        String sanitizer(String raw);
    }

    public interface HtmlTagProcessor {
        void process(String tagName, HtmlTagModifier modifier);
    }

    public interface HtmlTagModifier {

        String name();

        void name(String name);

        void innerText(String text);

        String attr(String key);

        void attr(String key, String val);
    }

    public interface SanitizerBuilder {
        SanitizerBuilder tag(String name);

        SanitizerBuilder attr(String key, Pattern pattern);

        SanitizerBuilder tagAndAttr(String name, String key, Pattern pattern);

        HtmlSanitizer get();
    }

    private static final class DefaultHtmlTagModifier implements HtmlTagModifier {

        private final HtmlStreamEventReceiver receiver;

        private String tagName;
        private final List<String> attrs;

        private Map<String, String> attrsMap = null;

        private DefaultHtmlTagModifier(HtmlStreamEventReceiver receiver, String tagName, List<String> attrs) {
            this.receiver = receiver;
            this.tagName = tagName;
            this.attrs = attrs;
        }

        private void mappingAttrs() {
            if (attrsMap != null) {
                return;
            }
            this.attrsMap = new HashMap<>(this.attrs.size() / 2);
            for (int i = 0, n = this.attrs.size(); i < n; i += 2) {
                this.attrsMap.put(this.attrs.get(i), this.attrs.get(i + 1));
            }
        }

        @Override
        public String name() {
            return tagName;
        }

        @Override
        public void name(String name) {
            this.tagName = name;
        }

        @Override
        public void innerText(String text) {
            receiver.text(text);
        }

        @Override
        public String attr(String key) {
            mappingAttrs();
            return this.attrsMap.get(key);
        }

        @Override
        public void attr(String key, String val) {
            mappingAttrs();
            this.attrsMap.put(key, val);
        }

        private List<String> attrs() {
            if (this.attrsMap == null) {
                return this.attrs;
            }
            this.attrs.clear();
            for (Map.Entry<String, String> entry : this.attrsMap.entrySet()) {
                this.attrs.add(entry.getKey());
                this.attrs.add(entry.getValue());
            }
            return this.attrs;
        }

    }

}
