package ru.utmn.baranov.internet_availability.model;

public class InternetAvailabilityModel {

    private String countryOrArea;
    private String subregion;
    private String region;
    private Long internetUsers;
    private Long population;

    public String getCountryOrArea() {
        return countryOrArea;
    }

    public void setCountryOrArea(String countryOrArea) {
        this.countryOrArea = countryOrArea;
    }

    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(String internetUsersStr) {
        this.internetUsers = parseStringToLong(internetUsersStr);
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(String populationStr) {
        this.population = parseStringToLong(populationStr);
    }

    private Long parseStringToLong(String numberStr) {
        if (numberStr == null) return null;
        String cleanStr = numberStr.replace("\"", "").replace(",", "");
        try {
            return Long.parseLong(cleanStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}