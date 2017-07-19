package za.gov.desk.sessionbean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import za.gov.desk.entity.Contractor;
import za.gov.desk.entity.ContractorBudget;

@Stateless
public class ContractorBudgetFacade
  extends AbstractFacade<ContractorBudget>
{
  @PersistenceContext(unitName="za.gov.desk_desk_war_1.0-SNAPSHOTPU")
  private EntityManager em;
  
  @Override
  protected EntityManager getEntityManager()
  {
    return this.em;
  }
  
  public ContractorBudgetFacade()
  {
    super(ContractorBudget.class);
  }
  
  public ContractorBudget findBudgetByYearContractor(Integer year, Contractor contractor)
  {
    try
    {
      String psql = "SELECT c FROM ContractorBudget c WHERE c.year = :year AND c.contractor.id = :contractor";
      Query query = getEntityManager().createQuery(psql, ContractorBudget.class);
      query.setParameter("year", year);
      query.setParameter("contractor", contractor.getId());
      return (ContractorBudget)query.getSingleResult();
    }
    catch (Exception e) {}
    return new ContractorBudget();
  }
  
  public List<ContractorBudget> findBudgetByContractor(Contractor contractor)
  {
    String psql = "SELECT c FROM ContractorBudget c WHERE c.contractor.id = :contractor";
    Query query = getEntityManager().createQuery(psql, ContractorBudget.class);
    query.setParameter("contractor", contractor.getId());
    
    return query.getResultList();
  }
}
