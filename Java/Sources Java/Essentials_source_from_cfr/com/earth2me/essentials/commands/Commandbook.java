/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.BookMeta
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class Commandbook
extends EssentialsCommand {
    public Commandbook() {
        super("book");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        ItemStack item = user.getItemInHand();
        String player = user.getName();
        if (item.getType() == Material.WRITTEN_BOOK) {
            BookMeta bmeta = (BookMeta)item.getItemMeta();
            if (args.length > 1 && args[0].equalsIgnoreCase("author")) {
                if (!user.isAuthorized("essentials.book.author") || !this.isAuthor(bmeta, player) && !user.isAuthorized("essentials.book.others")) throw new Exception(I18n._("denyChangeAuthor", new Object[0]));
                bmeta.setAuthor(args[1]);
                item.setItemMeta((ItemMeta)bmeta);
                user.sendMessage(I18n._("bookAuthorSet", Commandbook.getFinalArg(args, 1)));
                return;
            } else if (args.length > 1 && args[0].equalsIgnoreCase("title")) {
                if (!user.isAuthorized("essentials.book.title") || !this.isAuthor(bmeta, player) && !user.isAuthorized("essentials.book.others")) throw new Exception(I18n._("denyChangeTitle", new Object[0]));
                bmeta.setTitle(args[1]);
                item.setItemMeta((ItemMeta)bmeta);
                user.sendMessage(I18n._("bookTitleSet", Commandbook.getFinalArg(args, 1)));
                return;
            } else {
                if (!this.isAuthor(bmeta, player) && !user.isAuthorized("essentials.book.others")) throw new Exception(I18n._("denyBookEdit", new Object[0]));
                ItemStack newItem = new ItemStack(Material.BOOK_AND_QUILL, item.getAmount());
                newItem.setItemMeta((ItemMeta)bmeta);
                user.setItemInHand(newItem);
                user.sendMessage(I18n._("editBookContents", new Object[0]));
            }
            return;
        } else {
            if (item.getType() != Material.BOOK_AND_QUILL) throw new Exception(I18n._("holdBook", new Object[0]));
            BookMeta bmeta = (BookMeta)item.getItemMeta();
            if (!user.isAuthorized("essentials.book.author")) {
                bmeta.setAuthor(player);
            }
            ItemStack newItem = new ItemStack(Material.WRITTEN_BOOK, item.getAmount());
            newItem.setItemMeta((ItemMeta)bmeta);
            user.setItemInHand(newItem);
            user.sendMessage(I18n._("bookLocked", new Object[0]));
        }
    }

    private boolean isAuthor(BookMeta bmeta, String player) {
        String author = bmeta.getAuthor();
        return author != null && author.equalsIgnoreCase(player);
    }
}

