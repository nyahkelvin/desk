package za.gov.desk.sessionbean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import za.gov.desk.entity.Staff;
import za.gov.desk.entity.Usr;

@Stateless
public class StaffFacade
  extends AbstractFacade<Staff>
{
  @PersistenceContext(unitName="za.gov.desk_desk_war_1.0-SNAPSHOTPU")
  private EntityManager em;
  
  @Override
  protected EntityManager getEntityManager()
  {
    return this.em;
  }
  
  public StaffFacade()
  {
    super(Staff.class);
  }
  
  public Usr authenticate(String username, String password)
    throws AccountNotFoundException, FailedLoginException, AccountLockedException
  {
    try
    {
      Usr usr = findByUsername(username);
      if (!password.equals(usr.getPassword())) {
        throw new FailedLoginException();
      }
      return usr;
    }
    catch (NoResultException localNoResultException)
    {
      throw new AccountNotFoundException();
    }
  }
  
  public Usr findByUsername(String username)
  {
    String psql = "SELECT u FROM Usr u WHERE u.username = :username";
    Query query = getEntityManager().createQuery(psql, Usr.class);
    query.setParameter("username", username);
    return (Usr)query.getSingleResult();
  }
  
  public Usr findByUsernamePassword(String username, String password)
  {
    Usr u = null;
    try
    {
      String psql = "SELECT u FROM Usr u WHERE u.username = :username AND u.password = :password";
      Query query = getEntityManager().createQuery(psql, Usr.class);
      query.setParameter("username", username);
      query.setParameter("password", password);
      u = (Usr)query.getSingleResult();
    }
    catch (Exception e)
    {
      return new Usr();
    }
    return u;
  }
  
  public List<Usr> getUserByUserName(String username)
  {
    String sql = "SELECT u FROM Usr u WHERE u.username = :username";
    TypedQuery createQuery = getEntityManager().createQuery(sql, Usr.class);
    createQuery.setParameter("username", username);
    return createQuery.getResultList();
  }
  
  public List<Usr> getUserByStaff(Staff staff)
  {
    String sql = "SELECT u FROM Usr u WHERE u.staff.id = :staff";
    TypedQuery createQuery = getEntityManager().createQuery(sql, Usr.class);
    createQuery.setParameter("staff", staff.getId());
    return createQuery.getResultList();
  }
}
