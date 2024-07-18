package com.kduytran.classqueryservice.processor;

import com.kduytran.classqueryservice.constant.KafkaConstant;
import com.kduytran.classqueryservice.dto.ClassDTO;
import com.kduytran.classqueryservice.event.ClassEvent;
import com.kduytran.classqueryservice.service.IClassService;
import com.kduytran.classqueryservice.utils.LogUtils;
import com.kduytran.classqueryservice.utils.StreamUtils;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClassStreamsProcessor extends AbstractStreamsProcessor<ClassDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassStreamsProcessor.class);
    private final StreamsBuilder streamsBuilder;
    private final IClassService classService;

    @Override
    public String getStoreName() {
        return null;
    }

    @Override
    protected void handleStream() {
        KStream<String, ClassEvent> classStream = streamsBuilder.stream(KafkaConstant.TOPIC_CLASSES,
                Consumed.with(Serdes.String(), Serdes.String()))
                .mapValues(value -> StreamUtils.mapValue(value, ClassEvent.class))
                .peek((key, value) -> LogUtils.logEvent(LOGGER, value));

        classStream.foreach((key, value) -> handleService(value));
    }

    private void handleService(ClassEvent event) {
        if (event == null) {
            return;
        }
        ClassDTO dto = map(event);
        try {
            switch (event.getAction()) {
                case CREATE -> classService.create(dto);
                case UPDATE -> classService.update(dto.getId(), dto);
                case DELETE -> classService.delete(dto.getId());
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Exception %s: Action = %s, Id = %s, Name = %s",
                    e.getMessage(), event.getAction(), event.getId(), event.getName()));
        }
    }

    private ClassDTO map(ClassEvent event) {
        ClassDTO dto = new ClassDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setImage(event.getImage());
        dto.setStartAt(event.getStartAt());
        dto.setEndAt(event.getEndAt());
        dto.setStatus(event.getStatus());
        dto.setCategoryId(event.getCategoryId().toString());
        // Category code and name are not available in ClassEvent
        // dto.setCategoryCode(...);
        // dto.setCategoryName(...);
        dto.setOwnerType(event.getOwnerType());
        dto.setOwnerId(event.getOwnerId().toString());
        // Owner name is not available in ClassEvent
        // dto.setOwnerName(...);
        dto.setHasPassword(event.isHasPassword());
        // averageRating is not available in ClassEvent
        // dto.setAverageRating(...);
        // numberOfReviews is not available in ClassEvent
        // dto.setNumberOfReviews(...);
        // createAt is not available in ClassEvent
        // dto.setCreateAt(...);
        return dto;
    }

}
