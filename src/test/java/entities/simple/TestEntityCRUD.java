package entities.simple;

import org.junit.Assert;
import org.junit.Test;
import setup.TransactionalSetup;

public class TestEntityCRUD extends TransactionalSetup {

    @Test
    public void testCreateReadUpdateReadRemoveRead() {

        //select by ID - expect none
        {
            Entity entity = em.find(Entity.class, 1);
            Assert.assertNull(entity);
        }

        //create 1 record
        {
            Entity entity = new Entity();
            entity.setId(1);
            entity.setName("xxx");
            entity.setValue(3);
            em.persist(entity);
            em.flush();
            //entityManager.clear();
            em.detach(entity);
        }

        //select by id - expect exactly 1
        {
            Entity entity = em.find(Entity.class, 1);
            Assert.assertNotNull(entity);
            Assert.assertEquals(new Integer(1), entity.getId());
            Assert.assertEquals("xxx", entity.getName());
            Assert.assertEquals(new Integer(3), entity.getValue());
        }

        //update
        {
            Entity entity1 = em.find(Entity.class, 1);
            entity1.setName("yyy");
            Entity entity2 = em.find(Entity.class, 1);
            entity2.setName("yyy");
            em.flush();
            em.clear();
        }

        //select by id - expect exactly 1 updated
        {
            Entity entity = em.find(Entity.class, 1);
            Assert.assertNotNull(entity);
            Assert.assertEquals(new Integer(1), entity.getId());
            Assert.assertEquals("yyy", entity.getName());
            Assert.assertEquals(new Integer(3), entity.getValue());
        }

        //remove
        {
            Entity entity = em.find(Entity.class, 1);
            em.remove(entity);
            em.flush();
        }

        //select by ID - expect nothing
        {
            Entity entity = em.find(Entity.class, 1);
            Assert.assertNull(entity);
        }

    }
}


