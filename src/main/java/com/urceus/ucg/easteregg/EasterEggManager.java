// src/main/java/com/urceus/ucg/easteregg/EasterEggManager.java
package com.urceus.ucg.easteregg;

import com.urceus.ucg.UCGMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class EasterEggManager {
    private static final List<String> PHRASES = new ArrayList<>();
    private static final Random RANDOM = ThreadLocalRandom.current();
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        
        // Russian phrases (1-61)
        PHRASES.add("Кувшины следят за тобой.");
        PHRASES.add("Кувшины помнят.");
        PHRASES.add("Не зли святые сосуды.");
        PHRASES.add("Каждый глоток оставляет эхо.");
        PHRASES.add("Глина помнит твое прикосновение.");
        PHRASES.add("Кувшины мечтают о воде.");
        PHRASES.add("Трещины рассказывают истории.");
        PHRASES.add("Тебя взвешивают на весах судьбы.");
        PHRASES.add("Кувшины знают твое имя.");
        PHRASES.add("Терпение, смертный.");
        PHRASES.add("Печь еще не остыла.");
        PHRASES.add("Твоя жажда отмечена.");
        PHRASES.add("Кувшины ждут.");
        PHRASES.add("Они говорят каплями.");
        PHRASES.add("Не нарушай баланс.");
        PHRASES.add("Ручки кувшина теплые.");
        PHRASES.add("Кувшины наблюдали за тобой еще до твоего рождения.");
        PHRASES.add("Тени кружатся внутри.");
        PHRASES.add("Одна капля может нарушить тишину.");
        PHRASES.add("Кувшины собирают мгновения, а не воду.");
        PHRASES.add("Твое отражение старше, чем ты думаешь.");
        PHRASES.add("Глазурь видит всё.");
        PHRASES.add("Кувшины мечтают стать колодцами.");
        PHRASES.add("Не спрашивай, куда ушла вода.");
        PHRASES.add("Кувшины предпочитают вино, но принимают веру.");
        PHRASES.add("Кувшины кивают, когда ты спишь.");
        PHRASES.add("У них есть братья под землей — The Exiled Ones.");
        PHRASES.add("Однажды они изливают правду.");
        PHRASES.add("Кувшины любят запах дождя.");
        PHRASES.add("Твое сердцебиение эхом отдается внутри.");
        PHRASES.add("Они не прощают нетерпения.");
        PHRASES.add("Ободки следят за твоими губами.");
        PHRASES.add("Кувшины помнят каждую руку, что держала их.");
        PHRASES.add("Глина поет в полночь.");
        PHRASES.add("Они собирают вздохи.");
        PHRASES.add("Не наполняй их ложью.");
        PHRASES.add("Кувшины мечтают об океане.");
        PHRASES.add("Они наклоняются, когда ты лжешь.");
        PHRASES.add("У них свой собственный календарь.");
        PHRASES.add("Твое дыхание покрывает их поверхность тайнами.");
        PHRASES.add("Кувшины предпочитают тишину.");
        PHRASES.add("Носики целятся в твою душу.");
        PHRASES.add("Раньше они наполнялись лунным светом.");
        PHRASES.add("Они пьют страх.");
        PHRASES.add("Кувшины улыбаются во тьме.");
        PHRASES.add("Они знают, что ты налил туда вчера.");
        PHRASES.add("Они судят шепотом.");
        PHRASES.add("Иногда они гудят.");
        PHRASES.add("Кувшины помнят руку гончара.");
        PHRASES.add("Они не доверяют пустым комнатам.");
        PHRASES.add("Кувшины считают твои шаги.");
        PHRASES.add("У них есть любимое направление: север.");
        PHRASES.add("Они собирают заброшенные обещания.");
        PHRASES.add("Не стучи по ним три раза.");
        PHRASES.add("Кувшины видели падение королевств.");
        PHRASES.add("Они предпочитают оставаться в одиночестве.");
        PHRASES.add("Трещины растут, когда ты кричишь.");
        PHRASES.add("Им нравится, когда их полируют.");
        PHRASES.add("Они ненавидят, когда их бросают.");
        PHRASES.add("Кувшины помнят каждый язык, на котором говорили рядом.");
        PHRASES.add("Они прощают, но никогда не забывают.");

        // Additional Russian phrases (62-100)
        PHRASES.add("Иногда они проливают секреты.");
        PHRASES.add("Они старше, чем эта деревня.");
        PHRASES.add("У них есть родимые пятна внутри.");
        PHRASES.add("Кувшины смеются при землетрясениях.");
        PHRASES.add("Им нравится, когда их несут осторожно.");
        PHRASES.add("Они не любят громкую музыку.");
        PHRASES.add("У них есть мечты о пламени.");
        PHRASES.add("У ручек есть пульс.");
        PHRASES.add("Они собирают забытые молитвы.");
        PHRASES.add("Кувшины знают твою любимую чашку.");
        PHRASES.add("Некоторые из них были украдены раньше. Им это не понравилось.");
        PHRASES.add("Они любят дождь больше, чем солнце.");
        PHRASES.add("Иногда они светятся в 3 часа ночи.");
        PHRASES.add("Они знают, когда ты врешь про воду.");
        PHRASES.add("У них есть любимый игрок: ты. Пока что.");
        PHRASES.add("Им не нравится, когда их используют для супа.");
        PHRASES.add("Кувшины смотрят в ответ.");
        PHRASES.add("Они считают удары твоего сердца.");
        PHRASES.add("Им нравится, когда их упоминают в разговоре.");
        PHRASES.add("Им не нравится, когда их игнорируют.");
        PHRASES.add("Они помнят каждый всплеск.");
        PHRASES.add("Кувшины пишут стихи по ночам.");
        PHRASES.add("Они мечтают снова стать единым целым.");
        PHRASES.add("У них есть кузены в Nether.");
        PHRASES.add("Им нравится, когда их чистят аккуратно.");
        PHRASES.add("У них есть любимые песни. Но они не скажут.");
        PHRASES.add("Они знают дорогу домой.");
        PHRASES.add("Они собирают слезы.");
        PHRASES.add("Им не нравится, когда их используют в качестве шляп.");
        PHRASES.add("Они мечтают летать.");
        PHRASES.add("Они видели конец света. Это их не впечатлило.");
        PHRASES.add("Им нравится быть в центре внимания.");
        PHRASES.add("Им не нравится, когда их сравнивают с другими кувшинами.");
        PHRASES.add("Они помнят твой первый login.");
        PHRASES.add("У них есть план.");
        PHRASES.add("Они любят зиму больше, чем лето.");
        PHRASES.add("Им не нравится быть пустыми.");
        PHRASES.add("Они довольны твоим вниманием.");
        PHRASES.add("Кувшины благодарны. Пока что.");

        // Latin phrases (101-120)
        PHRASES.add("Vasa vitrea amant te.");
        PHRASES.add("Non obliviscimur.");
        PHRASES.add("Tacent, sed audiunt.");
        PHRASES.add("Exspectamus.");
        PHRASES.add("Nolite nos frangere.");
        PHRASES.add("Pocula Dei.");
        PHRASES.add("Nos videmus omnia.");
        PHRASES.add("Silentium nostrum est iudicium.");
        PHRASES.add("Urcei vos amant.");
        PHRASES.add("Aqua nostra, vita tua.");
        PHRASES.add("Oblivisci non possumus.");
        PHRASES.add("Date nobis aquam.");
        PHRASES.add("Nos sumus umbrae aquae.");
        PHRASES.add("Fides tua te salvet.");
        PHRASES.add("Nolite nos irritare.");
        PHRASES.add("Urcei veniunt.");
        PHRASES.add("Tu es in manibus nostris.");
        PHRASES.add("Clamamus in silentio.");
        PHRASES.add("Nos volumus te intellegere.");
        PHRASES.add("Vasa non mentiuntur.");

        initialized = true;
        UCGMod.LOGGER.info("EasterEggManager initialized with {} phrases", PHRASES.size());
    }

    public static String getRandomPhrase() {
        if (!initialized) init();
        return PHRASES.get(RANDOM.nextInt(PHRASES.size()));
    }

    public static void sendPhraseToChat() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            String phrase = getRandomPhrase();
            MutableComponent component = Component.literal(phrase).withStyle(ChatFormatting.GOLD);
            mc.player.sendSystemMessage(component);
        }
    }

    public static int getPhraseCount() {
        return PHRASES.size();
    }
}