package edu.harvard.dbmi.avillach.dataupload.upload;

import edu.harvard.dbmi.avillach.dataupload.aws.SelfRefreshingS3Client;
import edu.harvard.dbmi.avillach.dataupload.shared.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataUploadService {

    @Autowired
    private SelfRefreshingS3Client client;

    public void upload(Query query) {
        // validate the host institution S3 location

        // get the status of the query in hpds

        // request the genomic tsv

        // request the phenotypic tsv

        //
    }
}
