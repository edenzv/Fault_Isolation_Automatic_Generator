package org.afeka.fi.backend.repository;

import org.afeka.fi.backend.pojo.commonstructure.FI;
import org.afeka.fi.backend.pojo.commonstructure.FiDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FIDocRepository extends JpaRepository<FiDoc,Long> {
}
