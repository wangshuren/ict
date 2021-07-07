package com.ict.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ：wangsr
 * @description：生产者
 * @date ：Created in 2021/7/7 0007 14:30
 */
@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     *
     * kafka是一个分布式、多分区、多副本的、多订阅者，基于zookeeper协调的分布式日志系统
     *  以时间复杂度为O(1)的方式提供消息持久化能力，即使对TB级以上数据也能保证常数时间的访问性能。
     * 高吞吐率。即使在非常廉价的商用机器上也能做到单机支持每秒100K条消息的传输。
     * 支持Kafka Server间的消息分区，及分布式消费，同时保证每个partition内的消息顺序传输。
     * 同时支持离线数据处理和实时数据处理。
     * Scale out:支持在线水平扩展
     *
     * 优点
     *  1.解耦
     *  2.冗余（副本）
     *      消息队列把数据进行持久化直到它们已经被完全处理，通过这一方式规避了数据丢失风险。许多消息队列所采用的"插入-获取-删除"范式中，在把一个消息从队列中删除之前，需要你的处理系统明确的指出该消息已经被处理完毕，从而确保你的数据被安全的保存直到你使用完毕
     *  3.扩展性
     *      因为消息队列解耦了你的处理过程，所以增大消息入队和处理的频率是很容易的，只要另外增加处理过程即可。不需要改变代码、不需要调节参数。扩展就像调大电力按钮一样简单
     *  4.灵活性&峰值处理能力
     *  5.可恢复性
     *      系统的一部分组件失效时，不会影响到整个系统。消息队列降低了进程间的耦合度，所以即使一个处理消息的进程挂掉，加入队列中的消息仍然可以在系统恢复后被处理
     *  6.顺序保证
     *      在大多使用场景下，数据处理的顺序都很重要。大部分消息队列本来就是排序的，并且能保证数据会按照特定的顺序来处理。Kafka保证一个Partition内的消息的有序性。
     *  7.缓冲
     *      在任何重要的系统中，都会有需要不同的处理时间的元素。例如，加载一张图片比应用过滤器花费更少的时间。消息队列通过一个缓冲层来帮助任务最高效率的执行———写入队列的处理会尽可能的快速。该缓冲有助于控制和优化数据流经过系统的速度
     *  8.异步通信
     *      很多时候，用户不想也不需要立即处理消息。消息队列提供了异步处理机制，允许用户把一个消息放入队列，但并不立即处理它。想向队列中放入多少消息就放多少，然后在需要的时候再去处理它们。
     *
     * 发送消息到kafka
     */
    public void sendChannelMess(String channel, String message) {
        kafkaTemplate.send(channel, message);
    }
}
