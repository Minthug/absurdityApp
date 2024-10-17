package absurdity.ForOlder.order.adapter.out.persistence.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class DataBaseEntity (

    @CreatedDate
    @Column(name = "REG_DATE")
    var regDate: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "MOD_DATE")
    var modDate: LocalDateTime? = null
)