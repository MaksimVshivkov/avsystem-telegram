package ru.avperm.avsystemtelegram.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.avperm.avsystemtelegram.dto.Command;
import ru.avperm.avsystemtelegram.utils.TextUtil;

@Service
@Slf4j
public class CommandService {

    /**
     * Формирует комманду из запроса в телеграмме
     *
     * @param update Запрос из телеграмма {@link Update}
     * @return
     */
    public Command createCommandByUpdateTelegram(Update update) {
        Command command = new Command();
        if (!update.hasCallbackQuery()) {
            String textCommand = update.hasMessage() ? (update.getMessage().hasText() ? update.getMessage().getText() : "Сообщение пустое") : "Нет сообщений";
            command.setFullText(textCommand);
            command.setChatId(update.getMessage().getChatId());
            command.setFirstName(update.getMessage().getChat().getFirstName());
            command.setCommand(TextUtil.getCommandFromText(textCommand));
            command.setListParam(TextUtil.getParamsFromText(textCommand));
        } else {            //е! collback сообщение
            String textCommand = update.getCallbackQuery().getData();
            command.setFullText(textCommand);
            command.setChatId(update.getCallbackQuery().getMessage().getChatId());
            command.setFirstName(update.getCallbackQuery().getMessage().getChat().getFirstName());
            command.setCommand(TextUtil.getCommandFromText(textCommand));
            command.setListParam(TextUtil.getParamsFromText(textCommand));
        }
        log.info(command.toString());
        return command;
    }

    /**
     * Формирует SendMessage для отправки телеграмму
     *
     * @param command              Комманда {@link Command}
     * @param textMessage          Текстовое сообщение
     * @param isHTML               Формат сообщения HTML?
     * @param inlineKeyboardMarkup Кнопки для телеграмма {@link InlineKeyboardMarkup}
     * @return
     */
    public SendMessage getSendMessageByCommand(Command command, String textMessage, boolean isHTML, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();

        message.setChatId(command.getChatId()).setText(textMessage);
        if (isHTML) {
            message.enableHtml(true)
                    .setParseMode(ParseMode.HTML)
                    .enableWebPagePreview();
        }

        if (inlineKeyboardMarkup != null) {
            message.setReplyMarkup(inlineKeyboardMarkup);
        }
        return message;
    }


}
