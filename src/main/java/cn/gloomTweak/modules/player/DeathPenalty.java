package cn.gloomTweak.modules.player;

import cn.gloomTweak.Configuration;
import cn.gloomTweak.Main;
import cn.gloomTweak.utils.*;
import cn.gloomTweak.utils.SchedulerUtil.SchedulerInterface;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import redempt.crunch.CompiledExpression;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class DeathPenalty implements Listener {

    private final HashMap<UUID, List<Long>> recentDeaths;
    private final CompiledExpression expExpression;
    private Degree expDegree;
    private Set<PotionEffectType> potionTypes;
    private int potionTime;
    private Component titlePenalty;
    private String subTitlePenalty;
    private String expTextPenalty;
    private List<String> subTextPenalty;
    private List<Component> titleRespawn;
    private String subTitleRespawn;
    private String expTextRespawn;
    private boolean particleEnabled;
    private Particle particle;
    private int particleCount;
    private final SchedulerInterface scheduler;

    public DeathPenalty(Main main) {

        this.scheduler = main.getSchedule();
        recentDeaths = new HashMap<>();
        try {
            expDegree = Degree.build(Configuration.Player.DeathPenalty.Level.levelPenalty.degree);
            potionTypes = new HashSet<>(MaterialUtil.potionToSet(Configuration.Player.DeathPenalty.Level.LevelPenalty.maxLevelPenalty.potion));
            potionTime = Configuration.Player.DeathPenalty.Level.LevelPenalty.maxLevelPenalty.potionTime * 20;
            titlePenalty = MiniMessage.miniMessage().deserialize(Configuration.Player.DeathPenalty.Level.LevelPenalty.maxLevelPenalty.mainTitle);
            subTitlePenalty = Configuration.Player.DeathPenalty.Level.LevelPenalty.maxLevelPenalty.subTitle.replace("{exptext}", "<exptext>").replace("{text}", "<text>");
            expTextPenalty = Configuration.Player.DeathPenalty.Level.LevelPenalty.maxLevelPenalty.subExpText.replace("{exp}", "<exp>");
            subTextPenalty = Configuration.Player.DeathPenalty.Level.LevelPenalty.maxLevelPenalty.subText;
            titleRespawn = Message.buildComponentList(Configuration.Player.DeathPenalty.respawn.mainTitle);
            subTitleRespawn = Configuration.Player.DeathPenalty.respawn.subTitle.replace("{exptext}", "<exptext>");
            expTextRespawn = Configuration.Player.DeathPenalty.respawn.expText.replace("{exp}", "<exp>");
            particle = Particle.valueOf(Configuration.Player.DeathPenalty.Respawn.particle.type.toUpperCase(Locale.ROOT));
            particleCount = Configuration.Player.DeathPenalty.Respawn.particle.count;
            particleEnabled = (!Configuration.Player.DeathPenalty.Respawn.particle.type.isEmpty()) && (particleCount > 0);

        } catch (Exception e) {
            XLogger.err(e.getMessage());
        }
        expExpression = ExpressionUtil.build(Configuration.Player.DeathPenalty.Level.levelPenalty.exp, "exp", "degree", "level");

    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        long time = System.currentTimeMillis();
        List<Long> deaths = recentDeaths.getOrDefault(playerId, new ArrayList<>());

        if (!deaths.isEmpty()) {
            if (time - deaths.getFirst() > TimeUnit.MINUTES.toMillis(Configuration.Player.DeathPenalty.level.effectiveTime)) {
                deaths.clear();
            }
        }

        deaths.add(time);
        recentDeaths.put(playerId, deaths);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerPostRespawnEvent event) {

        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        List<Long> deaths = recentDeaths.getOrDefault(playerId, new ArrayList<>());

        int exp = PlayerUtil.getTotalExperience(player);
        int level = deaths.size();
        int deduction = (int) (expExpression.evaluate(exp, expDegree.getRandom(), level));
        if (deduction > 1) {
            player.giveExp(-deduction);
        }

        player.performCommand(Configuration.Player.DeathPenalty.respawn.command);

        if (particleEnabled) {
            scheduler.runTaskLater(() -> {
                player.spawnParticle(particle, player.getEyeLocation(), particleCount);
            }, 5);
        }

        if (deaths.size() >= Configuration.Player.DeathPenalty.level.maxLevel) {
            recentDeaths.remove(playerId);
            scheduler.runTaskLater(() -> {
                for (PotionEffectType p : potionTypes) {
                    player.addPotionEffect(new PotionEffect(p, potionTime, 1));
                }
            }, 1);
            scheduler.runTaskLaterAsync(() -> {
                String exptext = "";
                if (deduction > 1) {
                    exptext = expTextPenalty;
                }

                Component subtitle = MiniMessage.miniMessage().deserialize(
                        subTitlePenalty,
                        Placeholder.parsed("exptext", exptext),
                        Placeholder.parsed("text", subTextPenalty.get(ThreadLocalRandom.current().nextInt(subTextPenalty.size()))),
                        Placeholder.parsed("exp", String.valueOf(deduction)));
                Title title = Title.title(titlePenalty, subtitle);
                player.showTitle(title);
            }, 1);
            return;
        }

        scheduler.runTaskLaterAsync(() -> {
            String exptext = "";
            if (deduction > 1) {
                exptext = expTextRespawn;
            }
            Component subtitle = MiniMessage.miniMessage().deserialize(
                    subTitleRespawn,
                    Placeholder.parsed("exptext", exptext),
                    Placeholder.parsed("exp", String.valueOf(deduction)));
            Title title = Title.title(titleRespawn.get(ThreadLocalRandom.current().nextInt(titleRespawn.size())), subtitle);
            player.showTitle(title);
        }, 1);


    }

}
