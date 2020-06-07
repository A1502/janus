package com.wuxian.janus.core.calculate.error;

import com.wuxian.janus.struct.primary.IdType;

import java.util.ArrayList;
import java.util.List;

public class ErrorDataRecorder {

    public List<String> errorList = new ArrayList<>();

    public void add(String tableName, IdType id, String columnName, String data, String message) {
        add(tableName, id.toString(), columnName, data, message);
    }

    public void add(String tableName, String id, String columnName, String data, String message) {
        String errLog = String.format("tableName= %s ,id= %s , columnName= %s , data = %s , message = %s", tableName, id, columnName, data, message);
        errorList.add(errLog);
    }

    public boolean hasError() {
        return errorList.size() > 0;
    }

}
