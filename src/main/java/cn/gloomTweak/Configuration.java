package cn.gloomTweak;



import cn.gloomTweak.utils.configuration.Comment;
import cn.gloomTweak.utils.configuration.ConfigurationFile;
import cn.gloomTweak.utils.configuration.ConfigurationPart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Configuration extends ConfigurationFile {

    @Comment("版本号")
    public static int version = 1;

    @Comment("总开关")
    public static boolean enabled = true;

    @Comment("玩家模块")
    public static Player player = new Player();
    public static class Player extends ConfigurationPart {

        @Comment("模块开关")
        public boolean enabled = true;

        @Comment("屏幕下方显示血量变为20, 实际血量不变")
        public boolean healthScaled = true;

        @Comment("简易电梯")
        public static EasyLift easyLift = new EasyLift();
        public static class EasyLift extends ConfigurationPart {
            @Comment("功能开关")
            public boolean enabled = true;

            @Comment({"电梯方块", "默认为阳光探测器"})
            public List<String> block = Arrays.asList("DAYLIGHT_DETECTOR");

            @Comment({"电梯音效, 留空以禁止"})
            public String sound = "BLOCK_PISTON_EXTEND";

            @Comment("黑名单")
            public static BlackList blackList = new BlackList();
            public static class BlackList extends ConfigurationPart {

                @Comment("禁用该功能的世界")
                public List<String> world = new ArrayList<>();

            }
        }

        @Comment({"死亡惩罚", "死亡不掉落打开后, 玩家是否不惧死亡", "该功能给予一定的惩罚防止玩家动不动死亡"})
        public static DeathPenalty deathPenalty = new DeathPenalty();
        public static class DeathPenalty extends ConfigurationPart {

        }

    }

    @Comment("生物模块")
    public static Mob mob = new Mob();
    public static class Mob extends ConfigurationPart{
        @Comment("模块开关")
        public boolean enabled = true;

        @Comment({"生物捕捉蛋", "使用鸡蛋捕捉生物, 并给予玩家对应生物蛋, 不保留生物属性"})
        public static EggCapture eggCapture = new EggCapture();
        public static class EggCapture extends ConfigurationPart {
            @Comment("功能开关")
            public boolean enabled = true;

            @Comment("捕捉的成功率")
            public static Chance chance = new Chance();
            public static class Chance extends ConfigurationPart {

                @Comment({"捕捉的成功率, 最大为100", "可用变量: {max_health}目标最大血量, {health}目标剩余血量"})
                public String formula = "{max_health} / {health} * 2";

                @Comment("最大捕捉成功率")
                public double max = 8;
            }

            @Comment("捕获成功产生的粒子特效")
            public static Particle particle = new Particle();
            public static class Particle extends ConfigurationPart {

                @Comment("类型, 留空禁用")
                public String type = "ELECTRIC_SPARK";

                @Comment("数量")
                public int count = 60;
            }

            @Comment({"捕捉成功给玩家的提示", "只支持minimessage颜色格式", "可用变量: {mob}生物名称"})
            public String actionbar = "<white>成功捕捉<yellow> {mob} </yellow>!";

            @Comment("黑名单")
            public static BlackList blackList = new BlackList();
            public static class BlackList extends ConfigurationPart {

                @Comment("禁用该功能的世界")
                public List<String> world = new ArrayList<>();

                @Comment({"禁止捕捉的生物", "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html"})
                public List<String> entity = Arrays.asList("ENDER_DRAGON", "WARDEN", "ELDER_GUARDIAN", "WITHER", "IRON_GOLEM", "SNOW_GOLEM");

                @Comment({"该原因生成的生物禁止被捕捉", "SPAWNER_EGG 可以防止玩家多次捕捉同一个生物"})
                public List<String> spawnReason = List.of("SPAWNER_EGG");

            }
        }

    }

}
