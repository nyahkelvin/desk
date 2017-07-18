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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import za.gov.desk.jsf.util.DateUtil;

@Entity
@Table(name="job", catalog="desktop", schema="")
@XmlRootElement
@NamedQueries({@javax.persistence.NamedQuery(name="Job.findAll", query="SELECT j FROM Job j"), @javax.persistence.NamedQuery(name="Job.findById", query="SELECT j FROM Job j WHERE j.id = :id"), @javax.persistence.NamedQuery(name="Job.findByDescription", query="SELECT j FROM Job j WHERE j.description = :description"), @javax.persistence.NamedQuery(name="Job.findByCompleted", query="SELECT j FROM Job j WHERE j.completed = :completed"), @javax.persistence.NamedQuery(name="Job.findByCompletionDate", query="SELECT j FROM Job j WHERE j.completionDate = :completionDate"), @javax.persistence.NamedQuery(name="Job.findByInvoicePaid", query="SELECT j FROM Job j WHERE j.invoicePaid = :invoicePaid"), @javax.persistence.NamedQuery(name="Job.findByDateCreated", query="SELECT j FROM Job j WHERE j.dateCreated = :dateCreated"), @javax.persistence.NamedQuery(name="Job.findByBaseBudget", query="SELECT j FROM Job j WHERE j.baseBudget = :baseBudget")})
public class Job
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
  @Size(min=1, max=250)
  @Column(name="description")
  private String description;
  @Basic(optional=false)
  @NotNull(message="Reporter details may not be null")
  @Size(min=3, max=250, message="Reporter filed must be more than 3 characters")
  @Column(name="reporter")
  private String reporter;
  @Basic(optional=false)
  @NotNull(message="Reporter contact may not be null")
  @Size(min=10, max=10, message="Phone number filed must be more than 10 min")
  @Column(name="reporter_cell")
  private String reporterCell;
  @Basic(optional=false)
  @NotNull(message="Physical Address may not be null")
  @Size(min=5, max=145, message="Physical Address filed must be more than 5 characters")
  @Column(name="physical_address")
  private String physicalAddress;
  @Size(min=1, max=45)
  @Column(name="precaution")
  private String precaution;
  @Column(name="completed")
  private boolean completed;
  @Column(name="completion_date")
  @Temporal(TemporalType.DATE)
  private Date completionDate;
  @Column(name="due_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dueDate;
  @Column(name="invoice_paid")
  private boolean invoicePaid;
  @Basic(optional=false)
  @NotNull
  @Column(name="date_created")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateCreated;
  @Basic(optional=false)
  @NotNull
  @Column(name="base_budget")
  private double baseBudget;
  @Column(name="actual_cost")
  private double actualCost;
  @OneToMany(cascade={javax.persistence.CascadeType.ALL}, mappedBy="job")
  private List<JobSla> jobSlaList;
  @JoinColumn(name="category", referencedColumnName="id")
  @ManyToOne(optional=false)
  private JobCategory category;
  @JoinColumn(name="contractor", referencedColumnName="id")
  @ManyToOne
  private Contractor contractor;
  @JoinColumn(name="location", referencedColumnName="id")
  @ManyToOne(optional=false)
  private JobLocation location;
  @JoinColumn(name="priority", referencedColumnName="id")
  @ManyToOne(optional=false)
  private JobPriority priority;
  @JoinColumn(name="sub_category", referencedColumnName="id")
  @ManyToOne(optional=false)
  private JobSubCategory subCategory;
  @JoinColumn(name="usr", referencedColumnName="id")
  @ManyToOne(optional=false)
  private Usr usr;
  private transient String jobNumber;
  private transient String invoicePaidYesNo;
  private transient String completedYesNo;
  private transient long daysToStart;
  private transient String paddedID;
  private transient int overdueDuration;
  
  public Job() {}
  
  public Job(Integer id)
  {
    this.id = id;
  }
  
  public Job(Integer id, String description, Date dateCreated, double baseBudget)
  {
    this.id = id;
    this.description = description;
    this.dateCreated = dateCreated;
    this.baseBudget = baseBudget;
  }
  
  public Integer getId()
  {
    return this.id;
  }
  
  public void setId(Integer id)
  {
    this.id = id;
  }
  
  public String getPaddedID()
  {
    return this.location.getName().substring(0, 1) + "0000" + this.id;
  }
  
  public void setPaddedID(String paddedID)
  {
    this.paddedID = paddedID;
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public String getReporter()
  {
    return this.reporter;
  }
  
  public void setReporter(String reporter)
  {
    this.reporter = reporter;
  }
  
  public String getReporterCell()
  {
    return this.reporterCell;
  }
  
  public void setReporterCell(String reporterCell)
  {
    this.reporterCell = reporterCell;
  }
  
  public String getPhysicalAddress()
  {
    return this.physicalAddress;
  }
  
  public void setPhysicalAddress(String physicalAddress)
  {
    this.physicalAddress = physicalAddress;
  }
  
  public String getPrecaution()
  {
    return this.precaution;
  }
  
  public void setPrecaution(String precaution)
  {
    this.precaution = precaution;
  }
  
  public Boolean getCompleted()
  {
    return Boolean.valueOf(this.completed);
  }
  
  public void setCompleted(Boolean completed)
  {
    this.completed = completed.booleanValue();
  }
  
  public String getCompletedYesNo()
  {
    return this.completed == true ? "YES" : "NO";
  }
  
  public void setCompletedYesNo(String completedYesNo)
  {
    this.completedYesNo = completedYesNo;
  }
  
  public Date getCompletionDate()
  {
    return this.completionDate;
  }
  
  public void setCompletionDate(Date completionDate)
  {
    this.completionDate = completionDate;
  }
  
  public Date getDueDate()
  {
    return this.dueDate;
  }
  
  public void setDueDate(Date dueDate)
  {
    this.dueDate = dueDate;
  }
  
  public int getOverdueDuration()
  {
    return DateUtil.getDateDiffInDaysInt(new Date(), this.dueDate);
  }
  
  public Boolean getInvoicePaid()
  {
    return Boolean.valueOf(this.invoicePaid);
  }
  
  public void setInvoicePaid(Boolean invoicePaid)
  {
    this.invoicePaid = invoicePaid.booleanValue();
  }
  
  public String getInvoicePaidYesNo()
  {
    return this.completed == true ? "YES" : "NO";
  }
  
  public void setInvoicePaidYesNo(String invoicePaidYesNo)
  {
    this.invoicePaidYesNo = invoicePaidYesNo;
  }
  
  public Date getDateCreated()
  {
    return this.dateCreated;
  }
  
  public void setDateCreated(Date dateCreated)
  {
    this.dateCreated = dateCreated;
  }
  
  public double getBaseBudget()
  {
    return this.baseBudget;
  }
  
  public void setBaseBudget(double baseBudget)
  {
    this.baseBudget = baseBudget;
  }
  
  public long getDaysToStart()
  {
    return DateUtil.getDateDiffInDays(this.dateCreated, 
    
      DateUtil.stringToDateNoTime(DateUtil.getCurrentDateInString()));
  }
  
  public double getActualCost()
  {
    return this.actualCost;
  }
  
  public void setActualCost(double actualCost)
  {
    this.actualCost = actualCost;
  }
  
  public void setDaysToStart(long daysToStart)
  {
    this.daysToStart = daysToStart;
  }
  
  @XmlTransient
  public List<JobSla> getJobSlaList()
  {
    return this.jobSlaList;
  }
  
  public void setJobSlaList(List<JobSla> jobSlaList)
  {
    this.jobSlaList = jobSlaList;
  }
  
  public JobCategory getCategory()
  {
    return this.category;
  }
  
  public void setCategory(JobCategory category)
  {
    this.category = category;
  }
  
  public Contractor getContractor()
  {
    return this.contractor;
  }
  
  public void setContractor(Contractor contractor)
  {
    this.contractor = contractor;
  }
  
  public JobLocation getLocation()
  {
    return this.location;
  }
  
  public void setLocation(JobLocation location)
  {
    this.location = location;
  }
  
  public JobPriority getPriority()
  {
    return this.priority;
  }
  
  public void setPriority(JobPriority priority)
  {
    this.priority = priority;
  }
  
  public JobSubCategory getSubCategory()
  {
    return this.subCategory;
  }
  
  public void setSubCategory(JobSubCategory subCategory)
  {
    this.subCategory = subCategory;
  }
  
  public Usr getUsr()
  {
    return this.usr;
  }
  
  public void setUsr(Usr usr)
  {
    this.usr = usr;
  }
  
  public int hashCode()
  {
    int hash = 0;
    hash += (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }
  
  public boolean equals(Object object)
  {
    if (!(object instanceof Job)) {
      return false;
    }
    Job other = (Job)object;
    if (((this.id == null) && (other.id != null)) || ((this.id != null) && (!this.id.equals(other.id)))) {
      return false;
    }
    return true;
  }
  
  public String toString()
  {
    return this.category.getName() + " " + this.subCategory.getName();
  }
}
