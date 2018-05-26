package com.demo.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * Created on 2018/5/25
 *
 * @Author: Jeff Yang
 */
public class Greeter extends AbstractActor {
    // 继承AbstractActor类并实现createReceive方法
    // AbstractActor是一个Java 8特有的 API，另外还有一个较老版本的UntypedActor
    static public Props props(String message, ActorRef printerActor) {
        // 为保证将Actor的实例封装不让其被外部直接访问，将所有构造函数的参数传给一个Props的实例。
        // 传入Actor的类型以及一个变长的参数列表
        // 需要指定返回的actor的实际类型，因为Java 8 lambda的会删除某些运行时类型信息
        // 这里不太理解，文档的原句是：since Java 8 lambdas have some runtime type information erased
        return Props.create(Greeter.class, () -> new Greeter(message, printerActor));
    }

    /**
     * 问候语的接收者
     * 消息必须永远是不可变的，因为它们在不同的线程之间共享
     */
    static public class WhoToGreet {
        public final String who;

        public WhoToGreet(String who) {
            this.who = who;
        }
    }

    /**
     * 执行问候的指示
     */
    static public class Greet {
        public Greet() {
        }
    }

    private final String message;
    private final ActorRef printerActor;
    private String greeting = "";

    /**
     * 构造函数
     *
     * @param message      构建问候消息
     * @param printerActor 处理问候
     */
    public Greeter(String message, ActorRef printerActor) {
        this.message = message;
        this.printerActor = printerActor;
    }

    @Override
    public Receive createReceive() {
        // receiveBuilder定义Actor如何对接收的不同消息作出响应
        // 处理WhoToGreet和Greet类型的消息
        return receiveBuilder()
                .match(WhoToGreet.class, wtg -> {
                    // 更新 greeting Actor 的状态
                    this.greeting = message + ", " + wtg.who;
                })
                .match(Greet.class, x -> {
                    // 发送greeting给PrinterActor
                    printerActor.tell(new Printer.Greeting(greeting), getSelf());
                })
                .build();
    }
}