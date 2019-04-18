package me.devld.tour.util;

import org.owasp.html.HtmlPolicyBuilder;
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
    private static final PolicyFactory POLICY = new HtmlPolicyBuilder()
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
                    "table", "td", "th", "tr", "colgroup", "fieldset", "legend")
            .toFactory();

    private static Predicate<String> matchesEither(
            final Pattern a, final Pattern b) {
        return s -> a.matcher(s).matches() || b.matcher(s).matches();
    }

    ;

    private static final Pattern HTML_IMG_PATTERN = Pattern.compile("<img[^<]*/?>", Pattern.CASE_INSENSITIVE);
    private static final Pattern HTML_ATTR_PATTERN = Pattern.compile("([A-z0-9\\-_]+)=(?:[\"'](.*?)[\"'])?");
    private static final Pattern MARKDOWN_IMG_PATTERN = Pattern.compile("(?:!\\[(.*?)]\\((.*?)\\))");

    public static String sanitizer(String raw) {
        return POLICY.sanitize(raw);
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

    public static class ImageProp {
        private String src;
        private String alt;

        ImageProp(String src, String alt) {
            this.src = src;
            this.alt = alt;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        @Override
        public String toString() {
            return "ImageProp{" +
                    "src='" + src + '\'' +
                    ", alt='" + alt + '\'' +
                    '}';
        }
    }

}
