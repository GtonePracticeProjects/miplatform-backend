package com.bank.miplatform.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * JDK 표준 XML API 만으로 구현한 기본 어댑터 (벤더 jar 불필요, 컴파일 가능).
 *
 * 가정한 XML 형태 (실제 MiPlatform 버전의 스키마와 다를 수 있음 - 검증되지 않음):
 * <pre>
 * &lt;Root&gt;
 *   &lt;Variables&gt;&lt;Variable id="ErrorCode"&gt;0&lt;/Variable&gt;&lt;/Variables&gt;
 *   &lt;Dataset id="ds_x"&gt;
 *     &lt;ColumnInfo&gt;&lt;Column id="acctNo"/&gt;&lt;/ColumnInfo&gt;
 *     &lt;Rows&gt;&lt;Row&gt;&lt;Col id="acctNo"&gt;1234&lt;/Col&gt;&lt;/Row&gt;&lt;/Rows&gt;
 *   &lt;/Dataset&gt;
 * &lt;/Root&gt;
 * </pre>
 * 실제 연동 시에는 벤더 라이브러리 기반 구현체로 교체한다.
 */
@Component
public class XmlMiPlatformAdapter implements MiPlatformAdapter {

    private static final String CONTENT_TYPE = "text/xml;charset=UTF-8";

    @Override
    public MiRequest read(HttpServletRequest request) throws IOException {
        MiRequest result = new MiRequest();
        InputStream in = request.getInputStream();
        Document doc;
        try {
            doc = newBuilder().parse(in);
        } catch (Exception e) {
            throw new IOException("MiPlatform 요청 XML 을 해석할 수 없습니다.", e);
        }
        Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (!(n instanceof Element)) {
                continue;
            }
            Element e = (Element) n;
            if ("Variables".equals(e.getTagName())) {
                NodeList vars = e.getElementsByTagName("Variable");
                for (int j = 0; j < vars.getLength(); j++) {
                    Element v = (Element) vars.item(j);
                    result.getVariables().put(v.getAttribute("id"), v.getTextContent());
                }
            } else if ("Dataset".equals(e.getTagName())) {
                MiDataset ds = readDataset(e);
                result.getDatasets().put(ds.getName(), ds);
            }
        }
        return result;
    }

    private MiDataset readDataset(Element e) {
        MiDataset ds = new MiDataset(e.getAttribute("id"));
        NodeList cols = e.getElementsByTagName("Column");
        for (int i = 0; i < cols.getLength(); i++) {
            ds.addColumn(((Element) cols.item(i)).getAttribute("id"));
        }
        NodeList rows = e.getElementsByTagName("Row");
        for (int i = 0; i < rows.getLength(); i++) {
            Map<String, String> row = ds.addRow();
            NodeList cells = ((Element) rows.item(i)).getElementsByTagName("Col");
            for (int j = 0; j < cells.getLength(); j++) {
                Element c = (Element) cells.item(j);
                ds.addColumn(c.getAttribute("id"));
                row.put(c.getAttribute("id"), c.getTextContent());
            }
        }
        return ds;
    }

    @Override
    public void write(HttpServletResponse response, MiResponse data) throws IOException {
        try {
            Document doc = newBuilder().newDocument();
            Element root = doc.createElement("Root");
            doc.appendChild(root);

            Element vars = doc.createElement("Variables");
            root.appendChild(vars);
            for (Map.Entry<String, String> en : data.getVariables().entrySet()) {
                Element v = doc.createElement("Variable");
                v.setAttribute("id", en.getKey());
                v.setTextContent(en.getValue() == null ? "" : en.getValue());
                vars.appendChild(v);
            }
            for (MiDataset ds : data.getDatasets().values()) {
                root.appendChild(writeDataset(doc, ds));
            }

            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding("UTF-8");
            OutputStream out = response.getOutputStream();
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.transform(new DOMSource(doc), new StreamResult(out));
            out.flush();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("MiPlatform 응답 XML 생성 실패", e);
        }
    }

    private Element writeDataset(Document doc, MiDataset ds) {
        Element e = doc.createElement("Dataset");
        e.setAttribute("id", ds.getName());
        Element ci = doc.createElement("ColumnInfo");
        e.appendChild(ci);
        for (String c : ds.getColumns()) {
            Element col = doc.createElement("Column");
            col.setAttribute("id", c);
            ci.appendChild(col);
        }
        Element rows = doc.createElement("Rows");
        e.appendChild(rows);
        for (Map<String, String> r : ds.getRows()) {
            Element row = doc.createElement("Row");
            rows.appendChild(row);
            for (String c : ds.getColumns()) {
                String v = r.get(c);
                if (v == null) {
                    continue;
                }
                Element cell = doc.createElement("Col");
                cell.setAttribute("id", c);
                cell.setTextContent(v);
                row.appendChild(cell);
            }
        }
        return e;
    }

    /** XXE 방지 설정 */
    private DocumentBuilder newBuilder() throws Exception {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        f.setXIncludeAware(false);
        f.setExpandEntityReferences(false);
        return f.newDocumentBuilder();
    }
}
