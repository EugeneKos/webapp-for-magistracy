package org.eugene.webapp.core.parsing.filter;

public class DataConverter {
    private String key;
    private String input;
    private String output;

    public DataConverter(String key, String input, String output) {
        this.key = key;
        this.input = input;
        this.output = output;
    }

    public boolean isInputMatch(String input){
        return this.input.equals(input);
    }

    public String getKey() {
        return key;
    }

    public String getInput() {
        return input;
    }

    public String getOutput(){
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataConverter that = (DataConverter) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (input != null ? !input.equals(that.input) : that.input != null) return false;
        return output != null ? output.equals(that.output) : that.output == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (input != null ? input.hashCode() : 0);
        result = 31 * result + (output != null ? output.hashCode() : 0);
        return result;
    }
}
