package ru.avperm.avsystemtelegram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {

    /**
     * Полное сообщение, пришедшее от пользователя
     */
    private String fullText;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Номер чата, с которого пришло сообщение
     */
    private long chatId;

    /**
     * Комманда
      */
    private String command;

    /**
     * Список параметров после комманды, разделенных пробелом
     */
    private List<String> listParam;



}
