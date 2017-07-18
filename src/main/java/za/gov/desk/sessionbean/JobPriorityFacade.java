/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.desk.sessionbean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import za.gov.desk.entity.JobPriority;

/**
 *
 * @author clevasoft
 */
@Stateless
public class JobPriorityFacade extends AbstractFacade<JobPriority> {

    @PersistenceContext(unitName = "za.gov.desk_desk_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public JobPriorityFacade() {
        super(JobPriority.class);
    }
    
}
