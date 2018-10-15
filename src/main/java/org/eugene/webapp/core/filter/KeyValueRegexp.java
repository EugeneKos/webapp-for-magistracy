package org.eugene.webapp.core.filter;

import javax.persistence.*;

@Entity
@Table(name = "key_value_regexp")
public class KeyValueRegexp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DataFilter dataFilter;

    public KeyValueRegexp(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValueRegexp() {}

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

        KeyValueRegexp that = (KeyValueRegexp) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
