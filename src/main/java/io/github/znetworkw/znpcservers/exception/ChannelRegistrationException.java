package io.github.znetworkw.znpcservers.exception;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ChannelRegistrationException extends Throwable {
    private final Throwable throwable;
    private final List<String> channelNames;

    public ChannelRegistrationException(Throwable t, List<String> channelNames) {
        this.throwable = t;
        this.channelNames = new ImmutableList.Builder<String>().addAll(channelNames).build();
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public List<String> getChannelNames() {
        return channelNames;
    }
}
