package pl.bodzioch.damian.controller.conf;

import lombok.extern.slf4j.Slf4j;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.Precorrelation;

@Slf4j
public class CustomHttpLowWriter implements HttpLogWriter {

    @Override
    public boolean isActive() {
        return log.isInfoEnabled();
    }

    @Override
    public void write(final Precorrelation precorrelation, final String request) {
        log.info(request);
    }

    @Override
    public void write(final Correlation correlation, final String response) {
        log.info(response);
    }
}
