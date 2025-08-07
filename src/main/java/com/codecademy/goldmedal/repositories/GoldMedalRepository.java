package com.codecademy.goldmedal.repositories;

import com.codecademy.goldmedal.controller.GoldMedalController;
import org.springframework.data.repository.CrudRepository;

public interface GoldMedalRepository extends  CrudRepository<GoldMedalController, Integer> {
  public List<GoldMedalController> findGoldMedalsByYear()
}
