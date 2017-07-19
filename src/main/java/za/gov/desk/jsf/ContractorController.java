package za.gov.desk.jsf;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import za.gov.desk.entity.Contractor;
import za.gov.desk.jsf.util.JsfUtil;
import za.gov.desk.jsf.util.PaginationHelper;
import za.gov.desk.sessionbean.ContractorFacade;

@Named("contractorController")
@SessionScoped
public class ContractorController
        extends BaseController
        implements Serializable {

    private Contractor current;
    private Contractor selectedContractor;
    private DataModel items = null;
    @EJB
    private ContractorFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public Contractor getSelected() {
        if (this.current == null) {
            this.current = new Contractor();
            this.selectedItemIndex = -1;
        }
        return this.current;
    }

    private ContractorFacade getFacade() {
        return this.ejbFacade;
    }

    public Contractor getSelectedContractor() {
        return this.selectedContractor;
    }

    public void setSelectedContractor(Contractor selectedContractor) {
        this.selectedContractor = selectedContractor;
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
        return "/crud/contractor/List";
    }

    public String prepareView() {
        getSessionMap().put("current_contractor_object", this.selectedContractor);
        this.current = this.selectedContractor;
        return "/crud/contractor/View";
    }

    public String prepareBudget() {
        this.current = this.selectedContractor;
        return "/crud/contractorBudget/View";
    }

    public String prepareCreate() {
        this.current = new Contractor();
        this.selectedItemIndex = -1;
        return "/crud/contractor/Create";
    }

    public String create() {
        try {
            getFacade().create(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ContractorCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        return null;
    }

    public String prepareEdit() {
        getSessionMap().put("current_contractor_object", this.selectedContractor);
        this.current = this.selectedContractor;
        return "/crud/contractor/Edit";
    }

    public String update() {
        try {
            getFacade().edit(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ContractorUpdated"));
            return "/crud/contractor/View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        return null;
    }

    public String destroy() {
        this.current = ((Contractor) getItems().getRowData());
        this.selectedItemIndex = (this.pagination.getPageFirstItem() + getItems().getRowIndex());
        performDestroy();
        recreatePagination();
        recreateModel();
        return "/crud/contractor/List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (this.selectedItemIndex >= 0) {
            return "/crud/contractor/View";
        }
        recreateModel();
        return "/crud/contractor/List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ContractorDeleted"));
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
            this.current = ((Contractor) getFacade().findRange(new int[]{this.selectedItemIndex, this.selectedItemIndex + 1}).get(0));
        }
    }

    public DataModel getItems() {
        this.items = new ListDataModel(this.ejbFacade.findAll());
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
        return "/crud/contractor/List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "/crud/contractor/List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), true);
    }

    public Contractor getContractor(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Contractor.class)
    public static class ContractorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ContractorController controller = (ContractorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "contractorController");
            return controller.getContractor(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Contractor) {
                Contractor o = (Contractor) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Contractor.class.getName());
            }
        }

    }
}
