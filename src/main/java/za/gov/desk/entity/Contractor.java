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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="contractor", catalog="desktop", schema="")
@XmlRootElement
@NamedQueries({@javax.persistence.NamedQuery(name="Contractor.findAll", query="SELECT c FROM Contractor c"), @javax.persistence.NamedQuery(name="Contractor.findById", query="SELECT c FROM Contractor c WHERE c.id = :id"), @javax.persistence.NamedQuery(name="Contractor.findByName", query="SELECT c FROM Contractor c WHERE c.name = :name"), @javax.persistence.NamedQuery(name="Contractor.findByTelephone", query="SELECT c FROM Contractor c WHERE c.telephone = :telephone"), @javax.persistence.NamedQuery(name="Contractor.findByEmail", query="SELECT c FROM Contractor c WHERE c.email = :email"), @javax.persistence.NamedQuery(name="Contractor.findByCellNumber", query="SELECT c FROM Contractor c WHERE c.cellNumber = :cellNumber"), @javax.persistence.NamedQuery(name="Contractor.findByAddress", query="SELECT c FROM Contractor c WHERE c.address = :address")})
public class Contractor
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Basic(optional=false)
  @Column(name="id")
  private Integer id;
  @Size(max=145)
  @Column(name="name")
  private String name;
  @Size(max=45)
  @Column(name="telephone")
  private String telephone;
  @Size(max=45)
  @Column(name="email")
  private String email;
  @Size(max=45)
  @Column(name="cell_number")
  private String cellNumber;
  @Size(max=150)
  @Column(name="address")
  private String address;
  @OneToMany(cascade={javax.persistence.CascadeType.ALL}, mappedBy="contractor")
  private List<ContractorBudget> contractorBudgetList;
  @Size(max=145)
  @Column(name="contact_person")
  private String contactPerson;
  @JoinColumn(name="category", referencedColumnName="id")
  @ManyToOne
  private JobCategory category;
  @OneToMany(mappedBy="contractor")
  private List<Job> jobList;
  
  public Contractor() {}
  
  public Contractor(Integer id)
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
  
  public String getTelephone()
  {
    return this.telephone;
  }
  
  public void setTelephone(String telephone)
  {
    this.telephone = telephone;
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
  
  public String getAddress()
  {
    return this.address;
  }
  
  public void setAddress(String address)
  {
    this.address = address;
  }
  
  public String getContactPerson()
  {
    return this.contactPerson;
  }
  
  public void setContactPerson(String contactPerson)
  {
    this.contactPerson = contactPerson;
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
  public List<ContractorBudget> getContractorBudgetList()
  {
    return this.contractorBudgetList;
  }
  
  public void setContractorBudgetList(List<ContractorBudget> contractorBudgetList)
  {
    this.contractorBudgetList = contractorBudgetList;
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
  
  public int hashCode()
  {
    int hash = 0;
    hash += (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }
  
  public boolean equals(Object object)
  {
    if (!(object instanceof Contractor)) {
      return false;
    }
    Contractor other = (Contractor)object;
    if (((this.id == null) && (other.id != null)) || ((this.id != null) && (!this.id.equals(other.id)))) {
      return false;
    }
    return true;
  }
  
  public String toString()
  {
    return this.name + " -> " + this.category.getName() + " -> " + this.address;
  }
}
