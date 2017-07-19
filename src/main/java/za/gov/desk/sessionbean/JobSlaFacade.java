package za.gov.desk.sessionbean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import za.gov.desk.entity.Job;
import za.gov.desk.entity.JobSla;

@Stateless
public class JobSlaFacade
  extends AbstractFacade<JobSla>
{
  @PersistenceContext(unitName="za.gov.desk_desk_war_1.0-SNAPSHOTPU")
  private EntityManager em;
  
  @Override
  protected EntityManager getEntityManager()
  {
    return this.em;
  }
  
  public JobSlaFacade()
  {
    super(JobSla.class);
  }
  
  public List<JobSla> findSLAByJob(Job job)
  {
    String psql = "SELECT j FROM JobSla j WHERE j.job.id = :job";
    Query query = getEntityManager().createQuery(psql, JobSla.class);
    query.setParameter("job", job.getId());
    return query.getResultList();
  }
  
  public List<JobSla> findJobSLAName(Job job, String slaName)
  {
    String psql = "SELECT j FROM JobSla j WHERE j.job.id = :job AND j.slaName = :slaName";
    Query query = getEntityManager().createQuery(psql, JobSla.class);
    query.setParameter("job", job.getId());
    query.setParameter("slaName", slaName);
    return query.getResultList();
  }
}
