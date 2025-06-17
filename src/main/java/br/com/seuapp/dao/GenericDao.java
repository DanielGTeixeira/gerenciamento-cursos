package br.com.seuapp.dao;

import javax.persistence.*;
import java.util.List;
import br.com.seuapp.utils.JPAUtil;

public abstract class GenericDao<T> {
    protected EntityManager em = JPAUtil.getEntityManager();

    public void create(T obj) { EntityTransaction tx = em.getTransaction(); tx.begin(); em.persist(obj); tx.commit(); }
    public void update(T obj) { EntityTransaction tx = em.getTransaction(); tx.begin(); em.merge(obj); tx.commit(); }
    public void delete(T obj) { EntityTransaction tx = em.getTransaction(); tx.begin(); em.remove(em.contains(obj)? obj: em.merge(obj)); tx.commit(); }
    public T findById(Class<T> clazz, Long id) { return em.find(clazz, id); }
    public List<T> findAll(Class<T> clazz) { return em.createQuery("FROM " + clazz.getSimpleName(), clazz).getResultList(); }
}