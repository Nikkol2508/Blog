package blog.repositorys;

import blog.model.GlobalSetting;
import org.springframework.data.repository.CrudRepository;

public interface GlobalSettingRepository extends CrudRepository<GlobalSetting, Integer> {
}
