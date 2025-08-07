package com.codecademy.goldmedal.repositories;

import com.codecademy.goldmedal.model.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Integer> {
  Optional<Country> findCountryByName(String countryName);
}
