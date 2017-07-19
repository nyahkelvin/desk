package za.gov.desk.jsf;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import za.gov.desk.entity.Usr;
import za.gov.desk.jsf.util.JsfUtil;
import za.gov.desk.jsf.util.PaginationHelper;
import za.gov.desk.sessionbean.UsrFacade;

@Named("usrController")
@SessionScoped
public class UsrController
        implements Serializable {

    private Usr current;
    private Usr selectedUsr;
    private DataModel items = null;
    @EJB
    private UsrFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private static final Logger LOGGER = Logger.getLogger(UsrController.class.getName());

    public Usr getSelected() {
        if (this.current == null) {
            this.current = new Usr();
            this.selectedItemIndex = -1;
        }
        return this.current;
    }

    private UsrFacade getFacade() {
        return this.ejbFacade;
    }

    public Usr getSelectedUsr() {
        return this.selectedUsr;
    }

    public void setSelectedUsr(Usr selectedUsr) {
        this.selectedUsr = selectedUsr;
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
        return "/crud/usr/List";
    }

    public String prepareView() {
        this.current = this.selectedUsr;
        return "/crud/usr/View";
    }

    public String prepareCreate() {
        this.current = new Usr();
        this.selectedItemIndex = -1;
        return "/crud/usr/Create";
    }

    public String create() {
        System.out.println(this.current.toString());
        try {
            if (this.ejbFacade.findByUsername(this.current.getUsername().trim()).size() > 0) {
                JsfUtil.addErrorMessage("DUPLICATE ", "Username already in use");
                return null;
            }
            if (this.ejbFacade.findByStaff(this.current.getStaff()).size() > 0) {
                JsfUtil.addErrorMessage("DUPLICATE ", "Staff already have account created");
                return null;
            }
            this.current.setDateCreated(new Date());
            getFacade().create(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsrCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            LOGGER.log(Level.SEVERE, "Error occured", e);
        }
        return null;
    }

    public String prepareEdit() {
        this.current = this.selectedUsr;
        return "/crud/usr/Edit";
    }

    public String update() {
        try {
            getFacade().edit(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsrUpdated"));
            return "/crud/usr/View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        return null;
    }

    public String destroy() {
        this.current = ((Usr) getItems().getRowData());
        this.selectedItemIndex = (this.pagination.getPageFirstItem() + getItems().getRowIndex());
        performDestroy();
        recreatePagination();
        recreateModel();
        return "/crud/usr/List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (this.selectedItemIndex >= 0) {
            return "/crud/usr/View";
        }
        recreateModel();
        return "/crud/usr/List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsrDeleted"));
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
            this.current = ((Usr) getFacade().findRange(new int[]{this.selectedItemIndex, this.selectedItemIndex + 1}).get(0));
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
        return "/crud/usr/List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "/crud/usr/List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), true);
    }

    public Usr getUsr(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Usr.class)
    public static class UsrControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsrController controller = (UsrController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usrController");
            return controller.getUsr(getKey(value));
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
            if (object instanceof Usr) {
                Usr o = (Usr) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Usr.class.getName());
            }
        }

    }
}
