package edu.harvard.dbmi.avillach.dataupload.status;

import edu.harvard.dbmi.avillach.dataupload.shared.Query;
import edu.harvard.dbmi.avillach.dataupload.shared.UploadStatus;
import org.springframework.stereotype.Repository;

@Repository
public class UploadStatusRepository {
    public UploadStatus getGenomicStatus(Query query) {
        return UploadStatus.Error;
    }

    public UploadStatus getPhenotypicStatus(Query query) {
        return UploadStatus.Error;
    }

    public void setGenomicStatus(Query query, UploadStatus status) {

    }

    public void setPhenotypicStatus(Query query, UploadStatus status) {

    }
}
