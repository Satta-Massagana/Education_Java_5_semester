package ru.utmn.baranov.internet_availability.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import ru.utmn.baranov.internet_availability.model.InternetAvailabilityModel;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
@Profile("JdbcEngine")
@Primary
public class InternetAvailabilityJdbcRepository implements CommonRepository<InternetAvailabilityModel> {

    private static final String SQL_INSERT = """
            insert into internet_availability
                (country_or_area, subregion, region, internet_users, population)
            values
                (:countryOrArea, :subregion, :region, :internetUsers, :population)
            """;

    private static final String SQL_UPDATE = """
            update internet_availability
                set subregion = :subregion, region = :region, internet_users = :internetUsers, population = :population
            where country_or_area = :countryOrArea
            """;

    private static final String SQL_DELETE = "delete from internet_availability where country_or_area = :countryOrArea";

    private static final String SQL_EXIST = "select count(*) > 0 from internet_availability where country_or_area = :countryOrArea";

    private static final String SQL_FIND_ALL = "select country_or_area, subregion, region, internet_users, population from internet_availability";

    private static final String SQL_FIND_BY_ID = SQL_FIND_ALL + " where country_or_area = :countryOrArea";

    private static final String SQL_COUNT = "select count(*) from internet_availability";

    private final NamedParameterJdbcTemplate template;

    public InternetAvailabilityJdbcRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public InternetAvailabilityModel save(InternetAvailabilityModel domain) {
        if (exists(domain.getCountryOrArea()))
            return insertOrUpdate(SQL_UPDATE, domain);
        else
            return insertOrUpdate(SQL_INSERT, domain);
    }

    private InternetAvailabilityModel insertOrUpdate(final String sql, InternetAvailabilityModel domain) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("countryOrArea", domain.getCountryOrArea());
        namedParameters.put("subregion", domain.getSubregion());
        namedParameters.put("region", domain.getRegion());
        namedParameters.put("internetUsers", domain.getInternetUsers());
        namedParameters.put("population", domain.getPopulation());

        template.update(sql, namedParameters);
        return findById(domain.getCountryOrArea());
    }

    @Override
    public Iterable<InternetAvailabilityModel> save(Collection<InternetAvailabilityModel> domains) {
        template.batchUpdate(SQL_INSERT, SqlParameterSourceUtils.createBatch(domains));
        return findAll();
    }

    @Override
    public void delete(String id) {
        Map<String, String> namedParameters = Collections.singletonMap("countryOrArea", id);
        template.update(SQL_DELETE, namedParameters);
    }

    @Override
    public void delete(InternetAvailabilityModel domain) {
        delete(domain.getCountryOrArea());
    }

    @Override
    public InternetAvailabilityModel findById(String id) {
        Map<String, String> namedParameters = Collections.singletonMap("countryOrArea", id);
        return template.queryForObject(SQL_FIND_BY_ID, namedParameters, internetAvailabilityRowMapper);
    }

    @Override
    public Iterable<InternetAvailabilityModel> findAll() {
        return template.query(SQL_FIND_ALL, internetAvailabilityRowMapper);
    }

    private RowMapper<InternetAvailabilityModel> internetAvailabilityRowMapper = (ResultSet rs, int rowNum) -> {
        InternetAvailabilityModel model = new InternetAvailabilityModel();
        model.setCountryOrArea(rs.getString("country_or_area"));
        model.setSubregion(rs.getString("subregion"));
        model.setRegion(rs.getString("region"));
        model.setInternetUsers(String.valueOf(rs.getLong("internet_users")));
        model.setPopulation(String.valueOf(rs.getLong("population")));
        return model;
    };

    @Override
    public boolean exists(String id) {
        Map<String, String> namedParameters = Collections.singletonMap("countryOrArea", id);
        return template.queryForObject(SQL_EXIST, namedParameters, Boolean.class);
    }

    @Override
    public long count() {
        return template.queryForObject(SQL_COUNT, Collections.emptyMap(), Long.class);
    }
}