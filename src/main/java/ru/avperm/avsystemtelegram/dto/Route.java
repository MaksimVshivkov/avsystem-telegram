package ru.avperm.avsystemtelegram.dto;

import lombok.Data;

import java.sql.Time;

/**
 * Маршрут
 */
@Data
public class Route {

    private Long id;

    private String name;

    private Time timeOut;

    /**
     * Пункт посадки
     */
    private Stopplace spFrom;

    /**
     * Пункт назначения
     */
    private Stopplace spTo;

}
