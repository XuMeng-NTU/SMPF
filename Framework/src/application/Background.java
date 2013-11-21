/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import support.database.DatabaseManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import support.implementation.ImplementationManager;
import support.memory.MemoryManager;

/**
 *
 * @author Meng
 */
public class Background {
    
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private DatabaseManager databaseManager;
    private ImplementationManager implementationManager;
    private MemoryManager memoryManager;
    
    private Background() {
        entityManagerFactory = Persistence.createEntityManagerFactory("PersistenceUnit"); 
        entityManager = entityManagerFactory.createEntityManager();
        databaseManager = new DatabaseManager();
        implementationManager = new ImplementationManager();
        memoryManager = new MemoryManager();
    }
    
    public EntityManager getEntityManager(){
        return entityManager;
    }
    
    public DatabaseManager getDatabaseManager(){
        return databaseManager;
    }
    
    public ImplementationManager getImplementationManager(){
        return implementationManager;
    }
    
    public MemoryManager getMemoryManager(){
        return memoryManager;
    }
    
    public void close(){
        entityManager.close();
        entityManagerFactory.close();
        databaseManager.close();
    }
    
    public static Background getInstance() {
        return BackgroundHolder.INSTANCE;
    }
    
    private static class BackgroundHolder {

        private static final Background INSTANCE = new Background();
    }
}
