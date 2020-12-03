package main.Repository;

import main.Model.Captcha;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends CrudRepository<Captcha, Integer> {
}
