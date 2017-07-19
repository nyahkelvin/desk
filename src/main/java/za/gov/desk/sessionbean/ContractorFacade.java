/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.desk.sessionbean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import za.gov.desk.entity.Contractor;

/**
 *
 * @author clevasoft
 */
@Stateless
public class ContractorFacade extends AbstractFacade<Contractor> {

    @PersistenceContext(unitName = "za.gov.desk_desk_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ContractorFacade() {
        super(Contractor.class);
    }
    
}
