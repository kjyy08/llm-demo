package cloud.luigi99.llm.demo.thread.repository

import cloud.luigi99.llm.demo.thread.domain.entity.Thread
import org.springframework.data.jpa.repository.JpaRepository

interface ThreadRepository : JpaRepository<Thread, Long> {

}
