package com.kduytran.memberservice.event.processor;

import com.kduytran.memberservice.constant.KafkaConstant;
import com.kduytran.memberservice.dto.ClassMemberDTO;
import com.kduytran.memberservice.event.PaymentEvent;
import com.kduytran.memberservice.event.PaymentEventType;
import com.kduytran.memberservice.service.IClassMemberService;
import com.kduytran.memberservice.util.StreamUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class PaymentStreamProcessor extends AbstractStreamsProcessor {
    private final StreamsBuilder streamsBuilder;
    private final ModelMapper modelMapper;
    private final IClassMemberService classMemberService;

    @Override
    protected void handleStream() {
//        streamsBuilder.stream(KafkaConstant.TOPIC_PAYMENTS,
//                Consumed.with(Serdes.String(), Serdes.String()))
//                .mapValues(value -> StreamUtils.mapValue(value, PaymentEvent.class))
//                .split()
//                .branch((key, value) -> value.getAction() == PaymentEventType.EXECUTE,
//                        Branched.withConsumer(stream -> stream.foreach((key, value) ->
//                                classMemberService.joinClass(modelMapper.map(value, ClassMemberDTO.class))))
//                );
    }

}
