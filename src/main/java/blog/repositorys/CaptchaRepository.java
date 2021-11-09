package blog.repositorys;

import blog.model.CaptchaCode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CaptchaRepository extends CrudRepository<CaptchaCode, Integer> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM captcha_codes WHERE time < (UNIX_TIMESTAMP() - :period)", nativeQuery = true)
    void deleteOld(@Param("period") int period);
}
