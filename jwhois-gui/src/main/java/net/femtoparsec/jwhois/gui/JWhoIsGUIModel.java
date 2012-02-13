package net.femtoparsec.jwhois.gui;

import net.femtoparsec.jwhois.*;
import net.femtoparsec.jwhois.utils.Collections;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.lang.Validate;

import java.net.Proxy;
import java.util.HashSet;
import java.util.Set;

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
public class JWhoIsGUIModel<S extends SourceProvider> {

    private final ListenerManager listenerManager = new ListenerManager();

    private final JResultGUIFactory<S> jResultGUIFactory;

    private String query;

    private Set<Source> sources = new HashSet<Source>();

    private Set<Source> selectedSources = new HashSet<Source>();

    private Proxy proxy;

    private GenericWhoIs<S> whoIs;

    private Format preferredFormat;

    public JWhoIsGUIModel(GenericWhoIs<S> whoIs, JResultGUIFactory<S> jResultGUIFactory, Set<Source> proposedSources, Set<Source> selectedSources) {
        Validate.notNull(whoIs,"whoIs");
        Validate.notNull(jResultGUIFactory, "jResultGUIFactory");
        Validate.noNullElements(proposedSources, "proposedSources");
        Validate.notEmpty(proposedSources, "proposedSources");

        Validate.noNullElements(selectedSources, "selectedSources");

        this.whoIs = whoIs;
        this.proxy = null;
        this.jResultGUIFactory = jResultGUIFactory;
        this.preferredFormat = Format.RPSL;
        this.sources = new HashSet<Source>(proposedSources);
        this.selectedSources = new HashSet<Source>(CollectionUtils.intersection(proposedSources, selectedSources));
    }

    public JWhoIsGUIModel(GenericWhoIs<S> whoIs, JResultGUIFactory<S> resultGUIFactory, Set<Source> proposedSource) {
        this(whoIs, resultGUIFactory, proposedSource, Collections.asSet(Source.RIPE, Source.ARIN));
    }

    public JWhoIsGUIModel(GenericWhoIs<S> whoIs, JResultGUIFactory<S> resultGUIFactory) {
        this(whoIs, resultGUIFactory, Collections.asSet(Source.values()), Collections.asSet(Source.RIPE, Source.ARIN));
    }

    public Set<Source> getSources() {
        return sources;
    }

    public Set<Source> getSelectedSources() {
        return selectedSources;
    }

    public void selectSource(Source source, boolean selected) {
        boolean changed;
        if (selected) {
            changed = this.selectedSources.add(source);
        }
        else {
            changed = this.selectedSources.remove(source);
        }
        if (changed) {
                for (SourceSelectionListener listener : listenerManager.getListeners(SourceSelectionListener.class)) {
                    listener.onSourceSelected(source, selected);
                }
        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Format getPreferredFormat() {
        return preferredFormat;
    }

    public void setPreferredFormat(Format preferredFormat) {
        this.preferredFormat = preferredFormat;
    }

    public void launchQuery() {
        this.whoIs.setProxy(this.proxy);
        this.whoIs.setPreferredFormat(this.preferredFormat);
        Set<S> result = this.whoIs.request(this.getQuery(), this.getSelectedSources());

        WhoIsResultHelper<S> helper = new WhoIsResultHelper<S>(result);

        for (SourceResultListener<S> listener : listenerManager.getListeners(SourceResultListener.class)) {
            listener.onSourceResult(helper);
        }
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
        this.whoIs.setProxy(proxy);
    }

    public JResultGUI<S> createJResultGUI(Source source) {
        return this.jResultGUIFactory.create(source, this);
    }

    public void addSourceSelectionListener(SourceSelectionListener listener) {
        this.listenerManager.addListener(SourceSelectionListener.class, listener);
    }

    public void removeSourceSelectionListener(SourceSelectionListener listener) {
        this.listenerManager.removeListener(SourceSelectionListener.class, listener);
    }

    public void addSourceResultListener(SourceResultListener listener) {
        this.listenerManager.addListener(SourceResultListener.class, listener);
    }

    public void removeSourceResultListener(SourceResultListener listener) {
        this.listenerManager.removeListener(SourceResultListener.class, listener);
    }


}
