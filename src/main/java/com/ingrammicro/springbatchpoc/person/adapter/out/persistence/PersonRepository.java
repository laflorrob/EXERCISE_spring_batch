package com.ingrammicro.springbatchpoc.person.adapter.out.persistence;

import com.ingrammicro.springbatchpoc.person.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {

    String query = """
            SELECT * FROM PERSON P 
            WHERE P.STATUS IN ('PENDING') 
            AND P.COUNTRY IN (
                SELECT COUNTRY FROM (
                SELECT P.COUNTRY,COUNT(CASE WHEN P.STATUS = 'PROCESSING' THEN 1 END) AS JOBS_RUNNING
                FROM PERSON P GROUP BY P.COUNTRY) WHERE JOBS_RUNNING = 0)
            ORDER BY P.CREATION_DATE 
            FETCH FIRST 1 ROWS ONLY""";
    @Query(value = query, nativeQuery = true)
    Optional<PersonEntity> getOldestNotProcessed();
}
