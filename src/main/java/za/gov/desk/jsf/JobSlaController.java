package za.gov.desk.jsf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import za.gov.desk.entity.Job;
import za.gov.desk.entity.JobSla;
import za.gov.desk.jsf.util.JsfUtil;
import za.gov.desk.jsf.util.PaginationHelper;
import za.gov.desk.sessionbean.JobSlaFacade;

@Named("jobSlaController")
@SessionScoped
public class JobSlaController
        extends BaseController
        implements Serializable {

    private JobSla current;
    private JobSla selectedJobSla;
    private DataModel items = null;
    @EJB
    private JobSlaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private UploadedFile file = null;
    private String fileName;
    private StreamedContent fileStreem;
    private static final Logger LOGGER = Logger.getLogger(JobSlaController.class.getName());

    public JobSla getSelected() {
        if (this.current == null) {
            this.current = new JobSla();
            this.selectedItemIndex = -1;
        }
        return this.current;
    }

    private JobSlaFacade getFacade() {
        return this.ejbFacade;
    }

    public JobSla getSelectedJobSla() {
        return this.selectedJobSla;
    }

    public void setSelectedJobSla(JobSla selectedJobSla) {
        this.selectedJobSla = selectedJobSla;
    }

    public UploadedFile getFile() {
        return this.file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public StreamedContent getFileStreem(String name) {
        InputStream stream = null;
        try {
            String filePath = "C:\\dox\\" + name;

            stream = new FileInputStream(filePath);
            this.fileStreem = new DefaultStreamedContent(stream, "", name);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Exception occur ", e);
            JsfUtil.addErrorMessage("FILE NOT FOUND!", "Sorry requested file you are looking for is not found.");
            return this.fileStreem;
        }
        return this.fileStreem;
    }

    public void setFileStreem(StreamedContent fileStreem) {
        this.fileStreem = fileStreem;
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
        return "List";
    }

    public String prepareView() {
        this.current = ((JobSla) getItems().getRowData());
        this.selectedItemIndex = (this.pagination.getPageFirstItem() + getItems().getRowIndex());
        return "View";
    }

    public String prepareCreate() {
        this.current = new JobSla();
        this.selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            Job job = (Job) getSessionMap().get("current_job_object");
            if (this.ejbFacade.findJobSLAName(job, this.current.getSlaName()).size() > 0) {
                JsfUtil.addErrorMessage("DUPLICATE ", "Document already loaded");
                return null;
            }
            this.current.setJob(job);
            this.current.setFileUrl(this.fileName);

            this.current.setDateCreated(new Date());
            getFacade().create(this.current);
            JsfUtil.addSuccessMessage("Congrats: ", ResourceBundle.getBundle("/Bundle").getString("JobSlaCreated"));
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        return null;
    }

    public String prepareEdit() {
        this.current = this.selectedJobSla;
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("JobSlaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        return null;
    }

    public String destroy() {
        this.current = this.selectedJobSla;
        performDestroy();
        recreatePagination();
        recreateModel();
        return null;
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (this.selectedItemIndex >= 0) {
            return "View";
        }
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(this.current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("JobSlaDeleted"));
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
            this.current = ((JobSla) getFacade().findRange(new int[]{this.selectedItemIndex, this.selectedItemIndex + 1}).get(0));
        }
    }

    public DataModel getItems() {
        Job job = (Job) getSessionMap().get("current_job_object");
        this.items = new ListDataModel(this.ejbFacade.findSLAByJob(job));
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
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(this.ejbFacade.findAll(), true);
    }

    

    public void handleFileUpload()
            throws IOException {
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

        InputStream inputStream = null;
        OutputStream out = null;
        try {
            String fileCreated = "C:\\dox\\";

            File targetFolder = new File(fileCreated);
            inputStream = this.file.getInputstream();
            this.fileName = (new Date().getTime() + "_" + this.file.getFileName());
            out = new FileOutputStream(new File(targetFolder, this.fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occur ", e);
        } finally {
            inputStream.close();
            out.flush();
            out.close();
        }
    }

    public void handleFileUploadEdit()
            throws IOException {
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

        InputStream inputStream = null;
        OutputStream out = null;
        try {
            String fileCreatedDelete = "C:\\dox\\";
            if (deleteExistingFile(fileCreatedDelete)) {
                String fileCreated = "C:\\dox\\";

                File targetFolder = new File(fileCreated);
                inputStream = this.file.getInputstream();
                this.fileName = (new Date().getTime() + "_" + this.file.getFileName());
                out = new FileOutputStream(new File(targetFolder, this.fileName));
                int read = 0;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            } else if (!fileExist(fileCreatedDelete)) {
                String fileCreated = "C:\\dox\\";

                File targetFolder = new File(fileCreated);
                inputStream = this.file.getInputstream();
                this.fileName = (new Date() + "_" + this.file.getFileName());
                out = new FileOutputStream(new File(targetFolder, this.fileName));
                int read = 0;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                System.out.println("deleting has failed.");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occur ", e);
        } finally {
            inputStream.close();
            out.flush();
            out.close();
        }
    }

    public boolean deleteExistingFile(String tempFile) {
        boolean deleted = false;
        try {
            File fileTemp = new File(tempFile);
            if (fileTemp.exists()) {
                deleted = fileTemp.delete();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occur ", e);
        }
        return deleted;
    }

    public boolean fileExist(String tempFile) {
        boolean deleted = false;
        try {
            File fileTemp = new File(tempFile);
            if (fileTemp.exists()) {
                deleted = true;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occur ", e);
        }
        return deleted;
    }
    
    public JobSla getJobSla(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = JobSla.class)
    public static class JobSlaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            JobSlaController controller = (JobSlaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "jobSlaController");
            return controller.getJobSla(getKey(value));
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
            if (object instanceof JobSla) {
                JobSla o = (JobSla) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + JobSla.class.getName());
            }
        }

    }
}
