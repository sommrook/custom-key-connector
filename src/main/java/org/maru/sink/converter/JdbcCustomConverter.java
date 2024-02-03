package org.maru.sink.converter;


import java.util.Map;

public class JdbcCustomConverter {
    private Map<String, Object> schema;

    private Map<String, Object> payload;

    public Map<String, Object> getPayload() {
        return payload;
    }

    public JdbcCustomConverter(Map<String, Object> recordMap){
        this.schema = (Map<String, Object>) recordMap.get("schema");
        this.payload = (Map<String, Object>) recordMap.get("payload");
    }

}
