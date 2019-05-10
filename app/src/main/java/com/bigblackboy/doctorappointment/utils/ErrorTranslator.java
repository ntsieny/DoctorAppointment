package com.bigblackboy.doctorappointment.utils;

public class ErrorTranslator {

    public static final int COMMENTS_NOT_FOUND = 0;

    public static String getDescription(int errorType) {
        String error;
        switch (errorType) {
            case COMMENTS_NOT_FOUND:
                error = "Комментарии не найдены";
                break;
            default:
                error = "Ошибка";
                break;
        }
        return error;
    }
}
