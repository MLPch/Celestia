package horse.boo.bot.services;

import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import horse.boo.bot.database.repository.ConfigRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BotReadyService extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(BotReadyService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;

    public BotReadyService(ConfigRepository configRepository,
                           LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
    }


    /**
     * @param event - реагирует на первое подключение бота к гильдии
     *              Записывает начальный конфиг для каждой гильдии к которой подключается.
     *              Срабатывает только при приглашении бота в гильдию.
     */
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        var guild = event.getGuild();
        if (configRepository.getConfigByGuildId(guild.getIdLong()) == null) {
            ConfigsTable config = new ConfigsTable(guild);
            configRepository.save(config);
            logger.info("Joined a new guild! Name: " + guild.getName());
            logger.info("Saved the default config: " + config);
        }
    }

    /**
     * @param event - реагирует на включение (этого) бота находящегося в гильдии
     *              Сообщает об обновлениях при запуске бота.
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        Guild guild = event.getGuild();
        logger.info("I work in the guild: " + guild.getName() + " (id=" + guild.getIdLong() + ")");


    }

}