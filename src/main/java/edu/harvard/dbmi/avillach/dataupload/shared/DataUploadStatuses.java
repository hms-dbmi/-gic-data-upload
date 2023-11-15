package edu.harvard.dbmi.avillach.dataupload.shared;

import java.util.UUID;

public record DataUploadStatuses(UploadStatus genomic, UploadStatus phenotypic, String queryId) {
}
