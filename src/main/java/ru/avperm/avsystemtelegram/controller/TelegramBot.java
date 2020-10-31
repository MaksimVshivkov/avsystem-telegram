package ru.avperm.avsystemtelegram.controller;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.utils.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.avperm.avsystemtelegram.client.BusSpravsApiClient;
import ru.avperm.avsystemtelegram.config.TelegramBotSettings;
import ru.avperm.avsystemtelegram.dto.*;
import ru.avperm.avsystemtelegram.services.CommandService;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BusSpravsApiClient busSpravsApiClient;
    private final CommandService commandService;
    private final TelegramBotSettings telegramBotSettings;

    public TelegramBot(BusSpravsApiClient busSpravsApiClient, CommandService commandService, TelegramBotSettings telegramBotSettings) {
        this.busSpravsApiClient = busSpravsApiClient;
        this.commandService = commandService;
        this.telegramBotSettings = telegramBotSettings;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Command command = commandService.createCommandByUpdateTelegram(update);
            analiseCommandAndSendAnswer(command);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Анализ комманд и отправка ответа в зависимости от команды
     *
     * @param command Коммандра {@link Command}
     * @throws TelegramApiException
     */
    private synchronized void analiseCommandAndSendAnswer(Command command) throws TelegramApiException {
        String textMessage = "";
        boolean isHTML = false;
        InlineKeyboardMarkup inlineKeyboardMarkup = null;
        switch (command.getCommand()) {
            case "contact":
                if (command.getListParam().size() == 0) {
                    textMessage = "Выберите нужный автовокзал";
                    List<BusStation> busStations = busSpravsApiClient.getAllBusStation();
                    List<Pair> pairList = new ArrayList<>();
                    for (BusStation busStation : busStations) {
                        pairList.add(new Pair(busStation.getName(), busStation.getId()));
                    }
                    inlineKeyboardMarkup = getInlineKeyboardFromList("/contact ", pairList, 3);
                } else {
                    BusStation busStation = busSpravsApiClient.getBusStationById(command.getListParam().get(0));
                    textMessage = busStation.toString();
                }
                break;
            case "today":
                List<Race> raceList = busSpravsApiClient.getAllRacesToday();
                textMessage = raceList.toString();
                break;
            case "shedule":
                if (command.getListParam().size() == 0) {
                    textMessage = "Выберите пункт отправления";
                    List<Stopplace> stopplaces = busSpravsApiClient.getAllStopplaces();
                    List<Pair> pairList = new ArrayList<>();
                    for (Stopplace stopplace : stopplaces) {
                        pairList.add(new Pair(stopplace.getName(), stopplace.getId()));
                    }
                    inlineKeyboardMarkup = getInlineKeyboardFromList("/shedule ", pairList, 3);
                } else if (command.getListParam().size() == 1) {
                    textMessage = "Выберите пункт назначения";
                    List<Stopplace> stopplaces = busSpravsApiClient.getAllStopplaces();
                    List<Pair> pairList = new ArrayList<>();
                    for (Stopplace stopplace : stopplaces) {
                        pairList.add(new Pair(stopplace.getName(), stopplace.getId()));
                    }
                    inlineKeyboardMarkup = getInlineKeyboardFromList("/shedule " + command.getListParam().get(0) + " ", pairList, 3);
                } else {
                    textMessage += "Список рейсов \n";
                    List<Race> races = busSpravsApiClient.getRacesBySpFromSpTo(command.getListParam().get(0), command.getListParam().get(1));
                    isHTML = true;
                    textMessage += races.toString();
                }
                break;
            case "order":
                if (command.getListParam().size() > 0) {
                    Order order = busSpravsApiClient.getOrderById(command.getListParam().get(0));
                    textMessage = order.toString();
                } else {
                    textMessage = "Пожалуйста, укажите номер заказа";
                }
                break;
            case "":
            case "help":
                isHTML = true;
                textMessage = getHelpMessage();
                break;
            default: {
                textMessage = (command.getFirstName() + ", такой команды не существует. Список команд можно посмотреть через / или /help");
            }
        }
        sendMessage(command, textMessage, isHTML, inlineKeyboardMarkup);
    }

    /**
     * Получить сообщение помощи по боту
     *
     * @return
     */
    private String getHelpMessage() {
        return "<pre>/order {код заказа}</pre>\n    Выводит информацию о заказе.\n\n" +
                "<pre>/contact</pre>\n    Запрос информации об автовокзалах.\n\n" +
                "<pre>/contact {код вокзала}</pre>\n    Выводит информацию об автовокзале.\n\n" +
                "<pre>/today</pre>\n    Вывод сегодняшнего расписания.\n\n" +
                "<pre>/shedule</pre>\n    Поиск расписания.\n\n" +
                "<pre>/shedule {ID пункта посадки} {ID пункта назначения}</pre>\n    Выводит расписание об автовокзалах.\n\n" +
                "";
    }

    /**
     * Отправка сообщения телеграмму
     *
     * @param command              Комманда {@link Command}
     * @param textMessage          Текстовое сообщение
     * @param isHTML               Формат сообщения HTML?
     * @param inlineKeyboardMarkup Кнопки для телеграмма {@link InlineKeyboardMarkup}
     * @throws TelegramApiException
     */
    private synchronized void sendMessage(Command command, String textMessage, boolean isHTML, InlineKeyboardMarkup inlineKeyboardMarkup) throws TelegramApiException {
        SendMessage message = commandService.getSendMessageByCommand(command, textMessage, isHTML, inlineKeyboardMarkup);
        execute(message);
    }

    /**
     * Формирует кнопки для Телеграмма
     *
     * @param prefixCallback   prefix для callback
     * @param pairList         Список пар
     * @param countButtonInRow Количество кнопок в ряду
     * @return Клавиатура для телеграмма {@link InlineKeyboardMarkup}
     */
    private InlineKeyboardMarkup getInlineKeyboardFromList(String prefixCallback, List<Pair> pairList, int countButtonInRow) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowListCode = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtonsRow1Code = new ArrayList<>();
        int iRow = 0;
        for (int i = 0; i < pairList.size(); i++) {
            iRow++;
            keyboardButtonsRow1Code.add(new InlineKeyboardButton().setText(pairList.get(i).getFirst().toString())
                    .setCallbackData(prefixCallback + pairList.get(i).getSecond().toString()));

            if (iRow == countButtonInRow) {
                iRow = 0;
                rowListCode.add(keyboardButtonsRow1Code);
                keyboardButtonsRow1Code = new ArrayList<>();
            }
        }
        if (keyboardButtonsRow1Code.size() > 0) {
            rowListCode.add(keyboardButtonsRow1Code);
        }

        inlineKeyboardMarkup.setKeyboard(rowListCode);
        return inlineKeyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        return telegramBotSettings.getBotName();
    }

    @Override
    public String getBotToken() {
        return telegramBotSettings.getToken();
    }
}
