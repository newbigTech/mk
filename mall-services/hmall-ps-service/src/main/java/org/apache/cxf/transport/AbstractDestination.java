//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.apache.cxf.transport;

import org.apache.cxf.Bus;
import org.apache.cxf.message.Message;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.*;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.EndpointReferenceUtils;

import java.io.IOException;
import java.util.logging.Logger;

public abstract class AbstractDestination extends AbstractObservable implements Destination, DestinationWithEndpoint {
    protected final EndpointReferenceType reference;
    protected final EndpointInfo endpointInfo;
    protected final Bus bus;

    public AbstractDestination(EndpointReferenceType ref, EndpointInfo ei) {
        this((Bus)null, ref, ei);
    }

    public AbstractDestination(Bus b, EndpointReferenceType ref, EndpointInfo ei) {
        this.reference = ref;
        this.endpointInfo = ei;
        this.bus = b;
    }

    public EndpointReferenceType getAddress() {
        return this.reference;
    }

    public Conduit getBackChannel(Message inMessage) throws IOException {
        return this.getInbuiltBackChannel(inMessage);
    }

    public void shutdown() {
    }

    protected abstract Conduit getInbuiltBackChannel(Message var1);

    public EndpointInfo getEndpointInfo() {
        return this.endpointInfo;
    }

    public abstract class AbstractBackChannelConduit extends AbstractConduit {
        public AbstractBackChannelConduit() {
            super(EndpointReferenceUtils.getAnonymousEndpointReference());
        }

        public void setMessageObserver(MessageObserver observer) {
        }

        protected Logger getLogger() {
            return org.apache.cxf.transport.AbstractDestination.this.getLogger();
        }
    }
}
