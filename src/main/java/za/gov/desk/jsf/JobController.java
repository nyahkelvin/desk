package za.gov.desk.jsf;

import java.io.PrintStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import za.gov.desk.entity.Contractor;
import za.gov.desk.entity.ContractorBudget;
import za.gov.desk.entity.Job;
import za.gov.desk.entity.JobLocation;
import za.gov.desk.entity.JobPriority;
import za.gov.desk.entity.JobSubCategory;
import za.gov.desk.entity.Usr;
import za.gov.desk.jsf.util.DateUtil;
import za.gov.desk.jsf.util.EmailHelper;
import za.gov.desk.jsf.util.JsfUtil;
import za.gov.desk.jsf.util.PaginationHelper;
import za.gov.desk.sessionbean.ContractorBudgetFacade;
import za.gov.desk.sessionbean.JobFacade;
import za.gov.desk.sessionbean.JobSubCategoryFacade;

@Named("jobController")
@SessionScoped
public class JobController
        extends BaseController
        implements Serializable {

    private Job current;
    private Job selectedJob;
    private JobLocation selectedLocation;
    private DataModel items = null;
    @EJB
    private JobFacade ejbFacade;
    @EJB
    private JobSubCategoryFacade ejbSubCategoryFacade;
    @EJB
    private ContractorBudgetFacade ejbContractorBudgetFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private boolean searching;
    private boolean dashBoardAll = true;

    public Job getSelected() {
        if (this.current == null) {
            this.current = new Job();
            this.selectedItemIndex = -1;
        }
        return this.current;
    }

    private JobFacade getFacade() {
        return this.ejbFacade;
    }

    public Job getSelectedJob() {
        return this.selectedJob;
    }

    public void setSelectedJob(Job selectedJob) {
        this.selectedJob = selectedJob;
    }

    public JobLocation getSelectedLocation() {
        return this.selectedLocation;
    }

    public void setSelectedLocation(JobLocation selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public boolean isSearching() {
        return this.searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public boolean isDashBoardAll() {
        return this.dashBoardAll;
    }

    public void setDashBoardAll(boolean dashBoardAll) {
        this.dashBoardAll = dashBoardAll;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public Integer jobByStatusORLocation(boolean completed) {
        if (this.searching) {
            return jobByStatusLocation(completed);
        }
        return jobByStatus(completed);
    }

    public Integer jobByStatusORLocationOverView(boolean completed) {
        if (this.searching) {
            return jobByStatusLocationOverdue(completed);
        }
        return jobByStatusOverdue(completed).size();
    }

    public Integer jobByStatus(boolean completed) {
        return this.ejbFacade.findJobStatus(completed).size();
    }

    public List<Job> jobByStatusOverdue(boolean completed) {
        return this.ejbFacade.getOverdueJobs(completed, new Date());
    }

    public Integer jobByStatusLocation(boolean completed) {
        return this.ejbFacade.findJobStatusByLocation(completed, this.selectedLocation).size();
    }

    public Integer jobByStatusLocationOverdue(boolean completed) {
        return this.ejbFacade.getOverdueJobsLocation(completed, this.selectedLocation, new Date()).size();
    }

    public double performaceMonitor() {
        if (this.searching) {
            double total = this.ejbFacade.findJobStatusByLocation(this.selectedLocation).size();
            double notcompleted = jobByStatusLocation(false).doubleValue();
            double divi = notcompleted / total;
            System.out.println("divi: " + divi + " un: " + notcompleted + " total: " + total);
            return Math.round(divi * 100.0D);
        }
        double total = this.ejbFacade.findAll().size();
        double notcompleted = jobByStatus(false).doubleValue();
        double divi = notcompleted / total;
        System.out.println("divi: " + divi + " un: " + notcompleted + " total: " + total);
        return Math.round(divi * 100.0D);
    }

    public Double contractorBalance() {
        Double balance = 0.0D;
        Contractor contractor = (Contractor) getSessionMap().get("current_contractor_object");
        ContractorBudget cbudget = this.ejbContractorBudgetFacade.findBudgetByYearContractor(DateUtil.getCurrentYear(), contractor);
        if (cbudget != null) {
            balance = cbudget.getAmount() - this.selectedJob.getBaseBudget();
            System.out.println("year amount: " + cbudget.toString() + "current year: ");
        }
        return balance;
    }

    public String prepareDashboardGraph() {
        System.out.println("selected location: " + this.selectedLocation.getName());
        this.searching = true;
        return null;
    }

    public void onChangeCategory() {
        this.searching = true;
        if ((this.current != null) && (this.current.getCategory() != null)) {
            this.dashBoardAll = false;
            System.out.println("it is here");
        }
    }

    public String prepareList() {
        recreateModel();
        return "/crud/job/List";
    }

    public String prepareReport() {
        recreateModel();
        this.current = new Job();
        this.selectedLocation = new JobLocation();
        this.searching = false;
        return "/crud/job/Report";
    }

    public String prepareDashboard() {
        this.selectedLocation = new JobLocation();
        this.current = new Job();
        this.searching = false;
        return "/crud/dashboard/dashboard.xhtml";
    }

    public String prepareView() {
        getSessionMap().put("current_contractor_object", this.selectedJob.getContractor());
        getSessionMap().put("current_job_object", this.selectedJob);
        this.current = this.selectedJob;
        return "/crud/job/View";
    }

    public String prepareCreate() {
        this.current = new Job();
        this.selectedItemIndex = -1;
        return "/crud/job/Create";
    }

    public int dashMonthlyGraph(boolean status, String dateAB, String dateBB) {
        List<Job> l = new ArrayList();
        JobLocation location = null;
        try {
            if (!this.searching) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date dateA = formatter.parse(dateAB);
                Date dateb = formatter.parse(dateBB);
                l = getFacade().getJobByDateRange(dateA, dateb, status);
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date dateA = formatter.parse(dateAB);
                Date dateb = formatter.parse(dateBB);

                l = getFacade().getJobByDateRangeLocation(dateA, dateb, status, this.selectedLocation);
                System.out.println("it is searching now");
            }
        } catch (Exception localException) {
            localException
                    = localException;
        } finally {
        }
        return l.size();
    }

    public int dashMonthlyGraphOverdue(boolean status, String dateAB, String dateBB) {
        List<Job> l = new ArrayList();
        JobLocation location = null;
        try {
            if (!this.searching) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date dateA = formatter.parse(dateAB);
                Date dateb = formatter.parse(dateBB);
                l = getFacade().getJobByDateRangeOverdue(dateA, dateb, new Date(), status);
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date dateA = formatter.parse(dateAB);
                Date dateb = formatter.parse(dateBB);

                l = getFacade().getJobByDateRangeLocationOverdue(dateA, dateb, new Date(), status, this.selectedLocation);
                System.out.println("it is searching now");
            }
        } catch (Exception localException) {
            localException
                    = localException;
        } finally {
        }
        return l.size();
    }

    public String create() {
        try {
            Usr usr = (Usr) getSessionMap().get("current_user_object");
            this.current.setDateCreated(new Date());
            this.current.setBaseBudget(0.0D);
            this.current.setUsr(usr);
            this.current.setDueDate(DateUtil.addHours(this.current.getPriority().getRef().intValue(), this.current.getDateCreated()));

            getFacade().create(this.current);

            EmailHelper em = new EmailHelper();
            String msg = "Dear contractor,\n\nA Job has been allocated to you.\n\nJob Details:\n\nCategory:" + this.current.getSubCategory().getName() + "\nContractor:" + this.current.getContractor().getName() + "\nJob Reference:" + this.current.getPaddedID() + ".\n\nPlease respond accordingly." + "\n\nRegards,\nEkurhuleni Metropolitan Municipality.";

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("JobCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        return null;
    }

    public String seachJob() {
        setSearching(true);
        if (this.current != null) {
            this.items = new ListDataModel(this.ejbFacade.searchJOb(this.current));
        }
        return "";
    }

    public String prepareEdit() {
        getSessionMap().put("current_contractor_object", this.selectedJob.getContractor());
        getSessionMap().put("current_job_object", this.selectedJob);
        this.current = this.selectedJob;
        return "/crud/job/Edit";
    }

    public String update() {
        double totalAmount = 0.0D;
        try {
            Contractor contractor = (Contractor) getSessionMap().get("current_contractor_object");
            ContractorBudget budget = this.ejbContractorBudgetFacade.findBudgetByYearContractor(DateUtil.getCurrentYear(), contractor);

            List<Job> jobList = this.ejbFacade.findJobByContractor(contractor);
            for (Job j : jobList) {
                totalAmount += j.getBaseBudget();
            }
            double bal = budget.getAmount() - totalAmount;
            if (this.current.getBaseBudget() > bal) {
                JsfUtil.addErrorMessage("EXCESS ", "Amount can not be greater than contractor balance");
                return null;
            }
            getFacade().edit(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("JobUpdated"));
            return "/crud/job/View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        return null;
    }

    public String destroy() {
        this.current = ((Job) getItems().getRowData());
        this.selectedItemIndex = (this.pagination.getPageFirstItem() + getItems().getRowIndex());
        performDestroy();
        recreatePagination();
        recreateModel();
        return "/crud/job/List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (this.selectedItemIndex >= 0) {
            return "/crud/job/View";
        }
        recreateModel();
        return "/crud/job/List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("JobDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (this.selectedItemIndex >= count) {
            this.selectedItemIndex = (count - 1);
            if (this.pagination.getPageFirstItem() >= count) {
                this.pagination.previousPage();
            }
        }
        if (this.selectedItemIndex >= 0) {
            this.current = ((Job) getFacade().findRange(new int[]{this.selectedItemIndex, this.selectedItemIndex + 1}).get(0));
        }
    }

    public DataModel getItems() {
        if (isSearching()) {
            this.items = new ListDataModel(this.ejbFacade.searchJOb(this.current));
        } else {
            this.items = new ListDataModel(this.ejbFacade.findAll());
        }
        return this.items;
    }

    private void recreateModel() {
        this.items = null;
    }

    private void recreatePagination() {
        this.pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "/crud/job/List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "/crud/job/List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), true);
    }

    public SelectItem[] getItemsAvailableSelectOneJobCategory() {
        if (this.current.getCategory() != null) {
            return JsfUtil.getSelectItems(this.ejbSubCategoryFacade.getSubCategoryByCategory(this.current.getCategory()), true);
        }
        return JsfUtil.getSelectItemsEmpty();
    }

    public Job getJob(Integer id) {
        return (Job) this.ejbFacade.find(id);
    }
}
