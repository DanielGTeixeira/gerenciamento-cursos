package br.com.seuapp.utils;
import javax.persistence.*;
public class JPAUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("gerenciamentoPU");
    public static EntityManager getEntityManager() { return emf.createEntityManager(); }
}