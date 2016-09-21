package org.elasticsearch.mapper.annotations;

import java.util.List;
import java.util.Map;

public class ContextSuggesterField {
    private List<String> input;
    private String output;
    private Map<String, List<String>> context;
    private Object payload;
    private Integer weight;

    public Map<String, List<String>> getContext() {
        return context;
    }

    public void setContext(Map<String, List<String>> context) {
        this.context = context;
    }

    public List<String> getInput() {
        return input;
    }

    public void setInput(List<String> input) {
        this.input = input;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
