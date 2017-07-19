package za.gov.desk.jsf;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.codec.digest.DigestUtils;
import za.gov.desk.entity.Usr;
import za.gov.desk.jsf.util.ClickatellHttp;

@ManagedBean(name = "abstController")
@SessionScoped
public class BaseController {

    protected static final String USER_ID = "user_id";
    protected static final String CURRENT_USER = "current_user_object";
    protected static final String CURRENT_CONTRACTOR = "current_contractor_object";
    protected static final String CURRENT_JOB = "current_job_object";
    protected static final String CURRENT_LOCATION = "current_location_object";
    protected static final String CURRENT_COUNCILLOR_USER = "current_user_councillor_object";
    protected static final String CURRENT_ACCOUNT = "current_account_object";
    protected static final String CURRENT_DISASTER = "current_disaster_object";
    protected static final String CURRENT_PERSON = "current_person_object";
    protected static final String CURRENT_STAFF = "current_staff_object";
    protected static final String CURRENT_QUOTATION = "current_quotation_object";
    protected static final String CURRENT_TENDER = "current_tender_object";
    protected static final String CURRENT_INCIDENT = "current_incident_object";
    protected static final String CURRENT_MENU = "current_menu_object";
    protected static final String CURRENT_REPORT_STATUS = "current_report_status";
    protected static final String BASE_FOLDER = "/home/mila/hcm_docs/advert/";
    protected static final String SITE_MEETING = "/home/mila/hcm_docs/sitemeeting/";
    protected static final String BASE_FOLDER_QUOTATION = "/home/mila/hcm_docs/quotation/";
    protected static final String BASE_FOLDER_REPORT_COC = "/home/mila/hcm_docs/tender_report/";
    protected static final String BASE_FOLDER_SPECFICATION = "/home/mila/hcm_docs/spec/";
    protected static final String BASE_FOLDER_INCIDENT = "/home/mila/hcm_docs/incident/";
    protected static final String BASE_FOLDER_PROFILE_PICTURE = "/resources/images/profilers/";
    protected static final String BASE_FOLDER_JOB_DOUCMENT = "C:\\dox\\";

    public void infoVersionFive(String title, String messageDetail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, title, messageDetail));
    }

    public void warnVersionFive(String title, String messageDetail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, title, messageDetail));
    }

    public void errorVersionFive(String title, String messageDetail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, messageDetail));
    }

    public void fatalVersionFive(String title, String messageDetail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, title, messageDetail));
    }

    private void addActionAlert(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    protected void addInfoAlert(String summary, String detail) {
        addActionAlert(FacesMessage.SEVERITY_INFO, summary, detail);
    }

    protected void addWarningAlert(String summary, String detail) {
        addActionAlert(FacesMessage.SEVERITY_WARN, summary, detail);
    }

    protected void addErrorAlert(String summary, String detail) {
        addActionAlert(FacesMessage.SEVERITY_ERROR, summary, detail);
    }

    protected void addFatalAlert(String summary, String detail) {
        addActionAlert(FacesMessage.SEVERITY_FATAL, summary, detail);
    }

    protected String getRequestParameter(String name) {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    protected Map<String, Object> getSessionMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    }

    public String getIpAddress()
            throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public String getRandom() {
        String code = "";
        try {
            code = DigestUtils.md5Hex(new Date().getTime() + "");
            System.out.println("MD5: " + code.toUpperCase());
        } catch (Exception e) {
            System.out.println("Execption Generation Random " + e.getMessage());
        }
        return code.toUpperCase();
    }

    public String sendSMS(String messageToSend, String numberToSend) {
        String responseMessage = "";
        Usr usr = (Usr) getSessionMap().get("current_user_object");
        boolean smsActive = true;
        String USERNAME = "";
        String APIID = "";
        String PASSWORD = "";
        try {
            if (smsActive) {
                ClickatellHttp click = new ClickatellHttp(USERNAME, APIID, PASSWORD);
                ClickatellHttp.Message response = null;
                boolean cansend = false;
                click.testAuth();
                double balance = click.getBalance();

                cansend = canSendMessageWithBalance(messageToSend, balance);
                if (cansend) {
                    System.out.println("SEND SINGLE MESSAGE WITH HTTP API: " + APIID);
                    response = click.sendMessage(numberToSend, messageToSend);
                    if (response.error != null) {
                        return response.error;
                    }
                    responseMessage = click.getMessageStatus(response.message_id).toString();

                    System.out.println("ENDING WITH HTTP API: " + APIID);
                }
            }
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            responseMessage = e.getMessage();
        }
        System.out.println("Response Message EOF: " + responseMessage);

        return responseMessage;
    }

    private boolean canSendMessageWithBalance(String message, double balance) {
        boolean cansend = false;
        if (((message.length() <= 160) && (balance >= 1.0D))
                || ((message.length() <= 320) && (balance >= 2.0D))
                || ((message.length() <= 480) && (balance >= 3.0D)) || ((message.length() <= 640) && (balance >= 4.0D))) {
            cansend = true;
        }
        System.out.println("message size: " + message.length() + " Cansend: " + cansend);

        return cansend;
    }
}
