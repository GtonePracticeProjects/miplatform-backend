package com.bank.miplatform.adapter;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/** VO(JavaBean) 목록 → MiDataset 변환 유틸. 컬럼명은 VO 프로퍼티명(acctNo 등)을 그대로 쓴다. */
public final class MiDatasets {

    private MiDatasets() {
    }

    public static MiDataset fromBeans(String name, Class<?> type, List<?> beans) {
        MiDataset ds = new MiDataset(name);
        PropertyDescriptor[] props = describe(type);
        for (PropertyDescriptor pd : props) {
            ds.addColumn(pd.getName());
        }
        if (beans == null) {
            return ds;
        }
        for (Object bean : beans) {
            Map<String, String> row = ds.addRow();
            for (PropertyDescriptor pd : props) {
                row.put(pd.getName(), format(read(pd, bean)));
            }
        }
        return ds;
    }

    public static MiDataset fromBean(String name, Class<?> type, Object bean) {
        return fromBeans(name, type, bean == null ? null : java.util.Collections.singletonList(bean));
    }

    private static PropertyDescriptor[] describe(Class<?> type) {
        try {
            java.util.List<PropertyDescriptor> out = new java.util.ArrayList<PropertyDescriptor>();
            for (PropertyDescriptor pd : Introspector.getBeanInfo(type, Object.class).getPropertyDescriptors()) {
                if (pd.getReadMethod() != null) {
                    out.add(pd);
                }
            }
            return out.toArray(new PropertyDescriptor[0]);
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Object read(PropertyDescriptor pd, Object bean) {
        try {
            Method m = pd.getReadMethod();
            return m.invoke(bean);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static String format(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof Date) {
            return new SimpleDateFormat("yyyyMMddHHmmss").format((Date) v);
        }
        return String.valueOf(v);
    }
}
