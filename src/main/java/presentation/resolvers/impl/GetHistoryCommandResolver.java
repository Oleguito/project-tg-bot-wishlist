package presentation.resolvers.impl;

import domain.model.entity.HistoryEntity;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import presentation.resolvers.CommandResolver;
import application.service.HistoryService;
import application.service.impl.HistoryServiceImpl;
import application.service.sessions.SessionManager;
import application.service.statemachine.State;
import infrastructure.utils.TelegramBotUtils;

import java.util.ArrayList;
import java.util.List;

public class GetHistoryCommandResolver implements CommandResolver {

    private final HistoryService historyService;
    private final String COMMAND_NAME = "/history";
    private final SessionManager sessionManager = SessionManager.getInstance();

    public GetHistoryCommandResolver() {
        this.historyService = new HistoryServiceImpl();
    }

    @Override
    public void resolveCommand(TelegramLongPollingBot tg_bot, String text, Long chat_id) {
        if(text.startsWith("/history")) {
            List <HistoryEntity> userHistory = historyService.getUserHistory(chat_id);
            var to = userHistory
                    .stream()
                    .map(e -> e.toString())
                    .toList();
            
            TelegramBotUtils.sendMessage(tg_bot, "История ваших сообщений:", chat_id);
            generateMessages(to).stream().forEach(e -> {
                TelegramBotUtils.sendMessage(tg_bot, e, chat_id);
            });
            
            SessionManager.getInstance().getSession(chat_id).setState(State.IDLE);
        }

    }
    
    private static List<String> generateMessages(List <String> to) {
        var iterator = to.iterator();
        int counter = 0;
        var intermediate = new StringBuffer();
        List <String> messages = new ArrayList<>();
        
        while(iterator.hasNext()) {
            intermediate.append(iterator.next());
            counter++;
            if(counter == 10) {
                
                messages.add(intermediate.toString());
                
                counter = 0;
                intermediate.delete(0, intermediate.length());
            }
        }
        return messages;
    }
    
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
}
