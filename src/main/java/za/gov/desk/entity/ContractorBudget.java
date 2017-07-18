package za.gov.desk.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="contractor_budget", catalog="desktop", schema="")
@XmlRootElement
@NamedQueries({@javax.persistence.NamedQuery(name="ContractorBudget.findAll", query="SELECT c FROM ContractorBudget c"), @javax.persistence.NamedQuery(name="ContractorBudget.findById", query="SELECT c FROM ContractorBudget c WHERE c.id = :id"), @javax.persistence.NamedQuery(name="ContractorBudget.findByYear", query="SELECT c FROM ContractorBudget c WHERE c.year = :year"), @javax.persistence.NamedQuery(name="ContractorBudget.findByAmount", query="SELECT c FROM ContractorBudget c WHERE c.amount = :amount")})
public class ContractorBudget
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
  @Column(name="year")
  private int year;
  @Basic(optional=false)
  @NotNull
  @Column(name="amount")
  private double amount;
  @JoinColumn(name="contractor", referencedColumnName="id")
  @ManyToOne(optional=false)
  private Contractor contractor;
  
  public ContractorBudget() {}
  
  public ContractorBudget(Integer id)
  {
    this.id = id;
  }
  
  public ContractorBudget(Integer id, int year, double amount)
  {
    this.id = id;
    this.year = year;
    this.amount = amount;
  }
  
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id)
  {
    this.id = id;
  }
  
  public int getYear()
  {
    return this.year;
  }
  
  public void setYear(int year)
  {
    this.year = year;
  }
  
  public double getAmount()
  {
    return this.amount;
  }
  
  public void setAmount(double amount)
  {
    this.amount = amount;
  }
  
  public Contractor getContractor()
  {
    return this.contractor;
  }
  
  public void setContractor(Contractor contractor)
  {
    this.contractor = contractor;
  }
  
  public int hashCode()
  {
    int hash = 0;
    hash += (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }
  
  public boolean equals(Object object)
  {
    if (!(object instanceof ContractorBudget)) {
      return false;
    }
    ContractorBudget other = (ContractorBudget)object;
    if (((this.id == null) && (other.id != null)) || ((this.id != null) && (!this.id.equals(other.id)))) {
      return false;
    }
    return true;
  }
  
  public String toString()
  {
    return this.amount + "";
  }
}
