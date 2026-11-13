package com.bank.miplatform.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 벤더 라이브러리에 의존하지 않는 Dataset 중립 모델.
 * (MiPlatform Dataset: 컬럼 목록 + 행 목록. 값은 문자열로 보관한다.)
 */
public class MiDataset {

    private final String name;
    private final List<String> columns = new ArrayList<String>();
    private final List<Map<String, String>> rows = new ArrayList<Map<String, String>>();

    public MiDataset(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public List<String> getColumns() { return columns; }

    public List<Map<String, String>> getRows() { return rows; }

    public int getRowCount() { return rows.size(); }

    public MiDataset addColumn(String col) {
        if (!columns.contains(col)) {
            columns.add(col);
        }
        return this;
    }

    /** 새 행을 추가하고 값을 채울 수 있는 Map 을 돌려준다. */
    public Map<String, String> addRow() {
        Map<String, String> row = new LinkedHashMap<String, String>();
        rows.add(row);
        return row;
    }

    public String getString(int row, String col) {
        if (row < 0 || row >= rows.size()) {
            return null;
        }
        return rows.get(row).get(col);
    }

    public long getLong(int row, String col) {
        String v = getString(row, col);
        return (v == null || v.trim().isEmpty()) ? 0L : Long.parseLong(v.trim().replace(",", ""));
    }

    public int getInt(int row, String col) {
        return (int) getLong(row, col);
    }
}
