package za.gov.desk.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="job_sub_category", catalog="desktop", schema="")
@XmlRootElement
@NamedQueries({@javax.persistence.NamedQuery(name="JobSubCategory.findAll", query="SELECT j FROM JobSubCategory j"), @javax.persistence.NamedQuery(name="JobSubCategory.findById", query="SELECT j FROM JobSubCategory j WHERE j.id = :id"), @javax.persistence.NamedQuery(name="JobSubCategory.findByName", query="SELECT j FROM JobSubCategory j WHERE j.name = :name")})
public class JobSubCategory
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
  @Column(name="name")
  private String name;
  @JoinColumn(name="category", referencedColumnName="id")
  @ManyToOne(optional=false)
  private JobCategory category;
  @OneToMany(cascade={javax.persistence.CascadeType.ALL}, mappedBy="subCategory")
  private List<Job> jobList;
  
  public JobSubCategory() {}
  
  public JobSubCategory(Integer id)
  {
    this.id = id;
  }
  
  public JobSubCategory(Integer id, String name)
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
  
  public JobCategory getCategory()
  {
    return this.category;
  }
  
  public void setCategory(JobCategory category)
  {
    this.category = category;
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
    if (!(object instanceof JobSubCategory)) {
      return false;
    }
    JobSubCategory other = (JobSubCategory)object;
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
