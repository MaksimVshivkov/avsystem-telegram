package ru.avperm.avsystemtelegram.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextUtil {

    /**
     * Формирует команду вида "command" из строки вида /command param1 param2 param 3
     *
     * @param text Комманда
     * @return Комманда без параметров и слеша
     */
    public static String getCommandFromText(String text) {
        String command = getCommandWithoutSlash(text);
        if (!command.isEmpty() && command.split(" ").length > 0) {
            command = command.split(" ")[0];
        }
        return command;
    }

    private static String getCommandWithoutSlash(String text){
        return (text.split("/").length > 1 ? text.split("/")[1] : "");
    }

    /**
     * Формирует список параметров из строки вида /command param1 param2 param 3
     *
     * @param text Комманда
     * @return список параметров
     */
    public static List<String> getParamsFromText(String text) {
        String command = getCommandWithoutSlash(text);
        List<String> listParam = new ArrayList<>();
        String[] splittedParamWithCommand = command.split(" ");
        if (!command.isEmpty() && splittedParamWithCommand.length > 0) {
            listParam = Arrays.asList(splittedParamWithCommand).subList(1, splittedParamWithCommand.length);
        }
        return listParam;
    }

}
