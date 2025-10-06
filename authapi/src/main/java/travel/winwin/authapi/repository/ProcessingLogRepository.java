package travel.winwin.authapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travel.winwin.authapi.model.ProcessingLog;

public interface ProcessingLogRepository extends JpaRepository<ProcessingLog, Long> {

}
