package me.devld.tour.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18nUtil {

    private static MessageSource messageSource;

    public I18nUtil(MessageSource messageSource) {
        I18nUtil.messageSource = messageSource;
    }

    private static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public static String getMessage(String code, Object[] args, Locale locale) {
        return messageSource.getMessage(code, args, code, locale);
    }

    public static String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, code, getLocale());
    }

    public static String getMessage(String code) {
        return messageSource.getMessage(code, null, code, getLocale());
    }

}
