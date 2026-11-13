package com.bank.miplatform.adapter;

import java.util.LinkedHashMap;
import java.util.Map;

/** 서버가 돌려줄 VariableList + Dataset 묶음. 오류 코드/메시지는 ErrorCode(숫자, 0=정상), ErrorMsg 변수로 전달한다. */
public class MiResponse {

    public static final String VAR_ERROR_CODE = "ErrorCode";
    public static final String VAR_ERROR_MSG = "ErrorMsg";

    private final Map<String, String> variables = new LinkedHashMap<String, String>();
    private final Map<String, MiDataset> datasets = new LinkedHashMap<String, MiDataset>();

    public static MiResponse ok() {
        return new MiResponse().error("0", "OK");
    }

    public static MiResponse fail(String code, String message) {
        return new MiResponse().error(code, message);
    }

    private MiResponse error(String code, String message) {
        variables.put(VAR_ERROR_CODE, code);
        variables.put(VAR_ERROR_MSG, message);
        return this;
    }

    public MiResponse variable(String id, String value) {
        variables.put(id, value);
        return this;
    }

    public MiResponse dataset(MiDataset ds) {
        datasets.put(ds.getName(), ds);
        return this;
    }

    public Map<String, String> getVariables() { return variables; }

    public Map<String, MiDataset> getDatasets() { return datasets; }
}
