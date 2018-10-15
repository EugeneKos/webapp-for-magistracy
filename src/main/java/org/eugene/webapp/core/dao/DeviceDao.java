package org.eugene.webapp.core.dao;

import org.eugene.webapp.core.model.device.Device;
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
public class DeviceDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void persist(Device device){
        entityManager.persist(device);
    }

    @Transactional
    public void update(Device device) {
        entityManager.merge(device);
    }

    @Transactional
    public Device findByDeviceName(String name) {
        Device device = null;
        try {
            device = (Device) entityManager.createQuery("SELECT p FROM Device p WHERE p.name LIKE :name")
                    .setParameter("name",name)
                    .getSingleResult();
        } catch (NoResultException e){
            System.err.println("No result");
        }
        return device;
    }

    @Transactional
    public void removeByDeviceName(String deviceName) {
        Device device = findByDeviceName(deviceName);
        if (device != null) {
            entityManager.remove(device);
        }
    }

    @Transactional
    public Set<Device> findAll() {
        List<Device> devices = entityManager.createQuery("SELECT p FROM Device p").getResultList();
        return new HashSet<>(devices);
    }
}
