package za.gov.desk.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="job_category", catalog="desktop", schema="")
@XmlRootElement
@NamedQueries({@javax.persistence.NamedQuery(name="JobCategory.findAll", query="SELECT j FROM JobCategory j"), @javax.persistence.NamedQuery(name="JobCategory.findById", query="SELECT j FROM JobCategory j WHERE j.id = :id"), @javax.persistence.NamedQuery(name="JobCategory.findByName", query="SELECT j FROM JobCategory j WHERE j.name = :name")})
public class JobCategory
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional=false)
  @NotNull
  @Column(name="id")
  private Integer id;
  @Basic(optional=false)
  @NotNull
  @Size(min=1, max=45)
  @Column(name="name")
  private String name;
  @OneToMany(mappedBy="category")
  private List<Contractor> contractorList;
  @OneToMany(cascade={javax.persistence.CascadeType.ALL}, mappedBy="category")
  private List<JobSubCategory> jobSubCategoryList;
  @OneToMany(cascade={javax.persistence.CascadeType.ALL}, mappedBy="category")
  private List<Job> jobList;
  
  public JobCategory() {}
  
  public JobCategory(Integer id)
  {
    this.id = id;
  }
  
  public JobCategory(Integer id, String name)
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
  public List<Contractor> getContractorList()
  {
    return this.contractorList;
  }
  
  public void setContractorList(List<Contractor> contractorList)
  {
    this.contractorList = contractorList;
  }
  
  @XmlTransient
  public List<JobSubCategory> getJobSubCategoryList()
  {
    return this.jobSubCategoryList;
  }
  
  public void setJobSubCategoryList(List<JobSubCategory> jobSubCategoryList)
  {
    this.jobSubCategoryList = jobSubCategoryList;
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
    if (!(object instanceof JobCategory)) {
      return false;
    }
    JobCategory other = (JobCategory)object;
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
