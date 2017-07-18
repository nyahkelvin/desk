/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.desk.sessionbean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import za.gov.desk.entity.JobCategory;

/**
 *
 * @author clevasoft
 */
@Stateless
public class JobCategoryFacade extends AbstractFacade<JobCategory> {

    @PersistenceContext(unitName = "za.gov.desk_desk_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public JobCategoryFacade() {
        super(JobCategory.class);
    }
    
}
