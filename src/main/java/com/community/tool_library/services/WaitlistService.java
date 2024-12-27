package com.community.tool_library.services;

import com.community.tool_library.dtos.WaitlistEntryDTO;

import java.util.List;

public interface WaitlistService {

    void placeHold(Long itemId, Long userId);

    void cancelHold(Long itemId, Long userId);

    WaitlistEntryDTO notifyNextUser(Long itemId);

    List<WaitlistEntryDTO> getWaitlistForItem(Long itemId);

    List<Long> getWaitlistUserIdsByItem(Long itemId);

    List<Long> getWaitlistItemIdsByUser(Long userId);
}
