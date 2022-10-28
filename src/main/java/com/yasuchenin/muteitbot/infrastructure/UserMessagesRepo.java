package com.yasuchenin.muteitbot.infrastructure;

import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface UserMessagesRepo extends JpaRepository<UserMessageEntity, Long> {

    void deleteByUserNameAndChatId(String userName, Long chatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<UserMessageEntity> findByUserNameAndChatId(String userName, Long chatId);

}
