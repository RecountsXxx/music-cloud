package org.deus.src.seeders;

import lombok.Getter;
import lombok.Setter;
import org.deus.src.models.CountryModel;
import org.deus.src.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
public class CountrySeeder implements CommandLineRunner {
    private final CountryRepository countryRepository;
    private final RestTemplate restTemplate;
    @Value("${countries.url}")
    private String countriesUrl;

    public CountrySeeder(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void run(String... args) throws Exception {
        CountryResponse[] countries = restTemplate.getForObject(countriesUrl, CountryResponse[].class);

        if(countryRepository.count() == 0) {
            if (countries != null) {
                List<CountryModel> independentCountries = Arrays.stream(countries)
                        .filter(CountryResponse::isIndependent)
                        .map(country -> new CountryModel(null, country.getName().getCommon(), null))
                        .sorted(Comparator.comparing(CountryModel::getName))
                        .toList();

                countryRepository.saveAll(independentCountries);
                System.out.println("Table 'countries' was successfully filled.");
            }
        } else {
            System.out.println("Table 'countries' is already filled.");
        }
    }

    @Getter
    @Setter
    static class CountryResponse {
        private Name name;
        private boolean independent;

        @Getter
        static class Name {
            private String common;
        }
    }
}
