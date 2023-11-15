package horse.boo.bot;


import horse.boo.bot.services.BotReadyService;
import horse.boo.bot.services.MemberJoinService;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class DiscordClient implements CommandLineRunner {

    @Value("${discord.token}")
    private String token;

    private final MemberJoinService memberJoinService;
    private final BotReadyService botReadyService;
    public static String TYPE = "default";


    public DiscordClient(MemberJoinService memberJoinService,
                         BotReadyService botReadyService) {
        this.memberJoinService = memberJoinService;
        this.botReadyService = botReadyService;
    }

    @Override
    public void run(String... args) {
        var jda = JDABuilder
                .createDefault(token)
                .enableIntents(EnumSet.allOf(GatewayIntent.class))
                .disableCache(CacheFlag.SCHEDULED_EVENTS, CacheFlag.VOICE_STATE)
                .setBulkDeleteSplittingEnabled(false)
                .setCompression(Compression.NONE)
                .setActivity(Activity.playing("eating cakes"))        // Bot activity
                .setStatus(OnlineStatus.ONLINE)
                .build();

        jda.addEventListener(
                memberJoinService,
                botReadyService
        );
    }
}