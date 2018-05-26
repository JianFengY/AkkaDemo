package com.demo.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.io.IOException;

/**
 * Created on 2018/5/26
 *
 * @Author: Jeff Yang
 */
public class AkkaDemo {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("helloakka");
        try {
            // 在Akka中，无法使用new关键字创建Actor的实例而是使用工厂。
            // 工厂返回一个ActorRef指向actor实例的引用。
            // actorOf 方法会生成一个新的 Actor，并返回指向该 Actor 的引用。
            final ActorRef printerActor =
                    system.actorOf(Printer.props(), "printerActor");
            final ActorRef howdyGreeter =
                    system.actorOf(Greeter.props("Howdy", printerActor), "howdyGreeter");
            final ActorRef helloGreeter =
                    system.actorOf(Greeter.props("Hello", printerActor), "helloGreeter");
            final ActorRef goodDayGreeter =
                    system.actorOf(Greeter.props("Good day", printerActor), "goodDayGreeter");

            // 使用tell()方法发送消息到Actor

            howdyGreeter.tell(new Greeter.WhoToGreet("Akka"), ActorRef.noSender());
            howdyGreeter.tell(new Greeter.Greet(), ActorRef.noSender());

            howdyGreeter.tell(new Greeter.WhoToGreet("Lightbend"), ActorRef.noSender());
            howdyGreeter.tell(new Greeter.Greet(), ActorRef.noSender());

            helloGreeter.tell(new Greeter.WhoToGreet("Java"), ActorRef.noSender());
            helloGreeter.tell(new Greeter.Greet(), ActorRef.noSender());

            goodDayGreeter.tell(new Greeter.WhoToGreet("Play"), ActorRef.noSender());
            goodDayGreeter.tell(new Greeter.Greet(), ActorRef.noSender());

            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException ioe) {
        } finally {
            system.terminate();
        }
    }
}
