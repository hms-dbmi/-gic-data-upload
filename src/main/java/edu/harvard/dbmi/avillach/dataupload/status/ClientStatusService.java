package edu.harvard.dbmi.avillach.dataupload.status;

import org.springframework.stereotype.Service;

@Service
public class ClientStatusService {
    private String clientStatus = "uninitialized";

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }
}
