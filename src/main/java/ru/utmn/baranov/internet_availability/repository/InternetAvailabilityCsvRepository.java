package ru.utmn.baranov.internet_availability.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.utmn.baranov.internet_availability.model.InternetAvailabilityModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("CsvRepository")
@Profile({"CsvEngine", "JdbcEngine"})
public class InternetAvailabilityCsvRepository implements CommonRepository<InternetAvailabilityModel> {

    private final Map<String, InternetAvailabilityModel> records = new HashMap<>();

    @PostConstruct
    private void readAllLines() {
        try (InputStream is = InternetAvailabilityCsvRepository.class.getClassLoader().getResourceAsStream("Country.csv")) {
            if (is == null) {
                throw new RuntimeException("CSV файл Country.csv не найден в ресурсах");
            }
            try (
                    InputStreamReader streamReader = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(streamReader);
                    CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            ) {
                List<String[]> lines = csvReader.readAll();

                for (int lineNum = 0; lineNum < lines.size(); lineNum++) {
                    String[] line = lines.get(lineNum);
                    try {
                        if (line.length < 5) {
                            System.err.println("Пропущена строка " + (lineNum + 2) + ": недостаточно столбцов");
                            continue;
                        }
                        String countryOrArea = line[0];
                        if (countryOrArea == null || countryOrArea.isEmpty()) {
                            System.err.println("Пропущена строка " + (lineNum + 2) + ": пустое поле countryOrArea");
                            continue;
                        }

                        InternetAvailabilityModel record = new InternetAvailabilityModel();
                        record.setCountryOrArea(countryOrArea);
                        record.setSubregion(line[1]);
                        record.setRegion(line[2]);
                        record.setInternetUsers(line[3]);
                        record.setPopulation(line[4]);

                        records.put(countryOrArea, record);

                    } catch (Exception e) {
                        System.err.println("Ошибка в строке " + (lineNum + 2) + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InternetAvailabilityModel save(InternetAvailabilityModel domain) {
        records.put(domain.getCountryOrArea(), domain);
        return records.get(domain.getCountryOrArea());
    }

    @Override
    public Iterable<InternetAvailabilityModel> save(Collection<InternetAvailabilityModel> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(String id) {
        records.remove(id);
    }

    @Override
    public void delete(InternetAvailabilityModel domain) {
        delete(domain.getCountryOrArea());
    }

    @Override
    public InternetAvailabilityModel findById(String id) {
        return records.get(id);
    }

    @Override
    public Iterable<InternetAvailabilityModel> findAll() {
        return records.values();
    }

    @Override
    public boolean exists(String id) {
        return records.containsKey(id);
    }

    @Override
    public long count() {
        return records.size();
    }
}