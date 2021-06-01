package cz.tul.repositories;

import cz.tul.data.Town;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TownRepository extends CrudRepository<Town, Integer> {

    @Query(value = "select * from town where country_code = ?1",nativeQuery = true)
    public List<Town> findByCountryCode(String country_code);

    @Transactional
    @Modifying
    @Query("delete from Town as t where t.id=:id")
    public void deleteTownById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query(value = "delete from town",nativeQuery = true)
    public void deleteAllTowns();

    @Transactional
    @Modifying
    @Query(value = "delete from town where country_code = ?1",nativeQuery = true)
    public void  deleteByCountryCode(@Param("country_code") String country_code);
}
