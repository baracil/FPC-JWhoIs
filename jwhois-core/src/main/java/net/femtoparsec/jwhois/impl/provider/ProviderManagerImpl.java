package net.femtoparsec.jwhois.impl.provider;

import net.femtoparsec.jwhois.Format;
import net.femtoparsec.jwhois.Provider;
import net.femtoparsec.jwhois.ProviderManager;
import net.femtoparsec.jwhois.Source;

import java.util.*;

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
 * Date: 22/10/11
 */
public class ProviderManagerImpl implements ProviderManager {

    private final Map<Source, Map<String, Provider>> allProviders = new TreeMap<Source, Map<String, Provider>>();
    private final Map<Source, Map<String, Provider>> withProxyProviders = new TreeMap<Source, Map<String, Provider>>();

    public ProviderManagerImpl() {
    }

    public <T extends Provider> ProviderManagerImpl(T[] providers) {
        for (Provider provider : providers) {
            this.addProvider(provider);
        }
    }

    public ProviderManagerImpl(Collection<? extends Provider> providers) {
        for (Provider provider : providers) {
            this.addProvider(provider);
        }
    }

    public void removeProvider(String providerName) {
        removeFromMap(allProviders, providerName);
        removeFromMap(withProxyProviders, providerName);
    }

    public void addProvider(Provider provider) {
        for (Source source : provider.getProvidedSources()) {
            addToMap(allProviders, source, provider);
            if (provider.handleProxy()) {
                addToMap(withProxyProviders, source, provider);
            }
        }
    }

    @Override
    public Map<Source, Provider> getProviders(Set<Source> sources, boolean withProxy, Format preferredFormat) {
        Map<Source, Map<String, Provider>> reference;
        if (withProxy) {
            reference = withProxyProviders;
        }
        else {
            reference = allProviders;
        }

        Map<Source, Provider> results = new HashMap<Source, Provider>();
        for (Source source : sources) {
            Provider selectedProvider =getPreferredProvider(reference.get(source), preferredFormat);

            if(selectedProvider != null) {
                results.put(source, selectedProvider);
            }
        }

        return results;
    }

    private Provider getPreferredProvider(Map<String, Provider> providers, Format preferredFormat) {
        Provider selectedProvider = null;
        if (providers != null) {
            for (Provider provider : providers.values()) {
                selectedProvider = provider;
                if (provider.getResultFormat() == preferredFormat) {
                    break;
                }
            }
        }
        return selectedProvider;
    }

    private static void addToMap(Map<Source, Map<String, Provider>> map, Source source, Provider provider) {
        Map<String, Provider> providerMap = map.get(source);
        if (providerMap == null) {
            providerMap = new TreeMap<String, Provider>();
            map.put(source, providerMap);
        }

        providerMap.put(provider.getName(), provider);
    }

    private static void removeFromMap(Map<Source, Map<String, Provider>> map, String providerName) {
        Iterator<Map.Entry<Source, Map<String, Provider>>> itr = map.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry<Source, Map<String, Provider>> entry = itr.next();
            Map<String, Provider> providers = entry.getValue();

            if (providers.containsKey(providerName)) {
                providers.remove(providerName);
                if (providers.isEmpty()) {
                    itr.remove();
                }
            }
        }

    }


}
