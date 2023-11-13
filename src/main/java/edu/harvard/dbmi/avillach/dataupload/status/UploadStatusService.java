package edu.harvard.dbmi.avillach.dataupload.status;

import edu.harvard.dbmi.avillach.dataupload.hpds.Query;
import edu.harvard.dbmi.avillach.dataupload.shared.DataUploadStatuses;
import edu.harvard.dbmi.avillach.dataupload.shared.UploadStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadStatusService {
    @Autowired
    private UploadStatusRepository repository;

    public void setGenomicStatus(Query query, UploadStatus status) {
        repository.setGenomicStatus(query, status);
    }

    public void setPhenotypicStatus(Query query, UploadStatus status) {
        repository.setPhenotypicStatus(query, status);
    }

    public DataUploadStatuses getStatus(Query query) {
        return new DataUploadStatuses(repository.getGenomicStatus(query), repository.getPhenotypicStatus(query));
    }

}
