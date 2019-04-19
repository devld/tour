package me.devld.tour.util;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class TextUtils {

    public static boolean isEmail(String email) {
        if (email != null) {
            return email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        }
        return false;
    }

    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>();

    static {
        IMAGE_EXTENSIONS.add("jpg");
        IMAGE_EXTENSIONS.add("jpeg");
        IMAGE_EXTENSIONS.add("png");
        IMAGE_EXTENSIONS.add("webp");
        IMAGE_EXTENSIONS.add("gif");
    }

    public static boolean isImageFilename(String filename) {
        String ext = StringUtils.getFilenameExtension(filename);
        return ext != null && IMAGE_EXTENSIONS.contains(ext.toLowerCase());
    }

    public static String formatPath(String path) {
        return path.replaceAll("[\\\\/]+", "/");
    }

    public static String formatDirPath(String dirPath) {
        return formatPath(dirPath + "/");
    }

    public static Long parseLong(String s) {
        Long r = null;
        try {
            r = Long.parseLong(s);
        } catch (NumberFormatException ignored) {
        }
        return r;
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String base64Encode(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    public static String base64Encode(String s) {
        return base64Encode(s.getBytes(StandardCharsets.UTF_8));
    }

}
