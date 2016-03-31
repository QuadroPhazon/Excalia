/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.User;
import java.util.Comparator;

class UserNameComparator
implements Comparator<User> {
    UserNameComparator() {
    }

    @Override
    public int compare(User a, User b) {
        return a.getName().compareTo(b.getName());
    }
}

