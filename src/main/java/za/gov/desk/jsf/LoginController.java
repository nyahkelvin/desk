package za.gov.desk.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import za.gov.desk.dao.Customer;
import za.gov.desk.entity.Usr;
import za.gov.desk.jsf.util.Activation;
import za.gov.desk.sessionbean.StaffFacade;

@ManagedBean(name = "loginController")
@SessionScoped
public class LoginController
        extends BaseController
        implements Serializable {

    private static final long serialVersionUID = 1L;
    static final Logger LOGGER = Logger.getLogger(LoginController.class);
    private String username;
    private String password;
    @EJB
    private StaffFacade ejbFacade;
    
    private static final String REST_URI
            = "http://158.69.199.58:8080/activation/rest/xml/client/query";
    private final Client client = ClientBuilder.newClient();

   

    public Customer getJsonCustomer(String companyName) {
        return client
                .target(REST_URI)
                .queryParam("companyName", companyName)
                .request(MediaType.APPLICATION_JSON)
                .get(Customer.class);
    }

    private StaffFacade getFacade() {
        return this.ejbFacade;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String prepareHome() {
        return "/login";
    }

    public void logout() {
        try {
            getSessionMap().clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/desk/faces/logout.html?faces-redirect=true");
        } catch (IOException localIOException) {
        }
    }

    public Usr getLoggedInUser() {
        Usr staff = (Usr) getSessionMap().get("current_user_object");
        return staff;
    }

    public String login() {
        try {
            LOGGER.info("Entering login method");

            if(!Activation.isActive()){
                addErrorAlert("Ooops!", "System Locked");
                return "/login.xhtml";
            }
            Usr usr = getFacade().authenticate(this.username, this.password);
            if (usr == null) {
                addErrorAlert("Error", "Invalid login credentials.");
                return "/login.xhtml";
            }
            getSessionMap().put("current_user_object", usr);
            return "/crud/dashboard/dashboard.xhtml";
        } catch (AccountNotFoundException | FailedLoginException | AccountLockedException ex) {
            LOGGER.error("Error while login: " + ex);

            addErrorAlert("Error", "Invalid login credentials.");
        }
        return null;
    }
}
