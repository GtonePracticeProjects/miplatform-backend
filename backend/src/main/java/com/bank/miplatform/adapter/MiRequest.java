package com.bank.miplatform.adapter;

import java.util.LinkedHashMap;
import java.util.Map;

/** 클라이언트가 보낸 VariableList + Dataset 묶음 */
public class MiRequest {

    private final Map<String, String> variables = new LinkedHashMap<String, String>();
    private final Map<String, MiDataset> datasets = new LinkedHashMap<String, MiDataset>();

    public Map<String, String> getVariables() { return variables; }

    public Map<String, MiDataset> getDatasets() { return datasets; }

    public String getVariable(String id) { return variables.get(id); }

    /** 없으면 null */
    public MiDataset getDataset(String name) { return datasets.get(name); }
}
