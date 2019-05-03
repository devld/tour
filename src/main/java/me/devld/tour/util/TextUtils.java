package me.devld.tour.util;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    public static String urlDecode(String r) {
        try {
            return URLDecoder.decode(r, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException();
        }
    }

    public static String base64Encode(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    public static String base64Encode(String s) {
        return base64Encode(s.getBytes(StandardCharsets.UTF_8));
    }

    public static String join(String d, Object[] objects, int start) {
        return join(d, objects, start, objects.length - start);
    }

    public static String join(String d, Object[] objects, int start, int len) {
        if (start + len >= objects.length || start < 0 || len < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start, n = start + len; i < n; i++) {
            stringBuilder.append(objects[i]);
            if (i != n - 1) {
                stringBuilder.append(d);
            }
        }
        return stringBuilder.toString();
    }

    public static Map<String, String> parseQueryString(String query) {
        String[] temp = query.split("\\?");
        if (temp.length > 1) {
            query = temp[1];
        }
        Map<String, String> res = new HashMap<>();
        String[] queries = query.split("&");
        for (String q : queries) {
            String[] t = q.split("=");
            if (t.length > 0) {
                String key = t[0];
                String value = "";
                if (t.length > 1) {
                    value = t[1];
                }
                res.put(key, urlDecode(value));
            }
        }
        return res;
    }

    public static String makeQueryString(Map<String, String> queries) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> q : queries.entrySet()) {
            sb.append(q.getKey()).append("=").append(q.getValue()).append("&");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
