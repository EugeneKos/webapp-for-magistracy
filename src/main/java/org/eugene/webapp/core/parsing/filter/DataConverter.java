package org.eugene.webapp.core.parsing.filter;

import javax.persistence.*;

@Entity
@Table(name = "converters")
public class DataConverter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "key")
    private String key;
    @Column(name = "input")
    private String input;
    @Column(name = "output")
    private String output;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DataFilter dataFilter;

    public DataConverter(String key, String input, String output) {
        this.key = key;
        this.input = input;
        this.output = output;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isContains(String input){
        return input.contains(this.input);
    }

    public String getConvertValue(String input){
        return input.replace(this.input,this.output);
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

    public DataFilter getDataFilter() {
        return dataFilter;
    }

    public void setDataFilter(DataFilter dataFilter) {
        this.dataFilter = dataFilter;
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
