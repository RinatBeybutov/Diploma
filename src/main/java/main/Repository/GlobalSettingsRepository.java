package main.Repository;

import main.Model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Integer> {

    @Query(nativeQuery = true, value = "SELECT value from global_settings where code = :name")
    boolean getValueByCode(String name);

    @Query(nativeQuery = true, value = "select * from global_settings where code = :name")
    GlobalSettings getGlobalSettingsByCode(String name);
}
