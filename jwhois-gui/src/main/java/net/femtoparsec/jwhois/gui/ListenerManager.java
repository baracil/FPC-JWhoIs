package net.femtoparsec.jwhois.gui;

import net.femtoparsec.jwhois.utils.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/*
 *
 *     Copyright (c) 2011, Bastien Aracil
 *     All rights reserved.
 *     New BSD license. See http://en.wikipedia.org/wiki/Bsd_license
 *
 *     Redistribution and use in source and binary forms, with or without
 *     modification, are permitted provided that the following conditions are met:
 *        * Redistributions of source code must retain the above copyright
 *          notice, this list of conditions and the following disclaimer.
 *        * Redistributions in binary form must reproduce the above copyright
 *          notice, this list of conditions and the following disclaimer in the
 *          documentation and/or other materials provided with the distribution.
 *        * The name of Bastien Aracil may not be used to endorse or promote products
 *          derived from this software without specific prior written permission.
 *
 *     THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *     ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *     WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *     DISCLAIMED. IN NO EVENT SHALL BASTIEN ARACIL BE LIABLE FOR ANY
 *     DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *     (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *     LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *     ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *     (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *     SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * /
 */

/**
 * User: Bastien Aracil
 * Date: 23/10/11
 */
public class ListenerManager {
    
    private final List<Pair<Class, Object>> listeners = new ArrayList<Pair<Class, Object>> ();

    public void addListener(Class listenerType, Object listener) {
        if (findListener(listenerType, listener) == -1) {
            listeners.add(Pair.of(listenerType, listener));
        }
    }

    public void removeListener(Class listenerType, Object listener) {
        int idx = findListener(listenerType, listener);
        if (idx >= 0) {
            listeners.remove(idx);
        }
    }


    public <T> Collection<T> getListeners(Class<T> listenerType) {
        Collection<T> result = new LinkedList<T>();

        for (Pair<Class, Object> pair : listeners) {
            if (pair.getFirst().equals(listenerType)) {
                result.add(listenerType.cast(pair.getSecond()));
            }
        }
        return result;
    }

    private int findListener(Class<?> listenerType, Object listener) {
        int idx = 0;
        for (Pair<Class, Object> pair : listeners) {
            if (pair.getFirst().equals(listenerType) && listener == pair.getSecond()) {
                return idx;
            }
            ++idx;
        }
        return -1;
    }

}
