package main.Repository;

import main.Model.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, Integer> {

    @Query(nativeQuery = true, value = "select count(*) from captcha_codes")
    int countCaptcha();

    @Query(nativeQuery = true, value = "delete from captcha_codes where id = 6" )
        //    "where timestampdiff(hour, time, '2020-12-23 20:12:08') > 1 limit 1000")
    void deleteAllBeforeHour();

    @Query(nativeQuery = true, value = "select * from captcha_codes where secret_code = :captcha_secret")
    Captcha findAllBySecret_code(String captcha_secret);

}
