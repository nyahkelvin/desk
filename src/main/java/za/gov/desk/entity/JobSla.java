/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.desk.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author clevasoft
 */
@Entity
@Table(name = "job_sla", catalog = "desktop", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobSla.findAll", query = "SELECT j FROM JobSla j")
    , @NamedQuery(name = "JobSla.findById", query = "SELECT j FROM JobSla j WHERE j.id = :id")
    , @NamedQuery(name = "JobSla.findByFileUrl", query = "SELECT j FROM JobSla j WHERE j.fileUrl = :fileUrl")
    , @NamedQuery(name = "JobSla.findBySlaName", query = "SELECT j FROM JobSla j WHERE j.slaName = :slaName")
    , @NamedQuery(name = "JobSla.findByDateCreated", query = "SELECT j FROM JobSla j WHERE j.dateCreated = :dateCreated")})
public class JobSla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "file_url")
    private String fileUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "sla_name")
    private String slaName;
    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @JoinColumn(name = "job", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Job job;

    public JobSla() {
    }

    public JobSla(Integer id) {
        this.id = id;
    }

    public JobSla(Integer id, String fileUrl, String slaName) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.slaName = slaName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getSlaName() {
        return slaName;
    }

    public void setSlaName(String slaName) {
        this.slaName = slaName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobSla)) {
            return false;
        }
        JobSla other = (JobSla) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.gov.desk.entity.JobSla[ id=" + id + " ]";
    }
    
}
