package org.eugene.webapp.core.dao;

import org.eugene.webapp.core.filter.DataFilter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Transactional
public class DataFilterDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void persist(DataFilter dataFilter){
        entityManager.persist(dataFilter);
    }

    @Transactional
    public void update(DataFilter dataFilter) {
        entityManager.merge(dataFilter);
    }

    @Transactional
    public DataFilter findByDataFilterName(String name) {
        DataFilter dataFilter = null;
        try {
            dataFilter = (DataFilter) entityManager.createQuery("SELECT p FROM DataFilter p WHERE p.name LIKE :name")
                    .setParameter("name",name)
                    .getSingleResult();
        } catch (NoResultException e){
            System.err.println("No result");
        }
        return dataFilter;
    }

    @Transactional
    public void removeByDataFilterName(String dataFilterName) {
        DataFilter dataFilter = findByDataFilterName(dataFilterName);
        if (dataFilter != null) {
            entityManager.remove(dataFilter);
        }
    }

    @Transactional
    public Set<DataFilter> findAll() {
        List<DataFilter> dataFilters = entityManager.createQuery("SELECT p FROM DataFilter p").getResultList();
        return new HashSet<>(dataFilters);
    }
}
