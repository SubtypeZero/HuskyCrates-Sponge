package pw.codehusky.huskycrates.crate.config;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;

public class CrateRewardHolderParser {
    public static CrateRewardHolder fromConfig(ConfigurationNode holderNode){
        ItemStack dispItem = itemFromNode(holderNode);
        CrateReward reward = new CrateReward(null,"CODE ERROR, CONTACT DEVELOPER",false);
        String name;
        boolean single = false;
        //System.out.println(dispItem.get(Keys.DISPLAY_NAME));
        if(holderNode.getNode("huskydata","reward").getNode("overrideRewardName").isVirtual()){
            System.out.println("Virtual");
            if(dispItem.get(Keys.DISPLAY_NAME).isPresent()){
                name = dispItem.get(Keys.DISPLAY_NAME).get().toPlain();
            }else{
                name = dispItem.getItem().getName();
            }
        }else{
            System.out.println("Real");
            name = holderNode.getNode("huskydata","reward").getNode("overrideRewardName").getString("strings, please.");
        }
        if(!holderNode.getNode("huskydata","reward").getNode("overrideRewardName").isVirtual()){
            single = holderNode.getNode("reward").getNode("overrideRewardName").getBoolean();
        }
        if(holderNode.getNode("huskydata","reward","type").getString().equalsIgnoreCase("item")) {
            ItemStack rewardItem;
            if (holderNode.getNode("huskydata","reward").getNode("overrideItem").isVirtual()) {
                if(!holderNode.getNode("huskydata","reward").getNode("overrideCount").isVirtual()){
                    ItemStack rr = dispItem.copy();
                    rr.setQuantity(holderNode.getNode("huskydata","reward").getNode("overrideCount").getInt());
                    reward=new CrateReward(rr,name,single);
                }
            }
        }else if(holderNode.getNode("reward","type").getString().equalsIgnoreCase("command")){
            reward = new CrateReward(holderNode.getNode("reward","command").getString("/say You didn't set a command or something..."),name,single);
        }

        return new CrateRewardHolder(dispItem,reward,holderNode.getNode("huskydata","weight").getDouble());
    }
    private static ItemStack itemFromNode(ConfigurationNode itemRoot){
        try {
            ItemStack item = ItemStack.builder()
                    .itemType(itemRoot.getNode("id").getValue(TypeToken.of(ItemType.class)))
                    .quantity(itemRoot.getNode("count").getInt(1))
                    .add(Keys.DISPLAY_NAME, TextSerializers.FORMATTING_CODE.deserialize(itemRoot.getNode("name").getString()))
                    .build();

            item.offer(Keys.ITEM_LORE,itemRoot.getNode("lore").getList(TypeToken.of(Text.class)));
            item.offer(Keys.DISPLAY_NAME, TextSerializers.FORMATTING_CODE.deserialize(itemRoot.getNode("name").getString()));
            ArrayList<ItemEnchantment> enchantments = new ArrayList<>();
            for(Object key : itemRoot.getNode("enchants").getChildrenMap().keySet()){
                int level = itemRoot.getNode("enchants").getChildrenMap().get(key).getInt();
                String enchantID = (String) key;
                Enchantment enc = getEnchantment(enchantID); // STRINGS ONLY!
                ItemEnchantment itemEnchantment = new ItemEnchantment(enc,level);
                enchantments.add(itemEnchantment);
            }
            item.offer(Keys.ITEM_ENCHANTMENTS,enchantments);
            //item.offer(Keys.PICKUP_DELAY,itemRoot.getNode("pickupdelay").getInt())
            return item;
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static Enchantment getEnchantment(String id){
        switch(id){
            case "protection":
                return Enchantments.PROTECTION;
            case "fire_protection":
                return Enchantments.FIRE_PROTECTION;
            case "feather_falling":
                return Enchantments.FEATHER_FALLING;
            case "blast_protection":
                return Enchantments.BLAST_PROTECTION;
            case "projectile_protection":
                return Enchantments.PROJECTILE_PROTECTION;
            case "respiration":
                return Enchantments.RESPIRATION;
            case "aqua_affinity":
                return Enchantments.AQUA_AFFINITY;
            case "thorns":
                return Enchantments.THORNS;
            case "depth_strider":
                return Enchantments.DEPTH_STRIDER;
            case "frost_walker":
                return Enchantments.FROST_WALKER;
            case "binding_curse":
                return Enchantments.BINDING_CURSE;
            case "sharpness":
                return Enchantments.SHARPNESS;
            case "smite":
                return Enchantments.SMITE;
            case "bane_of_arthropods":
                return Enchantments.BANE_OF_ARTHROPODS;
            case "knockback":
                return Enchantments.KNOCKBACK;
            case "fire_aspect":
                return Enchantments.FIRE_ASPECT;
            case "looting":
                return Enchantments.LOOTING;
            case "sweeping":
                return Enchantments.SWEEPING;
            case "efficiency":
                return Enchantments.EFFICIENCY;
            case "silk_touch":
                return Enchantments.SILK_TOUCH;
            case "unbreaking":
                return Enchantments.UNBREAKING;
            case "fortune":
                return Enchantments.FORTUNE;
            case "power":
                return Enchantments.POWER;
            case "punch":
                return Enchantments.PUNCH;
            case "flame":
                return Enchantments.FLAME;
            case "infinity":
                return Enchantments.INFINITY;
            case "luck_of_the_sea":
                return Enchantments.LUCK_OF_THE_SEA;
            case "lure":
                return Enchantments.LURE;
            case "mending":
                return Enchantments.MENDING;
            case "vanishing_curse":
                return Enchantments.VANISHING_CURSE;
        }
        return null; //you done goof
    }
    private static Enchantment getEnchantment(int id){
        return null;
    }
}