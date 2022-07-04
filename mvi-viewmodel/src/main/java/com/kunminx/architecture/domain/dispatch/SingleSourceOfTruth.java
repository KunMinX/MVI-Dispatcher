package com.kunminx.architecture.domain.dispatch;

import com.kunminx.architecture.domain.dispatch.TruthDispatcher;
import com.kunminx.architecture.domain.event.Event;

/**
 * Create by KunMinX at 2022/7/4
 */
public class SingleSourceOfTruth<E extends Event> extends TruthDispatcher<E> {
}
