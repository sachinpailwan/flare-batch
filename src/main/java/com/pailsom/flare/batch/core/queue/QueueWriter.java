package com.pailsom.flare.batch.core.queue;

import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.core.io.Resource;

import java.util.List;

public class QueueWriter<T> extends AbstractItemStreamItemWriter<T> implements ResourceAwareItemReaderItemStream<T> {

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }

    @Override
    public void write(List<? extends T> list) throws Exception {

    }

    @Override
    public void setResource(Resource resource) {

    }
}
