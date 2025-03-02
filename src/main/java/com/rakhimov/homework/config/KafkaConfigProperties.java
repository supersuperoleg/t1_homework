package com.rakhimov.homework.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KafkaConfigProperties {

    @Value("${kafka.bootstrap.servers}")
    private String servers;

    @Value("${kafka.session.timeout.ms:15000}")
    private String sessionTimeout;

    @Value("${kafka.max.partition.fetch.bytes:300000}")
    private String maxPartitionFetchBytes;

    @Value("${kafka.max.poll.records:10}")
    private String maxPollRecords;

    @Value("${kafka.max.poll.intervals.ms:3000}")
    private String maxPollIntervalsMs;

    @Value("${kafka.group-id}")
    private String groupId;

    @Value("${kafka.task-topic}")
    private String taskTopic;
}
