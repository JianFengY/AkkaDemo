package com.demo.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created on 2018/5/25
 *
 * @Author: Jeff Yang
 */
public class Printer extends AbstractActor {
    static public Props props() {
        return Props.create(Printer.class, () -> new Printer());
    }

    /**
     * 包含问候语的消息
     */
    static public class Greeting {
        public final String message;

        public Greeting(String message) {
            this.message = message;
        }
    }

    // 创建一个记录器
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public Printer() {
    }

    @Override
    public Receive createReceive() {
        // 只处理一种类型的消息Greeting，并记录该消息的内容
        return receiveBuilder()
                .match(Greeting.class, greeting -> {
                    log.info(greeting.message);
                })
                .build();
    }
}
