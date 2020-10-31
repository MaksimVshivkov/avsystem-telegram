package ru.avperm.avsystemtelegram.dto;

import lombok.Data;

import java.util.Date;

/**
 * Рейс
 */
@Data
public class Race {

    private Long id;

    /**
     * Дата рейса
     */
    private Date dateRace;

    /**
     * Статус рейса
     */
    private int statusId;

    /**
     * Автовокзал
     */
    private BusStation busStation;

    /**
     * Маршрут
     */
    private Route route;


}
