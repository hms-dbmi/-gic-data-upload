package edu.harvard.dbmi.avillach.dataupload.status;

import edu.harvard.dbmi.avillach.dataupload.hpds.Query;
import edu.harvard.dbmi.avillach.dataupload.shared.UploadStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UploadStatusRepository {
    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UploadStatusRowMapper mapper;

    public UploadStatus getGenomicStatus(Query query) {
        try {
            return template.queryForObject(
                "SELECT genomic_status AS status FROM query_status WHERE query = unhex(?)",
                mapper,
                query.getId().replace("-", "")
            );
        } catch (EmptyResultDataAccessException e) {
            return UploadStatus.Unknown;
        }
    }

    public UploadStatus getPhenotypicStatus(Query query) {
        try {
            return template.queryForObject(
                "SELECT phenotypic_status AS status FROM query_status WHERE query = unhex(?)",
                mapper,
                query.getId().replace("-", "")
            );
        } catch (EmptyResultDataAccessException e) {
            return UploadStatus.Unknown;
        }
    }

    public void setGenomicStatus(Query query, UploadStatus status) {
        String sql = """
            INSERT INTO query_status
                (query, genomic_status)
                VALUES (unhex(?), ?)
                ON DUPLICATE KEY UPDATE genomic_status=?
        """;
        template.update(sql, query.getId().replace("-", ""), status.toString(), status.toString());
    }

    public void setPhenotypicStatus(Query query, UploadStatus status) {
        String sql = """
            INSERT INTO query_status
                (query, phenotypic_status)
                VALUES (unhex(?), ?)
                ON DUPLICATE KEY UPDATE phenotypic_status=?
        """;
        template.update(sql, query.getId().replace("-", ""), status.toString(), status.toString());
    }
}
