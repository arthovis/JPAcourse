package entities.simple;

import org.junit.Assert;
import org.junit.Test;
import setup.TransactionalSetup;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestEntityCRUDSimple {

    @Test
    public void testCreateReadUpdateReadRemoveRead() {
        String PERSISTENCE_UNIT_NAME = "examplePersistenceUnit";
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        //select by ID - expect none
        {
            Entity entity = entityManager.find(Entity.class, 1);
            Assert.assertNull(entity);
        }

        //create 1 record
        {
            Entity entity = new Entity();
            entity.setId(1);
            entity.setName("xxx");
            entity.setValue(3);
            entityManager.persist(entity);
            entityManager.flush();
            //entityManager.clear();
            entityManager.detach(entity);
        }

        //select by id - expect exactly 1
        {
            Entity entity = entityManager.find(Entity.class, 1);
            Assert.assertNotNull(entity);
            Assert.assertEquals(new Integer(1), entity.getId());
            Assert.assertEquals("xxx", entity.getName());
            Assert.assertEquals(new Integer(3), entity.getValue());
        }

        //update
        {
            Entity entity1 = entityManager.find(Entity.class, 1);
            entity1.setName("yyy");
            Entity entity2 = entityManager.find(Entity.class, 1);
            entity2.setName("yyy");
            entityManager.flush();
            entityManager.clear();
        }

        //select by id - expect exactly 1 updated
        {
            Entity entity = entityManager.find(Entity.class, 1);
            Assert.assertNotNull(entity);
            Assert.assertEquals(new Integer(1), entity.getId());
            Assert.assertEquals("yyy", entity.getName());
            Assert.assertEquals(new Integer(3), entity.getValue());
        }

        //remove
        {
            Entity entity = entityManager.find(Entity.class, 1);
            entityManager.remove(entity);
            entityManager.flush();
        }

        //select by ID - expect nothing
        {
            Entity entity = entityManager.find(Entity.class, 1);
            Assert.assertNull(entity);
        }

        entityManager.getTransaction().rollback();
        entityManager.close();
        entityManagerFactory.close();
    }

}