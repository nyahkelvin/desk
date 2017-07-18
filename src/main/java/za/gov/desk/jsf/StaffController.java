package za.gov.desk.jsf;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import za.gov.desk.entity.Staff;
import za.gov.desk.jsf.util.JsfUtil;
import za.gov.desk.jsf.util.PaginationHelper;
import za.gov.desk.sessionbean.StaffFacade;

@Named("staffController")
@SessionScoped
public class StaffController
        implements Serializable {

    private Staff current;
    private Staff selectedStaff;
    private DataModel items = null;
    @EJB
    private StaffFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public Staff getSelected() {
        if (this.current == null) {
            this.current = new Staff();
            this.selectedItemIndex = -1;
        }
        return this.current;
    }

    private StaffFacade getFacade() {
        return this.ejbFacade;
    }

    public Staff getSelectedStaff() {
        return this.selectedStaff;
    }

    public void setSelectedStaff(Staff selectedStaff) {
        this.selectedStaff = selectedStaff;
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

    public String prepareList() {
        recreateModel();
        return "/crud/staff/List";
    }

    public String prepareView() {
        this.current = this.selectedStaff;
        return "/crud/staff/View";
    }

    public String prepareCreate() {
        this.current = new Staff();
        this.selectedItemIndex = -1;
        return "/crud/staff/Create";
    }

    public String create() {
        try {
            this.current.setDateCreated(new Date());
            getFacade().create(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StaffCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        return null;
    }

    public String prepareEdit() {
        this.current = this.selectedStaff;
        return "/crud/staff/Edit";
    }

    public String update() {
        try {
            getFacade().edit(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StaffUpdated"));
            return "/crud/staff/View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        return null;
    }

    public String destroy() {
        this.current = ((Staff) getItems().getRowData());
        this.selectedItemIndex = (this.pagination.getPageFirstItem() + getItems().getRowIndex());
        performDestroy();
        recreatePagination();
        recreateModel();
        return "/crud/staff/List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (this.selectedItemIndex >= 0) {
            return "/crud/staff/View";
        }
        recreateModel();
        return "/crud/staff/List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StaffDeleted"));
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
            this.current = ((Staff) getFacade().findRange(new int[]{this.selectedItemIndex, this.selectedItemIndex + 1}).get(0));
        }
    }

    public DataModel getItems() {
        if (this.items == null) {
            this.items = getPagination().createPageDataModel();
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
        return "/crud/staff/List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "/crud/staff/List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), true);
    }

    public Staff getStaff(Integer id) {
        return (Staff) this.ejbFacade.find(id);
    }
}
