package manager.retail.models;

import javax.validation.constraints.NotNull;
import javax.websocket.server.ServerEndpoint;

/*
@CreatedBy Hasan on 22-Feb-2017
 */
public class ServiceResponse {
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public ServiceResponse(Boolean success) {
        this.success = success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
