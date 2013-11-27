package ru.xpoft.vaadin.sample.integration.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.Serializable;

/**
 * @author xpoft
 */
@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SessionCounter implements Serializable
{
    private int count = 0;

    public int getCount()
    {
        return ++count;
    }
}
