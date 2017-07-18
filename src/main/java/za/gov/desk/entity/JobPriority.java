package za.gov.desk.entity;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="job_priority", catalog="desktop", schema="")
@XmlRootElement
@NamedQueries({@javax.persistence.NamedQuery(name="JobPriority.findAll", query="SELECT j FROM JobPriority j"), @javax.persistence.NamedQuery(name="JobPriority.findById", query="SELECT j FROM JobPriority j WHERE j.id = :id"), @javax.persistence.NamedQuery(name="JobPriority.findByName", query="SELECT j FROM JobPriority j WHERE j.name = :name")})
public class JobPriority
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Basic(optional=false)
  @Column(name="id")
  private Integer id;
  @Size(max=45)
  @Column(name="name")
  private String name;
  @Column(name="ref")
  private Integer ref;
  @OneToMany(cascade={javax.persistence.CascadeType.ALL}, mappedBy="priority")
  private List<Job> jobList;
  
  public JobPriority() {}
  
  public JobPriority(Integer id)
  {
    this.id = id;
  }
  
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id)
  {
    this.id = id;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public Integer getRef()
  {
    return this.ref;
  }
  
  public void setRef(Integer ref)
  {
    this.ref = ref;
  }
  
  @XmlTransient
  public List<Job> getJobList()
  {
    return this.jobList;
  }
  
  public void setJobList(List<Job> jobList)
  {
    this.jobList = jobList;
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
    if (!(object instanceof JobPriority)) {
      return false;
    }
    JobPriority other = (JobPriority)object;
    if (((this.id == null) && (other.id != null)) || ((this.id != null) && (!this.id.equals(other.id)))) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString()
  {
    return this.name;
  }
}
