package edu.harvard.dbmi.avillach.dataupload.shared;

import java.util.UUID;

public record Query(UUID queryId, UUID commonAreaQueryId, String hostInstitution) {
}
