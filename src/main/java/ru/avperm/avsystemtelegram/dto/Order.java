package ru.avperm.avsystemtelegram.dto;

import lombok.Data;

@Data
public class Order {

    private Long id;

    /**
     * Id пользователя, который сделал заказ
     */
    private int userId;

    /**
     * Сумма заказа
     */
    private Double price;

    /**
     * Пункт посадки
     */
    private Stopplace spFrom;

    /**
     * Пункт назначения
     */
    private Stopplace spTo;

    /**
     * Рейс, на который куплен билет
     */
    private Race race;

    /**
     * Статус заказа
     */
    private int statusId;

}
