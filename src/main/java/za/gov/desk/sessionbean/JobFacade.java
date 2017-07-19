package za.gov.desk.sessionbean;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import za.gov.desk.entity.Contractor;
import za.gov.desk.entity.Job;
import za.gov.desk.entity.JobCategory;
import za.gov.desk.entity.JobLocation;
import za.gov.desk.entity.JobPriority;
import za.gov.desk.entity.JobSubCategory;

@Stateless
public class JobFacade
  extends AbstractFacade<Job>
{
  @PersistenceContext(unitName="za.gov.desk_desk_war_1.0-SNAPSHOTPU")
  private EntityManager em;
  
  @Override
  protected EntityManager getEntityManager()
  {
    return this.em;
  }
  
  public JobFacade()
  {
    super(Job.class);
  }
  
  public List<Job> getOverdueJobs(boolean completed, Date nowDate)
  {
    String sql = "SELECT j FROM Job j WHERE j.completed = :completed AND :nowDate > j.dueDate ";
    
    TypedQuery createQuery = getEntityManager().createQuery(sql, Job.class);
    createQuery.setParameter("completed", completed);
    createQuery.setParameter("nowDate", nowDate);
    return createQuery.getResultList();
  }
  
  public List<Job> getOverdueJobsLocation(boolean completed, JobLocation location, Date nowDate)
  {
    String sql = "SELECT j FROM Job j WHERE j.completed = :completed AND :nowDate > j.dueDate AND j.location.id = :location ";
    TypedQuery createQuery = getEntityManager().createQuery(sql, Job.class);
    createQuery.setParameter("completed", completed);
    createQuery.setParameter("nowDate", nowDate);
    createQuery.setParameter("location", location.getId());
    return createQuery.getResultList();
  }
  
  public List<Job> findJobStatus(boolean completed)
  {
    String psql = "SELECT j FROM Job j WHERE j.completed = :completed";
    Query query = getEntityManager().createQuery(psql, Job.class);
    query.setParameter("completed", completed);
    return query.getResultList();
  }
  
  public List<Job> findJobStatusByLocation(boolean completed, JobLocation location)
  {
    String psql = "SELECT j FROM Job j WHERE j.completed = :completed AND j.location.id = :location";
    Query query = getEntityManager().createQuery(psql, Job.class);
    query.setParameter("completed", completed);
    query.setParameter("location", location.getId());
    return query.getResultList();
  }
  
  public List<Job> findJobStatusByLocation(JobLocation location)
  {
    String psql = "SELECT j FROM Job j WHERE j.location.id = :location";
    Query query = getEntityManager().createQuery(psql, Job.class);
    query.setParameter("location", location.getId());
    return query.getResultList();
  }
  
  public List<Job> findJobByContractor(Contractor contractor)
  {
    String psql = "SELECT j FROM Job j WHERE j.contractor.id = :contractor";
    Query query = getEntityManager().createQuery(psql, Job.class);
    query.setParameter("contractor", contractor.getId());
    return query.getResultList();
  }
  
  public List<Job> searchJOb(Job job)
  {
    String psql = "SELECT j FROM Job j WHERE j.category IS NOT NULL ";
    if (job.getCategory() != null) {
      psql = psql + " AND j.category.id = :category";
    }
    if (job.getSubCategory() != null) {
      psql = psql + " AND j.subCategory.id = :subCategory";
    }
    if (job.getLocation() != null) {
      psql = psql + " AND j.location.id = :location";
    }
    if (job.getPriority() != null) {
      psql = psql + " AND j.priority.id = :priority";
    }
    psql = psql + " ORDER BY j.completed ";
    
    Query query = getEntityManager().createQuery(psql, Job.class);
    if (job.getCategory() != null) {
      query.setParameter("category", job.getCategory().getId());
    }
    if (job.getSubCategory() != null) {
      query.setParameter("subCategory", job.getSubCategory().getId());
    }
    if (job.getLocation() != null) {
      query.setParameter("location", job.getLocation().getId());
    }
    if (job.getPriority() != null) {
      query.setParameter("priority", job.getPriority().getId());
    }
    return query.getResultList();
  }
  
  public List<Job> getJobByDateRange(Date dateA, Date dateB, boolean completed)
  {
    String sql = "SELECT i FROM Job i WHERE i.dateCreated BETWEEN :dateA AND :dateB AND i.completed = :completed";
    
    TypedQuery createQuery = getEntityManager().createQuery(sql, Job.class);
    createQuery.setParameter("dateA", dateA);
    createQuery.setParameter("dateB", dateB);
    createQuery.setParameter("completed", completed);
    return createQuery.getResultList();
  }
  
  public List<Job> getJobByDateRangeLocation(Date dateA, Date dateB, boolean completed, JobLocation location)
  {
    String sql = "SELECT i FROM Job i, JobLocation jl WHERE i.location.id = jl.id AND i.dateCreated BETWEEN :dateA AND :dateB AND i.completed = :completed AND i.location = :location ";
    
    TypedQuery createQuery = getEntityManager().createQuery(sql, Job.class);
    createQuery.setParameter("dateA", dateA);
    createQuery.setParameter("dateB", dateB);
    createQuery.setParameter("completed", completed);
    createQuery.setParameter("location", location);
    return createQuery.getResultList();
  }
  
  public List<Job> getJobByDateRangeOverdue(Date dateA, Date dateB, Date nowDate, boolean completed)
  {
    String sql = "SELECT i FROM Job i WHERE i.dateCreated BETWEEN :dateA AND :dateB  AND :nowDate > i.dueDate  ";
    
    TypedQuery createQuery = getEntityManager().createQuery(sql, Job.class);
    createQuery.setParameter("dateA", dateA);
    createQuery.setParameter("dateB", dateB);
    createQuery.setParameter("nowDate", nowDate);
    
    return createQuery.getResultList();
  }
  
  public List<Job> getJobByDateRangeLocationOverdue(Date dateA, Date dateB, Date nowDate, boolean completed, JobLocation location)
  {
    String sql = "SELECT i FROM Job i, JobLocation jl WHERE i.location.id = jl.id AND i.dateCreated BETWEEN :dateA AND :dateB AND i.location = :location AND :nowDate > i.dueDate ";
    
    TypedQuery createQuery = getEntityManager().createQuery(sql, Job.class);
    createQuery.setParameter("dateA", dateA);
    createQuery.setParameter("dateB", dateB);
    createQuery.setParameter("nowDate", nowDate);
    
    createQuery.setParameter("location", location);
    return createQuery.getResultList();
  }
}
