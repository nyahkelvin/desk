package za.gov.desk.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="staff", catalog="desktop", schema="")
@XmlRootElement
@NamedQueries({@javax.persistence.NamedQuery(name="Staff.findAll", query="SELECT s FROM Staff s"), @javax.persistence.NamedQuery(name="Staff.findById", query="SELECT s FROM Staff s WHERE s.id = :id"), @javax.persistence.NamedQuery(name="Staff.findBySurname", query="SELECT s FROM Staff s WHERE s.surname = :surname"), @javax.persistence.NamedQuery(name="Staff.findByNames", query="SELECT s FROM Staff s WHERE s.names = :names"), @javax.persistence.NamedQuery(name="Staff.findByEmail", query="SELECT s FROM Staff s WHERE s.email = :email"), @javax.persistence.NamedQuery(name="Staff.findByCellNumber", query="SELECT s FROM Staff s WHERE s.cellNumber = :cellNumber"), @javax.persistence.NamedQuery(name="Staff.findByDateCreated", query="SELECT s FROM Staff s WHERE s.dateCreated = :dateCreated")})
public class Staff
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Basic(optional=false)
  @Column(name="id")
  private Integer id;
  @Basic(optional=false)
  @NotNull
  @Size(min=1, max=45)
  @Column(name="surname")
  private String surname;
  @Basic(optional=false)
  @NotNull
  @Size(min=1, max=45)
  @Column(name="names")
  private String names;
  @Basic(optional=false)
  @NotNull
  @Size(min=1, max=45)
  @Column(name="email")
  private String email;
  @Basic(optional=false)
  @NotNull
  @Size(min=1, max=45)
  @Column(name="cell_number")
  private String cellNumber;
  @Basic(optional=false)
  @NotNull
  @Column(name="date_created")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateCreated;
  @OneToMany(cascade={javax.persistence.CascadeType.ALL}, mappedBy="staff")
  private List<Usr> usrList;
  
  public Staff() {}
  
  public Staff(Integer id)
  {
    this.id = id;
  }
  
  public Staff(Integer id, String surname, String names, String email, String cellNumber, Date dateCreated)
  {
    this.id = id;
    this.surname = surname;
    this.names = names;
    this.email = email;
    this.cellNumber = cellNumber;
    this.dateCreated = dateCreated;
  }
  
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id)
  {
    this.id = id;
  }
  
  public String getSurname()
  {
    return this.surname;
  }
  
  public void setSurname(String surname)
  {
    this.surname = surname;
  }
  
  public String getNames()
  {
    return this.names;
  }
  
  public void setNames(String names)
  {
    this.names = names;
  }
  
  public String getEmail()
  {
    return this.email;
  }
  
  public void setEmail(String email)
  {
    this.email = email;
  }
  
  public String getCellNumber()
  {
    return this.cellNumber;
  }
  
  public void setCellNumber(String cellNumber)
  {
    this.cellNumber = cellNumber;
  }
  
  public Date getDateCreated()
  {
    return this.dateCreated;
  }
  
  public void setDateCreated(Date dateCreated)
  {
    this.dateCreated = dateCreated;
  }
  
  @XmlTransient
  public List<Usr> getUsrList()
  {
    return this.usrList;
  }
  
  public void setUsrList(List<Usr> usrList)
  {
    this.usrList = usrList;
  }
  
  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }
  
  @Override
  public boolean equals(Object object)
  {
    if (!(object instanceof Staff)) {
      return false;
    }
    Staff other = (Staff)object;
    if (((this.id == null) && (other.id != null)) || ((this.id != null) && (!this.id.equals(other.id)))) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString()
  {
    return this.names.toUpperCase() + " " + this.surname.toUpperCase();
  }
}
