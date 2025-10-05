DROP TABLE IF EXISTS internet_availability;

CREATE TABLE internet_availability
(
    country_or_area varchar(255) not null primary key,
    subregion varchar(255),
    region varchar(255),
    internet_users bigint,
    population bigint
);