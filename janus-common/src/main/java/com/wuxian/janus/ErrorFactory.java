package com.wuxian.janus;

public final class ErrorFactory {

    private static final String PRE = "JANUS_0:";

    public static final String CREATE_INDEX = PRE + "CREATE_INDEX";

    public static final String RETRIEVE_INDEX = PRE + "RETRIEVE_INDEX";

    public static ErrorCodeException createCreateIndexError(String detail) {
        String message = "创建索引发生错误";
        return new ErrorCodeException(CREATE_INDEX, message, detail);
    }

    public static ErrorCodeException createRetrieveIndexError(String detail) {
        String message = "检索索引发生错误";
        return new ErrorCodeException(RETRIEVE_INDEX, message, detail);
    }
}
