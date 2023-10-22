package edu.harvard.dbmi.avillach.dataupload.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusController {

    @Autowired
    ClientStatusService statusService;

    @GetMapping("/status/server")
    public ResponseEntity<String> getServerStatus() {
        return ResponseEntity.ok(statusService.getClientStatus());
    }
}
