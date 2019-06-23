package com.differz.bc.web.message;

import com.differz.bc.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private static final String MESSAGE_SENT = "message successfully sent";

    private final MessageService messageService;

    @PostMapping("/message")
    public ResultDto message(@RequestBody MessageInputDto messageInputDto) {
        messageService.saveMessage(messageInputDto);
        log.debug(MESSAGE_SENT);
        return new ResultDto(MESSAGE_SENT);
    }
}
