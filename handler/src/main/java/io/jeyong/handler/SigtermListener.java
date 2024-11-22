package io.jeyong.handler;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import sun.misc.Signal;

public class SigtermListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Signal.handle(new Signal("TERM"), signal -> handleSigterm());
    }

    private void handleSigterm() {
        System.exit(0);
    }
}
