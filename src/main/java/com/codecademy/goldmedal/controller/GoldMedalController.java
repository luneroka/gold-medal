package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import com.codecademy.goldmedal.repositories.GoldMedalRepository;
import com.codecademy.goldmedal.repositories.CountryRepository;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
  private final GoldMedalRepository goldMedalRepository;
  private final CountryRepository countryRepository;

  public GoldMedalController(GoldMedalRepository goldMedalRepository, CountryRepository countryRepository) {
    this.goldMedalRepository = goldMedalRepository;
    this.countryRepository = countryRepository;
  }

  @GetMapping
  public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
    var ascendingOrder = ascending.toLowerCase().equals("y");
    return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
  }

  @GetMapping("/{country}")
  public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
    String countryName = WordUtils.capitalizeFully(country);
    return getCountryDetailsResponse(countryName);
  }

  @GetMapping("/{country}/medals")
  public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
    String countryName = WordUtils.capitalizeFully(country);
    var ascendingOrder = ascending.toLowerCase().equals("y");
    return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
  }

  private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
    List<GoldMedal> medalsList;
    switch (sortBy) {
      case "year":
        medalsList = ascendingOrder
            ? goldMedalRepository.findByCountryOrderByYearAsc(countryName)
            : goldMedalRepository.findByCountryOrderByYearDesc(countryName);
        break;
      case "season":
        medalsList = ascendingOrder
            ? goldMedalRepository.findByCountryOrderBySeasonAsc(countryName)
            : goldMedalRepository.findByCountryOrderBySeasonDesc(countryName);
        break;
      case "city":
        medalsList = ascendingOrder
            ? goldMedalRepository.findByCountryOrderByCityAsc(countryName)
            : goldMedalRepository.findByCountryOrderByCityDesc(countryName);
        break;
      case "name":
        medalsList = ascendingOrder
            ? goldMedalRepository.findByCountryOrderByNameAsc(countryName)
            : goldMedalRepository.findByCountryOrderByNameDesc(countryName);
        break;
      case "event":
        medalsList = ascendingOrder
            ? goldMedalRepository.findByCountryOrderByEventAsc(countryName)
            : goldMedalRepository.findByCountryOrderByEventDesc(countryName);
        break;
      default:
        medalsList = new ArrayList<>();
        break;
    }

    return new CountryMedalsListResponse(medalsList);
  }

  private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
    var countryOptional = countryRepository.findCountryByName(countryName);
    if (countryOptional.isEmpty()) {
      return new CountryDetailsResponse(countryName);
    }

    var country = countryOptional.get();
    var goldMedalCount = goldMedalRepository.getCountByCountry(countryName);

    var summerWins = goldMedalRepository.getByCountryAndSeasonOrderByYearAsc(countryName, "Summer");
    var numberSummerWins = !summerWins.isEmpty() ? summerWins.size() : null;
    var totalSummerEvents = goldMedalRepository.getCountBySeason("Summer");
    var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
    var yearFirstSummerWin = !summerWins.isEmpty() ? summerWins.get(0).getYear() : null;

    var winterWins = goldMedalRepository.getByCountryAndSeasonOrderByYearAsc(countryName, "Winter");
        var numberWinterWins = !winterWins.isEmpty() ? winterWins.size() : null;
    var totalWinterEvents = goldMedalRepository.getCountBySeason("Winter");
    var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
    var yearFirstWinterWin = !winterWins.isEmpty() ? winterWins.get(0).getYear() : null;

    var numberEventsWonByFemaleAthletes = goldMedalRepository.getCountByCountryAndGender(countryName, "Female");
        var numberEventsWonByMaleAthletes = goldMedalRepository.getCountByCountryAndGender(countryName, "Male");

    return new CountryDetailsResponse(
        countryName,
        country.getGdp(),
        country.getPopulation(),
        goldMedalCount,
        numberSummerWins,
        percentageTotalSummerWins,
        yearFirstSummerWin,
        numberWinterWins,
        percentageTotalWinterWins,
        yearFirstWinterWin,
        numberEventsWonByFemaleAthletes,
        numberEventsWonByMaleAthletes);
  }

  private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
    List<Country> countries;
    switch (sortBy) {
      case "name":
        countries = // TODO: list of countries sorted by name in the given order
        break;
      case "gdp":
        countries = // TODO: list of countries sorted by gdp in the given order
        break;
      case "population":
        countries = // TODO: list of countries sorted by population in the given order
        break;
      case "medals":
      default:
        countries = // TODO: list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
        break;
    }

    var countrySummaries = getCountrySummariesWithMedalCount(countries);

    if (sortBy.equalsIgnoreCase("medals")) {
      countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
    }

    return countrySummaries;
  }

  private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
    return countrySummaries.stream()
        .sorted((t1, t2) -> ascendingOrder ?
            t1.getMedals() - t2.getMedals() :
            t2.getMedals() - t1.getMedals())
        .collect(Collectors.toList());
  }

  private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
    List<CountrySummary> countrySummaries = new ArrayList<>();
    for (var country : countries) {
      var goldMedalCount = // TODO: get count of medals for the given country
          countrySummaries.add(new CountrySummary(country, goldMedalCount));
    }
    return countrySummaries;
  }
}
