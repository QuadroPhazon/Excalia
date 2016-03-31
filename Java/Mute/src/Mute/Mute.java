/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mute;

import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author QuadroPhazon
 */
public class Mute extends JavaPlugin
{

    /**
     */
    
    @Override
    public void onEnable()
    {
        getLogger().info("Plugin Mute démarré !");
        saveDefaultConfig();
    }
    
    /**
     *
     */
    @Override
    public void onDisable()
    {
        getLogger().warning("Plugin Mute arrêté !");
    }
    
    public static void main(String[] args)
    {
        // TODO code application logic here
    }
    
}
