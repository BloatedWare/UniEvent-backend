package com.unievt.repository;

import com.unievt.entity.Partenaire;
import com.unievt.enums.TypePartenaireEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {
    List<Partenaire> findByActif(Boolean actif);
    List<Partenaire> findByTypePartenariat(TypePartenaireEnum type);
}
