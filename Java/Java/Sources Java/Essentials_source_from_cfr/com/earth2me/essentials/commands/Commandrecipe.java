/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.FurnaceRecipe
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.Recipe
 *  org.bukkit.inventory.ShapedRecipe
 *  org.bukkit.inventory.ShapelessRecipe
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.NumberUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class Commandrecipe
extends EssentialsCommand {
    public Commandrecipe() {
        super("recipe");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        List recipesOfType;
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        ItemStack itemType = this.ess.getItemDb().get(args[0]);
        int recipeNo = 0;
        if (args.length > 1) {
            if (NumberUtil.isInt(args[1])) {
                recipeNo = Integer.parseInt(args[1]) - 1;
            } else {
                throw new Exception(I18n._("invalidNumber", new Object[0]));
            }
        }
        if ((recipesOfType = this.ess.getServer().getRecipesFor(itemType)).size() < 1) {
            throw new Exception(I18n._("recipeNone", this.getMaterialName(itemType)));
        }
        if (recipeNo < 0 || recipeNo >= recipesOfType.size()) {
            throw new Exception(I18n._("recipeBadIndex", new Object[0]));
        }
        Recipe selectedRecipe = (Recipe)recipesOfType.get(recipeNo);
        sender.sendMessage(I18n._("recipe", this.getMaterialName(itemType), recipeNo + 1, recipesOfType.size()));
        if (selectedRecipe instanceof FurnaceRecipe) {
            this.furnaceRecipe(sender, (FurnaceRecipe)selectedRecipe);
        } else if (selectedRecipe instanceof ShapedRecipe) {
            this.shapedRecipe(sender, (ShapedRecipe)selectedRecipe);
        } else if (selectedRecipe instanceof ShapelessRecipe) {
            this.shapelessRecipe(sender, (ShapelessRecipe)selectedRecipe);
        }
        if (recipesOfType.size() > 1 && args.length == 1) {
            sender.sendMessage(I18n._("recipeMore", commandLabel, args[0], this.getMaterialName(itemType)));
        }
    }

    public void furnaceRecipe(CommandSource sender, FurnaceRecipe recipe) {
        sender.sendMessage(I18n._("recipeFurnace", this.getMaterialName(recipe.getInput())));
    }

    public void shapedRecipe(CommandSource sender, ShapedRecipe recipe) {
        Map recipeMap = recipe.getIngredientMap();
        if (sender.isPlayer()) {
            User user = this.ess.getUser(sender.getPlayer());
            user.closeInventory();
            user.setRecipeSee(true);
            InventoryView view = user.openWorkbench(null, true);
            String[] recipeShape = recipe.getShape();
            Map ingredientMap = recipe.getIngredientMap();
            for (int j = 0; j < recipeShape.length; ++j) {
                for (int k = 0; k < recipeShape[j].length(); ++k) {
                    ItemStack item = (ItemStack)ingredientMap.get(Character.valueOf(recipeShape[j].toCharArray()[k]));
                    if (item == null) continue;
                    item.setAmount(0);
                    view.getTopInventory().setItem(j * 3 + k + 1, item);
                }
            }
        } else {
            HashMap<Material, String> colorMap = new HashMap<Material, String>();
            int i = 1;
            char[] arr$ = "abcdefghi".toCharArray();
            int len$ = arr$.length;
            for (int i$ = 0; i$ < len$; ++i$) {
                Character c = Character.valueOf(arr$[i$]);
                ItemStack item = (ItemStack)recipeMap.get(c);
                if (colorMap.containsKey((Object)(item == null ? null : item.getType()))) continue;
                colorMap.put(item == null ? null : item.getType(), String.valueOf(i++));
            }
            Material[][] materials = new Material[3][3];
            for (int j = 0; j < recipe.getShape().length; ++j) {
                for (int k = 0; k < recipe.getShape()[j].length(); ++k) {
                    ItemStack item = (ItemStack)recipe.getIngredientMap().get(Character.valueOf(recipe.getShape()[j].toCharArray()[k]));
                    materials[j][k] = item == null ? null : item.getType();
                }
            }
            sender.sendMessage(I18n._("recipeGrid", colorMap.get((Object)materials[0][0]), colorMap.get((Object)materials[0][1]), colorMap.get((Object)materials[0][2])));
            sender.sendMessage(I18n._("recipeGrid", colorMap.get((Object)materials[1][0]), colorMap.get((Object)materials[1][1]), colorMap.get((Object)materials[1][2])));
            sender.sendMessage(I18n._("recipeGrid", colorMap.get((Object)materials[2][0]), colorMap.get((Object)materials[2][1]), colorMap.get((Object)materials[2][2])));
            StringBuilder s = new StringBuilder();
            for (Material items : colorMap.keySet().toArray((T[])new Material[colorMap.size()])) {
                s.append(I18n._("recipeGridItem", colorMap.get((Object)items), this.getMaterialName(items)));
            }
            sender.sendMessage(I18n._("recipeWhere", s.toString()));
        }
    }

    public void shapelessRecipe(CommandSource sender, ShapelessRecipe recipe) {
        List ingredients = recipe.getIngredientList();
        if (sender.isPlayer()) {
            User user = this.ess.getUser(sender.getPlayer());
            user.setRecipeSee(true);
            InventoryView view = user.openWorkbench(null, true);
            for (int i = 0; i < ingredients.size(); ++i) {
                view.setItem(i + 1, (ItemStack)ingredients.get(i));
            }
        } else {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < ingredients.size(); ++i) {
                s.append(this.getMaterialName((ItemStack)ingredients.get(i)));
                if (i != ingredients.size() - 1) {
                    s.append(",");
                }
                s.append(" ");
            }
            sender.sendMessage(I18n._("recipeShapeless", s.toString()));
        }
    }

    public String getMaterialName(ItemStack stack) {
        if (stack == null) {
            return I18n._("recipeNothing", new Object[0]);
        }
        return this.getMaterialName(stack.getType());
    }

    public String getMaterialName(Material type) {
        if (type == null) {
            return I18n._("recipeNothing", new Object[0]);
        }
        return type.toString().replace("_", " ").toLowerCase(Locale.ENGLISH);
    }
}

