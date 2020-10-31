package ru.avperm.avsystemtelegram.dto;

import lombok.Data;

/**
 * Автовокзал
 */
@Data
public class BusStation {

    private Long id;

    /**
     * Наименование
     */
    private String name;

    /**
     * Адрес
     */
    private String address;

    /**
     * Телефон
     */
    private String phones;

}
