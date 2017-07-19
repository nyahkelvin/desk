package za.gov.desk.sessionbean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import za.gov.desk.entity.JobCategory;
import za.gov.desk.entity.JobSubCategory;

@Stateless
public class JobSubCategoryFacade
  extends AbstractFacade<JobSubCategory>
{
  @PersistenceContext(unitName="za.gov.desk_desk_war_1.0-SNAPSHOTPU")
  private EntityManager em;
  
  @Override
  protected EntityManager getEntityManager()
  {
    return this.em;
  }
  
  public JobSubCategoryFacade()
  {
    super(JobSubCategory.class);
  }
  
  public List<JobSubCategory> getSubCategoryByCategory(JobCategory jobCategory)
  {
    String sql = "SELECT i FROM JobSubCategory i WHERE i.category.id = :jobCategory";
    TypedQuery createQuery = getEntityManager().createQuery(sql, JobSubCategory.class);
    createQuery.setParameter("jobCategory", jobCategory.getId());
    return createQuery.getResultList();
  }
}
