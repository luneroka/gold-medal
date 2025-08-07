package com.codecademy.goldmedal.repositories;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface GoldMedalRepository extends CrudRepository<GoldMedal, Integer> {
  List<GoldMedal> findByCountryOrderByYearAsc(String country);
  List<GoldMedal> findByCountryOrderByYearDesc(String country);
}
