/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 *  org.bukkit.plugin.Plugin
 *  org.yaml.snakeyaml.TypeDescription
 *  org.yaml.snakeyaml.constructor.Constructor
 *  org.yaml.snakeyaml.constructor.Constructor$ConstructMapping
 *  org.yaml.snakeyaml.constructor.Constructor$ConstructScalar
 *  org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor
 *  org.yaml.snakeyaml.error.YAMLException
 *  org.yaml.snakeyaml.introspector.Property
 *  org.yaml.snakeyaml.nodes.MappingNode
 *  org.yaml.snakeyaml.nodes.Node
 *  org.yaml.snakeyaml.nodes.NodeId
 *  org.yaml.snakeyaml.nodes.NodeTuple
 *  org.yaml.snakeyaml.nodes.ScalarNode
 *  org.yaml.snakeyaml.nodes.SequenceNode
 *  org.yaml.snakeyaml.nodes.Tag
 */
package com.earth2me.essentials.storage;

import com.earth2me.essentials.storage.EnchantmentLevel;
import com.earth2me.essentials.utils.NumberUtil;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

public class BukkitConstructor
extends CustomClassLoaderConstructor {
    private final transient Plugin plugin;

    public BukkitConstructor(Class clazz, Plugin plugin) {
        super(clazz, plugin.getClass().getClassLoader());
        this.plugin = plugin;
        this.yamlClassConstructors.put(NodeId.scalar, new ConstructBukkitScalar());
        this.yamlClassConstructors.put(NodeId.mapping, new ConstructBukkitMapping());
    }

    private class ConstructBukkitMapping
    extends Constructor.ConstructMapping {
        private ConstructBukkitMapping() {
            super((Constructor)BukkitConstructor.this);
        }

        public Object construct(Node node) {
            if (node.getType().equals(Location.class)) {
                MappingNode mnode = (MappingNode)node;
                String worldName = "";
                double x = 0.0;
                double y = 0.0;
                double z = 0.0;
                float yaw = 0.0f;
                float pitch = 0.0f;
                if (mnode.getValue().size() < 4) {
                    return null;
                }
                for (NodeTuple nodeTuple : mnode.getValue()) {
                    String key = (String)BukkitConstructor.this.constructScalar((ScalarNode)nodeTuple.getKeyNode());
                    ScalarNode snode = (ScalarNode)nodeTuple.getValueNode();
                    if (key.equalsIgnoreCase("world")) {
                        worldName = (String)BukkitConstructor.this.constructScalar(snode);
                    }
                    if (key.equalsIgnoreCase("x")) {
                        x = Double.parseDouble((String)BukkitConstructor.this.constructScalar(snode));
                    }
                    if (key.equalsIgnoreCase("y")) {
                        y = Double.parseDouble((String)BukkitConstructor.this.constructScalar(snode));
                    }
                    if (key.equalsIgnoreCase("z")) {
                        z = Double.parseDouble((String)BukkitConstructor.this.constructScalar(snode));
                    }
                    if (key.equalsIgnoreCase("yaw")) {
                        yaw = Float.parseFloat((String)BukkitConstructor.this.constructScalar(snode));
                    }
                    if (!key.equalsIgnoreCase("pitch")) continue;
                    pitch = Float.parseFloat((String)BukkitConstructor.this.constructScalar(snode));
                }
                if (worldName == null || worldName.isEmpty()) {
                    return null;
                }
                World world = Bukkit.getWorld((String)worldName);
                if (world == null) {
                    return null;
                }
                return new Location(world, x, y, z, yaw, pitch);
            }
            return super.construct(node);
        }

        protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
            Map typeDefinitions;
            try {
                Field typeDefField = Constructor.class.getDeclaredField("typeDefinitions");
                typeDefField.setAccessible(true);
                typeDefinitions = (Map)typeDefField.get((Object)BukkitConstructor.this);
                if (typeDefinitions == null) {
                    throw new NullPointerException();
                }
            }
            catch (Exception ex) {
                throw new YAMLException((Throwable)ex);
            }
            BukkitConstructor.this.flattenMapping(node);
            Class beanType = node.getType();
            List nodeValue = node.getValue();
            for (NodeTuple tuple : nodeValue) {
                if (!(tuple.getKeyNode() instanceof ScalarNode)) {
                    throw new YAMLException("Keys must be scalars but found: " + (Object)tuple.getKeyNode());
                }
                ScalarNode keyNode = (ScalarNode)tuple.getKeyNode();
                Node valueNode = tuple.getValueNode();
                keyNode.setType((Class)String.class);
                String key = (String)BukkitConstructor.this.constructObject((Node)keyNode);
                try {
                    MappingNode mnode;
                    Class[] arguments;
                    Property property;
                    try {
                        property = this.getProperty(beanType, key);
                    }
                    catch (YAMLException e) {
                        continue;
                    }
                    valueNode.setType(property.getType());
                    TypeDescription memberDescription = (TypeDescription)typeDefinitions.get(beanType);
                    boolean typeDetected = false;
                    if (memberDescription != null) {
                        switch (valueNode.getNodeId()) {
                            case sequence: {
                                SequenceNode snode = (SequenceNode)valueNode;
                                Class memberType = memberDescription.getListPropertyType(key);
                                if (memberType != null) {
                                    snode.setListType(memberType);
                                    typeDetected = true;
                                    break;
                                }
                                if (!property.getType().isArray()) break;
                                snode.setListType(property.getType().getComponentType());
                                typeDetected = true;
                                break;
                            }
                            case mapping: {
                                mnode = (MappingNode)valueNode;
                                Class keyType = memberDescription.getMapKeyType(key);
                                if (keyType == null) break;
                                mnode.setTypes(keyType, memberDescription.getMapValueType(key));
                                typeDetected = true;
                            }
                        }
                    }
                    if (!typeDetected && valueNode.getNodeId() != NodeId.scalar && (arguments = property.getActualTypeArguments()) != null) {
                        Class t;
                        if (valueNode.getNodeId() == NodeId.sequence) {
                            t = arguments[0];
                            SequenceNode snode = (SequenceNode)valueNode;
                            snode.setListType(t);
                        } else if (valueNode.getTag().equals((Object)Tag.SET)) {
                            t = arguments[0];
                            mnode = (MappingNode)valueNode;
                            mnode.setOnlyKeyType(t);
                            mnode.setUseClassConstructor(Boolean.valueOf(true));
                        } else if (property.getType().isAssignableFrom(Map.class)) {
                            Class ketType = arguments[0];
                            Class valueType = arguments[1];
                            MappingNode mnode2 = (MappingNode)valueNode;
                            mnode2.setTypes(ketType, valueType);
                            mnode2.setUseClassConstructor(Boolean.valueOf(true));
                        }
                    }
                    Object value = BukkitConstructor.this.constructObject(valueNode);
                    property.set(object, value);
                    continue;
                }
                catch (Exception e) {
                    throw new YAMLException("Cannot create property=" + key + " for JavaBean=" + object + "; " + e.getMessage(), (Throwable)e);
                }
            }
            return object;
        }
    }

    private class ConstructBukkitScalar
    extends Constructor.ConstructScalar {
        private ConstructBukkitScalar() {
            super((Constructor)BukkitConstructor.this);
        }

        public Object construct(Node node) {
            if (node.getType().equals(Material.class)) {
                Material mat;
                String val = (String)BukkitConstructor.this.constructScalar((ScalarNode)node);
                if (NumberUtil.isInt(val)) {
                    int typeId = Integer.parseInt(val);
                    mat = Material.getMaterial((int)typeId);
                } else {
                    mat = Material.matchMaterial((String)val);
                }
                return mat;
            }
            if (node.getType().equals(MaterialData.class)) {
                Material mat;
                String val = (String)BukkitConstructor.this.constructScalar((ScalarNode)node);
                if (val.isEmpty()) {
                    return null;
                }
                String[] split = val.split("[:+',;.]", 2);
                if (split.length == 0) {
                    return null;
                }
                if (NumberUtil.isInt(split[0])) {
                    int typeId = Integer.parseInt(split[0]);
                    mat = Material.getMaterial((int)typeId);
                } else {
                    mat = Material.matchMaterial((String)split[0]);
                }
                if (mat == null) {
                    return null;
                }
                byte data = 0;
                if (split.length == 2 && NumberUtil.isInt(split[1])) {
                    data = Byte.parseByte(split[1]);
                }
                return new MaterialData(mat, data);
            }
            if (node.getType().equals(ItemStack.class)) {
                Material mat;
                String val = (String)BukkitConstructor.this.constructScalar((ScalarNode)node);
                if (val.isEmpty()) {
                    return null;
                }
                String[] split1 = val.split("\\W");
                if (split1.length == 0) {
                    return null;
                }
                String[] split2 = split1[0].split("[:+',;.]", 2);
                if (split2.length == 0) {
                    return null;
                }
                if (NumberUtil.isInt(split2[0])) {
                    int typeId = Integer.parseInt(split2[0]);
                    mat = Material.getMaterial((int)typeId);
                } else {
                    mat = Material.matchMaterial((String)split2[0]);
                }
                if (mat == null) {
                    return null;
                }
                short data = 0;
                if (split2.length == 2 && NumberUtil.isInt(split2[1])) {
                    data = Short.parseShort(split2[1]);
                }
                int size = mat.getMaxStackSize();
                if (split1.length > 1 && NumberUtil.isInt(split1[1])) {
                    size = Integer.parseInt(split1[1]);
                }
                ItemStack stack = new ItemStack(mat, size, data);
                if (split1.length > 2) {
                    for (int i = 2; i < split1.length; ++i) {
                        Enchantment enchantment;
                        String[] split3 = split1[0].split("[:+',;.]", 2);
                        if (split3.length < 1) continue;
                        if (NumberUtil.isInt(split3[0])) {
                            int enchantId = Integer.parseInt(split3[0]);
                            enchantment = Enchantment.getById((int)enchantId);
                        } else {
                            enchantment = Enchantment.getByName((String)split3[0].toUpperCase(Locale.ENGLISH));
                        }
                        if (enchantment == null) continue;
                        int level = enchantment.getStartLevel();
                        if (split3.length == 2 && NumberUtil.isInt(split3[1])) {
                            level = Integer.parseInt(split3[1]);
                        }
                        if (level < enchantment.getStartLevel()) {
                            level = enchantment.getStartLevel();
                        }
                        if (level > enchantment.getMaxLevel()) {
                            level = enchantment.getMaxLevel();
                        }
                        stack.addUnsafeEnchantment(enchantment, level);
                    }
                }
                return stack;
            }
            if (node.getType().equals(EnchantmentLevel.class)) {
                Enchantment enchant;
                String val = (String)BukkitConstructor.this.constructScalar((ScalarNode)node);
                if (val.isEmpty()) {
                    return null;
                }
                String[] split = val.split("[:+',;.]", 2);
                if (split.length == 0) {
                    return null;
                }
                if (NumberUtil.isInt(split[0])) {
                    int typeId = Integer.parseInt(split[0]);
                    enchant = Enchantment.getById((int)typeId);
                } else {
                    enchant = Enchantment.getByName((String)split[0].toUpperCase(Locale.ENGLISH));
                }
                if (enchant == null) {
                    return null;
                }
                int level = enchant.getStartLevel();
                if (split.length == 2 && NumberUtil.isInt(split[1])) {
                    level = Integer.parseInt(split[1]);
                }
                if (level < enchant.getStartLevel()) {
                    level = enchant.getStartLevel();
                }
                if (level > enchant.getMaxLevel()) {
                    level = enchant.getMaxLevel();
                }
                return new EnchantmentLevel(enchant, level);
            }
            return super.construct(node);
        }
    }

}

