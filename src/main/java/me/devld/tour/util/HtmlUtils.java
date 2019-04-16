package me.devld.tour.util;

import org.owasp.html.PolicyFactory;
import org.owasp.html.examples.EbayPolicyExample;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {

    private static final PolicyFactory POLICY = EbayPolicyExample.POLICY_DEFINITION;

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
