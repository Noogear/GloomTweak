package cn.gloomTweak;


import cn.gloomTweak.utils.configuration.Comments;
import cn.gloomTweak.utils.configuration.ConfigurationFile;
import cn.gloomTweak.utils.configuration.ConfigurationPart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Configuration extends ConfigurationFile {

    @Comments("版本号")
    public static int version = 2;

    @Comments("总开关")
    public static boolean enabled = true;

    @Comments("玩家模块")
    public static Player player = new Player();
    @Comments("生物模块")
    public static Mob mob = new Mob();

    public static class Player extends ConfigurationPart {

        @Comments("模块开关")
        public boolean enabled = true;
        @Comments("屏幕下方显示血量变为20, 实际血量不变")
        public boolean healthScaled = true;
        @Comments("简易电梯")
        public static EasyLift easyLift = new EasyLift();
        @Comments({"死亡惩罚", "死亡不掉落打开后, 让玩家依旧畏惧死亡", "该功能给予一定的惩罚防止玩家动不动死亡"})
        public static DeathPenalty deathPenalty = new DeathPenalty();

        public static class EasyLift extends ConfigurationPart {
            @Comments("功能开关")
            public boolean enabled = true;
            @Comments({"电梯方块", "默认为阳光探测器"})
            public List<String> block = List.of("DAYLIGHT_DETECTOR");
            @Comments({"电梯音效, 留空以禁止"})
            public String sound = "BLOCK_PISTON_EXTEND";
            @Comments("黑名单")
            public static BlackList blackList = new BlackList();

            public static class BlackList extends ConfigurationPart {

                @Comments("禁用该功能的世界")
                public List<String> world = new ArrayList<>();

            }
        }

        public static class DeathPenalty extends ConfigurationPart {
            @Comments("功能开关")
            public boolean enabled = true;
            @Comments("复活相关功能")
            public static Respawn respawn = new Respawn();
            @Comments({"惩罚等级", "每次死亡会累加惩罚等级"})
            public static Level level = new Level();

            public static class Level extends ConfigurationPart {


                @Comments({"最大惩罚等级", "玩家获得最大惩罚等级后会重置惩罚等级"})
                public int maxLevel = 6;
                @Comments({"惩罚等级有效时间", "单位为分钟"})
                public int effectiveTime = 15;

                @Comments("等级对惩罚的影响")
                public static LevelPenalty levelPenalty = new LevelPenalty();
                public static class LevelPenalty extends ConfigurationPart {

                    @Comments({"扣除的经验", "可用变量: {exp}玩家当前经验, {degree}比例, {level}惩罚等级"})
                    public String exp = "{exp} * {degree} * {level} * 0.01";
                    @Comments("比例，配合上方公式使用")
                    public String degree = "0.4-1.4";

                    @Comments("当惩罚等级有效时间内到达最大惩罚等级获得的惩罚")
                    public static MaxLevelPenalty maxLevelPenalty = new MaxLevelPenalty();
                    public static class MaxLevelPenalty extends ConfigurationPart {

                        @Comments("给予的药水效果")
                        public List<String> potion = Arrays.asList("BLINDNESS", "NAUSEA");
                        @Comments("药水持续时间")
                        public int potionTime = 30;

                        @Comments({"惩罚提示, 会覆盖复活提示"})
                        public String mainTitle = "<red>死亡惩罚</red>";
                        @Comments({"复活副标题", "可用变量: {exptext}经验文本, {text}随机文本"})
                        public String subTitle = "{exptext}<gray>{text}<gray>";
                        @Comments({"经验文本, 配合上方使用, 扣除经验为空时自动隐藏", "可用变量: {exp}扣除的经验"})
                        public String subExpText = "<aqua>-{exp}</aqua> <white>EXP</white> ";
                        @Comments("随机文本, 配合上方使用")
                        public List<String> subText = Arrays.asList("视生命如草芥, 该罚!", "求求你不要再屎啦~", "我去, 上瘾了是吧?");
                    }
                }
            }

            public static class Respawn extends ConfigurationPart {

                @Comments("复活提示")
                public List<String> mainTitle = Arrays.asList("夺舍成功!", "复活成功!", "你重生了...", "你转生了...", "秽土转生之术", "外道轮回天生之术", "双鱼佩复活次数 -1", "提瓦特煎蛋 -1");
                @Comments({"复活副标题", "可用变量: {exptext}经验文本"})
                public String subTitle = "{exptext}";
                @Comments({"经验文本, 配合上方使用, 扣除经验为空时自动隐藏", "可用变量: {exp}扣除的经验"})
                public String expText = "<aqua>-{exp}</aqua> <white>EXP</white>";
                @Comments({"玩家复活后执行的指令", "配合其他插件使用效果更好"})
                public String command = "ctdeathmsg";

                @Comments("复活产生的粒子特效")
                public static Particle particle = new Particle();
                public static class Particle extends ConfigurationPart {

                    @Comments("类型, 留空禁用")
                    public String type = "TOTEM_OF_UNDYING";

                    @Comments("数量")
                    public int count = 50;
                }

            }
        }

    }

    public static class Mob extends ConfigurationPart {
        @Comments("模块开关")
        public boolean enabled = true;
        @Comments({"生物捕捉蛋", "使用鸡蛋捕捉生物, 并给予玩家对应生物蛋, 不保留生物属性"})
        public static EggCapture eggCapture = new EggCapture();


        public static class EggCapture extends ConfigurationPart {

            @Comments("功能开关")
            public boolean enabled = true;
            @Comments("捕捉的成功率")
            public static Chance chance = new Chance();
            @Comments("捕获成功产生的粒子特效")
            public static Particle particle = new Particle();
            @Comments({"平衡", "捕捉蛋导致养鸡的人剧增, 该功能用于削减"})
            public static Balance balance = new Balance();
            @Comments({"捕捉成功给玩家的提示", "只支持minimessage颜色格式", "可用变量: {mob}生物名称"})
            public String actionbar = "<white>成功捕捉<yellow> {mob} </yellow>!";
            @Comments("有名称的生物蛋生成的生物是否和命名生物一样不会消失")
            public Boolean nameSpawnerEggFix = true;
            @Comments("黑名单")
            public static BlackList blackList = new BlackList();

            public static class Chance extends ConfigurationPart {

                @Comments({"捕捉的成功率, 最大为100", "可用变量: {max_health}目标最大血量, {health}目标剩余血量"})
                public String formula = "{max_health} / {health} * 2";

                @Comments("最大捕捉成功率")
                public double max = 8;
            }

            public static class Particle extends ConfigurationPart {

                @Comments("类型, 留空禁用")
                public String type = "ELECTRIC_SPARK";

                @Comments("数量")
                public int count = 60;
            }

            public static class Balance extends ConfigurationPart {

                @Comments({"捕捉蛋导致养鸡的人剧增", "这个可以让鸡蛋砸生物孵出的鸡概率减半"})
                public boolean decreaseEggHatching = true;

                @Comments({"可以让鸡生蛋时一定概率变羽毛", "可以为原版提供更多获得羽毛的途径", "可填整数, 最大100, 负数或零禁止"})
                public int eggToFeather = 10;
            }

            public static class BlackList extends ConfigurationPart {

                @Comments("禁用该功能的世界")
                public List<String> world = new ArrayList<>();

                @Comments({"禁止捕捉的生物", "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html"})
                public List<String> entity = Arrays.asList("ENDER_DRAGON", "WARDEN", "ELDER_GUARDIAN", "WITHER", "IRON_GOLEM", "SNOW_GOLEM");

                @Comments({"该原因生成的生物禁止被捕捉", "SPAWNER_EGG 可以防止玩家多次捕捉同一个生物"})
                public List<String> spawnReason = List.of("SPAWNER_EGG");

            }
        }

    }

}
