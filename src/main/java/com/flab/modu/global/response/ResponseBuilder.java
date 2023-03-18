package com.flab.modu.global.response;

public class ResponseBuilder {

    public static String getErrorJsonString(String errorMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"message\":\"");
        sb.append(errorMessage);
        sb.append("\"}");
        return sb.toString();
    }
}
