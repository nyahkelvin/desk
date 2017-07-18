package za.gov.desk.sessionbean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import za.gov.desk.entity.Staff;
import za.gov.desk.entity.Usr;

@Stateless
public class UsrFacade
  extends AbstractFacade<Usr>
{
  @PersistenceContext(unitName="za.gov.desk_desk_war_1.0-SNAPSHOTPU")
  private EntityManager em;
  
  @Override
  protected EntityManager getEntityManager()
  {
    return this.em;
  }
  
  public UsrFacade()
  {
    super(Usr.class);
  }
  
  public List<Usr> findByUsername(String username)
  {
    String psql = "SELECT u FROM Usr u WHERE u.username = :username";
    Query query = getEntityManager().createQuery(psql, Usr.class);
    query.setParameter("username", username);
    return query.getResultList();
  }
  
  public List<Usr> findByStaff(Staff staff)
  {
    String psql = "SELECT u FROM Usr u WHERE u.staff.id = :staff";
    Query query = getEntityManager().createQuery(psql, Usr.class);
    query.setParameter("staff", staff.getId());
    return query.getResultList();
  }
}
