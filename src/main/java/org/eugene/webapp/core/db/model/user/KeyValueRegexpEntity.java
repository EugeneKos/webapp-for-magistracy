package org.eugene.webapp.core.db.model.user;

import javax.persistence.*;

@Entity
@Table(name = "key_value_regexp")
public class KeyValueRegexpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DataFilterEntity getDataFilterEntity() {
        return dataFilterEntity;
    }

    public void setDataFilterEntity(DataFilterEntity dataFilterEntity) {
        this.dataFilterEntity = dataFilterEntity;
    }
}
