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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="job_location", catalog="desktop", schema="")
@XmlRootElement
@NamedQueries({@javax.persistence.NamedQuery(name="JobLocation.findAll", query="SELECT j FROM JobLocation j"), @javax.persistence.NamedQuery(name="JobLocation.findById", query="SELECT j FROM JobLocation j WHERE j.id = :id"), @javax.persistence.NamedQuery(name="JobLocation.findByName", query="SELECT j FROM JobLocation j WHERE j.name = :name")})
public class JobLocation
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
  @Size(min=1, max=75)
  @Column(name="name")
  private String name;
  @OneToMany(cascade={javax.persistence.CascadeType.ALL}, mappedBy="location")
  private List<Job> jobList;
  
  public JobLocation() {}
  
  public JobLocation(Integer id)
  {
    this.id = id;
  }
  
  public JobLocation(Integer id, String name)
  {
    this.id = id;
    this.name = name;
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
    if (!(object instanceof JobLocation)) {
      return false;
    }
    JobLocation other = (JobLocation)object;
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
