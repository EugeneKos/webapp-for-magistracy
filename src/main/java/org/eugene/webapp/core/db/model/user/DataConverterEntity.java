package org.eugene.webapp.core.db.model.user;

import javax.persistence.*;

@Entity
@Table(name = "converters")
public class DataConverterEntity {
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
    private DataFilterEntity dataFilterEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public DataFilterEntity getDataFilterEntity() {
        return dataFilterEntity;
    }

    public void setDataFilterEntity(DataFilterEntity dataFilterEntity) {
        this.dataFilterEntity = dataFilterEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataConverterEntity that = (DataConverterEntity) o;

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
